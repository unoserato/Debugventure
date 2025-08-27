package entities;

import static utilz.Constants.Directions.*;
import static utilz.Constants.Enemy.*;

import java.awt.geom.Rectangle2D;

import audio.AudioPlayer;
import main.Game;

public class MechaStoneGolem extends Enemy {

	float hitboxYDead;

	public MechaStoneGolem(float x, float y) {
		super(x, y, MECHA_DRAW_WIDTH, MECHA_DRAW_HEIGHT, GOLEM);
		initHitbox(x, y, MECHA_HITBOX_WIDTH, MECHA_HITBOX_HEIGHT);
		this.aniSpeed = 20;
	}

	public void update(int[][] levelData, Player player) {
		updateBehavior(levelData, player);
		updateAnimation();

		if (enemyState != DEAD)
			hitboxYDead = hitbox.y + (hitbox.height/2);
	}

	private void updateBehavior(int[][] levelData, Player player) {
		getYIndexOfFlyingEnemies(levelData);

		switch (enemyState) {
		case IDLE:
			newState(WALK);
			break;
		case WALK:
			if (canFlyingEnemySeePlayer(levelData, player)) {
				turnTowardsPlayer(player);
				if (hitbox.intersects(player.getHitbox())) {
					player.changePlayerHealth(-GetEnemyDamage(GOLEM));
					player.changePlayerEnergy(-0.2f);
				}
			}
			moveFlyingEnemies(levelData, player);
			break;
		case ATK:
			break;
		case HURT:
			break;
		case DEAD:
			aniSpeed = 50;
			initHitbox(hitbox.x, hitboxYDead, Game.scale * 14, Game.scale * 14);
			if (inAir)
				updateInAir(levelData);
			break;
		}

	}

	public int flipX() {
		if (walkDir == LEFT)
			return (int) width;
		else
			return 0;
	}

	public int flipWidth() {
		if (walkDir == LEFT)
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
			return -MECHA_Y_DEAD_OFFSET;
		return 0;
	}

}
