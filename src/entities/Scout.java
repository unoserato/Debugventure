package entities;

import static utilz.Constants.Directions.RIGHT;
import static utilz.Constants.Enemy.*;

import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import audio.AudioPlayer;
import main.Game;

public class Scout extends Enemy {

	// attack box
	public Rectangle2D.Float attackBox, deathBox;
	private int attackBoxOffsetX;

	public Scout(float x, float y) {
		super(x, y, SCOUT_DRAW_WIDTH, SCOUT_DRAW_HEIGHT, SCOUT);
		initHitbox(x, y, SCOUT_HITBOX_WIDTH, SCOUT_HITBOX_HEIGHT);
		inAir = true;
		initAttackBox();
		initDeathBox();
	}

	private void initDeathBox() {
		deathBox = new Rectangle2D.Float(x, y, 30, 30);
	}

	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y, (int) (SCOUT_HITBOX_WIDTH), (int) (SCOUT_HITBOX_HEIGHT * 1.2f));
		attackBoxOffsetX = (int) (SCOUT_HITBOX_WIDTH);
	}

	public void update(int[][] levelData, Player player) {

		deathBox.x = hitbox.x;
		deathBox.y = hitbox.y;
		updateBehavior(levelData, player);
		updateAnimation();
		updateAttackBox();
	}

	private void updateAttackBox() {
		attackBox.x = hitbox.x + (attackBoxOffsetX * (flipWidth() * -1));
		attackBox.y = hitbox.y;
	}

	private void updateBehavior(int[][] levelData, Player player) {

		getYIndexOfFlyingEnemies(levelData);

		if (isKnockedBack)
			applyFlyingKnockback(levelData);

		switch (enemyState) {
		case IDLE:
			newState(WALK);
			break;
		case WALK:
			walkSpeed = 0.5f * Game.scale;

			if (canFlyingEnemySeePlayer(levelData, player)) {
				if (isPlayerInRange(player)) {
					turnTowardsPlayer(player);
					if (isPlayerInYAxisAsEnemy(levelData, player))
						if (isPlayerCloseForAttack(player))
							newState(ATK);
				}
			}

			moveFlyingEnemies(levelData, player);
			break;
		case ATK:
			aniSpeed = 5;
			if (aniIndex == 0)
				attackOnce = false;

			if (aniIndex == 9) {
				player.getPlayingState().getGame().getAudioPlayer().playEffect(AudioPlayer.ATK_SCOUT);
				hurtPlayer(attackBox, player, SCOUT);
			}
			aniSpeed = 10;
			break;
		case HURT:
			break;
		case DEAD:
			aniSpeed = 6;
			if (inAir)
				updateInAir(levelData);
			break;
		}

	}

	public void renderAttackBox(Graphics g, int xLevelOffset) {
		g.setColor(Color.red);
		g.drawRect((int) attackBox.x - xLevelOffset, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
	}

	public int flipX() {
		if (walkDir == RIGHT)
			return (int) width;
		else
			return 0;
	}

	public int flipWidth() {
		if (walkDir == RIGHT)
			return -1;
		else
			return 1;
	}

	public int deadXOffset() {
		if (enemyState == DEAD)
			if (walkDir == RIGHT)
				return SCOUT_X_DEAD_OFFSET;
			else
				return -SCOUT_X_DEAD_OFFSET;
		return 0;
	}

	public int deadYOffset() {
		if (enemyState == DEAD)
			return -SCOUT_Y_DEAD_OFFSET;
		return 0;
	}
}
