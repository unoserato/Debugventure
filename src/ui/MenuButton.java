package ui;

import static utilz.Constants.UI.Buttons.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import gamestates.GameState;
import utilz.LoadSave;

public class MenuButton {
	public int xPos, yPos, rowIndex, index, xOffsetCenter = MENU_BUTTON_WIDTH / 2;
	private GameState state;
	private BufferedImage[] images;
	private boolean mouseHover, mousePressed;

	private Rectangle buttonHitbox;

	public MenuButton(int x, int y, int rowIndex, GameState state) {
		this.xPos = x;
		this.yPos = y;
		this.rowIndex = rowIndex;
		this.state = state;
		initializeButtonHitbox();
		loadImages();
	}

	private void initializeButtonHitbox() {
		buttonHitbox = new Rectangle(xPos - xOffsetCenter, yPos, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT);
	}

	private void loadImages() {
		images = new BufferedImage[3];

		BufferedImage temp = LoadSave.getSprite(LoadSave.START_BUTTONS);
		for (int i = 0; i < images.length; i++) {
			images[i] = temp.getSubimage(i * MenuButtonWidthDefault, rowIndex * MenuButtonHeightDefault, MenuButtonWidthDefault,
					MenuButtonHeightDefault);
		}
	}

	public void render(Graphics g) {
		g.setColor(Color.red);
		g.drawImage(images[index], xPos - xOffsetCenter, yPos, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT, null);
//		g.drawRect(xPos - xOffsetCenter, yPos, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT);
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

	public void setGameState() {
		GameState.state = state;
	}

	public void resetBooleans() {
		mouseHover = false;
		mousePressed = false;
	}

	public Rectangle getHitbox() {
		return buttonHitbox;
	}
	
	public GameState getState() {
		return state;
	}
}
