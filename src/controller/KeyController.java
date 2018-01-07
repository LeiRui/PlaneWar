package controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import music.MusicPlayer;
/**
 * 按键控制器，响应按键操作
 */
public class KeyController extends KeyAdapter{

    private Controller Controller;
    public KeyController(Controller Controller){
        this.Controller = Controller;
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if(MusicPlayer.isturnOn())
            MusicPlayer.actionPlay();

        if(e.getKeyCode()==KeyEvent.VK_A) {
            this.Controller.keyResume();
            if(!MusicPlayer.isRunning()){
                MusicPlayer.bgmPlay();
            }
            return;
        }

        if(Controller.isRunning()) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    this.Controller.keyUp();
                    break;
                case KeyEvent.VK_DOWN:
                    this.Controller.keyDown();
                    break;
                case KeyEvent.VK_LEFT:
                    this.Controller.keyLeft();
                    break;
                case KeyEvent.VK_RIGHT:
                    this.Controller.keyRight();
                    break;
                case KeyEvent.VK_SPACE:
                    this.Controller.keySpace();
                    break;
                case KeyEvent.VK_Z:
                    this.Controller.keyPause();
                    break;
                default:
                    break;
            }
        }



    }

}
