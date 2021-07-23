package com.auc.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    List<String> titleList;
    ArrayAdapter<String> adpater;
    ListView listView;

    SocketService sc;

    public static Context mContext;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 90  : setList(msg.obj);
                    break ;
                case 100 :
                    break ;
                case 110 : existRoom(msg.obj);
                    break ;
                case 120 : existNick(msg.obj);
                    break ;
                // TODO : add case.
            }
        }
    } ;

    public void existNick(Object obj) {
        String msg3 = (String)obj;
        String[] msgs3;
        msgs3 = msg3.split("\\|");

        if(msgs3[0].equals("120")) {
            Toast.makeText(MainActivity.this, "중복된 닉네임 입니다!", Toast.LENGTH_SHORT).show();
        }
    }

    public void existRoom(Object obj) {
        String msg2 = (String)obj;
        String[] msgs2;
        msgs2 = msg2.split("\\|");

        if(msgs2[0].equals("110")) {
            Toast.makeText(MainActivity.this, "중복된 방 이름 입니다!", Toast.LENGTH_SHORT).show();
        }
    }

    public void setList(Object obj){

        String msg = (String)obj;
        String[] msgs;
        msgs = msg.split("\\|");

        //방 목록 생성
        if(msgs[0].equals("90")){
            titleList.clear();
            for(int i = 1; i < msgs.length-1; i++){
                titleList.add(msgs[i]);
            }
            listView.setAdapter(adpater);
        }

    }

    public SocketService getSocketService(){
        return this.sc;
    }

    public void setHandler(){
        sc.setHandler(mHandler);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //스프링부트로 경락시세정보 불러와서 DB저장하고 웹으로 그리드만들기 로그인기능 추가

        //채팅방 만들어서 내용 저장 후 나중에 접속 시 채팅 내용 보여주게 로그인기능 추가
        //아이디 한번등록하면 계속 유지되게
        //같은이름 방 생성 안되게

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sendBtn = (Button) findViewById(R.id.sendBtn);
        EditText sendText = (EditText) findViewById(R.id.editText);

        Button sendRoomNameBtn = (Button) findViewById(R.id.sendRoomBtn);
        EditText sendRommNameText = (EditText) findViewById(R.id.editTextRoomName);

        listView = (ListView) findViewById(R.id.listView);

        titleList = new ArrayList<String>();
        adpater = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titleList);

        mContext = this;

        // 소켓 연결
        // 리스트 클릭시 해당 room 번호방 입장
        // 채팅~~~~
        // 채팅방 나가기 - 방 나가기


        // 방만들면 바로 방으로 입장으로 변경

        sc = new SocketService(mHandler);
        sc.getSocket();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //사용자명 등록
                if(sendText.getText().toString().equals("")) { //아무것도 입력하지 않았을 때
                    Toast.makeText(MainActivity.this, "이름을 등록해주세요!", Toast.LENGTH_SHORT).show();

                } else {
                    String sendMsg = "150|" + sendText.getText().toString();
                    sc.sendData(sendMsg);
                    Toast.makeText(MainActivity.this, "이름이 등록되었습니다!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        sendRoomNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //방 등록
                    if (sendRommNameText.getText().toString().equals("")) { //아무것도 입력하지 않았을 때
                        Toast.makeText(MainActivity.this, "방 이름을 등록해주세요!", Toast.LENGTH_SHORT).show();

                    } else {
                        String sendMsg = "160|" + sendRommNameText.getText().toString();
                        sc.sendData(sendMsg);
                    }
            }
        });

        //채팅방 입장
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String aucTitleName = titleList.get(position).trim();

                String sendMsg = "200|" + aucTitleName;
                sc.sendData(sendMsg);

                Toast.makeText(getApplicationContext(), aucTitleName, Toast.LENGTH_SHORT).show();

                Bundle extras = new Bundle();
                extras.putString("title", aucTitleName);
                extras.putString("userName", sendText.getText().toString().trim());

                Intent intent = new Intent(getApplicationContext(), AucChatActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

    }
}