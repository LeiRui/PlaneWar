package music;

import constants.ConstantUtil;

import java.applet.AudioClip;
import java.io.*;
import java.applet.Applet;
import java.net.MalformedURLException;
import java.net.URL;

public class MusicPlayer {

    static volatile boolean running =false;
    static volatile boolean turnOn =true;
    private static URL bgmUrl= MusicPlayer.class.getClassLoader().getResource(ConstantUtil.musicBgmURL);
    private static URL actionUrl=MusicPlayer.class.getClassLoader().getResource(ConstantUtil.musicActionURL);

    private static AudioClip bgmAudioClip;
    private static AudioClip actionAudioClip;

    public static void bgmPlay(){
        if(!turnOn)
            return;
        if(bgmAudioClip==null){
            try {
                //URL cb;
                //File file = new File(bgmUrl.toString());
                //cb = file.toURL();
                bgmAudioClip = Applet.newAudioClip(bgmUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try{
            bgmAudioClip.loop();
            running =true;
        }catch(Exception exception){
            exception.printStackTrace();
        }

    }
    public static void bgmStop(){
        try{
            bgmAudioClip.stop();
            running =false;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void actionPlay(){
        if(!turnOn)
            return;
        if(actionAudioClip==null){
            try {
                //URL cb;
                //File file = new File(actionUrl.toString());
                //cb = file.toURL();
                actionAudioClip = Applet.newAudioClip(actionUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try{
            actionAudioClip.play();
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    /**
     * 返回是否正在运行
     * @return
     */
    public static boolean isRunning(){
        return running;
    }
    /**
     * 返回是否打开了音乐开关
     * 没打开表示静音状态
     * @return
     */
    public static boolean isturnOn(){
        return turnOn;
    }
    /**
     * 设置是否静音
     * @param turn
     */
    public static void setturnOn(Boolean turn){
        turnOn=turn;
    }

}

