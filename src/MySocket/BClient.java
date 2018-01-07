package MySocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BClient {
    //    public static final int PORT = 12345;//监听的端口号
    private static BExchangeThread clientExchangeThread;

    public static void Init(int PORT){
        try {
            Socket socket = new Socket("localhost",PORT);
            System.out.println("客户端IP:"+socket.getLocalAddress()+"端口"+socket.getPort());
            // 启动交流线程
            clientExchangeThread=new BExchangeThread(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static BExchangeThread getExchangeThread(){
        return clientExchangeThread;
    }
}
