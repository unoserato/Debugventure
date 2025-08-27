package gamestates;

import static utilz.Constants.Environment.PLAYING_BG_HEIGHT;
import static utilz.Constants.Environment.PLAYING_BG_WIDTH;
import static utilz.Constants.PlayerConstants.weaponType;
import static utilz.HelpMethod.CheckWhiteDoor;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import objects.ChestManager;
import ui.GameOverOverlay;
import ui.PauseOverlay;
import ui.TutorialOverlay;
import ui.UserInterface;
import utilz.LoadSave;

public class PlayingState extends State implements StateMethods {
	private Player player;
	private LevelManager levelManager;
	private EnemyManager enemyManager;
	private PauseOverlay pauseOverlay;
	private GameOverOverlay gameOverOverlay;
	private UserInterface userInterface;
	private TutorialOverlay tutorialOverlay;
	private ChestManager chestManager;

	private boolean gameOver;
	private boolean isLevelOne;
	private boolean isLevelClear;
	boolean gameComplete;

	// Background images
	private BufferedImage bgImage1, bgImage2, bgImage3;
	private BufferedImage[] stage2BGImage, stage3BGImage;

	// Pause button properties
	private BufferedImage[] pauseButton;
	private Rectangle pauseHitbox;
	private int pauseHitboxSize = (int) (Game.TILE);
	private int pauseHitboxXPos = (int) (Game.SCREEN_WIDTH - (pauseHitboxSize * 1.50f));
	private int pauseHitboxYPos = (int) (pauseHitboxSize / 2.20f);
	public boolean paused = false, pauseHover, pausePressed, musicOn = true, sfxOn = true;
	private int index = 0;

	// Level offset properties
	public int xLevelOffset;
	private int leftBorder = (int) (0.4 * Game.SCREEN_WIDTH);
	private int rightBorder = (int) (0.5 * Game.SCREEN_WIDTH);
	private int maxLevelOffsetX;

	// Constructor
	public PlayingState(Game game) {
		super(game);
		initializeClasses();
	}

	// Initialize all necessary classes
	private void initializeClasses() {
		levelManager = new LevelManager(game);
		enemyManager = new EnemyManager(this);
		player = new Player(350, 90, 48 * Game.scale, 48 * Game.scale, this);
		player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
		player.loadLevelData(levelManager.getCurrentLevel().getLevelData());
		pauseOverlay = new PauseOverlay(game);
		gameOverOverlay = new GameOverOverlay(this);
		userInterface = new UserInterface(this);
		tutorialOverlay = new TutorialOverlay(this);
		chestManager = new ChestManager(this);

		gameComplete = false;

		// Load background images
		bgImage1 = LoadSave.getSprite("stage1_BG/1.png");
		bgImage2 = LoadSave.getSprite("stage1_BG/2.png");
		bgImage3 = LoadSave.getSprite("stage1_BG/3.png");

		// Load stage background images and entities
		loadStarteLevel();
		loadStage2BG();
		loadStage3BG();
		loadPauseButtonOnGame();
		calculateLevelOffsetX();
	}

	// Load the next level
	public void loadNextLevel() {
		resetAll();
		levelManager.loadNextLevel();
		player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
		isLevelOne = false;
	}

	// Load the starting level
	private void loadStarteLevel() {
		enemyManager.loadEnemy(levelManager.getCurrentLevel());
		chestManager.loadChest(levelManager.getCurrentLevel());
		isLevelOne = true;
	}

	// Calculate the maximum level offset
	private void calculateLevelOffsetX() {
		maxLevelOffsetX = levelManager.getCurrentLevel().getMaxLevelOffseXt();
	}

	// Update game state
	@Override
	public void update() {
		updatePauseIndex();
		if(gameOver)
			LevelManager.levelIndex = 0;
		if (!paused && !gameOver) {
			levelManager.update();
			player.update();
			enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
			userInterface.update();
			chestManager.update();
			checkCloseToBorder();
		} else {
			pauseOverlay.update();
		}
	}

	// Check if the player is close to the level borders
	public void checkCloseToBorder() {
		int playerX = (int) player.getHitbox().x;
		int diff = playerX - xLevelOffset;

		if (diff > rightBorder) {
			xLevelOffset += diff - rightBorder;
		} else if (diff < leftBorder) {
			xLevelOffset += diff - leftBorder;
		}

		if (xLevelOffset > maxLevelOffsetX) {
			xLevelOffset = maxLevelOffsetX;
		} else if (xLevelOffset < 0) {
			xLevelOffset = 0;
		}
	}

	// Render game elements
	public void render(Graphics g) {
		if (levelManager.getLevelIndex() < 2) {
			renderForeground(g); // Render foreground for stage 1
		} else if (levelManager.getLevelIndex() == 2) {
			renderStage2Test(g); // Render for stage 2
		} else if (levelManager.getLevelIndex() > 2) {
			renderStage3Test(g); // Render for stage 2
		}

		levelManager.render(g, xLevelOffset);
		enemyManager.render(g, xLevelOffset);
		userInterface.render(g);
		player.render(g, xLevelOffset);
		chestManager.render(g, xLevelOffset);

		if (isLevelOne) {
			tutorialOverlay.render(g, xLevelOffset); // Render tutorial overlay if level one
		}

		if (paused) {
			g.setColor(new Color(0, 0, 0, 150));
			g.fillRect(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
			pauseOverlay.render(g); // Render pause overlay
		} else if (gameOver) {
			gameOverOverlay.render(g); // Render game over overlay
		} else {
			renderPause(g); // Render pause button
		}

		if (gameComplete) {
			g.setColor(Color.green);
			g.setFont(new Font("Arial Black", Font.PLAIN, 50));
			g.drawString("YOU WIN", Game.scale * 120, Game.scale * 120);
		}
	}

	// Render the foreground elements
	public void renderForeground(Graphics g) {
		g.drawImage(bgImage1, 0, 0, PLAYING_BG_WIDTH, PLAYING_BG_HEIGHT, null);
		for (int i = 0; i < 3; i++) {
			g.drawImage(bgImage2, 0 + i * PLAYING_BG_WIDTH - (int) (xLevelOffset * 0.2), 0, PLAYING_BG_WIDTH,
					PLAYING_BG_HEIGHT, null);
			g.drawImage(bgImage3, 0 + i * PLAYING_BG_WIDTH - (int) (xLevelOffset * 0.3), 0, PLAYING_BG_WIDTH,
					PLAYING_BG_HEIGHT, null);
		}
	}

	// Mouse event handling
	@Override
	public void mouseClicked(MouseEvent e) {
		if (!gameOver && e.getButton() == MouseEvent.BUTTON3) {
			Player.arrowCount++; // Increment arrow count on right-click
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (!gameOver) {
			if (isIn(e, pauseHitbox)) {
				pausePressed = true; // Set pause pressed state
			}
			if (!paused) {
				if (userInterface.buttons[0].contains(e.getX(), e.getY())) {
					player.setAttackIndex();
					player.setAttack(true); // Set player attack
				}
			} else {
				pauseOverlay.mousePressed(e); // Handle mouse press in pause overlay
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (!gameOver) {
			if (isIn(e, pauseHitbox) && !Player.isDead) {
				paused = true;
			}
			if (!paused && !Player.isDead) {
				if (userInterface.buttons[1].contains(e.getX(), e.getY())) {
					if (weaponType == 0)
						weaponType = 1;
					else if (weaponType == 1)
						weaponType = 0;
				}

				if (userInterface.buttons[2].contains(e.getX(), e.getY())) {
					if (UserInterface.redPotionCount > 0) {
						player.changePlayerHealth(10);
						UserInterface.redPotionCount--;
					}
//					System.out.println(userInterface.redPotionCount);
				}

				if (userInterface.buttons[3].contains(e.getX(), e.getY())) {
					if (UserInterface.bluePotionCount > 0) {
						player.changePlayerEnergy(50);
						UserInterface.bluePotionCount--;
					}
//					System.out.println(userInterface.redPotionCount);
				}
			} else {
				pauseOverlay.mouseReleased(e);
			}
		}
		resetPauseBoolean();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		pauseHover = false;
		if (!gameOver) {
			if (isIn(e, pauseHitbox)) {
				pauseHover = true; // Set pause hover state
			}
			if (paused) {
				pauseOverlay.mouseMoved(e); // Handle mouse movement in pause overlay
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (gameOver) {
			gameOverOverlay.keyPressed(e); // Handle key press in game over state
		} else {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_A:
				player.setLeft(true); // Move player left
				break;
			case KeyEvent.VK_D:
				player.setRight(true); // Move player right
				break;
			case KeyEvent.VK_SPACE:
				player.setJump(true); // Make player jump
				break;
			case KeyEvent.VK_BACK_SPACE:
				GameState.state = GameState.MENU; // Go to menu
				break;
			case KeyEvent.VK_ESCAPE:
				paused = true; // Pause the game
				break;
			case KeyEvent.VK_SHIFT:
				player.setShift(true); // Enable player shift
				break;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (!gameOver) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_A:
				player.setLeft(false); // Stop moving left
				break;
			case KeyEvent.VK_D:
				player.setRight(false); // Stop moving right
				break;
			case KeyEvent.VK_SPACE:
				player.setJump(false); // Stop jumping
				break;
			case KeyEvent.VK_SHIFT:
				player.setShift(false); // Disable player shift
				break;
			case KeyEvent.VK_ENTER:
				if (isLevelClear && CheckWhiteDoor(player.getHitbox().x, player.getHitbox().y,
						levelManager.getCurrentLevel().getLevelData())) {
					if (levelManager.getLevelIndex() == 3)
						gameComplete = true;
					else
						loadNextLevel(); // Load next level if conditions are met
				}
				break;
			}
		}
	}

	// Getters for various components
	public Player getPlayer() {
		return player;
	}

	public void windowFocusLost() {
		player.resetMovementBooleans(); // Reset player movement when window loses focus
		System.out.println("FOCUS LOST");
	}

	// PAUSE FUNCTIONS
	public void loadPauseButtonOnGame() {
		BufferedImage temp = LoadSave.getSprite(LoadSave.PAUSE_BUTTON);
		pauseButton = new BufferedImage[3];

		for (int i = 0; i < pauseButton.length; i++) {
			pauseButton[i] = temp.getSubimage(i * 16, 0, 16, 16); // Load pause button images
		}

		// Initialize pause hitbox
		pauseHitbox = new Rectangle(pauseHitboxXPos, pauseHitboxYPos, pauseHitboxSize, pauseHitboxSize);
	}

	private void renderPause(Graphics g) {
		g.drawImage(pauseButton[index], pauseHitbox.x, pauseHitbox.y, pauseHitbox.width, pauseHitbox.height, null); // Render
																													// pause
																													// button
	}

	public void updatePauseIndex() {
		index = 0;
		if (pauseHover) {
			index = 1; // Change index if hovered
		}
		if (pausePressed) {
			index = 2; // Change index if pressed
		}
	}

	public void resetPauseBoolean() {
		pauseHover = false; // Reset pause hover state
		pausePressed = false; // Reset pause pressed state
	}

	public void setPaused(boolean paused) {
		this.paused = paused; // Set paused state
	}

	public boolean isPaused() {
		return paused; // Return paused state
	}

	public boolean isMusicOn() {
		return musicOn; // Return music state
	}

	public void setMusicOn(boolean musicOn) {
		this.musicOn = musicOn; // Set music state
	}

	public boolean isSfxOn() {
		return sfxOn; // Return sound effects state
	}

	public void setSfxOn(boolean sfxOn) {
		this.sfxOn = sfxOn; // Set sound effects state
	}

	private void loadStage2BG() {
		stage2BGImage = new BufferedImage[7];

		for (int i = 0; i < stage2BGImage.length; i++) {
			String path = "stage2_BG/" + (i + 1) + ".png";
			stage2BGImage[i] = LoadSave.getSprite(path); // Load stage 2 background images
		}
	}

	private void loadStage3BG() {
		stage3BGImage = new BufferedImage[3];

		for (int i = 0; i < stage3BGImage.length; i++) {
			String path = "stage3_BG/" + (i + 1) + ".png";
			stage3BGImage[i] = LoadSave.getSprite(path); // Load stage 2 background images
		}
	}

	// Render for stage 2
	private void renderStage2Test(Graphics g) {
		float xSpeedMultiplier = 0f;

		for (int j = 0; j < stage2BGImage.length; j++) {
			for (int i = 0; i < 3; i++) {
				g.drawImage(stage2BGImage[j], 0 + i * PLAYING_BG_WIDTH - (int) (xLevelOffset * xSpeedMultiplier), 0,
						PLAYING_BG_WIDTH, PLAYING_BG_HEIGHT, null);
			}
			xSpeedMultiplier += 0.08f; // Increment speed multiplier for parallax effect
		}
	}

	// Render for stage 3
	private void renderStage3Test(Graphics g) {
		float xSpeedMultiplier = 0f;

		for (int j = 0; j < stage3BGImage.length; j++) {
			for (int i = 0; i < 3; i++) {
				g.drawImage(stage3BGImage[j], 0 + i * PLAYING_BG_WIDTH - (int) (xLevelOffset * xSpeedMultiplier), 0,
						PLAYING_BG_WIDTH, PLAYING_BG_HEIGHT, null);
			}
			xSpeedMultiplier += 0.08f; // Increment speed multiplier for parallax effect
		}
	}

	// Reset all game states
	public void resetAll() {
		isLevelOne = true;
		gameOver = false;
		paused = false;
		isLevelClear = false;
		player.resetAll(); // Reset player state
		enemyManager.resetAllEnemies(); // Reset enemy states
	}

	// Set game over state
	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver; // Update game over state
//		LevelManager.levelIndex = 0;
	}

	// Handle player hitting an enemy
	public void playerHitEnemy(Rectangle2D.Float attackBox, Player player) {
		enemyManager.playerHitEnemy(attackBox, player); // Delegate to enemy manager
	}

	// Getters for managers
	public EnemyManager getEnemyManager() {
		return enemyManager; // Return enemy manager
	}

	public ChestManager getChestManager() {
		return chestManager; // Return chest manager
	}

	public LevelManager getLevelManager() {
		return levelManager; // Return level manager
	}

	// Handle arrow hitting an enemy
	public boolean arrowHitEnemy(Rectangle2D.Float attackBox) {
		return enemyManager.arrowHitEnemy(attackBox); // Delegate to enemy manager
	}

	// Set maximum level offset
	public void setMaxLevelOffset(int maxLevelOffsetX) {
		this.maxLevelOffsetX = maxLevelOffsetX; // Update max level offset
	}

	// Set level clear state
	public void setLevelClear(boolean isLevelClear) {
		this.isLevelClear = isLevelClear; // Update level clear state
	}
}
