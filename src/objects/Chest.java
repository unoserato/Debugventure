package objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import entities.Player;
import main.Game;
import utilz.LoadSave;

public class Chest {

	private float x, y, yOffset = Game.scale * 5f;
	public boolean isOpen;
	private BufferedImage[] chestSprite;
	private Rectangle2D.Float chestHitbox;

	public Chest(float x, float y) {
		this.x = x;
		this.y = y;
		loadImages();
		initHitbox();
		this.isOpen = false;
	}

	private void initHitbox() {
		chestHitbox = new Rectangle2D.Float(x, y + yOffset, Game.TILE, Game.scale * 11);
	}

	private void loadImages() {
		chestSprite = new BufferedImage[2];
		BufferedImage temp = LoadSave.getSprite(LoadSave.CHEST);
		for (int i = 0; i < 2; i++) {
			chestSprite[i] = temp.getSubimage(i * 36, 0, 36, 23);
		}
	}

	public void render(Graphics g, int xLevelOffset) {
		// Render the chest based on its state (open or closed)
		int spriteIndex = isOpen ? 1 : 0; // Assuming index 0 is closed and index 1 is open
		g.drawImage(chestSprite[spriteIndex], (int) chestHitbox.x - xLevelOffset, (int) (chestHitbox.y),
				(int) chestHitbox.width, (int) chestHitbox.height, null);
		g.setColor(Color.red);
//      g.drawRect((int)chestHitbox.x - xLevelOffset, (int)chestHitbox.y, (int)chestHitbox.width, (int)chestHitbox.height);
	}

	public boolean playerIntersectsChest(Rectangle2D.Float player) {
		if (!isOpen)
			return player.intersects(chestHitbox);
		return false;
	}

	public void openChest() {
		isOpen = true;
	}
}
