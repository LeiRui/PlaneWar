package MySocket;


import java.io.*;
import java.net.Socket;

import constants.ConstantUtil;
import controller.BController;

/**
 * 进程通信线程
 */

public class BExchangeThread implements Runnable {
    private Socket socket;
    BufferedReader bufferedReader;
    BufferedWriter bufferedWriter;

    public static boolean isNum(String str){
        return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }

    public BExchangeThread(Socket socket) {
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
                    String name = mess.split(",")[1];
                    BController.Controller.remoteName(name);
                }
                else if(mess.startsWith("Enemy")){ // 来自A的敌机生成消息
                    String[] info = mess.split(",");
                    if(info[1].equals(ConstantUtil.PlaneType.GeneralEnemy.name())) {//普通敌机
                        int rx = Integer.parseInt(info[2]);
                        int ry = Integer.parseInt(info[3]);
                        BController.Controller.remoteGenerator(ConstantUtil.PlaneType.GeneralEnemy,rx,ry);
                    }
                    else if(info[1].equals(ConstantUtil.PlaneType.GeneralSuicideEnemy.name())){//自杀式敌机
                        int rx = Integer.parseInt(info[2]);
                        int ry = Integer.parseInt(info[3]);
                        String obj = info[4]; //自杀式飞机多一个信息：攻击目标
                        BController.Controller.remoteGenerator(ConstantUtil.PlaneType.GeneralSuicideEnemy,rx,ry,
                                ConstantUtil.HeroType.valueOf(obj));
                    }
                    else {//加强敌机
                        int rx = Integer.parseInt(info[2]);
                        int ry = Integer.parseInt(info[3]);
                        BController.Controller.remoteGenerator(ConstantUtil.PlaneType.GeneralStrongEnemy,rx,ry);
                    }
                }
                else if(mess.startsWith("Bullet")){
                    String[] info = mess.split(",");
                    int rx = Integer.parseInt(info[1]);
                    int ry = Integer.parseInt(info[2]);
                    BController.Controller.remoteGenerator(rx,ry);
                }
                else {
                    switch (mess) {
                        case "Up":
                            BController.Controller.remoteUp();
                            break;
                        case "Down":
                            BController.Controller.remoteDown();
                            break;
                        case "Left":
                            BController.Controller.remoteLeft();
                            break;
                        case "Right":
                            BController.Controller.remoteRight();
                            break;
                        case "Space":
                            BController.Controller.remoteSpace();
                            break;
                        case "Pause":
                            BController.Controller.remotePause();
                            break;
                        case "Resume":
                            BController.Controller.remoteResume();
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