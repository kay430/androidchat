package com.auc.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    TextView emailTv, pwdTv;
    Button chatBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acrivity_result);
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String pwd = intent.getStringExtra("pwd");

        emailTv = (TextView)findViewById(R.id.emailTv);
        pwdTv   = (TextView)findViewById(R.id.pwdTv);
        emailTv.setText("email: " + email);
        pwdTv.setText("password: " + pwd);

        chatBtn = (Button)findViewById(R.id.chatBtn);
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplication(), MainActivity.class);
                startActivity(intent2);
                finish();
            }
        });
    }

}
