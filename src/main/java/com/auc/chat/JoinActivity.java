package com.auc.chat;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class JoinActivity extends AppCompatActivity implements View.OnClickListener {

    EditText idEt, pwdEt;
    Button joinBtn, loginBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        idEt  = (EditText)findViewById(R.id.idEt2);
        pwdEt    = (EditText)findViewById(R.id.pwdEt2);

        joinBtn  = (Button)findViewById(R.id.joinBtn2);
        loginBtn = (Button)findViewById(R.id.loginBtn2);
        joinBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.loginBtn2:
                startActivity(new Intent(JoinActivity.this, LoginActivity.class));
                finish();
                break;

            case R.id.joinBtn2:
                join();
                break;
        }

    }

    void join() {
        Log.w("join", "회원가입 하는 중");
        try {
            String id = idEt.getText().toString();
            String pw = pwdEt.getText().toString();
            Log.w("앱에서 보낸 값", id + ", " + pw);

            CustomTask task = new CustomTask();
            String result = task.execute(id, pw).get();
            Log.w("받은 값", result);

            if (result.equals("join success")) {
                Toast.makeText(this.getApplicationContext(), "회원가입 성공!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(JoinActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            } else if(("").equals(id) || ("").equals(pw)) {
                Toast.makeText(this.getApplicationContext(), "빈칸을 채워주세요!", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this.getApplicationContext(), "이미 존재하는 회원ID 입니다", Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;

        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://192.168.1.171:8080/join");
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
