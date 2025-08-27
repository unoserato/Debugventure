package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import gamestates.GameState;
import main.Game;

public abstract class Entity {

	protected float x, y, width, height;
	protected Rectangle2D.Float hitbox;

	public Entity(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	protected void renderHitbox(Graphics g, int xLevelOffset) {
		g.setColor(Color.red);
		g.drawRect((int) hitbox.x - xLevelOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
	}

	protected void initHitbox(float x, float y, float width, float height) {
		hitbox = new Rectangle2D.Float(x, y, width, height);
	}

	public Rectangle2D.Float getHitbox() {
		return hitbox;
	}
}
