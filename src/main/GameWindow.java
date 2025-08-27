package main;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JFrame;

public class GameWindow  extends JFrame{

	private JFrame jf;
	
	public GameWindow(GamePanel gp) {
		
		jf = new JFrame();
		
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setTitle("PROTOTYPE0");
//		jf.setUndecorated(true);
		jf.add(gp);
		jf.setResizable(false);
		
		jf.pack();
		jf.setLocationRelativeTo(null);
		jf.setVisible(true);
		jf.addWindowFocusListener(new WindowFocusListener() {
			
			@Override
			public void windowLostFocus(WindowEvent e) {
				gp.getGame().windowFocusLost();
			}
			
			@Override
			public void windowGainedFocus(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
