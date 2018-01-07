package entity;

import java.awt.*;
import java.util.*;
import constants.ConstantUtil;


public class Bullet extends BaseObject{

    public boolean valid;

    public Bullet(int x,int y,int w,int h,String url) {
        super(x,y,w,h,url);
        valid=true;
        speed=ConstantUtil.SpeedBullet;
        dir=ConstantUtil.Dir.Down; // 初始默认敌方子弹方向Down
        //note: 我方子弹方向用的时候记得自己重新设置成Up
    }

    public void BoundDetect() { // 子弹出了边界就消失失效
        if(x<0 || y<0 || x+w> ConstantUtil.GameWinWidth || y+h > ConstantUtil.GmaeWinHeight){
            valid=false;
        }
        //for drawing safely
        if(x<0) {
            x=0;
        }
        if(y<0) {
            y=0;
        }
        if(x+w>ConstantUtil.GameWinWidth) {
            x=ConstantUtil.GameWinWidth-w;
        }
        if(y+h>ConstantUtil.GmaeWinHeight){
            y=ConstantUtil.GmaeWinHeight-h;
        }
    }


    public void Draw(Graphics g){
        if(valid) {
            g.drawImage(img,x,y,w,h,null);
        }
    }

    public boolean isValid() {
        return valid;
    }
}
