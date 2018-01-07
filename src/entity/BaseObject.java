package entity;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;
import javax.imageio.*;

import constants.ConstantUtil;

public abstract class BaseObject {
    public int x;
    public int y;
    public int w;
    public int h;
    public Image img;


    public int speed;
    public ConstantUtil.Dir dir;

    public BaseObject(int x, int y, int w, int h, String url) {

        URL fullURL=this.getClass().getClassLoader().getResource(url);
        img = new ImageIcon(fullURL).getImage();
        this.x=x;
        this.y=y;
        this.w=w;
        this.h=h;
    }

    public Rectangle GetRectangle()
    {
        return new Rectangle(this.x, this.y, this.w, this.h);
    }

    public void move() { // 边界、碰撞检测处理不包含在此函数中
        switch(dir) {
            case Down:
                y+=speed;
                break;
            case Up:
                y-=speed;
                break;
            case Left:
                x-=speed;
                break;
            case Right:
                x+=speed;
                break;
            case Left_Up:
                y-=speed*0.7;
                x-=speed*0.7;
                break;
            case Left_Down:
                y+=speed*0.7;
                x-=speed*0.7;
                break;
            case Right_Up:
                y-=speed*0.7;
                x+=speed*0.7;
                break;
            case Right_Down:
                y+=speed*0.7;
                x+=speed*0.7;
                break;
            default:break;
        }
        BoundDetect(); // 子弹和飞机不同
    }

    public abstract void BoundDetect(); // 边界检测处理函数
    //public abstract void CollisionHandle(); // 碰撞检测处理函数

    public abstract void Draw(Graphics g);

    public abstract boolean isValid();




}
