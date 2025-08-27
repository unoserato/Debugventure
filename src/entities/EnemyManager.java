package entities;

import static utilz.Constants.Enemy.*;
import static utilz.Constants.PlayerConstants.GetPlayerDamage;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.PlayingState;
import levels.Level;
import main.Game;
import utilz.LoadSave;

public class EnemyManager {

	private PlayingState playingState;
	private BufferedImage[][] TV_spriteSheet, Scout_spriteSheet, MechaGolem_spriteSheet;
	private ArrayList<TV> TV_Monsters = new ArrayList<>();
	private ArrayList<Scout> Scout_Monsters = new ArrayList<>();
	private ArrayList<MechaStoneGolem> Mecha_Monsters = new ArrayList<>();
//	private MechaStoneGolem mechaGolem;

	public EnemyManager(PlayingState playing) {
		this.playingState = playing;
		loadEnemyImages();
	}

	// LOAD MONSTERS
	public void loadEnemy(Level level) {
		TV_Monsters = level.getTV();
		Scout_Monsters = level.getScout();
		Mecha_Monsters = level.getGolem();
//		mechaGolem = new MechaStoneGolem(Game.scale * 100, Game.scale * 150);
	}

	// UPDATE
	public void update(int[][] levelData, Player player) {
		boolean isAnyActive = false;
		for (TV tv : TV_Monsters)
			if (tv.isActive()) {
				tv.update(levelData, player);
				isAnyActive = true;
			}
		for (Scout sc : Scout_Monsters)
			if (sc.isActive()) {
				sc.update(levelData, player);
				isAnyActive = true;
			}
		for (MechaStoneGolem msg : Mecha_Monsters)
			if (msg.isActive()) {
				msg.update(levelData, player);
				isAnyActive = true;
			}
		
//		if (mechaGolem.isActive()) {
//			mechaGolem.update(levelData, player);
//			isAnyActive = true;
//		}

		if (!isAnyActive)
			playingState.setLevelClear(true);
	}

	// MAIN RENDER METHOD
	public void render(Graphics g, int xLevelOffset) {
		renderScoutMonsters(g, xLevelOffset);
		renderTVMonsters(g, xLevelOffset);
		renderGolemMonsters(g, xLevelOffset);
	}

	private void renderGolemMonsters(Graphics g, int xLevelOffset) {
		for (MechaStoneGolem msg : Mecha_Monsters)
			if (msg.isActive()) {
				g.drawImage(MechaGolem_spriteSheet[msg.getEnemyState()][msg.getAniIndex()],
						(int) msg.getHitbox().x - MECHA_X_DRAW_OFFSET - xLevelOffset + msg.flipX(),
						(int) msg.getHitbox().y - MECHA_Y_DRAW_OFFSET  + msg.deadYOffset(), 
						MECHA_DRAW_WIDTH * msg.flipWidth(),
						MECHA_DRAW_HEIGHT, null);

				// drawing borders
//				msg.renderHitbox(g, xLevelOffset);
//				msg.renderAttackBox(g, xLevelOffset);
			}
//		if (mechaGolem.isActive()) {
//			g.drawImage(MechaGolem_spriteSheet[mechaGolem.getEnemyState()][mechaGolem.getAniIndex()],
//					(int) mechaGolem.getHitbox().x - MECHA_X_DRAW_OFFSET - xLevelOffset + mechaGolem.flipX(),
//					(int) mechaGolem.getHitbox().y - MECHA_Y_DRAW_OFFSET  + mechaGolem.deadYOffset(), 
//					MECHA_DRAW_WIDTH * mechaGolem.flipWidth(),
//					MECHA_DRAW_HEIGHT, null);
//		mechaGolem.renderHitbox(g, xLevelOffset);
//		}
	}

	private void renderScoutMonsters(Graphics g, int xLevelOffset) {
		for (Scout sc : Scout_Monsters)
			if (sc.isActive()) {
				g.drawImage(Scout_spriteSheet[sc.getEnemyState()][sc.getAniIndex()],
						(int) sc.getHitbox().x - xLevelOffset - SCOUT_X_DRAW_OFFSET + sc.flipX() + sc.deadXOffset(),
						(int) sc.getHitbox().y - SCOUT_Y_DRAW_OFFSET + sc.deadYOffset(),
						SCOUT_DRAW_WIDTH * sc.flipWidth(), SCOUT_DRAW_HEIGHT, null);

				// drawing borders
//				sc.renderHitbox(g, xLevelOffset);
//				sc.renderAttackBox(g, xLevelOffset);
			}
	}

	// RENDER MONSTERS
	public void renderTVMonsters(Graphics g, int xLevelOffset) {
		for (TV tv : TV_Monsters)
			if (tv.isActive()) {
				g.drawImage(TV_spriteSheet[tv.getEnemyState()][tv.getAniIndex()],
						(int) tv.getHitbox().x - xLevelOffset - TV_X_DRAW_OFFSET + tv.flipX(),
						(int) tv.getHitbox().y - TV_Y_DRAW_OFFSET, TV_DRAW_WIDTH * tv.flipWidth(), TV_DRAW_HEIGHT,
						null);

				// drawing borders
//				tv.renderHitbox(g, xLevelOffset);
//				tv.renderAttackBox(g, xLevelOffset);
			}
	}

	public void playerHitEnemy(Rectangle2D.Float attackBox, Player player) {
		for (TV tv : TV_Monsters)
			if (tv.isActive())
				if (attackBox.intersects(tv.getHitbox())) {
					tv.enemyIsHurt(GetPlayerDamage(), player);
					return;
				}

		for (Scout sc : Scout_Monsters)
			if (sc.isActive())
				if (attackBox.intersects(sc.getHitbox())) {
					sc.enemyIsHurt(GetPlayerDamage(), player);
					return;
				}

		for (MechaStoneGolem msg : Mecha_Monsters)
			if (msg.isActive())
				if (attackBox.intersects(msg.getHitbox())) {
					msg.enemyIsHurt(GetPlayerDamage(), player);
					return;
				}
//		if (mechaGolem.isActive())
//			if (attackBox.intersects(mechaGolem.getHitbox())) {
//				mechaGolem.enemyIsHurt(GetPlayerDamage(), player);
//				return;
//			}
	}

	public boolean arrowHitEnemy(Rectangle2D.Float arrowBox) {
		for (TV tv : TV_Monsters)
			if (tv.isActive())
				if (arrowBox.intersects(tv.getHitbox())) {
					return true;
				}
		for (Scout sc : Scout_Monsters)
			if (sc.isActive())
				if (arrowBox.intersects(sc.getHitbox())) {
					return true;
				}
		for (MechaStoneGolem msg : Mecha_Monsters)
			if (msg.isActive())
				if (arrowBox.intersects(msg.getHitbox())) {
					return true;
				}
		return false;

	}

	// LOAD IMAGEES
	private void loadEnemyImages() {
		TV_spriteSheet = new BufferedImage[6][16];
		BufferedImage temp = LoadSave.getSprite(LoadSave.TV_MONSTER);

		for (int y = 0; y < TV_spriteSheet.length; y++) {
			for (int x = 0; x < TV_spriteSheet[0].length; x++) {
				TV_spriteSheet[y][x] = temp.getSubimage(x * TVWidthDefault, y * TVHeightDefault, TVWidthDefault,
						TVHeightDefault);
			}
		}

		Scout_spriteSheet = new BufferedImage[6][16];
		BufferedImage temp0 = LoadSave.getSprite(LoadSave.SCOUT_MONSTER);
		for (int y = 0; y < Scout_spriteSheet.length; y++) {
			for (int x = 0; x < Scout_spriteSheet[0].length; x++) {
				Scout_spriteSheet[y][x] = temp0.getSubimage(x * ScoutWidthDefault, y * ScoutHeightDefault,
						ScoutWidthDefault, ScoutHeightDefault);
			}
		}

		MechaGolem_spriteSheet = new BufferedImage[6][9];
		BufferedImage temp1 = LoadSave.getSprite(LoadSave.MECHA_STONE_GOLEM);
		for (int y = 0; y < MechaGolem_spriteSheet.length; y++) {
			for (int x = 0; x < MechaGolem_spriteSheet[0].length; x++) {
				MechaGolem_spriteSheet[y][x] = temp1.getSubimage(x * MechaWidthDefault, y * MechaHeightDefault,
						MechaWidthDefault, MechaHeightDefault);
			}
		}
	}

	public void resetAllEnemies() {
		for (TV tv : TV_Monsters)
			tv.resetEnemy();
		for (Scout sc : Scout_Monsters)
			sc.resetEnemy();
	}
}
