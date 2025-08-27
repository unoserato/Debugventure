package gamestates;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.text.BreakIterator;

import main.Game;
import ui.MenuButton;
import utilz.LoadSave;

public class MenuState extends State implements StateMethods {

	private MenuButton[] buttons = new MenuButton[3];
	private BufferedImage[][] backgroundImage;
	private float[][] xPosition;
	private float xSpeed = 0.8f * Game.scale;

	public MenuState(Game game) {
		super(game);
		loadButtons();
		loadImagesBG();
	}

	private void loadImagesBG() {
		backgroundImage = new BufferedImage[10][2];
		xPosition = new float[10][2];

		for (int y = 0; y < backgroundImage.length; y++)
			for (int x = 0; x < backgroundImage[0].length; x++) {
				String path = "menuBG/" + (y + 1) + ".png";
				backgroundImage[y][x] = LoadSave.getSprite(path);
				xPosition[y][x] = x * Game.SCREEN_WIDTH;
			}
	}

	private void loadButtons() {
		buttons[0] = new MenuButton(Game.SCREEN_WIDTH / 2, 115 * Game.scale, 0, GameState.STORY);
		buttons[1] = new MenuButton(Game.SCREEN_WIDTH / 2, 145 * Game.scale, 1, GameState.ABOUT);
		buttons[2] = new MenuButton(Game.SCREEN_WIDTH / 2, 175 * Game.scale, 2, GameState.EXIT);
	}

	@Override
	public void update() {

		moveBG();

		for (MenuButton mb : buttons) {
			mb.update();
		}
	}

	@Override
	public void render(Graphics g) {
		for (int y = 0; y < backgroundImage.length; y++)
			for (int x = 0; x < backgroundImage[0].length; x++) {
				g.drawImage(backgroundImage[y][x], (int) xPosition[y][x], 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT,
						null);
			}

		for (MenuButton mb : buttons) {
			mb.render(g);
		}
		
		g.setColor(Color.BLUE);
		g.setFont(new Font("Arial Black", Font.BOLD, 80));
		g.drawString("DEBUGVENTURE", Game.scale * 35, Game.scale * 80);
		g.setColor(Color.white);
		g.drawString("DEBUGVENTURE", Game.scale * 33, Game.scale * 80);
	}

	private void moveBG() {
		xPosition[6][0] += xSpeed;
		xPosition[6][1] += xSpeed;

		if (xPosition[6][1] > Game.SCREEN_WIDTH) {
			xPosition[6][1] = -Game.SCREEN_WIDTH;
		}
		if (xPosition[6][0] > Game.SCREEN_WIDTH) {
			xPosition[6][0] = -Game.SCREEN_WIDTH;
		}

		float speedMultiplier = 0.01f;
		for (int y = 0; y < backgroundImage.length; y++) {
			for (int x = 0; x < backgroundImage[0].length; x++) {
				if (y == 0 || y == 1 || y == 6) {
					continue;
				}

				xPosition[y][x] -= (xSpeed * speedMultiplier);

				if (xPosition[y][x] < -Game.SCREEN_WIDTH) {
					xPosition[y][x] = Game.SCREEN_WIDTH;
				}
			}
			speedMultiplier += 0.09f;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("MOUSE REACHED MENU STATE ");

	}

	@Override
	public void mousePressed(MouseEvent e) {
		for (MenuButton mb : buttons) {
			if (isIn(e, mb)) {
				mb.setMousePressed(true);
				break;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		for (MenuButton mb : buttons) {
			if (isIn(e, mb)) {
				if (mb.isMousePressed())
					mb.setGameState();
				if(mb.getState() == GameState.PLAYING)
					game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex());
				break;
			}
		}

		resetButtons();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		for (MenuButton mb : buttons) {
			mb.setMouseHover(false);
		}
		for (MenuButton mb : buttons) {
			if (isIn(e, mb)) {
				mb.setMouseHover(true);
				break;
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			GameState.state = GameState.PLAYING;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void resetButtons() {
		for (MenuButton mb : buttons) {
			mb.resetBooleans();
		}
	}
}
