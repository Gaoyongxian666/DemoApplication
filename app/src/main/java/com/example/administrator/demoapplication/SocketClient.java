package com.example.administrator.demoapplication;


// 客户端线程开启三个线程：接收线程,发送线程和心跳包线程，
// 接收线程无消息时会堵塞在bufferedReader.readLine()处
// 发送线程无发送消息时阻塞，有发送消息时唤醒
// 使用ConcurrentLinkedQueue来作为消息队列并将发送线程和心跳包线程的输出流加锁来防止粘包，
// 心跳包线程每隔五秒发送一次，并且进行sendUrgentData(0xFF)操作，判断连接是否断开,
// 去除sendUrgentData,防止windows系统下发送多次后断开的问题。



import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class SocketClient {

    private volatile Thread Processing_001=null;
    private static final String TAG = SocketClient.class.getSimpleName();
    private SocketClientThread socketClientThread;

    public SocketClient(SocketClientResponseInterface socketClientResponseInterface,String ip,String port) throws InterruptedException {
        socketClientThread = new SocketClientThread("socketClientThread", socketClientResponseInterface,ip,port);
        socketClientThread.start();
        Log.i(TAG, "socketClientThread已经开启");
        socketClientThread.join();
        //ThreadPoolUtil.getInstance().addExecuteTask(socketClientThread);
    }

    public <T> void sendData(T data) {
        //convert to string or serialize object
        String s = (String) data;
        if (TextUtils.isEmpty(s)) {
            Log.e(TAG, "sendData: 消息不能为空");
            return;
        }
        if (socketClientThread != null) {
            socketClientThread.sendMsg(s);
        }else {
            Log.i(TAG, "发送消息失败，socketClientThread为空");
        }
    }

    public synchronized void stopSocket() {
        //一定要在子线程内执行关闭socket等IO操作
        Processing_001=new Thread(() -> {
            final Thread currentThread = Thread.currentThread();
            currentThread.setName("Processing-001" );
            Log.e(TAG,"关闭连接并且要求可以重连");
            socketClientThread.setReConnect(true);
            try {
                socketClientThread.stopThread();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Processing_001.start();



    }
}