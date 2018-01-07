package view;

import controller.AController;
import controller.BController;
import controller.KeyController;
import MySocket.BClient;
import MySocket.AServer;
import constants.ConstantUtil;
import music.MusicPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class StartFrame extends JFrame {
    public StartFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MusicPlayer.bgmPlay();

        //设置标题
        this.setTitle("飞机大战");
        //设置窗体大小
        this.setSize(470, 410);
        //居中
        this.setLocationRelativeTo(null);
        //关闭事件
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //设置默认Panel
        this.setContentPane(new StartPanel());
    }

    class StartPanel extends JPanel {
        public JLabel label;
        public StartPanel() {
            setLayout(null);

            JButton btnMode1 = new JButton("创建房间");
            btnMode1.setFont(new Font("黑体", Font.BOLD, 16));
            btnMode1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    chooseMode(1);
                }
            });
            btnMode1.setBounds(315, 269, 123, 28);
            add(btnMode1);

            JButton btnMode2 = new JButton("进入房间");
            btnMode2.setFont(new Font("黑体", Font.BOLD, 16));
            btnMode2.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    chooseMode(2);
                }
            });
            btnMode2.setBounds(315, 307, 123, 28);
            add(btnMode2);

            label=new JLabel("");
            label.setBounds(470/2-25, 410/2-10,100,20);
            add(label);
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            URL fullURL=this.getClass().getClassLoader().getResource(ConstantUtil.ImgStartURL);
            Image img = new ImageIcon(fullURL).getImage();
            g.drawImage(img, 0,0,470,410,null);
        }

    }

    public void chooseMode(int mode){
        System.out.println("mode"+mode);
        switch (mode){
            case 1:
                String name1=JOptionPane.showInputDialog("请输入游戏昵称:");
                String port=JOptionPane.showInputDialog("请输入房间号:");
                AServer.Init(Integer.parseInt(port));
                System.out.println("连接成功");

                GamePanel panel1=new GamePanel();
                panel1.APlayerName=name1;

                this.setContentPane(panel1);
                AController.Controller=new AController(AServer.getExchangeThread(),panel1);
                //note: AExchangeThread里要等到上一步执行完过了才能执行，否则调用AController.Controller为null
                this.addKeyListener(new KeyController(AController.Controller));

                this.setTitle("双人飞机大战-玩家A:"+name1);
                this.setSize(ConstantUtil.GameWinWidth,ConstantUtil.GmaeWinHeight);
                AController.Controller.gameStart();
                break;
            case 2:
                String name2=JOptionPane.showInputDialog("请输入游戏昵称:");
                String port2=JOptionPane.showInputDialog("请输入房间号:");
                BClient.Init(Integer.parseInt(port2));
                System.out.println("连接成功");

                GamePanel panel2=new GamePanel();
                panel2.BPlayerName=name2;
                BController.Controller=new BController(BClient.getExchangeThread(),panel2);
                sendNameThread st=new sendNameThread(name2);
                st.start();

                this.setContentPane(panel2);
                this.addKeyListener(new KeyController(BController.Controller));

                this.setTitle("双人飞机大战-玩家B:"+name2);
                this.setSize(ConstantUtil.GameWinWidth,ConstantUtil.GmaeWinHeight);
                BController.Controller.gameStart();
                break;
        }
        requestFocus();
    }

    class sendNameThread extends Thread {
        String name;
        sendNameThread(String name){
            this.name=name;
        }
        public void run(){
            //sleep很重要 给AController实例化时间，否则AExchangeThread收到消息之后调用AController.Controller为null
            try{Thread.sleep(1000);} catch (Exception e){}
            BController.Controller.exchangeThread.sendMessage("Name,"+name); // 给对方发送队友游戏昵称
        }
    }


}
