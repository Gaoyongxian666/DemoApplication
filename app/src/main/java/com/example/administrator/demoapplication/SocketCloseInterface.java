package com.example.administrator.demoapplication;

import java.io.IOException;

public interface SocketCloseInterface {

    /**
     * 客户端收到服务端消息回调
     */
    void onSocketShutdownInput();

    /**
     * 客户端关闭回调
     */
    void onSocketDisconnection() throws IOException;
}