package entity;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import constants.ConstantUtil;

import javax.swing.*;
import java.awt.*;

/**
 * 背景图片
 */
public class Background extends BaseObject{
    public Background(int x,int y,int w,int h){
        super(x,y,w,h,ConstantUtil.ImgBgURL);
        speed=ConstantUtil.SpeedBg;
        dir=ConstantUtil.Dir.Down;
    }
    //private Image background = new ImageIcon(ConstantUtil.ImgBgURL).getImage();

    public void BoundDetect() {
        if(y>ConstantUtil.GmaeWinHeight){
            y=0;
        }
    }

    public void Draw(Graphics g){

        g.drawImage(img, x,y, ConstantUtil.GameWinWidth,ConstantUtil.GmaeWinHeight,
                0,0,
                img.getWidth(null),
                (int)((double)(ConstantUtil.GmaeWinHeight-y)/ConstantUtil.GmaeWinHeight*img.getHeight(null)),
                null);

        g.drawImage(img, 0,0, ConstantUtil.GameWinWidth, y,
                0,
                (int)((double)(ConstantUtil.GmaeWinHeight-y)/ConstantUtil.GmaeWinHeight*img.getHeight(null)),
                img.getWidth(null),
                img.getHeight(null),
                null);


    }

    public boolean isValid() {
        return true;
    }
}
