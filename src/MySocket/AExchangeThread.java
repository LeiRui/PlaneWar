package MySocket;


import java.io.*;
import java.net.Socket;

import controller.AController;

/**
 * 进程通信线程
 */

public class AExchangeThread implements Runnable {
    private Socket socket;
    BufferedReader bufferedReader;
    BufferedWriter bufferedWriter;

    public static boolean isNum(String str){
        return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }

    public AExchangeThread(Socket socket) {
        this.socket = socket;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }catch (Exception e){
            e.printStackTrace();
        }
        new Thread(this).start();
    }
    public void run() {
        try {
            while(true) {
                //读
                String mess = bufferedReader.readLine();
                if(mess.startsWith("Name")){
                    String ss[] = mess.split(",");
                    String name = ss[1];
                    System.out.println("name:"+name);
                    AController.Controller.remoteName(name);
                }
                else {
                    switch (mess) {
                        case "Up":
                            AController.Controller.remoteUp();
                            break;
                        case "Down":
                            AController.Controller.remoteDown();
                            break;
                        case "Left":
                            AController.Controller.remoteLeft();
                            break;
                        case "Right":
                            AController.Controller.remoteRight();
                            break;
                        case "Space":
                            AController.Controller.remoteSpace();
                            break;
                        case "Pause":
                            AController.Controller.remotePause();
                            break;
                        case "Resume":
                            AController.Controller.remoteResume();
                            break;

                    }
                }
            }
        } catch (Exception e) {
            System.out.println("服务器 run 异常: " + e.getMessage());
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception e) {
                    socket = null;
                    System.out.println("服务端 finally 异常:" + e.getMessage());
                }
            }
        }
    }

    public void sendMessage(String str){
        //写
        try {
            bufferedWriter.write(str);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}