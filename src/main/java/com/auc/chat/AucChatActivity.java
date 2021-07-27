package com.auc.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class AucChatActivity extends AppCompatActivity {
    EditText editText;
    private final Handler aHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.i("핸들러", "채팅방");
            switch (msg.what) {
                case 300:
                    setContent(msg.obj);
                    break;
                case 100:
                    break;
                // TODO : add case.
            }
        }
    };


    SocketService sc;

    TextView textView;

    String title;

    public void setContent(Object obj) {

        String msg = (String) obj;
        String[] msgs;
        msgs = msg.split("\\|");

        textView.setText(textView.getText().toString() + msgs[1] + "\n");
        Log.i("대화내용", msgs[1]);

    }




    @Override
    public void onBackPressed() {
        ((MainActivity) MainActivity.mContext).setHandler();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auc_chat);

        Button sendBtn = (Button) findViewById(R.id.sendBtn);
        EditText sendText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView2);

        Intent intent = getIntent();

        title = intent.getStringExtra("title");

        sc = ((MainActivity) MainActivity.mContext).getSocketService();
        sc.setHandler(aHandler);

        //대화 입력
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //대화 송신
                String sendMsg = "300|" + sendText.getText().toString();
                sc.sendData(sendMsg);
                sendText.setText("");
//
//                switch (v.getId()) {
//                    case R.id.sendBtn:
//                        Btn:
//                        //전송 버튼 클릭 시
//                        chat();
//
//                        break;
//                }
                System.out.println("sendText???????" + sendMsg);
            }
        });

    }

//        void chat() {
//        System.out.println("2222222222222");
//        Log.w("chat", "채팅");
//        try {
//            String chat = editText.getText().toString();
//
//            Log.w("앱에서 보낸 값", chat + ", " );
//
//            CustomTask task = new CustomTask();
//            String result = task.execute(chat).get();
//            System.out.println(result+"?????????"); // result null
//
//            if (result.equals("join success")) {
//                Toast.makeText(this.getApplicationContext(), "ㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇ!", Toast.LENGTH_SHORT).show();
//                System.out.println("2222222222222");
//                finish();
//            }else{
//                System.out.println("11111111111111111111111111111111111");
//               }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    class CustomTask extends AsyncTask<String, Void, String> {
//        String sendText, receiveText;
//
//        @Override
//        protected String doInBackground(String... strings) {
//            try {
//                String str;
//                System.out.println("!!!!!!!!!!!");
//                URL url = new URL("http://192.168.1.171:8080/chat");
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//                conn.setRequestMethod("POST");
//                conn.setDoOutput(true);
//
//                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
//                sendText = "text" + strings[0];
//
//                osw.write(sendText);
//                osw.flush();
//
//
//                if (conn.getResponseCode() == conn.HTTP_OK) {
//                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
//                    BufferedReader reader = new BufferedReader(tmp);
//                    StringBuffer buffer = new StringBuffer();
//
//                    while ((str = reader.readLine()) != null) {
//                        buffer.append(str);
//                    }
//                    receiveText = buffer.toString();
//
//                } else {
//                    Log.i("", conn.getResponseCode() + "에러");
//                }
//
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return receiveText;
//        }
//    }

}