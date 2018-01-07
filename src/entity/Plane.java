package entity;
import java.net.URL;
import java.util.*;
import java.awt.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

import constants.ConstantUtil;
public abstract class Plane extends BaseObject{

    public int life;
    public ConstantUtil.PlaneState state;

    public int count=0;//为了动画显示爆炸效果的计数器

    public ConstantUtil.PlaneType type;

    public Plane(int x,int y,int w,int h, String url, ConstantUtil.PlaneType type) {
        super(x,y,w,h,url);
        this.type=type;
        state=ConstantUtil.PlaneState.alive;
    }

    public boolean isValid(){
        if(state == ConstantUtil.PlaneState.alive)
            return true;
        else
            return false; //爆炸中或已沉默
    }

    public void hit() { // 飞机被击中之后的反应
        if(life==1){ // 那说明这次被击就要爆炸
            state=ConstantUtil.PlaneState.exploding;
        }
        life--;

    }
    public void Draw(Graphics g) {
        if(state == ConstantUtil.PlaneState.alive) { // alive
            g.drawImage(img, x, y, w, h, null);
        }
        else if(state==ConstantUtil.PlaneState.exploding) {// explode
                System.out.println("explode");
                URL fullURL=this.getClass().getClassLoader().getResource(ConstantUtil.ImgExplodeURL);
                Image im1 = new ImageIcon(fullURL).getImage();
            // Image im1 = ImageIO.read(new File(Plane.class.getClassLoader().getResource(ConstantUtil.ImgExplodeURL).toString()));
                System.out.println("finish explosion");
                g.drawImage(im1, x, y, w, h, null);
                count++;
                if(count==20) {
                    state = ConstantUtil.PlaneState.dead;
                }
        }
        else return; // dead:有可能是爆炸过后，也有可能是飞出边界
    }

}
