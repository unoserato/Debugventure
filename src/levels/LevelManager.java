package levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.ArrayList;

import gamestates.GameState;
import main.Game;
import objects.Chest;
import utilz.LoadSave;

public class LevelManager {

	private Game game;
	public BufferedImage[] blockSprite;
	private ArrayList<Level> levels;
	public static int levelIndex = 0;

	public LevelManager(Game game) {
		this.game = game;
		importSprite();
		levels = new ArrayList<Level>();
		buildAllLevels();
	}

	private void buildAllLevels() {
		BufferedImage[] allLevels = LoadSave.GetAllLevels();
		for (BufferedImage image : allLevels)
			levels.add(new Level(image));
	}

	public void render(Graphics g, int xLevelOffset) {
		for (int y = 0; y < Game.height; y++) {
			for (int x = 0; x < levels.get(levelIndex).getLevelData()[0].length; x++) {
				int index = levels.get(levelIndex).getSpriteIndex(x, y);
				g.drawImage(blockSprite[index], x * Game.TILE - xLevelOffset, y * Game.TILE, Game.TILE, Game.TILE,
						null);
			}
		}
	}

	public void update() {

	}

	public Level getCurrentLevel() {
		return levels.get(levelIndex);
	}

	public int getAmountOfLevels() {
		return levels.size();
	}

	public void importSprite() {
		BufferedImage image = null;
		if (levelIndex < 2)
			image = LoadSave.getSprite(LoadSave.TILES);
		else if(levelIndex >= 2)
			image = LoadSave.getSprite(LoadSave.TILES2);
			
		blockSprite = new BufferedImage[50];
		for (int y = 0; y < image.getHeight() / 32; y++) {
			for (int x = 0; x < image.getWidth() / 32; x++) {
				int index = y * image.getWidth() / 32 + x;
				blockSprite[index] = image.getSubimage(x * Game.tile * 2, y * Game.tile * 2, Game.tile * 2,
						Game.tile * 2);
			}
		}
	}

	public void loadNextLevel() {
		levelIndex++;
		importSprite();
		if (levelIndex >= levels.size()) {
			levelIndex = 0;
			System.out.println("NO MORE LEVELS");
			GameState.state = GameState.MENU;
		}

		Level newLevel = levels.get(levelIndex);
		game.getPlaying().getEnemyManager().loadEnemy(newLevel);
		game.getPlaying().getPlayer().loadLevelData(newLevel.getLevelData());
		game.getPlaying().getChestManager().loadChest(newLevel);
		game.getPlaying().setMaxLevelOffset(newLevel.getMaxLevelOffseXt());
	}

	public ArrayList<Chest> getChest() {
		return levels.get(levelIndex).getChest();
	}
	
	public int getLevelIndex() {
		return levelIndex;
	}
	
	public void gameOver() {
		this.levelIndex = 0;
	}
}
