package view;


import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.Buffer;
import java.util.*;
import java.util.List;

import constants.ConstantUtil;
import controller.AController;
import controller.BController;
import entity.*;

public class GamePanel extends JPanel {
    public ArrayList<Plane> EnemyPlanes;
    public ArrayList<Bullet> EnemyBullets;
    public HeroPlane A,B;
    public ArrayList<Bullet> ABullets,BBullets;
    public int AScore,BScore;

    public Background bg;

    public boolean isGameOver;
    public String APlayerName,BPlayerName;
    public long timeused;

    public GamePanel() {
        setLayout(null);
        isGameOver=false;
        timeused=System.currentTimeMillis();

        EnemyPlanes=new ArrayList<Plane>();
        /*
        SuicideEnemyPlane ep=new SuicideEnemyPlane(50,0,50,50);
        EnemyPlanes.add(ep);
        */

        EnemyBullets=new ArrayList<Bullet>(); // 子弹方向一会儿再说
        A=new HeroPlane(ConstantUtil.GameWinWidth/4-ConstantUtil.SizeHeroPlane/2,
                ConstantUtil.GmaeWinHeight-ConstantUtil.SizeHeroPlane,
                ConstantUtil.SizeHeroPlane,ConstantUtil.SizeHeroPlane);
        B=new HeroPlane(ConstantUtil.GameWinWidth*3/4-ConstantUtil.SizeHeroPlane/2,
                ConstantUtil.GmaeWinHeight-ConstantUtil.SizeHeroPlane,
                ConstantUtil.SizeHeroPlane,ConstantUtil.SizeHeroPlane);
        ABullets=new ArrayList<Bullet>();
        BBullets=new ArrayList<Bullet>();
        AScore=0;
        BScore=0;

        bg=new Background(0,0,ConstantUtil.GameWinWidth,ConstantUtil.GmaeWinHeight);

    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        bg.Draw(g);

        for(int i = 0; i<EnemyPlanes.size(); i++) {
            EnemyPlanes.get(i).Draw(g);
        }

        for(int i = 0; i<EnemyBullets.size(); i++) {
            EnemyBullets.get(i).Draw(g);
        }

        for(int i=0; i<ABullets.size(); i++){
            ABullets.get(i).Draw(g);
        }

        for(int i=0; i<BBullets.size(); i++){
            BBullets.get(i).Draw(g);
        }

        A.Draw(g);
        B.Draw(g);

        g.setColor(Color.WHITE);
        g.setFont(new Font("黑体", Font.BOLD, 16));
        g.drawString("ctrl+z: Pause",40,40);
        g.drawString("ctrl+a: Resume",40,70);
        g.drawString("Player A Score: "+Integer.toString(AScore), 40,100);
        g.drawString("Player B Score: "+Integer.toString(BScore), 40,130);

        if(isGameOver) {
            g.drawString("Game Over", ConstantUtil.GameWinWidth/2-25,ConstantUtil.GmaeWinHeight/3);

            //读取历史成绩并显示
            try {
                File f = new File(ConstantUtil.ScoreRecordPath);
                BufferedReader reader = new BufferedReader(new FileReader(f));
                List<String> strs= new ArrayList<String>();
                String line = "";
                while((line = reader.readLine())!=null ) {
                    strs.add(line);
                }
                reader.close();

                String res="";
                res="Name,"+APlayerName+",Score,"+Integer.toString(AScore)+",Time,"
                        +Double.toString(timeused/1000);
                strs.add(res);
                res="Name,"+BPlayerName+",Score,"+Integer.toString(BScore)+",Time,"+Double.toString(timeused/1000);
                strs.add(res);

                Collections.sort(strs, new myComparator()); // core
                //显示排名前五
                for(int i=0;i<strs.size() && i<5; i++){
                    g.drawString(strs.get(i), ConstantUtil.GameWinWidth/2-30,
                            ConstantUtil.GmaeWinHeight/3+20+20*i);
                }

                FileWriter writer = new FileWriter(ConstantUtil.ScoreRecordPath);
                for(String val : strs) {
                    writer.write(val);
                    writer.write("\r\n"); //\n as new lines is viable on printstreams but not writing to files.
                }
                writer.close();


            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    //自定义的比较器，先比较得分，后比较通关时间
    private static class myComparator implements Comparator<String> {
        public int compare(String str1, String str2) {
            String [] ss1 = str1.split(","); //replaces 1 or more spaces.
            String [] ss2 = str2.split(",");
            int num1 = Integer.parseInt(ss1[3]);
            int num2 = Integer.parseInt(ss2[3]);
            int result = num1-num2;
            if(result > 0)
                return -1;
            else if(result < 0)
                return 1;
            else{
                Double t1=Double.parseDouble(ss1[5]);
                Double t2=Double.parseDouble(ss1[5]);
                Double com=t1-t2;
                if(com < 0){
                    return -1;
                }
                else if(com > 0){
                    return 1;
                }
                else {
                    return 0;
                }
            }
        }
    }




}
