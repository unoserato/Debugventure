package levels;

import static utilz.HelpMethod.GetLevelData;
import static utilz.HelpMethod.GetScout;
import static utilz.HelpMethod.GetTV;
import static utilz.HelpMethod.GetGolem;
import static utilz.HelpMethod.GetChest;
import static utilz.HelpMethod.GetPlayerSpawn;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.MechaStoneGolem;
import entities.Scout;
import entities.TV;
import objects.Chest;
import main.Game;

public class Level {

	private BufferedImage image;
	private int[][] levelData;
	private ArrayList<TV> Tv_Monsters;
	private ArrayList<Scout> Scout_Monsters;
	private ArrayList<MechaStoneGolem> Mecha_Monsters;
	private ArrayList<Chest> Chest;
	private int levelTilesWide;
	private int maxTileOffset;
	private int maxLevelOffsetX;
	private Point playerSpawn;

	public Level(BufferedImage image) {
		this.image = image;
		createLevelData();
		createEnemies();
		calculateLevelOffset();
		calcPlayerSpawn();
	}
	
	private void calcPlayerSpawn() {
		playerSpawn = GetPlayerSpawn(image);
	}

	private void calculateLevelOffset() {
		levelTilesWide = image.getWidth();
		maxTileOffset = levelTilesWide - Game.width;
		maxLevelOffsetX = maxTileOffset * Game.TILE;
	}

	private void createEnemies() {
		Tv_Monsters = GetTV(image);
		Scout_Monsters = GetScout(image);
		Mecha_Monsters = GetGolem(image); 
		Chest = GetChest(image);
	}

	private void createLevelData() {
		levelData = GetLevelData(image);
		Chest = GetChest(image);
	}

	public int getSpriteIndex(int x, int y) {
		return levelData[y][x];
	}

	public int[][] getLevelData() {
		return levelData;
	}

	public int getMaxLevelOffseXt() {
		return maxLevelOffsetX;
	}

	public ArrayList<TV> getTV() {
		return Tv_Monsters;
	}

	public ArrayList<Scout> getScout() {
		return Scout_Monsters;
	}

	public ArrayList<MechaStoneGolem> getGolem() {
		return Mecha_Monsters;
	}
	
	public ArrayList<Chest> getChest() {
		return Chest;
	}
	
	public BufferedImage getCurrentImage() {
		return image;
	}
	
	public Point getPlayerSpawn() {
		return playerSpawn;
	}
}
