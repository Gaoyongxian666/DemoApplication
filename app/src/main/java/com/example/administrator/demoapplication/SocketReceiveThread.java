package com.example.administrator.demoapplication;


import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

import static com.example.administrator.demoapplication.MainActivity.socketClient;

public class SocketReceiveThread extends Thread {

    private static final String TAG = SocketReceiveThread.class.getSimpleName();

    private volatile long lastReceiveTime = System.currentTimeMillis();

    private volatile String name;

    private volatile boolean isCancel = false;

    private InputStream bufferedReader;

    private SocketCloseInterface socketCloseInterface;

    private SocketClientResponseInterface socketClientResponseInterface;



    public SocketReceiveThread(String name, InputStream bufferedReader,
                               SocketClientResponseInterface socketClientResponseInterface,
                               SocketCloseInterface socketCloseInterface) {
        this.name = name;
        this.bufferedReader = bufferedReader;
        this.socketClientResponseInterface = socketClientResponseInterface;
        this.socketCloseInterface = socketCloseInterface;
    }

    public void run() {
        final Thread currentThread = Thread.currentThread();
        currentThread.setName("SocketReceiveThread" );
        byte b[]=new byte[1024];
        Log.e(TAG,"SocketReceiveThread已经开启");
        int bytes;
        try {
            while (!isInterrupted()) {
                while (!isCancel) {
                    lastReceiveTime = System.currentTimeMillis();

                    Message msg = new Message();
                    msg.what = 1;
                    Bundle bundle = new Bundle();
                    bundle.putLong("time",lastReceiveTime);  //往Bundle中存放数据
                    msg.setData(bundle);//mes利用Bundle传递数据
                    MainActivity.handler.sendMessage(msg);//用activity中的handler发送消息

                    Log.e(TAG, "SocketReceiveThread start");
                    if (bufferedReader != null) {
                        bytes = bufferedReader.read(b);
                        if (bytes != -1) {
                            Log.e(TAG, "bytes: " + bytes);
                            String receiverData = bytes2HexString(subBytes(b, 0, bytes));
                            if (receiverData != null) {
                                Log.e(TAG, "run: receiverData是" + receiverData);
                                successMessage(receiverData);
                            } else {
                                Log.e(TAG, "run: receiverData为空");
                                break;
                            }
                        }
                    } else {
                        Log.e(TAG, "bufferedReader==null");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            //循环结束则退出输入流，说明出现了问题bufferedReader不为空，但是读出的数据是null
            Log.e(TAG, "SocketReceiveThread已经关闭");
        }
        System.out.println("SocketReceiveThread已经关闭");
    }

    public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        System.arraycopy(src, begin, bs, 0, count);
        return bs;
    }

    public static String bytes2HexString(byte[] b) {
        StringBuffer result = new StringBuffer();
        String hex;
        for (int i = 0; i < b.length; i++) {
            hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            result.append(hex.toUpperCase());
        }
        return result.toString();
    }

    private void successMessage(String data) {
        if (socketClientResponseInterface != null) {
            Log.e("successMessage",data);
            socketClientResponseInterface.onSocketReceive(data, SocketUtil.SUCCESS);
        }
    }

    public void close() throws IOException {
        isCancel = true;
        this.interrupt();

        if (bufferedReader != null) {

            synchronized (bufferedReader) {
                SocketUtil.closeBufferedReader(bufferedReader);
            }
            bufferedReader=null;
        }
    }

}