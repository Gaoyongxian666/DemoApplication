package com.example.administrator.demoapplication;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentLinkedQueue;


public class SocketSendThread extends Thread {

    private static final String TAG = SocketSendThread.class.getSimpleName();

    private volatile String name;

    private volatile boolean isCancel = false;
    private boolean closeSendTask;
    private final OutputStream printWriter;

    protected volatile ConcurrentLinkedQueue<String> dataQueue = new ConcurrentLinkedQueue<>();

    public SocketSendThread(String name, OutputStream printWriter) {
        this.name = name;
        this.printWriter = printWriter;
    }

    @Override
    public void run() {
        final Thread currentThread = Thread.currentThread();
        currentThread.setName("SocketSendThread");
        Log.e(TAG, "SocketSendThread已经开启");

        try {
            while (!isCancel) {
                String dataContent = dataQueue.poll();

                if (dataContent == null) {
                    //没有发送数据则等待
                    Log.e(TAG,"dataContent == null");
                    SocketUtil.toWait(dataQueue, 0);
                    if (closeSendTask) {
                        //notify()调用后，并不是马上就释放对象锁的，所以在此处中断发送线程
                        close();
                    }
                } else if (printWriter != null) {
                    Log.e(TAG,"dataContent ！= null");
                    synchronized (printWriter) {
                        Log.e(TAG,dataContent);

                        SocketUtil.write2Stream(dataContent, printWriter);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //循环结束则退出输出流
            if (printWriter != null) {
                synchronized (printWriter) {
                    try {
                        SocketUtil.closePrintWriter(printWriter);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            Log.e(TAG, "SocketSendThread已经关闭");
        }
    }


    public void sendMsg(String data) {
        dataQueue.add(data);
        //有新增待发送数据，则唤醒发送线程
        SocketUtil.toNotifyAll(dataQueue);
    }

    public void clearData() {
        dataQueue.clear();
    }

    public void close() throws IOException {
        isCancel = true;
        this.interrupt();
        if (printWriter != null) {
            //防止写数据时停止，写完再停
            Log.e(TAG,"1printWriter != null");

            synchronized (printWriter) {
                SocketUtil.closePrintWriter(printWriter);
            }
        }
    }

    // 唤醒发送消息
    public void wakeSendTask() {
        closeSendTask = true;
        SocketUtil.toNotifyAll(dataQueue);
    }

    public void setCloseSendTask(boolean closeSendTask) {
        this.closeSendTask = closeSendTask;
    }
}
