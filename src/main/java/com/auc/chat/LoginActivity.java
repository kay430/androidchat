package com.auc.chat;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText idEt, pwdEt;
    Button loginBtn, joinBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acrivity_login);

        idEt  = (EditText) findViewById(R.id.idEt);
        pwdEt = (EditText) findViewById(R.id.pwdEt);

        loginBtn = (Button) findViewById(R.id.loginBtn);
        joinBtn  = (Button) findViewById(R.id.joinBtn);
        loginBtn.setOnClickListener(this);
        joinBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.joinBtn:  //회원가입 버튼 클릭 시
                Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.loginBtn: //로그인 버튼 클릭 시
                login();

                break;

        }
    }



        void login() {
            Log.w("login", "로그인 하는 중");
            try {
                String id = idEt.getText().toString();
                String pw = pwdEt.getText().toString();
                Log.w("앱에서 보낸 값", id + ", " + pw);

                CustomTask task = new CustomTask();
                String result = task.execute(id,pw).get();
                Log.w("받은 값", result);

                if(result.equals("login success")) {
                    Toast.makeText(this.getApplicationContext(), id+"님 환영합니다!", Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent2);
                    finish();


                } else if(("").equals(id) || ("").equals(pw)) {
                    Toast.makeText(this.getApplicationContext(), "빈칸을 채워주세요!", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this.getApplicationContext(), "회원정보가 일치하지 않거나 존재하지 않습니다", Toast.LENGTH_SHORT).show();
                }


            } catch (Exception e) {

            }
        }

        class CustomTask extends AsyncTask<String, Void, String> {
            String sendMsg, receiveMsg;

            @Override
            protected String doInBackground(String... strings) {
                try {
                    String str;
                    URL url = new URL("http://192.168.1.171:8080/login");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);

                    OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                    sendMsg = "id=" + strings[0] + "&pw=" + strings[1];
                    osw.write(sendMsg);
                    osw.flush();


                    if(conn.getResponseCode() == conn.HTTP_OK) {
                        InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                        BufferedReader reader = new BufferedReader(tmp);
                        StringBuffer buffer = new StringBuffer();

                        while ((str = reader.readLine()) != null) {
                            buffer.append(str);
                        }
                        receiveMsg = buffer.toString();

                    } else {
                        Log.i("통신 결과", conn.getResponseCode() + "에러");
                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return receiveMsg;
            }
        }


}
