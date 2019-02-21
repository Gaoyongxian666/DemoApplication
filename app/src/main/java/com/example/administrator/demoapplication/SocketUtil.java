package com.example.administrator.demoapplication;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

import static com.example.administrator.demoapplication.MainActivity.hexStrToBinaryStr;

public class SocketUtil {

    private static final String TAG = SocketUtil.class.getSimpleName();
//    public static String ADDRESS = "192.168.200.47";
//    public static int PORT = 4466;

    public static final int SUCCESS = 100;
    public static final int FAILED = -1;

    /**
     * 读数据
     *
     * @param bufferedReader
     */
    public static String readFromStream(BufferedReader bufferedReader) {
        Log.e(TAG,"readFromStream:开始从socket流中读入");
        try {
            String s;
            Log.e(TAG,"readLine：从socket流中读入一行");
            if ((s = bufferedReader.readLine()) != null) {
                Log.e(TAG,s);
                return s;
            }
        } catch (IOException e) {
            // 客户端已经断开
            Log.e(TAG,"客户端已经断开");
            Log.e(TAG,"IOException");


            e.printStackTrace();
        }
        return null;
    }

    /**
     * 写数据
     *
     * @param data
     * @param printWriter
     */
    public static void write2Stream(String data, OutputStream printWriter) throws IOException {
        if (data == null) {
            return;
        }
        if (printWriter != null) {
            byte[] bstream=hexStrToBinaryStr(data); //转化为字节流
            printWriter.write(bstream);
            Log.e(TAG,"readLine：从socket流中写入一行");

        }
    }


    /**
     * 关闭输入流
     *
     * @param socket
     */
    public static void inputStreamShutdown(Socket socket) {
        try {
            if (!socket.isClosed() && !socket.isInputShutdown()) {
                socket.shutdownInput();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭输入流
     *
     * @param br
     */
    public static void closeBufferedReader(InputStream br) {
        try {
            if (br != null) {
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭输出流
     *
     * @param pw
     */
    public static void closePrintWriter(OutputStream pw) throws IOException {
        if (pw != null) {
            pw.close();
        }
    }

    /**
     * 阻塞线程,millis为0则永久阻塞,知道调用notify()
     */
    public static void toWait(Object o, long millis) {
        synchronized (o) {
            try {
                o.wait(millis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * notify()调用后，并不是马上就释放对象锁的，而是在相应的synchronized(){}语句块执行结束，自动释放锁后
     *
     * @param o
     */
    public static void toNotifyAll(Object o) {
        synchronized (o) {
            o.notifyAll();
        }
    }

}