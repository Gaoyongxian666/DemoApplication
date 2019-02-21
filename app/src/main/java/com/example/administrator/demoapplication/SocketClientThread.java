package com.example.administrator.demoapplication;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import javax.net.SocketFactory;


// 写数据采用死循环，没有数据时wait，有新消息时notify
// 在initSocket()的catch块内调用initSocket()，这样就能实现第一次连接超过10秒后重连
// 在stopThread()内调用initSocket()来实现通讯过程中由于网络环境较差导致的连接断开后的重连操作。

// socketclientsocket 不能够终止，因为主线程发送消息的客户端就是绑定在了这个线程，
// 我们并不是发送消息的时候才开启客户端线程，这个线程自从app打开之后就会一直运行。
// 在这个线程里我们又开启了：数据发送线程，心跳线程，接收线程。当连接出现问题的时候我们应该重启这三个线程
public class SocketClientThread extends Thread implements SocketCloseInterface {

    private static final String TAG = SocketClientThread.class.getSimpleName();

    private volatile String name;

    //  volatile具备两种特性，第一就是保证共享变量对所有线程的可见性。将一个共享变量声明为volatile后，会有以下效应：
    //  1.当写一个volatile变量时，JMM会把该线程对应的本地内存中的变量强制刷新到主内存中去；
    //  2.这个写会操作会导致其他线程中的缓存无效

    private boolean isLongConnection = true;
    private boolean isReConnect = true;
    private SocketSendThread mSocketSendThread;
    private SocketReceiveThread mSocketReceiveThread;
    private SocketHeartBeatThread mSocketHeartBeatThread;
    private Socket mSocket;
    private static String ip;
    private static String port;

    private boolean isSocketAvailable;

    private SocketClientResponseInterface socketClientResponseInterface;

    public SocketClientThread(String name, SocketClientResponseInterface socketClientResponseInterface,String ip,String port) {
        this.name = name;
        this.socketClientResponseInterface = socketClientResponseInterface;
        this.ip=ip;
        this.port=port;
    }

    public void run() {
        // 获取当前线程的名称并且改名
// main线程调用自己定义的线程，
// 构造方法中打印main，又因为执行run方法普通方法，并不是启动了一个线程，所以run方法中还是main
// start()方法显示打印结果,构造函数中肯定是主线程的名称，而run就会是新启线程的名称
// Thread.currentThread().getName()  this.getName()   setname()会影响currentThread的名称

// final关键字提高了性能。JVM和Java应用都会缓存final变量。
// final变量可以安全的在多线程环境下进行共享，而不需要额外的同步开销。
// 使用final关键字，JVM会对方法、变量及类进行优化。
// 不可变类
// 创建不可变类要使用final关键字。不可变类是指它的对象一旦被创建了就不能被更改了。
// String是不可变类的代表。不可变类有很多好处，譬如它们的对象是只读的，可以在多线程环境下安全的共享，
// 不用额外的同步开销等等。
// value3时一个引用变量，这里我们可以看到final修饰引用变量时，只是限定了引用变量的引用不可改变，
// 即不能将value3再次引用另一个Value对象，但是引用的对象的值是可以改变的，从内存模型中我们看的更加清晰：
        final Thread currentThread = Thread.currentThread();
        currentThread.setName("SocketClientThread" );
        Log.e(TAG, "Processing-SocketClientThread" );
        try {
            initSocket();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }


    private void initSocket() throws IOException {
        try {
            mSocket = SocketFactory.getDefault().createSocket();
            Log.e(TAG,"当前ip"+ip+"端口："+port);
            SocketAddress socketAddress = new InetSocketAddress(ip,Integer.valueOf(port));
            mSocket.connect(socketAddress, 5000);
            Log.e(TAG,"socket连接成功");
            isSocketAvailable = true;

            //开启接收线程
            mSocketReceiveThread = new SocketReceiveThread("SocketReceiveThread",
                    mSocket.getInputStream(),
                    socketClientResponseInterface, this);
            mSocketReceiveThread.start();
            OutputStream printWriter = mSocket.getOutputStream();

            //开启发送线程
            mSocketSendThread = new SocketSendThread("SocketSendThread", printWriter);
            mSocketSendThread.setCloseSendTask(false);
            mSocketSendThread.start();


            //开启心跳线程
            if (isLongConnection) {
                mSocketHeartBeatThread = new SocketHeartBeatThread("SocketHeartBeatThread",
                        printWriter, mSocket, this);
                mSocketHeartBeatThread.start();
            }

            if (socketClientResponseInterface != null) {
                socketClientResponseInterface.onSocketConnect();
            }

            Message msg = new Message();
            msg.what = 2;
            Bundle bundle = new Bundle();
            bundle.putString("zhuangtai","当前连接状态：正 常      ");  //往Bundle中存放数据
            bundle.putInt("FLAG",1);  //往Bundle中存放数据
            msg.setData(bundle);//mes利用Bundle传递数据
            MainActivity.handler.sendMessage(msg);//用activity中的handler发送消息

        } catch (ConnectException e) {
            Log.e(TAG,"服务器连接异常，请检查网络");
            failedMessage("服务器连接异常，请检查网络", SocketUtil.FAILED);
            e.printStackTrace();
            stopThread();
        } catch (IOException e) {
            Log.e(TAG,"网络发生异常，请稍后重试");
            failedMessage("网络发生异常，请稍后重试", SocketUtil.FAILED);
            e.printStackTrace();
            stopThread();
        }
    }


    public void sendMsg(String data) {
        if (mSocketSendThread != null) {
            mSocketSendThread.sendMsg(data);
        }
    }


    public synchronized void stopThread() throws IOException {
        Log.e(TAG,"开始关闭三个tcp处理线程");

        //关闭接收线程
        closeReceiveTask();
        //唤醒发送线程并关闭
        wakeSendTask();
        //关闭心跳线程
        closeHeartBeatTask();
        //关闭socket
        closeSocket();
        //清除数据
        clearData();
        failedMessage("断开连接", SocketUtil.FAILED);
        if (isReConnect) {
            isReConnect=false;

            Message msg = new Message();
            msg.what = 2;
            Bundle bundle = new Bundle();
            bundle.putString("zhuangtai","当前连接状态：重连中      ");  //往Bundle中存放数据
            bundle.putInt("FLAG",0);  //往Bundle中存放数据
            msg.setData(bundle);//mes利用Bundle传递数据
            MainActivity.handler.sendMessage(msg);//用activity中的handler发送消息
            initSocket();
        }
    }

    /**
     * 唤醒后关闭发送线程
     */
    private void wakeSendTask() {
        if (mSocketSendThread != null) {
            mSocketSendThread.wakeSendTask();
        }
    }


    private void closeReceiveTask() throws IOException {
        if (mSocketReceiveThread != null) {
            mSocketReceiveThread.close();
            mSocketReceiveThread = null;
        }
    }

    /**
     * 关闭心跳线程
     */
    private void closeHeartBeatTask() throws IOException {
        if (mSocketHeartBeatThread != null) {
            mSocketHeartBeatThread.close();
        }
    }

    /**
     * 关闭socket
     */
    private void closeSocket() {
        if (mSocket != null) {
            if (!mSocket.isClosed() && mSocket.isConnected()) {
                try {
                    mSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            isSocketAvailable = false;
            mSocket = null;
        }
    }

    /**
     * 清除数据
     */
    private void clearData() {
        if (mSocketSendThread != null) {
            mSocketSendThread.clearData();
        }
    }

    /**
     * 连接失败回调
     */
    private void failedMessage(String msg, int code) {
        if (socketClientResponseInterface != null) {
            socketClientResponseInterface.onSocketDisable(msg, code);
        }
    }

    @Override
    public void onSocketShutdownInput() {
        if (isSocketAvailable) {
            SocketUtil.inputStreamShutdown(mSocket);
        }
    }

    @Override
    public void onSocketDisconnection() throws IOException {
        isSocketAvailable = false;
        stopThread();
    }

    /**
     * 设置是否断线重连
     */
    public void setReConnect(boolean reConnect) {
        isReConnect = reConnect;
    }

}
