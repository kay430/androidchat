package com.auc.chat;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketService extends Thread {

    Socket socket;
    ObjectOutputStream outstream;
    ObjectInputStream instream;

    String receiveData;

    Handler handler;

    public SocketService(Handler handler){
        this.handler = handler;
    }

    public void setHandler(Handler handler){
        this.handler = handler;
    }

    //소켓 접속
    public void getSocket() {
        new Thread() {
            public void run() {
                try {
//                    socket = new Socket("192.168.0.119", 5000); //wifi
                    socket = new Socket("192.168.0.138", 5000);
                    outstream = new ObjectOutputStream(socket.getOutputStream());
                    outstream.writeObject("100|\r\n");
                    outstream.flush();

                    instream = new ObjectInputStream(socket.getInputStream());
                    while (true) {
                        Object input = (String) instream.readObject();
                        receiveData((String)input);
                        //handler.post(new MainActivity.msgUpdate((String)input));
                        //handler.sendMessage((String)input);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //데이터 송신
    public void sendData(String sendMsg){
        new Thread(){
            @Override
            public void run(){
                try {
                    outstream.writeObject(sendMsg + "\r\n");
                    outstream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //데이터 수신
    public String receiveData(String receiveMsg){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("data", receiveMsg);
                receiveData = receiveMsg;
                String[] msgs;
                msgs = receiveMsg.split("\\|");

                Message message = handler.obtainMessage();

                message.what = Integer.parseInt(msgs[0]);
                message.obj = receiveMsg;
                handler.sendMessage(message);

            }
        }).start();

        return receiveData;

    }



}
