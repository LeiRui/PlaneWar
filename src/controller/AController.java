package controller;

import MySocket.AExchangeThread;
import constants.ConstantUtil;
import entity.*;
import music.MusicPlayer;
import view.GamePanel;

import java.awt.*;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class AController extends Controller{
    public static AController Controller;

    GamePanel panel;
    private Timer timer;
    private boolean isRunning =false;

    public AExchangeThread exchangeThread; // 远程通信用的线程

    public AController(AExchangeThread exchangeThread, GamePanel panel) {
        this.exchangeThread=exchangeThread;// 收发消息
        this.panel=panel;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void gameStart(){
        isRunning =true;

        timer = new Timer();
        timer.schedule(new AController.GameTask(), 100,30);
    }

    private class GameTask extends TimerTask {
        int freqPlane=5; // 控制敌机产生频率
        int freqBullet=30;
        public void run() {
            if(!isRunning){
                return ;
            }

            /*
                AController要负责动态产生敌机和敌机子弹，并把产生消息通过socket exchangeThread传给B玩家
                B玩家根据产生消息绘制画面并执行游戏逻辑判断
             */

            //敌机生成
            if(freqPlane<=0) {
                if (panel.EnemyPlanes.size() < 3) {
                    Random random = new Random();
                    int choose = random.nextInt(3);
                    if (choose == 0) { // 普通敌机
                        int rx = random.nextInt(ConstantUtil.GameWinWidth);
                        EnemyPlane ep = new EnemyPlane(rx,
                                0, ConstantUtil.SizeEnemyPlane, ConstantUtil.SizeEnemyPlane);
                        panel.EnemyPlanes.add(ep);
                        //发消息
                        exchangeThread.sendMessage("Enemy," + ConstantUtil.PlaneType.GeneralEnemy.name() + ","
                                + String.valueOf(rx) + ",0");
                    } else if (choose == 1) { // 自杀式敌机
                        int rx = random.nextInt(ConstantUtil.GameWinWidth);
                        SuicideEnemyPlane sp = new SuicideEnemyPlane(rx,
                                0, ConstantUtil.SizeEnemyPlane, ConstantUtil.SizeEnemyPlane);
                        panel.EnemyPlanes.add(sp);
                        //发消息
                        exchangeThread.sendMessage("Enemy," + ConstantUtil.PlaneType.GeneralSuicideEnemy.name() + ","
                                + String.valueOf(rx) + ",0," + sp.attackObj.name());
                    } else { // 加强敌机
                        int rx = random.nextInt(ConstantUtil.GameWinWidth);
                        StrongPlane sp = new StrongPlane(rx,
                                0, ConstantUtil.SizeEnemyPlane, ConstantUtil.SizeEnemyPlane);
                        panel.EnemyPlanes.add(sp);
                        //发消息
                        exchangeThread.sendMessage("Enemy," + ConstantUtil.PlaneType.GeneralStrongEnemy.name() + ","
                                + String.valueOf(rx) + ",0");
                    }
                }

                //子弹生成
                if(freqBullet<=0) {
                    if(panel.EnemyBullets.size()<8) {
                        Random random = new Random();
                        for (int i = 0; i < panel.EnemyPlanes.size(); i++) {
                            Plane p = panel.EnemyPlanes.get(i);
                            if (!p.isValid())
                                continue;
                            if (random.nextInt(2) == 0) {
                                int rx=p.x+40;
                                int ry=p.y+60;
                                Bullet b = new Bullet(rx, ry, ConstantUtil.SizeBullet, ConstantUtil.SizeBullet,
                                        ConstantUtil.BulletEnemy); //子弹方向默认Down,此处无需修改
                                panel.EnemyBullets.add(b);
                                //发消息
                                exchangeThread.sendMessage("Bullet," + String.valueOf(rx) + "," + String.valueOf(ry));
                            }
                        }
                    }
                    freqBullet = 30;//计数重置
                }

                freqPlane=5; //计数重置
            }
            else {
                freqPlane--;
                freqBullet--;
            }

            GameStateUpdate();

        }
    }

    public void GameStateUpdate() { // 更新游戏状态，包括碰撞检测处理
        panel.bg.move();

        for(int i=0; i<panel.ABullets.size();i++){
            panel.ABullets.get(i).move();
        }
        for(int i=0; i<panel.BBullets.size();i++){
            panel.BBullets.get(i).move();
        }
        for(int i=0; i<panel.EnemyBullets.size();i++){
            panel.EnemyBullets.get(i).move();
        }
        for(int i=0; i<panel.EnemyPlanes.size();i++){
            Plane p=panel.EnemyPlanes.get(i);
            if(p.isValid()) {
                if(p.type == ConstantUtil.PlaneType.GeneralSuicideEnemy) { // 自杀式飞机的move独特
                   if(((SuicideEnemyPlane)p).attackObj==ConstantUtil.HeroType.A) {
                       ((SuicideEnemyPlane)p).move(panel.A.x,panel.A.y);
                   }
                   else{
                        ((SuicideEnemyPlane)p).move(panel.B.x,panel.B.y);
                   }
                }
                else {
                    panel.EnemyPlanes.get(i).move();
                }
            }
        }

        for(int i=0;i<panel.ABullets.size();i++){
            Bullet b=panel.ABullets.get(i);
            if(!b.valid)
                continue;
            Rectangle rec = b.GetRectangle();
            for(int j=0;j<panel.EnemyPlanes.size();j++){
                Plane ep=panel.EnemyPlanes.get(j);
                if(ep.isValid() && ep.GetRectangle().intersects(rec)) {
                    ep.hit(); //被击中
                    if(panel.A.isValid()) //判断A还没死，A的子弹才加分
                        panel.AScore++;
                    b.valid=false; // 子弹失效
                    break;
                }
            }
        }

        for(int i=0;i<panel.BBullets.size();i++){
            Bullet b=panel.BBullets.get(i);
            if(!b.valid)
                continue;
            Rectangle rec = b.GetRectangle();
            for(int j=0;j<panel.EnemyPlanes.size();j++){
                Plane ep=panel.EnemyPlanes.get(j);
                if(ep.isValid() && ep.GetRectangle().intersects(rec)) {
                    ep.hit(); //被击中
                    if(panel.B.isValid()) //判断A还没死，A的子弹才加分
                        panel.BScore++;
                    b.valid=false; // 子弹失效
                    break;
                }
            }
        }

        for(int i=0; i<panel.EnemyBullets.size();i++){
            Bullet b=panel.EnemyBullets.get(i);
            if(!b.isValid()){
                continue;
            }
            Rectangle rec=b.GetRectangle();
            if(panel.A.isValid()){
                if(panel.A.GetRectangle().intersects(rec)) {
                    panel.A.hit();
                    b.valid=false;
                    continue;
                }
            }
            if(panel.B.isValid()){
                if(panel.B.GetRectangle().intersects(rec)){
                    panel.B.hit();
                    b.valid=false;
                }
            }
        }

        if(panel.A.isValid()){ // 躲过子弹，飞机碰撞检测
            for(int i=0; i<panel.EnemyPlanes.size();i++){
                Plane ep = panel.EnemyPlanes.get(i);
                if(!ep.isValid()){
                    continue;
                }
                if(ep.GetRectangle().intersects(panel.A.GetRectangle())){
                    ep.hit();
                    panel.A.hit();
                    break;
                }
            }
        }

        if(panel.B.isValid()){ // 躲过子弹，飞机碰撞检测
            for(int i=0; i<panel.EnemyPlanes.size();i++){
                Plane ep = panel.EnemyPlanes.get(i);
                if(!ep.isValid()){
                    continue;
                }
                if(ep.GetRectangle().intersects(panel.B.GetRectangle())){
                    ep.hit();
                    panel.B.hit();
                    break;
                }
            }
        }

        if(!panel.A.isValid() && !panel.B.isValid()) {
            long tmp=System.currentTimeMillis();
            panel.timeused=tmp-panel.timeused; // 计时
            System.out.print("gameover");

            isRunning=false;

            panel.isGameOver=true;
        }

        removeInvalid();
        panel.repaint();
    }


    public void removeInvalid() { // 移除所有失效的子弹和飞机
        for(Iterator<Plane> iterator = panel.EnemyPlanes.iterator(); iterator.hasNext();) {
            Plane p=iterator.next();
            if(p.state == ConstantUtil.PlaneState.dead) {
                iterator.remove();
            }
        }
        for(Iterator<Bullet> iterator = panel.EnemyBullets.iterator(); iterator.hasNext();) {
            Bullet b=iterator.next();
            if(!b.isValid()) {
                iterator.remove();
            }
        }
    }

    public void keyUp() {
        if(!isRunning) return;
        if(!panel.A.isValid()) return;

        panel.A.dir=ConstantUtil.Dir.Up;
        panel.A.move();
        if(exchangeThread!=null)
            exchangeThread.sendMessage("Up");
        panel.repaint();

    }

    public void keyDown() {
        if(!isRunning) return;
        if(!panel.A.isValid()) return;

        panel.A.dir=ConstantUtil.Dir.Down;
        panel.A.move();
        if(exchangeThread!=null)
            exchangeThread.sendMessage("Down");
        panel.repaint();

    }

    public void keyLeft() {
        if(!isRunning) return;
        if(!panel.A.isValid()) return;

        panel.A.dir=ConstantUtil.Dir.Left;
        panel.A.move();
        if(exchangeThread!=null)
            exchangeThread.sendMessage("Left");
        panel.repaint();

    }

    public void keyRight() {
        if(!isRunning) return;
        if(!panel.A.isValid()) return;

        panel.A.dir=ConstantUtil.Dir.Right;
        panel.A.move();
        if(exchangeThread!=null)
            exchangeThread.sendMessage("Right");
        panel.repaint();

    }


    public void keySpace()  { // 开火
        if(!isRunning) return;
        if(!panel.A.isValid()) return;

        Bullet b=new Bullet(panel.A.x+40, panel.A.y-10,ConstantUtil.SizeBullet,
                ConstantUtil.SizeBullet,ConstantUtil.BulletHero);
        b.dir=ConstantUtil.Dir.Up;
        panel.ABullets.add(b);
        if(exchangeThread!=null)
            exchangeThread.sendMessage("Space");
        panel.repaint();
    }

    /**
     * 按键暂停，此时向远程发送暂停命令
     */
    public void keyPause()  {
        isRunning =false;
        if(MusicPlayer.isRunning()){
            MusicPlayer.bgmStop();
        }
        if(exchangeThread!=null)
            exchangeThread.sendMessage("Pause");
    }

    /**
     * 按键恢复，此时向远程发送恢复命令
     */
    public void keyResume() {
        isRunning =true;
        if(exchangeThread!=null)
            exchangeThread.sendMessage("Resume");
    }


    /**
     * 以上函数接受本地键盘指令
     * 以下函数接受远程的指令
     */
    public void remotePause()  {
        isRunning =false;
        if(MusicPlayer.isRunning()){
            MusicPlayer.bgmStop();
        }
    }

    public void remoteResume() {
        isRunning =true;
    }

    public void remoteUp() {
        if(!isRunning) return;

        panel.B.dir=ConstantUtil.Dir.Up;
        panel.B.move();
        panel.repaint();

    }

    public void remoteDown() {
        if(!isRunning) return;

        panel.B.dir=ConstantUtil.Dir.Down;
        panel.B.move();
        panel.repaint();

    }

    public void remoteLeft() {
        if(!isRunning) return;

        panel.B.dir=ConstantUtil.Dir.Left;
        panel.B.move();
        panel.repaint();

    }

    public void remoteRight() {
        if(!isRunning) return;

        panel.B.dir=ConstantUtil.Dir.Right;
        panel.B.move();
        panel.repaint();

    }

    public void remoteSpace()  { // 开火
        if(!isRunning) return;

        Bullet b=new Bullet(panel.B.x+40, panel.B.y-10,15,15,ConstantUtil.BulletHero);
        b.dir=ConstantUtil.Dir.Up;
        panel.BBullets.add(b);
        panel.repaint();
    }

    public void remoteName(String name) {
        panel.BPlayerName=name;
        //然后回发本机玩家昵称
        exchangeThread.sendMessage("Name,"+panel.APlayerName);
    }
}
