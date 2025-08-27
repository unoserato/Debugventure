package ui;

import static utilz.Constants.UI.Buttons.PAUSED_MENU_BUTTON_WIDTH;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.plaf.multi.MultiScrollBarUI;

import main.Game;
import main.GamePanel;
import utilz.LoadSave;
import gamestates.GameState;
import gamestates.PlayingState;
import gamestates.State;

public class PauseOverlay extends State {

	private PauseMenuButtons[] buttons = new PauseMenuButtons[4];
	private BufferedImage backgorundImage;
	private int bgX, bgY, bgW, bgH;
	private int centerScreen = Game.SCREEN_WIDTH / 2 - PAUSED_MENU_BUTTON_WIDTH / 2;

	public PauseOverlay(Game game) {
		super(game);
		loadBackground();
		loadPauseMenuButtons();
	}

	private void loadBackground() {
		backgorundImage = LoadSave.getSprite(LoadSave.PAUSE_BACKGROUND);
		bgW = backgorundImage.getWidth() * (Game.scale + Game.scale);
		bgH = backgorundImage.getHeight() * (Game.scale + Game.scale);
		bgX = Game.SCREEN_WIDTH / 2 - bgW / 2;
		bgY = 10;
	}

	public void update() {
		for (PauseMenuButtons pb : buttons) {
			pb.update();
		}
	}

	public void render(Graphics g) {
		g.drawImage(backgorundImage, bgX, bgY, bgW, bgH, null);

		for (PauseMenuButtons pb : buttons) {
			pb.render(g);
		}

	}

	private void loadPauseMenuButtons() {
		buttons[0] = new PauseMenuButtons(centerScreen, 70 * Game.scale, 0, GameState.PLAYING);
		buttons[1] = new PauseMenuButtons(centerScreen, 105 * Game.scale, 1, GameState.PLAYING);
		buttons[2] = new PauseMenuButtons(centerScreen, 140 * Game.scale, 2, GameState.PLAYING);
		buttons[3] = new PauseMenuButtons(centerScreen, 175 * Game.scale, 5, GameState.MENU);
	}

	public void mouseDragged(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {
		for (PauseMenuButtons pb : buttons) {
			if (isIn(e, pb)) {
				pb.setMousePressed(true);
				break;
			}
		}
	}

	public void mouseReleased(MouseEvent e) {
		for (PauseMenuButtons pb : buttons) {
			if (isIn(e, pb)) {
				if (pb.isMousePressed()) {
					if (isIn(e, buttons[0]) || isIn(e, buttons[3])) {
						game.getPlaying().setPaused(false);
					}
					if (isIn(e, buttons[1])) {
						game.getPlaying().setSfxOn(!game.getPlaying().isSfxOn());
						game.getAudioPlayer().toggleEffectMute();
					}
					if (isIn(e, buttons[2])) {
						game.getPlaying().setMusicOn(!game.getPlaying().isMusicOn());
						game.getAudioPlayer().toggleSongMute();
					}
					pb.setGameState();
					break;
				}
			}
		}
		setMusicSfx();
		resetButtons();
	}

	public void mouseMoved(MouseEvent e) {
		for (PauseMenuButtons pb : buttons) {
			pb.setMouseHover(false);
		}

		for (PauseMenuButtons pb : buttons) {
			if (isIn(e, pb)) {
				pb.setMouseHover(true);
				break;
			}
		}
	}

	public void resetButtons() {
		for (PauseMenuButtons pb : buttons) {
			pb.resetBooleans();
		}
	}

	private void setMusicSfx() {
		if (!game.getPlaying().isSfxOn()) {
			buttons[1] = new PauseMenuButtons(centerScreen, 105 * Game.scale, 3, GameState.PLAYING);
		} else {
			buttons[1] = new PauseMenuButtons(centerScreen, 105 * Game.scale, 1, GameState.PLAYING);
		}
		if (!game.getPlaying().isMusicOn()) {
			buttons[2] = new PauseMenuButtons(centerScreen, 140 * Game.scale, 4, GameState.PLAYING);
		} else {
			buttons[2] = new PauseMenuButtons(centerScreen, 140 * Game.scale, 2, GameState.PLAYING);
		}
	}

	private boolean isIn(MouseEvent e, PauseMenuButtons pb) {
//		System.out.println("HELLO 2");
		return pb.getHitbox().contains(e.getX(), e.getY());
	}
}
