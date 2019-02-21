package com.example.administrator.demoapplication;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 心跳实现，频率5秒
 * Created by gavinandre on 18-3-13.
 */
public class SocketHeartBeatThread extends Thread {

    private static final String TAG = SocketHeartBeatThread.class.getSimpleName();

    private volatile String name;

    private static final int REPEAT_TIME = 5000;
    private boolean isCancel = false;
    private final OutputStream printWriter;
    private Socket mSocket;

    private SocketCloseInterface socketCloseInterface;

    public SocketHeartBeatThread(String name, OutputStream printWriter,
                                 Socket mSocket, SocketCloseInterface socketCloseInterface) {
        this.name = name;
        this.printWriter = printWriter;
        this.mSocket = mSocket;
        this.socketCloseInterface = socketCloseInterface;
    }

    public void run() {
        final Thread currentThread = Thread.currentThread();
        currentThread.setName("SocketHeartBeatThread");
        Log.e(TAG,"SocketHeartBeatThread已经开启");

        try {
            while (!isCancel) {
                if (printWriter != null) {

                    synchronized (printWriter) {
                        SocketUtil.write2Stream("FE 02 00 00 00 04 6D C6", printWriter);
                    }
                }
                try {
                    Thread.sleep(REPEAT_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //循环结束则退出输入流
            if (printWriter != null) {
                synchronized (printWriter) {
                    try {
                        SocketUtil.closePrintWriter(printWriter);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            Log.e(TAG, "SocketHeartBeatThread已经关闭");
        }
    }

    private boolean isConnected() throws IOException {
        if (mSocket.isClosed() || !mSocket.isConnected() ||
                mSocket.isInputShutdown() || mSocket.isOutputShutdown()) {
            if (socketCloseInterface != null) {
                socketCloseInterface.onSocketDisconnection();
            }
            return false;
        }
        return true;
    }

    public void close() throws IOException {
        isCancel = true;
        if (printWriter != null) {
            Log.e(TAG,"1printWriter != null");
            synchronized (printWriter) {
                SocketUtil.closePrintWriter(printWriter);
            }
        }
    }

}