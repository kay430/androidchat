package com.auc.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class AucChatActivity extends AppCompatActivity {

    private final Handler aHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.i("핸들러", "채팅방");
            switch (msg.what) {
                case 300 : setContent(msg.obj);
                    break ;
                case 100 :
                    break ;
                // TODO : add case.
            }
        }
    } ;

    SocketService sc;

    TextView textView;

    String title;

    public void setContent(Object obj){

        String msg = (String)obj;
        String[] msgs;
        msgs = msg.split("\\|");

        textView.setText(textView.getText().toString() + msgs[1] + "\n");
        Log.i("대화내용", msgs[1]);

    }

    @Override
    public void onBackPressed(){
        ((MainActivity)MainActivity.mContext).setHandler();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auc_chat);

        Button sendBtn  = (Button) findViewById(R.id.sendBtn);
        EditText sendText = (EditText) findViewById(R.id.editText);
        textView = (TextView)findViewById(R.id.textView2);

        Intent intent = getIntent();

        title = intent.getStringExtra("title");

        sc = ((MainActivity)MainActivity.mContext).getSocketService();
        sc.setHandler(aHandler);

        //대화 입력
        sendBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //대화 송신
                String sendMsg = "300|" + sendText.getText().toString();
                sc.sendData(sendMsg);
                sendText.setText("");

            }
        });

    }

}