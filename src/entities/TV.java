package entities;

import static utilz.Constants.Directions.RIGHT;
import static utilz.Constants.Enemy.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import audio.AudioPlayer;
import main.Game;

public class TV extends Enemy {

	// attack box
	public Rectangle2D.Float attackBox;
	private int attackBoxOffset;

	public TV(float x, float y) {
		super(x, y, TV_DRAW_WIDTH, TV_DRAW_HEIGHT, TV);
		initHitbox(x, y, TV_HITBOX_WIDTH, TV_HITBOX_HEIGHT);
		initAttackBox();
	}

	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y, (int) (TV_HITBOX_WIDTH), (int) (TV_HITBOX_HEIGHT));
		attackBoxOffset = (int) (TV_HITBOX_WIDTH / 2.0f);
	}

	public void update(int[][] levelData, Player player) {
		updateBehavior(levelData, player);
		updateAnimation();
		updateAttackBox();
	}

	private void updateAttackBox() {
		attackBox.x = hitbox.x + (attackBoxOffset * (flipWidth() * -1));
		attackBox.y = hitbox.y;
	}

	private void updateBehavior(int[][] levelData, Player player) {
		if (firstUpdate)
			firstUpdateCheck(levelData);

		if (isKnockedBack)
			applyKnockback(levelData);

		if (inAir)
			updateInAir(levelData);
		else {
			switch (enemyState) {
			case IDLE:
				newState(WALK);
				break;
			case WALK:
				// checks if cant see the player at all: all booleans resets
				if (!canSeePlayer(levelData, player)) {
					doAlertAnimation = false;
					animateOnce = false;
					aniSpeed = 10;
					walkSpeed = 0.1f * Game.scale;
				}

				// checks if the player is in the same Y axis to do the alert animation
				if (isPlayerInYAxisAsEnemy(levelData, player)) {
					doAlertAnimation = true;
					walkSpeed = 0.5f * Game.scale;
					aniSpeed = 6;
				} else {
					walkSpeed = 0.1f * Game.scale;
					aniSpeed = 10;
				}
				
				// checks if permitted to do animation and if haven't animated alert animation yet
				if (doAlertAnimation && !animateOnce) {
					
					// if permitted to do animation and haven't animated it yet
					animateOnce = true;
					doAlertAnimation = false;
					aniSpeed = 6;
					turnTowardsPlayer(player);
					newState(ALERT);
				}
				
				if(canSeePlayer(levelData, player) && isPlayerInYAxisAsEnemy(levelData, player)) {
					turnTowardsPlayer(player);
					if(isPlayerCloseForAttack(player))
						newState(ATK);
				}

				move(levelData);
				break;
			case ATK:
				if(aniIndex == 0)
					attackOnce = false;
				
				if (aniIndex == 8) {
					player.getPlayingState().getGame().getAudioPlayer().playEffect(AudioPlayer.ATK_TV);
					hurtPlayer(attackBox, player, TV);
				}
				break;
			case ALERT:

				break;
			case HURT:
				break;
			case DEAD:
				break;
			}
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
}
