package inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import gamestates.GameState;
import main.GamePanel;

public class MouseInputs implements MouseListener, MouseMotionListener, MouseWheelListener {

	private GamePanel gamePanel;

	public MouseInputs(GamePanel gp) {
		this.gamePanel = gp;
	}

	public void mouseDragged(MouseEvent e) {

	}

	public void mouseMoved(MouseEvent e) {
		switch (GameState.state) {
		case MENU:
			gamePanel.getGame().getMenu().mouseMoved(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlaying().mouseMoved(e);
			break;
		default:
			break;

		}
	}

	public void mouseClicked(MouseEvent e) {
		switch (GameState.state) {
		case MENU:
			gamePanel.getGame().getMenu().mouseClicked(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlaying().mouseClicked(e);
			break;
		default:
			break;

		}
	}

	public void mousePressed(MouseEvent e) {
		switch (GameState.state) {
		case MENU:
			gamePanel.getGame().getMenu().mousePressed(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlaying().mousePressed(e);
			break;
		default:
			break;
		}
	}

	public void mouseReleased(MouseEvent e) {
		switch (GameState.state) {
		case MENU:
			gamePanel.getGame().getMenu().mouseReleased(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlaying().mouseReleased(e);
			break;
		default:
			break;

		}
	}

	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		switch (GameState.state) {
		case ABOUT:
			gamePanel.getGame().getAboutState().mouseWheelMoved(e);
			break;
		default:
			break;

		}

	}

}
