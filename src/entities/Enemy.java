package entities;

import static utilz.Constants.Enemy.*;
import static utilz.HelpMethod.*;
import static utilz.Constants.*;

import java.awt.geom.Rectangle2D;
import java.util.Random;

import static utilz.Constants.Directions.*;

import main.Game;

public abstract class Enemy extends Entity {

	protected int walkDir = LEFT;

	// for flying enemies
	protected float patrolStartX;
	protected float patrolEndX;
	protected float patrolStartY;
	protected float patrolLength = Game.TILE * 10;
	protected boolean isFollowingPlayer = false;

	protected int aniIndex, //
			tick, // for animation
			aniSpeed = 10, //
			enemyState = 0, // changes the enemy action
			enemyType, // type of enemy e.g. tv, scout
			tileY, // y position of enemy
			maxHealth, currentHealth;

	protected float fallSpeed, walkSpeed = 0.1f * Game.scale, ySpeed;;

	// Knock back variables
	protected float knockbackX, knockbackY;
	protected boolean isKnockedBack = false;

	protected boolean firstUpdate = true, inAir, active = true, attackOnce, doAlertAnimation = false,
			animateOnce = false;
	protected float attackDistance = Game.TILE;

	public Enemy(float x, float y, float width, float height, int enemyType) {
		super(x, y, width, height);
		this.enemyType = enemyType;
		initHitbox(x, y, width, height);
		this.patrolStartX = x;
		this.patrolEndX = x + patrolLength;
		this.patrolStartY = y;
		maxHealth = GetMaxHealth(enemyType);
		currentHealth = maxHealth;
	}

	protected void firstUpdateCheck(int[][] levelData) {
		if (!IsEntityOnFloor(hitbox, levelData))
			inAir = true;
		firstUpdate = false;
	}

	protected void getYIndexOfFlyingEnemies(int[][] levelData) {
		tileY = (int) (hitbox.y / Game.TILE);
	}

	protected void updateInAir(int[][] levelData) {
		if (CanMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, levelData)) {
			hitbox.y += fallSpeed;
			fallSpeed += GRAVITY;
		} else {
			inAir = false;
			hitbox.y = getEntityUnderRoofOrAboveFloor(hitbox, fallSpeed);
			tileY = (int) (hitbox.y / Game.TILE);
		}
	}

	protected void move(int[][] levelData) {
		float xSpeed = 0;

		if (walkDir == LEFT)
			xSpeed = -walkSpeed;
		else
			xSpeed = walkSpeed;

		if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, levelData)) {
			if (IsFLoor(hitbox, xSpeed, levelData)) {
				hitbox.x += xSpeed;
				return;
			}
		}

		changeWalkDir();
	}

	protected void applyKnockback(int[][] levelData) {
		if (CanMoveHere(hitbox.x + knockbackX, hitbox.y, hitbox.width, hitbox.height, levelData)) {
			hitbox.x += knockbackX;
		}
		if (CanMoveHere(hitbox.x, hitbox.y + knockbackY, hitbox.width, hitbox.height, levelData)) {
			hitbox.y += knockbackY;
		}

		knockbackX = 0;
		knockbackY = 0;
		isKnockedBack = false;

		if (!IsEntityOnFloor(hitbox, levelData)) {
			inAir = true;
		}
	}

	protected void turnTowardsPlayer(Player player) {
		if (player.hitbox.x > hitbox.x)
			walkDir = RIGHT;
		else
			walkDir = LEFT;
	}

	protected boolean canSeePlayer(int[][] levelData, Player player) {
		int playerTileY = (int) (player.getHitbox().y / Game.TILE);
		if (playerTileY == tileY || playerTileY + 1 == tileY || playerTileY + 2 == tileY)
			if (isPlayerInRange(player)) {
				if (IsSightClear(levelData, hitbox, player.hitbox, tileY))
					return true;
			}
		return false;
	}

	protected boolean isPlayerInYAxisAsEnemy(int[][] levelData, Player player) {
		int playerTileY = (int) (player.getHitbox().y / Game.TILE);
		if (playerTileY == tileY) {
			if (isPlayerInRange(player)) {
				if (IsSightClear(levelData, hitbox, player.hitbox, tileY))
					return true;
			}
		}
		return false;
	}

	protected boolean isPlayerInRange(Player player) {
		int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
		return absValue <= attackDistance * 5;
	}

	protected boolean isPlayerCloseForAttack(Player player) {
		int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
		return absValue <= attackDistance;
	}

	// FOR FLYING ENEMIES
	protected void moveFlyingEnemies(int[][] levelData, Player player) {
		float xSpeed = 0;
		float ySpeed = 0;

		// Check if the player is visible
		if (canFlyingEnemySeePlayer(levelData, player)) {
			isFollowingPlayer = true; // Set the state to following

			// Determine X direction
			if (player.hitbox.x > hitbox.x) {
				xSpeed = walkSpeed; // Move right
			} else {
				xSpeed = -walkSpeed; // Move left
			}

			// Determine Y direction
			if (player.hitbox.y > hitbox.y) {
				ySpeed = walkSpeed; // Move down
			} else {
				ySpeed = -walkSpeed; // Move up
			}
		} else {
			isFollowingPlayer = false; // Set the state to patrolling
		}

		// Move in X direction
		if (isFollowingPlayer) {
			if (CanMoveHere(hitbox.x, hitbox.y + ySpeed, hitbox.width, hitbox.height, levelData)) {
				hitbox.y += ySpeed;
			}
			if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, levelData)) {
				hitbox.x += xSpeed;
			}
			// Move in Y direction
		} else {
			// Patrol logic
			patrol(levelData);
		}

	}

	protected void patrol(int[][] levelData) {
		// Patrol logic for X direction
		float xSpeed = (walkDir == LEFT) ? -walkSpeed : walkSpeed;
		// Patrol logic for Y direction
		ySpeed = (hitbox.y < patrolStartY) ? walkSpeed : -walkSpeed;

		// Check if the enemy is within the patrol area in X
		if ((walkDir == LEFT && hitbox.x + xSpeed < patrolStartX)
				|| (walkDir == RIGHT && hitbox.x + xSpeed > patrolEndX)) {
			changeWalkDir(); // Change direction if out of bounds
		}
		// Check if the enemy is within the patrol area in Y
		if ((hitbox.y + ySpeed < patrolStartY)) {
			ySpeed = -ySpeed; // Reverse direction if out of bounds
		}

		// Move in Y direction
		if (CanMoveHere(hitbox.x, hitbox.y + ySpeed, hitbox.width, hitbox.height, levelData)) {
			hitbox.y += ySpeed;
		}
		// Move in X direction
		if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, levelData)) {
			hitbox.x += xSpeed;
		} else {
			changeWalkDir();
		}
	}

	protected boolean canFlyingEnemySeePlayer(int[][] levelData, Player player) {
		int playerTileY = (int) (player.getHitbox().y / Game.TILE);
		int minVisibleTileY = tileY - 3; // 3 tiles above
		int maxVisibleTileY = tileY + 3; // 3 tiles below
		if (isPlayerInRange(player))
			if (playerTileY >= minVisibleTileY && playerTileY <= maxVisibleTileY)
				if(CanMoveHere(hitbox.x, hitbox.y + ySpeed, hitbox.width, hitbox.height, levelData))
					return true;
		return false;
//				(int)(hitbox.y / Game.TILE)
	}

	protected void applyFlyingKnockback(int[][] levelData) {
		if (CanMoveHere(hitbox.x + knockbackX, hitbox.y, hitbox.width, hitbox.height, levelData)) {
			hitbox.x += knockbackX;
		}

		knockbackX = 0;
		knockbackY = 0;
		isKnockedBack = false;
	}

	protected void newState(int enemyState) {
		this.enemyState = enemyState;
		tick = 0;
		aniIndex = 0;
	}

	public void enemyIsHurt(int amount, Player player) {
		currentHealth -= amount;

		float knockBack = 10f * Game.scale;
		if (player.hitbox.x > hitbox.x) {
			knockbackX = -knockBack;
//			walkDir = RIGHT;
		} else {
			knockbackX = knockBack;
//			walkDir = LEFT;
		}
		knockbackY = -knockBack;
		isKnockedBack = true;

		if (currentHealth <= 0)
			newState(DEAD);
		else
			newState(HURT);
	}

	// THIS TO HURT THE PLAYER
	protected void hurtPlayer(Rectangle2D.Float attackBox, Player player, int enemyType) {
		if (attackBox.intersects(player.hitbox)) {
			player.changePlayerHealth(-GetEnemyDamage(enemyType));
			player.applyKnockback(this);
		}
		attackOnce = true;
	}

	protected void updateAnimation() {
		tick++;
		if (tick >= aniSpeed) {
			tick = 0;
			aniIndex++;
			if (aniIndex >= GetSpriteAmount(enemyType, enemyState)) {
				aniIndex = 0;

				switch (enemyState) {
				case ATK, HURT, ALERT -> enemyState = IDLE;
				case DEAD -> active = false;
				}
			}
		}
	}

	protected void changeWalkDir() {
		if (walkDir == LEFT) {
			walkDir = RIGHT;
		} else {
			walkDir = LEFT;
		}
	}

	public int getAniIndex() {
		return aniIndex;
	}

	public int getEnemyState() {
		return enemyState;
	}

	public int getDirection() {
		return walkDir;
	}

	public boolean isActive() {
		return active;
	}

	public void resetEnemy() {
		hitbox.x = x;
		hitbox.y = y;
		firstUpdate = true;
		currentHealth = maxHealth;
		newState(IDLE);
		active = true;
		fallSpeed = 0;

	}
}
