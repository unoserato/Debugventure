package objects;

import static utilz.HelpMethod.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import main.Game;
import utilz.LoadSave;

public class Arrow {

	private Rectangle2D.Float arrowAttackBox;
	private boolean isActive;
	private int arrowDrawWidth = Game.scale * 18, arrowDrawHeight = Game.scale * 8, arrowDrawYOffset = Game.scale * 3;

	private float arrowHitboxYOffset = Game.scale * 9, arrowHitboxXOffset = Game.scale * 0;
	private int arrowRange = Game.TILE * 10, arrowSpeed = Game.scale * 3;

	private int flipX = 0;
	private float xPos, yPos, dir;
	private BufferedImage arrowImage;

	public Arrow(float xPos, float yPos, int dir) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.dir = dir;
		isActive = true;
		initAttackBox();
	}

	private void initAttackBox() {
		if (dir < 0)
			flipX = arrowDrawWidth;

		arrowAttackBox = new Rectangle2D.Float(xPos + arrowHitboxXOffset - flipX, yPos + arrowHitboxYOffset,
				(int) (18 * Game.scale), (int) (2 * Game.scale));
		arrowImage = LoadSave.getSprite(LoadSave.ARROW);
		arrowSpeed = (int) (arrowSpeed * dir);
		arrowDrawWidth = (int) (arrowDrawWidth * dir);
	}

	public void updateXPos(int[][] levelData) {
		if (CanMoveHere(arrowAttackBox.x + arrowSpeed, arrowAttackBox.y, arrowAttackBox.width, arrowAttackBox.height,
				levelData))
			arrowAttackBox.x += arrowSpeed;
		else
			isActive = false;
		
		if (Math.abs(arrowAttackBox.x - xPos) > arrowRange)
			isActive = false;
	}

	public void renderArrow(Graphics g, int xLevelOffset) {
		g.drawImage(arrowImage, (int) arrowAttackBox.x - xLevelOffset + flipX,
				(int) (arrowAttackBox.y - arrowDrawYOffset), arrowDrawWidth, arrowDrawHeight, null);
//		renderBowAttackBox(g, xLevelOffset);

	}

	private void renderBowAttackBox(Graphics g, int xLevelOffset) {
		g.setColor(Color.red);
		g.drawRect((int) arrowAttackBox.x - xLevelOffset, (int) arrowAttackBox.y, (int) arrowAttackBox.width,
				(int) arrowAttackBox.height);
	}

	public boolean isArrowActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Rectangle2D.Float getArrowHitbox() {
		return arrowAttackBox;
	}

}
