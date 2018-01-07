package controller;

import MySocket.BExchangeThread;
import constants.ConstantUtil;
import entity.*;
import music.MusicPlayer;
import view.GamePanel;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class BController extends Controller{
    public static BController Controller;

    GamePanel panel;
    private Timer timer;
    private boolean isRunning =false;

    public BExchangeThread exchangeThread; // 远程通信用的线程

    public BController(BExchangeThread exchangeThread, GamePanel panel) {
        this.exchangeThread=exchangeThread;// 收发消息
        this.panel=panel;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void gameStart(){
        isRunning =true;

        timer = new Timer();
        timer.schedule(new BController.GameTask(), 100,30);
    }

    private class GameTask extends TimerTask {
        public void run() {
            if(!isRunning){
                return ;
            }

            //BController不主动产生敌机和敌机子弹，而是被动触发式产生
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
                    if(panel.A.isValid()) {
                        if (ep.type == ConstantUtil.PlaneType.GeneralStrongEnemy) {
                            if (ep.life == 0) {
                                panel.AScore += 3; // 加强飞机击杀的最后一下得3分
                            }
                        } else {
                            panel.AScore++; // 普通敌机和自杀敌机击败得一分
                        }
                    }
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
                    if(panel.B.isValid()) {
                        if (ep.type == ConstantUtil.PlaneType.GeneralStrongEnemy) {
                            if (ep.life == 0) {
                                panel.BScore += 3; // 加强飞机击杀的最后一下得3分
                            }
                        } else {
                            panel.BScore++; // 普通敌机和自杀敌机击败得一分
                        }
                    }
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
        for(int i=0;i<panel.EnemyPlanes.size();i++){
            if(panel.EnemyPlanes.get(i).state==ConstantUtil.PlaneState.dead){// 正在爆炸的还不能移走
                panel.EnemyPlanes.remove(i);
            }
        }
        for(int i=0;i<panel.EnemyBullets.size();i++){
            if(!panel.EnemyBullets.get(i).isValid()){
                panel.EnemyBullets.remove(i);
            }
        }

    }

    public void keyUp() {
        if(!isRunning) return;
        if(!panel.B.isValid()) return;

        panel.B.dir=ConstantUtil.Dir.Up;
        panel.B.move();
        if(exchangeThread!=null)
            exchangeThread.sendMessage("Up");
        panel.repaint();

    }

    public void keyDown() {
        if(!isRunning) return;
        if(!panel.B.isValid()) return;

        panel.B.dir=ConstantUtil.Dir.Down;
        panel.B.move();
        if(exchangeThread!=null)
            exchangeThread.sendMessage("Down");
        panel.repaint();

    }

    public void keyLeft() {
        if(!isRunning) return;
        if(!panel.B.isValid()) return;

        panel.B.dir=ConstantUtil.Dir.Left;
        panel.B.move();
        if(exchangeThread!=null)
            exchangeThread.sendMessage("Left");
        panel.repaint();

    }

    public void keyRight() {
        if(!isRunning) return;
        if(!panel.B.isValid()) return;

        panel.B.dir=ConstantUtil.Dir.Right;
        panel.B.move();
        if(exchangeThread!=null)
            exchangeThread.sendMessage("Right");
        panel.repaint();

    }

    public void keySpace()  { // 开火
        if(!isRunning) return;
        if(!panel.B.isValid()) return;

        Bullet b=new Bullet(panel.B.x+40, panel.B.y-10,15,15,ConstantUtil.BulletHero);
        b.dir=ConstantUtil.Dir.Up;
        panel.BBullets.add(b);
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

        panel.A.dir=ConstantUtil.Dir.Up;
        panel.A.move();
        panel.repaint();

    }

    public void remoteDown() {
        if(!isRunning) return;

        panel.A.dir=ConstantUtil.Dir.Down;
        panel.A.move();
        panel.repaint();

    }

    public void remoteLeft() {
        if(!isRunning) return;

        panel.A.dir=ConstantUtil.Dir.Left;
        panel.A.move();
        panel.repaint();

    }

    public void remoteRight() {
        if(!isRunning) return;

        panel.A.dir=ConstantUtil.Dir.Right;
        panel.A.move();
        panel.repaint();

    }

    public void remoteSpace()  { // 开火
        if(!isRunning) return;

        Bullet b=new Bullet(panel.A.x+40, panel.A.y-10,15,15,ConstantUtil.BulletHero);
        b.dir=ConstantUtil.Dir.Up;
        panel.ABullets.add(b);
        panel.repaint();
    }

    //remoteGenerator被动触发：根据A发来的产生消息，在画面生成敌机或敌机子弹并加入B自己的游戏逻辑中
    //1 产生敌机飞机或者加强敌机
    public void remoteGenerator(ConstantUtil.PlaneType type, int x, int y){
        if(type == ConstantUtil.PlaneType.GeneralEnemy){
            EnemyPlane ep = new EnemyPlane(x,y,ConstantUtil.SizeEnemyPlane,ConstantUtil.SizeEnemyPlane);
            panel.EnemyPlanes.add(ep);
        }
        else {
            StrongPlane sp = new StrongPlane(x,y,ConstantUtil.SizeEnemyPlane,ConstantUtil.SizeEnemyPlane);
            panel.EnemyPlanes.add(sp);
        }
    }
    //2 重载函数，产生自杀式敌机
    public void remoteGenerator(ConstantUtil.PlaneType type, int x, int y, ConstantUtil.HeroType attackObj){
        SuicideEnemyPlane sp = new SuicideEnemyPlane(x,y,ConstantUtil.SizeEnemyPlane,ConstantUtil.SizeEnemyPlane);
        sp.attackObj=attackObj; // 重设攻击对象
        panel.EnemyPlanes.add(sp);
    }

    //3 重载函数，产生敌机子弹
    public void remoteGenerator(int x, int y) {
        Bullet b = new Bullet(x,y,ConstantUtil.SizeBullet,ConstantUtil.SizeBullet,ConstantUtil.BulletEnemy);
        panel.EnemyBullets.add(b);
    }

    public void remoteName(String name) {
        panel.APlayerName=name; // 对于B来说被动记录即可
    }
}
