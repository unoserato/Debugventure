package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import gamestates.GameState;
import main.GamePanel;
import static utilz.Constants.Directions.*;

public class KeyboardInputs implements KeyListener {

	private GamePanel gamePanel;
	
	public KeyboardInputs(GamePanel gp) {
		this.gamePanel = gp;
	}
	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(GameState.state) {
		case MENU:
			gamePanel.getGame().getMenu().keyPressed(e);
			break;
		case STORY:
			gamePanel.getGame().getStoryState().keyPressed(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlaying().keyPressed(e);
			break;
		case ABOUT:
			gamePanel.getGame().getAboutState().keyPressed(e);
			break;
		default:
			break;
		}
		
//		for troubleshooting player movements
		if(e.getKeyCode() == KeyEvent.VK_1) {
			gamePanel.getGame().getPlaying().getPlayer().troubleShoot();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(GameState.state) {
		case MENU:
			gamePanel.getGame().getMenu().keyReleased(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlaying().keyReleased(e);
			break;
		default:
			break;
		
		}
	}

}
