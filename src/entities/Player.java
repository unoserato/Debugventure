package entities;

import static utilz.Constants.GRAVITY;
import static utilz.Constants.PlayerConstants.*;
import static utilz.Constants.PlayerConstants.HURT;
import static utilz.Constants.PlayerConstants.BOW;
import static utilz.Constants.PlayerConstants.DEAD;
import static utilz.Constants.PlayerConstants.FALL;
import static utilz.Constants.PlayerConstants.GetPlayerActionEnergyCost;
import static utilz.Constants.PlayerConstants.GetSpriteAmount;
import static utilz.Constants.PlayerConstants.IDLE;
import static utilz.Constants.PlayerConstants.JUMP;
import static utilz.Constants.PlayerConstants.RUN;
import static utilz.Constants.PlayerConstants.WALK;
import static utilz.Constants.PlayerConstants.weaponType;
import static utilz.HelpMethod.CanMoveHere;
import static utilz.HelpMethod.IsEntityOnFloor;
import static utilz.HelpMethod.getEntityUnderRoofOrAboveFloor;
import static utilz.HelpMethod.getEntityXPos;
import static utilz.HelpMethod.CheckTrap;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import audio.AudioPlayer;
import gamestates.PlayingState;
import main.Game;
import objects.Arrow;
import utilz.LoadSave;

public class Player extends Entity {

	public static boolean isDead = false;

	// OTHER CLASS
	PlayingState playingState;
	EnemyManager enemyManager;

	// animation
	private BufferedImage[][] animation;
	private int tick = 0, aniSpeed = 18, aniIndex = 0;
	private int flipX = 0, flipWidth = 1; // flip image
	public static boolean isEnergyLow = false;

	// STATUS
	private BufferedImage statusBar, statusBarTransparent, redScreenOverlay;

	private int statusBarWidth = (int) (100 * Game.scale);
	private int statusBarHeight = (int) (30 * Game.scale);
	private int statusBarXPos = (int) (Game.TILE / 3f);
	private int statusBarYPos = (int) (Game.TILE / 3f);

	private int healthBarWidth = (int) (79 * Game.scale);
	private int healthBarHeight = (int) (2 * Game.scale);
	private int healthBarXPos = (int) (17.2 * Game.scale);
	private int healthBarYPos = (int) (7.5 * Game.scale);

	private int energyBarWidth = (int) (54 * Game.scale);
	private int energyBarHeight = (int) (1.3 * Game.scale);
	private int energyBarXPos = (int) (23 * Game.scale);
	private int energyBarYPos = (int) (17.8 * Game.scale);

	private float maxHealth = 100;
	private float maxEnergy = 500;
	private float currentHealth = maxHealth;
	private float currentEnergy = maxEnergy;
	private float healthWidth = healthBarWidth;
	private float energyWidth = energyBarWidth;

	// player movement
	private int playerAction = IDLE, attackIndex = 6;
	private boolean left, up, right, down, jump = false, shift = false, firstUpdate = true;
	public boolean moving = false, attack = false, playerAttacking, isKnockbacked;
	private float playerSpeed = 0.8f * Game.scale;

	// jump & gravity
	private float jumpSpeed = -1.2f * Game.scale;
	private float airSpeed = 0f;
	private float fallSpeedAfterCollision = 0.5f * Game.scale;
	private boolean inAir = false, checkDead = false;

	// level data
	private int[][] levelData;

	// attack box
	public Rectangle2D.Float attackBox;
	// player hitBox
	private int xDrawOffset = 19 * Game.scale, yDrawOffset = 17 * Game.scale;

	// arrow
	private List<Arrow> arrows;
	private Arrow arrow;
	public static int arrowCount = 10;

	public Player(float x, float y, float width, float height, PlayingState playingState) {
		super(x, y, width, height);
		this.playingState = playingState;
		enemyManager = new EnemyManager(playingState);
		importImage();
		initHitbox(x, y, 9 * Game.scale, 14 * Game.scale);

		// attack box
		attackBox = new Rectangle2D.Float(x, y, (int) (17 * Game.scale), (int) (14 * Game.scale));
		arrows = new ArrayList<>();
	}

	public void setSpawn(Point spawn) {
		this.x = spawn.x;
		this.y = spawn.y;
		hitbox.x = x;
		hitbox.y = y;
	}

	// CLASS UPDATE
	public void update() {
		updateHealthEnergyBar();
		updatePosition();
		updateAnimation();
		setAnimation();
		updateAttackBox();

//		System.out.println(arrowCount);

		// Update all arrows
		for (int i = 0; i < arrows.size(); i++) {
			arrow = arrows.get(i);
			arrow.updateXPos(levelData);
			if (!arrow.isArrowActive()) {
				arrows.remove(i);
				i--; // Adjust index after removal
			} else if (playingState.arrowHitEnemy(arrow.getArrowHitbox())) {
				playingState.playerHitEnemy(arrow.getArrowHitbox(), this);
				arrow.setActive(false);
				arrows.remove(i);
				i--; // Adjust index after removal
			}
		}

		if (attack)
			if (!isEnergyLow)
				playerAttack();
		if (currentHealth <= 0) {
			isDead = true;
		}
	}

	// UPDATE HEALTHBAR
	private void updateHealthEnergyBar() {
		healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBarWidth);
		energyWidth = (int) ((currentEnergy / (float) maxEnergy) * energyBarWidth);
		if (!isDead)
			isDead = CheckTrap(hitbox.x, hitbox.y, hitbox.width, hitbox.height, levelData);

		changePlayerEnergy(GetPlayerActionEnergyCost(playerAction));
		if (currentEnergy <= 5)
			isEnergyLow = true;
		else if (currentEnergy > 40)
			isEnergyLow = false;
	}

	// UPDATE ATTACK BOX
	private void updateAttackBox() {
		// sword
		if (flipWidth > 0) {
			attackBox.x = hitbox.x + hitbox.width;
		} else if (flipWidth < 0) {
			attackBox.x = hitbox.x - attackBox.width;
		}

		attackBox.y = hitbox.y;
	}

	// RENDER PLAYER
	public void render(Graphics g, int xLevelOffset) {
		g.setColor(Color.yellow);
		g.setFont(new Font("Arial Black", Font.PLAIN, Game.scale * 5));

		g.drawImage(animation[playerAction][aniIndex], (int) (hitbox.x + flipX - xDrawOffset) - xLevelOffset,
				(int) (hitbox.y - yDrawOffset), (int) (width * flipWidth), (int) (height), null);

		if (isEnergyLow)
			g.drawString("Energy Low!", (int) hitbox.x - Game.scale * 8 - xLevelOffset,
					(int) hitbox.y - Game.scale * 3);

		// Render all arrows
//		if (!arrows.isEmpty())
//			for (Arrow arrow : arrows) {
//				if (arrow != null) {
//					arrow.renderArrow(g, xLevelOffset);
//				}
//			}
		for (int i = 0; i < arrows.size(); i++) {
			arrow = arrows.get(i);
			arrow.renderArrow(g, xLevelOffset);
		}

//		g.setColor(Color.BLACK);
//		g.setFont(new Font("Arial", Font.PLAIN, 36));
//		g.drawString(String.valueOf(arrowCount), (int) hitbox.x - xLevelOffset, (int) hitbox.y);

		if (currentHealth <= 20)
			g.drawImage(redScreenOverlay, 0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT, null);
		if (currentHealth <= 10)
			g.drawImage(redScreenOverlay, 0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT, null);
		drawUI(g);

//		 renderHitbox(g, xLevelOffset);
//		 renderAttackBox(g, xLevelOffset);
		// renderBowAttackBox(g, xLevelOffset);
	}

	// RENDER ATTACK BOX
	private void renderAttackBox(Graphics g, int xLevelOffset) {
		g.setColor(Color.red);
		g.drawRect((int) attackBox.x - xLevelOffset, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
	}

	// CHECK IF PLAYER ATTACK
	public void playerAttack() {
		if (playerAttacking || aniIndex != 2)
			return;

		playerAttacking = true;
		
		if (weaponType == 0) {
			playingState.playerHitEnemy(attackBox, this);
			playingState.getGame().getAudioPlayer().playAttackSound();;
		} else if (weaponType == 1) {
			if (arrowCount > 0) { // Check if the player has arrows
				Arrow newArrow = new Arrow(hitbox.x, hitbox.y, flipWidth);
				arrows.add(newArrow); // Add the new arrow to the list
				if (CanMoveHere(newArrow.getArrowHitbox().x, newArrow.getArrowHitbox().y,
						newArrow.getArrowHitbox().width, newArrow.getArrowHitbox().height, levelData)) {
					arrowCount--; // Decrease the arrow count
					playingState.getGame().getAudioPlayer().playEffect(AudioPlayer.BOW);
				}
			} else {
				System.out.println("No arrows left!");
			}

		}
	}

	// RENDER UI
	private void drawUI(Graphics g) {

		// status bar
		if (hitbox.x >= statusBarXPos && hitbox.x <= statusBarXPos + statusBarWidth && hitbox.y >= statusBarYPos
				&& hitbox.y <= statusBarYPos + statusBarHeight)
			g.drawImage(statusBarTransparent, statusBarXPos, statusBarYPos, statusBarWidth, statusBarHeight, null);
		else
			g.drawImage(statusBar, statusBarXPos, statusBarYPos, statusBarWidth, statusBarHeight, null);

		// health bar
		g.setColor(Color.red);
		g.fillRect((int) (healthBarXPos + statusBarXPos), (int) (healthBarYPos + statusBarYPos), (int) healthWidth,
				(int) healthBarHeight);
		// energy bar
		g.setColor(Color.blue);
		g.fillRect((int) energyBarXPos + statusBarXPos, (int) energyBarYPos + statusBarYPos, (int) energyWidth,
				(int) energyBarHeight);
	}

	long deathTimer;

	// UPDATE ANIMATION SPRITE
	private void updateAnimation() {
		// UPDATE PLAYER POSITION
		tick++;
		if (tick >= aniSpeed) {
			tick = 0;
			aniIndex++;
			if (aniIndex >= GetSpriteAmount(playerAction)) {
				if (!isDead) {
					aniIndex = 0;
					attack = false;
					playerAttacking = false;
					if (isKnockbacked)
						isKnockbacked = false;

				} else {
					aniIndex--;
					if (!checkDead) {
						deathTimer = System.currentTimeMillis();
						checkDead = true;
					}
					if (System.currentTimeMillis() - deathTimer >= 3000) {
						playingState.setGameOver(true);
						playingState.getGame().getAudioPlayer().playEffect(AudioPlayer.DIE);
						playingState.getGame().getAudioPlayer().stopSong();
					}
				}
			}
		}
	}

	// ANIMATION SETTER DEPENDING ON ACTION
	public void setAnimation() {
		int startAni = playerAction;

		if (isDead) {
			currentHealth = 0;
			currentEnergy = 0;
			playerAction = DEAD;
			return;
		}
		if (isKnockbacked) {
			playerAction = HURT;
		}
		if (moving) {
			if (!shift) {
				playerSpeed = 0.4f * Game.scale;
				playerAction = WALK;
			} else {
				playerSpeed = 0.8f * Game.scale;
				playerAction = RUN;
			}
		} else {
			playerAction = IDLE;
		}

		if (inAir) {
			if (airSpeed < 0) {
				playerAction = JUMP;
			} else {
				playerAction = FALL;
			}
		}

		if (attack) {
			if (!isEnergyLow) {
				if (weaponType == 0) {
					playerAction = attackIndex;
					aniSpeed = 15;
//					if (startAni != ATK0) {
//						aniIndex = 1;
//						tick = 0;
//						return;
//					}
				} else {
					if (arrowCount > 0) {
						playerAction = BOW;
						if (startAni != BOW) {
							aniIndex = 1;
							tick = 0;
							return;
						}
					}
				}
			}

		} else
			aniSpeed = 18;

		if (startAni != playerAction) {
			tick = 0;
			aniIndex = 0;
		}
	}

	// UPDATE PLAYER POSITION
	private void updatePosition() {
		moving = false;

		if (isDead && !inAir)
			return;

		if (firstUpdate)
			if (!IsEntityOnFloor(hitbox, levelData))
				inAir = true;
		firstUpdate = false;

		if (jump) {
			jump();
		}

		if (!inAir)
			if ((!left && !right) || (right && left))
				return;
		float xSpeed = 0;

		if (left) {
			xSpeed = -playerSpeed;
			flipX = (int) width - (1 * Game.scale);
			flipWidth = -1;
		}
		if (right) {
			xSpeed = playerSpeed;
			flipX = 0;
			flipWidth = 1;
		}

		if (!inAir) {
			if (!IsEntityOnFloor(hitbox, levelData)) {
				inAir = true;
			}
		}

		if (inAir) {
			if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, levelData)) {
				hitbox.y += airSpeed;
				airSpeed += GRAVITY;
				if (airSpeed > 5f) {
					airSpeed = 4.9f;
				}
				updateXPosition(xSpeed);
			} else {
				hitbox.y = getEntityUnderRoofOrAboveFloor(hitbox, airSpeed);
				// if(airSpeed > 6.8f)
				// isDead = true;
				if (airSpeed > 0) {
					resetInAir();
				} else {
					airSpeed = fallSpeedAfterCollision;
				}
				updateXPosition(xSpeed);
			}
		} else {
			if (!isEnergyLow)
				updateXPosition(xSpeed);
		}

		if (!isEnergyLow)
			moving = true;
	}

	public void updateXPosition(float xSpeed) {
		if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, levelData)) {
			hitbox.x += xSpeed;
		} else {
			hitbox.x = getEntityXPos(hitbox, xSpeed);
		}
	}

	// TODO: clean up

	// IF ENEMY ATTACK OR PLAYER USE HEALTH POTION
	public void changePlayerHealth(float value) {
		currentHealth += value;
		if (currentHealth <= 0) {
			currentHealth = 0;
		} else if (currentHealth > maxHealth)
			currentHealth = maxHealth;
	}

	// IF ENEMY HIT PLAYER DO KNOCKBACK
	public void applyKnockback(Enemy enemy) {
		if (isDead)
			return;
		isKnockbacked = true;
		float knockBack = 2f * Game.scale;
		float knockbackX, knockbackY;
		if (enemy.hitbox.x > hitbox.x) {
			knockbackX = -knockBack;
			flipX = 0;
			flipWidth = 1;
		} else {
			knockbackX = knockBack;
			flipX = (int) width - (1 * Game.scale);
			flipWidth = -1;
		}
		knockbackY = -knockBack;

		if (CanMoveHere(hitbox.x + knockbackX, hitbox.y, hitbox.width, hitbox.height, levelData)) {
			hitbox.x += knockbackX;
		}
		if (CanMoveHere(hitbox.x, hitbox.y + knockbackY, hitbox.width, hitbox.height, levelData)) {
			hitbox.y += knockbackY;
		}

		knockbackX = 0;
		knockbackY = 0;

		if (!IsEntityOnFloor(hitbox, levelData)) {
			inAir = true;
		}
	}

	// IF PLAYER USE ENERGY POTION
	public void changePlayerEnergy(float value) {
		currentEnergy += value;
		if (currentEnergy <= 0) {
			currentEnergy = 0;
		} else if (currentEnergy > maxEnergy)
			currentEnergy = maxEnergy;
	}

	// RESET JUMP
	private void resetInAir() {
		airSpeed = 0f;
		inAir = false;
	}

	// JUMP METHOD
	private void jump() {
		if (inAir) {
			return;
		} else {
			if (!isEnergyLow) {
				inAir = true;
				airSpeed = jumpSpeed;
//				playingState.getGame().getAudioPlayer().playEffect(AudioPlayer.JUMP);
			}
		}
	}

	// IMPORTS
	private void importImage() {

		BufferedImage image = LoadSave.getSprite(LoadSave.PLAYER);

		animation = new BufferedImage[17][8];

		for (int row = 0; row < animation.length; row++) {
			for (int col = 0; col < animation[row].length; col++) {
				animation[row][col] = image.getSubimage(col * 48, row * 48, 48, 48);
			}
		}

		statusBar = LoadSave.getSprite(LoadSave.PLAYER_HEALTH_BAR);
		statusBarTransparent = LoadSave.getSprite(LoadSave.PLAYER_HEALTH_BAR_TRANSPARENT);
		redScreenOverlay = LoadSave.getSprite(LoadSave.RED_SCREEN_OVERLAY);
	}

	public void loadLevelData(int[][] levelData) {
		this.levelData = levelData;
	}

	// SETTER & GETTER
	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public void setAttack(boolean attack) {
		this.attack = attack;
	}

	public void resetMovementBooleans() {
		left = false;
		up = false;
		right = false;
		down = false;
	}

	public void setJump(boolean jump) {
		this.jump = jump;
	}

	public void setShift(boolean shift) {
		this.shift = shift;
	}

	public void setAttackIndex() {
		if (!attack)
			this.attackIndex++;

		if (attackIndex > 10)
			attackIndex = 6;

	}

	public void troubleShoot() {
		Toolkit.getDefaultToolkit().beep();
		;
		System.out.println("KEYBOARD REACHED PLAYER CLASS");
	}

	public void resetAll() {
		resetMovementBooleans();
		inAir = false;
		attack = false;
		moving = false;
		playerAction = IDLE;
		currentHealth = maxHealth;
		currentEnergy = maxEnergy;
		isDead = false;
		checkDead = false;

		hitbox.x = x;
		hitbox.y = y;

		if (!IsEntityOnFloor(hitbox, levelData))
			inAir = true;
	}
	
	public PlayingState getPlayingState() {
		return playingState;
	}
}
