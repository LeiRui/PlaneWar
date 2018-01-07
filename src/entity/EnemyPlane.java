package entity;

import constants.ConstantUtil;


public class EnemyPlane extends Plane{ // 普通敌机
    public EnemyPlane(int x, int y, int w, int h){
        super(x,y,w,h, ConstantUtil.ImgEnemyURL,ConstantUtil.PlaneType.GeneralEnemy);
        life=1;
        speed=ConstantUtil.SpeedEnemy;
        dir=ConstantUtil.Dir.Down;
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
