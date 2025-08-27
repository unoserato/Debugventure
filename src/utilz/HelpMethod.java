package utilz;

import static utilz.Constants.Enemy.SCOUT;
import static utilz.Constants.Enemy.TV;
import static utilz.Constants.Enemy.GOLEM;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.MechaStoneGolem;
import entities.Scout;
import entities.TV;
import main.Game;
import objects.Chest;

public class HelpMethod {

	public static boolean CanMoveHere(float x, float y, float width, float height, int[][] levelData) {
		if (!isSolid(x, y, levelData))
			if (!isSolid(x + width, y + height, levelData))
				if (!isSolid(x + width, y, levelData))
					if (!isSolid(x, y + height, levelData))
						return true;
//		System.out.println("COLLISION DETECTED");
		return false;
	}

	public static boolean isSolid(float x, float y, int[][] levelData) {
		int maxWidth = levelData[0].length * Game.TILE;
		if (x < 0 || x >= maxWidth)
			return true;
		if (y < 0 || y >= Game.SCREEN_HEIGHT)
			return true;

		float xIndex = x / Game.TILE;
		float yIndex = y / Game.TILE;

		return IsTileSolid((int) xIndex, (int) yIndex, levelData);
	}

	public static boolean IsTileSolid(int xTile, int yTile, int[][] levelData) {
		int value = levelData[yTile][xTile];

		if (value != 0 && value < 38)
			return true;
		if (value == 40) {
			return true;
		}
		return false;
	}

	public static boolean CheckTrap(float x, float y, float width, float height, int[][] levelData) {
		if (!IsTrap(x, y + height + 1, levelData))
			if (!IsTrap(x + width, y + height + 1, levelData))
				return false;
		return true;
	}

	public static boolean IsTrap(float x, float y, int[][] levelData) {
		float xIndex = x / Game.TILE;
		float yIndex = y / Game.TILE;

		int value = levelData[(int) yIndex][(int) xIndex];

		if (value == 40)
			return true;
		return false;
	}

	public static boolean CheckWhiteDoor(float x, float y, int[][] levelData) {
		if (IsWhiteDoor(x, y, levelData))
				return true;
		return false;
	}

	public static boolean IsWhiteDoor(float x, float y, int[][] levelData) {
		float xIndex = x / Game.TILE;
		float yIndex = y / Game.TILE;

		int value = levelData[(int) yIndex][(int) xIndex];

		if (value == 41)
			return true;
		return false;
	}

	public static float getEntityXPos(Rectangle2D.Float hitbox, float xSpeed) {
		int currentTile = (int) (hitbox.x / Game.TILE);
		if (xSpeed > 0) {
			int tileXPos = currentTile * Game.TILE;
			int xOffset = (int) (Game.TILE - hitbox.width);
			return tileXPos + xOffset - 1;
		} else {
			return currentTile * Game.TILE;
		}
	}

	public static float getEntityUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
		int currentTile = (int) (hitbox.y / Game.TILE);
		if (airSpeed > 0) {
			int tileYPos = currentTile * Game.TILE;
			int yOffset = (int) (Game.TILE - hitbox.height);
			return tileYPos + yOffset - 1;
		} else {
			return currentTile * Game.TILE;
		}
	}

	public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] levelData) {
		if (!isSolid(hitbox.x, hitbox.y + hitbox.height + 1, levelData))
			if (!isSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, levelData))
				return false;
		return true;
	}

	public static boolean IsFLoor(Rectangle2D.Float hitbox, float xSpeed, int[][] levelData) {
		if (xSpeed > 0)
			return isSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, levelData);
		else
			return isSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, levelData);
	}

	public static boolean IsAllTileWalkable(int xStart, int xEnd, int y, int[][] levelData) {
		for (int i = 0; i < xEnd - xStart; i++) {
			if (IsTileSolid(xStart + i, y, levelData))
				return false;
			if (!IsTileSolid(xStart + i, y + 1, levelData))
				return false;
		}
		return true;
	}

	public static boolean IsSightClear(int[][] levelData, Rectangle2D.Float enemyHitbox, Rectangle2D.Float playerHitbox,
			int tileY) {
//		int firstTileX = (int) (enemyHitbox.x / Game.TILE);
//		int secondTileX = (int) (playerHitbox.x / Game.TILE);
//
//		if (firstTileX > secondTileX)
//			return IsAllTileWalkable(secondTileX, firstTileX, tileY, levelData);
//		else
//			return IsAllTileWalkable(firstTileX, secondTileX, tileY, levelData);
		int firstTileX = (int) (enemyHitbox.x / Game.TILE);

		int secondTileX;
		if (isSolid(playerHitbox.x, playerHitbox.y + playerHitbox.height + 1, levelData))
			secondTileX = (int) (playerHitbox.x / Game.TILE);
		else
			secondTileX = (int) ((playerHitbox.x + playerHitbox.width) / Game.TILE);

		if (firstTileX > secondTileX)
			return IsAllTileWalkable(secondTileX, firstTileX, tileY, levelData);
		else
			return IsAllTileWalkable(firstTileX, secondTileX, tileY, levelData);
	}

	public static int[][] GetLevelData(BufferedImage image) {
		int[][] levelData = new int[image.getHeight()][image.getWidth()];

		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				Color color = new Color(image.getRGB(x, y));
				int value = color.getRed();
				if (value >= 50) {
					value = 0;
				}
				levelData[y][x] = value;
			}
		}
		return levelData;
	}

	public static ArrayList<TV> GetTV(BufferedImage image) {
		ArrayList<TV> list = new ArrayList<>();
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				Color color = new Color(image.getRGB(x, y));
				int value = color.getGreen();
				if (value == TV) {
					list.add(new TV(x * Game.TILE, y * Game.TILE));
				}
			}
		}
		return list;
	}

	public static ArrayList<Scout> GetScout(BufferedImage image) {
		ArrayList<Scout> list = new ArrayList<>();
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				Color color = new Color(image.getRGB(x, y));
				int value = color.getGreen();
				if (value == SCOUT) {
					list.add(new Scout(x * Game.TILE, y * Game.TILE));
				}
			}
		}
		return list;
	}
	
	public static ArrayList<MechaStoneGolem> GetGolem(BufferedImage image) {
		ArrayList<MechaStoneGolem> list = new ArrayList<>();
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				Color color = new Color(image.getRGB(x, y));
				int value = color.getGreen();
				if (value == GOLEM) {
					list.add(new MechaStoneGolem(x * Game.TILE, y * Game.TILE));
				}
			}
		}
		return list;
	}

	public static ArrayList<Chest> GetChest(BufferedImage image) {
		ArrayList<Chest> list = new ArrayList<>();
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				Color color = new Color(image.getRGB(x, y));
				int value = color.getBlue();
				if (value == 1) {
					list.add(new Chest(x * Game.TILE, y * Game.TILE));
//					System.out.println("Chest added at: " + x * Game.TILE + ", " + y * Game.TILE);
				}
			}
		}
		return list;
	}

	public static Point GetPlayerSpawn(BufferedImage img) {
		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getBlue();
				if (value == 0)
					return new Point(i * Game.TILE, j * Game.TILE);
			}
		return new Point(1 * Game.TILE, 1 * Game.TILE);
	}
}
