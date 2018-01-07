package app;
import java.awt.EventQueue;

import view.StartFrame;

public class App {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {

                    StartFrame frame = new StartFrame();
                    frame.setVisible(true);


					/*
					AGamePanel panel=new AGamePanel();
					AGameController.Controller=new AGameController(panel);
					AGameController.Controller.gameStart();

					JFrame f = new JFrame();
					f.setTitle("Drawing Graphics on Panel");
					f.setLocationRelativeTo(null);

					int x = Toolkit.getDefaultToolkit().getScreenSize().width;
					//int y = Toolkit.getDefaultToolkit().getScreenSize().height;
					int width=ConstantUtil.GameWinWidth;
					int height=ConstantUtil.GmaeWinHeight;
					f.setBounds((x-width)/2,0, width,height+50);
					f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					f.setContentPane(panel);
					f.setVisible(true);
					*/


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
