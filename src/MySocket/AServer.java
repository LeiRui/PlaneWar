package MySocket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class AServer {
    //    public static final int PORT = 12345;//监听的端口号
    private static AExchangeThread serverExchangeThread;

    public static void Init(int PORT){
        try {
            ServerSocket ss = new ServerSocket(PORT);
            System.out.println("端口号"+PORT+",服务器已启动");
            Socket s = ss.accept(); //阻塞等待客户端连接
            // 启动交流线程
            serverExchangeThread=new AExchangeThread(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static AExchangeThread getExchangeThread(){
        return serverExchangeThread;
    }

}