package entity;

import constants.ConstantUtil;

public class HeroPlane extends Plane{
    public HeroPlane(int x,int y,int w,int h){
        super(x,y,w,h, ConstantUtil.ImgHeroURL,ConstantUtil.PlaneType.Hero);
        life=1;
        speed=ConstantUtil.SpeedHero;
    }

    public void BoundDetect() { // 我方飞机移不出边界
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


}
