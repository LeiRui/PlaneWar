package entity;

import constants.ConstantUtil;

import java.util.Random;

public class SuicideEnemyPlane extends Plane{ // 自杀式敌机
    public ConstantUtil.HeroType attackObj;
    public SuicideEnemyPlane(int x,int y,int w,int h){
        super(x,y,w,h, ConstantUtil.ImgEnemyURL,ConstantUtil.PlaneType.GeneralSuicideEnemy);
        life=1;
        speed=ConstantUtil.SpeedEnemy;
        dir=ConstantUtil.Dir.Down;

        //随机生成攻击对象，也可以直接修改attackObj属性
        Random random = new Random();
        if(random.nextInt(2)==0) {
            attackObj=ConstantUtil.HeroType.A;
        }
        else {
            attackObj=ConstantUtil.HeroType.B;
        }
    }

    public void move(int dx, int dy) { // 朝目标移动
        int stepx=dx-x;
        int stepy=dy-y;
        int cx,cy;
        if(stepy > 0) {
            if (stepx > 0) {
                cx = 1;
                if (stepy / stepx == 0) {
                    cy = 1;
                } else {
                    cy = stepy / stepx;
                    if (cy > 3) {
                        cy = 3; // 控制速度
                    }
                }
            } else if (stepx < 0) {
                cx = -1;
                if (stepy / stepx == 0) {
                    cy = 1;
                } else {
                    cy = -1 * stepy / stepx;
                    if (cy > 3) {
                        cy = 3;
                    }
                }

            } else {
                cx = 0;
                cy = speed;
            }
        }
        else { // 已经飞过去了,不回头
            cx=0;
            cy=speed;
        }
        x+=cx;
        y+=cy;
        BoundDetect(); // 子弹和飞机不同
    }

    public void BoundDetect() { // 敌方飞机遇到左右边界就反弹
        if(x<0) {
            x=0;
            dir=ConstantUtil.Dir.Right_Down; // x<0只可能是由于left_down
        }
        else if(x+w>ConstantUtil.GameWinWidth) {
            x=ConstantUtil.GameWinWidth-w;
            dir=ConstantUtil.Dir.Left_Down;
        }
        else if(y+h>ConstantUtil.GmaeWinHeight) { // 飞出去了
            life=0;
            state=ConstantUtil.PlaneState.dead;
        }
    }
}
