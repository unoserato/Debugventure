package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import static utilz.Constants.UI.Buttons.*;

import utilz.LoadSave;
import gamestates.*;

public class PauseMenuButtons {
	private GameState gameState;

	protected int x, y, index, rowIndex;
	protected Rectangle buttonHitbox;
	private BufferedImage[] pauseMenuButtons;
	public boolean mouseHover, mousePressed;

	public PauseMenuButtons(int x, int y, int rowIndex, GameState gameState) {
		this.x = x;
		this.y = y;
		this.rowIndex = rowIndex;
		this.gameState = gameState;
		initializeButtonHitbox();
		loadImages();
	}

	private void loadImages() {
		pauseMenuButtons = new BufferedImage[3];
		BufferedImage temp = LoadSave.getSprite(LoadSave.PAUSE_MENU_BUTTONS);

		for (int i = 0; i < pauseMenuButtons.length; i++)
			pauseMenuButtons[i] = temp.getSubimage(i * PausedMenuButtonWidthDefault,
					rowIndex * PausedMenuButtonHeightDefault, PausedMenuButtonWidthDefault,
					PausedMenuButtonHeightDefault);

	}

	private void initializeButtonHitbox() {
		buttonHitbox = new Rectangle(x, y, PAUSED_MENU_BUTTON_WIDTH, PAUSED_MENU_BUTTON_HEIGHT);
	}

	public void update() {
		index = 0;

		if (mouseHover) {
			index = 1;
		}
		if (mousePressed) {
			index = 2;
		}
	}

	public void render(Graphics g) {
		g.setColor(Color.red);
		g.drawImage(pauseMenuButtons[index], x, y, PAUSED_MENU_BUTTON_WIDTH, PAUSED_MENU_BUTTON_HEIGHT, null);
//		g.drawRect(x, y, PAUSED_MENU_BUTTON_WIDTH, PAUSED_MENU_BUTTON_HEIGHT);
	}

	public boolean isMouseHover() {
		return mouseHover;
	}

	public void setMouseHover(boolean mouseHover) {
		this.mouseHover = mouseHover;
	}

	public boolean isMousePressed() {
		return mousePressed;
	}

	public void setMousePressed(boolean mousePressed) {
		this.mousePressed = mousePressed;
	}

	public Rectangle getHitbox() {
		return buttonHitbox;
	}

	public void setButtonHitbox(Rectangle buttonHitbox) {
		this.buttonHitbox = buttonHitbox;
	}

	public void setGameState() {
		GameState.state = gameState;
	}

	public void resetBooleans() {
		mouseHover = false;
		mousePressed = false;
	}

}
