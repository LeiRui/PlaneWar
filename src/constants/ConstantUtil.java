package constants;

import java.io.File;

public class ConstantUtil {
    public static enum Dir{
        Up,Down,Left,Right,Left_Up,Left_Down,Right_Up,Right_Down
    }

    public static int GameWinWidth=600;
    public static int GmaeWinHeight=700;


    public static enum PlaneState{
        alive,exploding,dead
    }

    public static enum PlaneType{
        Hero,GeneralEnemy,GeneralStrongEnemy,GeneralSuicideEnemy
    }

    public static enum HeroType{
        A,B
    }

    public static String musicBgmURL="Resources/Music/bgm.wav";
    public static String musicActionURL="Resources/Music/action.wav";

    public static String ImgEnemyURL = "Resources/Graphics/planes/enemy.png";
    public static String ImgEnemyStrongURL="Resources/Graphics/planes/enemyStrong.png";
    public static String ImgHeroURL = "Resources/Graphics/planes/hero.png";

    public static int PlaneLife=1;
    public static int PlaneStrongLife=2;

    public static String ImgBgURL="Resources/Graphics/background/bg.jpg";
    public static String ImgStartURL="Resources/Graphics/background/start.jpg";

    public static int SpeedBg=2;
    public static int SpeedBullet=2;
    public static int SpeedEnemy=1;
    public static int SpeedHero=2;

    public static int SizeHeroPlane=100;
    public static int SizeEnemyPlane=50;
    public static int SizeBullet=15;

    public static String BulletEnemy="Resources/Graphics/bullets/EnemyBullet.png";
    public static String BulletHero ="Resources/Graphics/bullets/HeroBullet.png";

    public static String ImgExplodeURL="Resources/Graphics/explode/explode.png";

    public static String ScoreRecordPath="."+ File.separator+"Record.txt";

    public static int WinThreshold = 10;
}
