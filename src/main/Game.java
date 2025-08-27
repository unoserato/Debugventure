package main;

import java.awt.Graphics;

import audio.AudioPlayer;
import gamestates.GameState;
import gamestates.AboutState;
import gamestates.MenuState;
import gamestates.PlayingState;
import gamestates.StoryState;
import utilz.LoadSave;

public class Game implements Runnable {

	private GamePanel gamePanel;
	private GameWindow gameWindow;
	private Thread gameThread;
	private PlayingState playingState;
	private MenuState menuState;
	private AboutState aboutState;
	private StoryState storyState;
	private AudioPlayer audioPlayer;
	
	private final int FPS = 120;

	// actual size of the elements
	public static final int tile = 16, // 16x16
							scale = 3, // scale up the elements
							height = 15, 
							width = 20, 
							extraScale = 2;

	// scaled size elements
	public static final int TILE = tile * scale, 
							SCREEN_HEIGHT = height * TILE, 
							SCREEN_WIDTH = width * TILE;

	public Game() {
		initializeClasses();

		gamePanel = new GamePanel(this);
		gameWindow = new GameWindow(gamePanel);
		gamePanel.setFocusable(true);
		gamePanel.requestFocusInWindow();

		startGame();
	}

	private void initializeClasses() {
		menuState = new MenuState(this);
		playingState = new PlayingState(this);
		aboutState = new AboutState(this);
		storyState = new StoryState(this);
		audioPlayer = new AudioPlayer();
	}

	public void startGame() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	public void update() {
		switch (GameState.state) {
		case MENU:
			menuState.update();
			break;
		case STORY:
			storyState.update();
			break;
		case PLAYING:
			playingState.update();
			break;
		case ABOUT:
			aboutState.update();
			break;
		case EXIT:
			System.exit(0);
			break;
		default:
			break;
		}
	}

	public void render(Graphics g) {
		switch (GameState.state) {
		case MENU:
			menuState.render(g);
			break;
		case STORY:
			storyState.render(g);
			break;
		case PLAYING:
			playingState.render(g);
			break;
		case ABOUT:
			aboutState.render(g);
			break;
		default:
			break;
		}
	}

	public void run() {
		long lastTime = System.nanoTime();
		;
		long currentTime;

		double interval = 1000000000 / FPS;
		double delta = 0;

		double fpsCheck = System.currentTimeMillis();
		int fps = 0;

		while (true) {
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / interval;
			lastTime = currentTime;

			if (delta > 0) {
				delta--;
				fps++;
				update();
				gamePanel.repaint();
			}
			if (System.currentTimeMillis() - fpsCheck >= 1000) {
				fpsCheck = System.currentTimeMillis();
//				System.out.println("FPS:" + fps);
				fps = 0;
			}

		}
	}

	public void windowFocusLost() {
		if (GameState.state == GameState.PLAYING) {
			playingState.getPlayer().resetMovementBooleans();
		}
	}

	public MenuState getMenu() {
		return menuState;
	}

	public PlayingState getPlaying() {
		return playingState;
	}

	public AboutState getAboutState() {
		return aboutState;
	}
	
	public StoryState getStoryState() {
		return storyState;
	}
	
	public AudioPlayer getAudioPlayer() {
		return audioPlayer;
	}
}
