package com.example.administrator.demoapplication;


//接口用来产生回调
//接口类，接口实现类，控制类（在控制类里导入接口类，在控制类中的一个a方法中调用接口类的方法）
//结果就是在我们使用的测试类中定义控制类的对象并传入实现类的对象，然后使用它的a方法就可以完成调用接口实现类的那个方法
public interface SocketClientResponseInterface<T> {
    //客户端连接回调
    void onSocketConnect();

    //客户端收到服务端消息回调
    void onSocketReceive(T socketResult, int code);

    //客户端关闭回调
    void onSocketDisable(String msg, int code);
}
