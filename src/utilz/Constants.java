package utilz;

import main.Game;
import ui.UserInterface;

public class Constants {
	
	public static final float GRAVITY = 0.04f * Game.scale; 
	
	public static class Enemy{
		public static final int TV = 0;
		public static final int SCOUT = 1;
		public static final int GOLEM = 2;
		
		public static final int IDLE = 0;
		public static final int ALERT = 1;
		public static final int WALK = 2;
		public static final int ATK = 3;
		public static final int HURT = 4;
		public static final int DEAD = 5;
		
		// TV MONSTER PROPERTIES
		public static final int TVWidthDefault = 112;
		public static final int TVHeightDefault = 64;
		public static final float TVscale = Game.scale / 2.0f;

		public static final int TV_DRAW_WIDTH = (int)(TVWidthDefault * TVscale);
		public static final int TV_DRAW_HEIGHT = (int)(TVHeightDefault * TVscale);
		public static final int TV_X_DRAW_OFFSET = (int)(18.33333333333334 * Game.scale);
		public static final int TV_Y_DRAW_OFFSET = (int)(18.33333333333333 * Game.scale);
		
		public static final float TV_HITBOX_WIDTH = 37 * TVscale;
		public static final float TV_HITBOX_HEIGHT = 26 * TVscale;
		public static final int TV_X_HITBOX_OFFSET = (int) (7.7 * Game.scale);
		public static final int TV_Y_HITBOX_OFFSET = (int) (2.333333333333334 * Game.scale );
		
		// SCOUT MONSTER PROPERTIES
		public static final int ScoutWidthDefault = 96;
		public static final int ScoutHeightDefault = 64;
		public static final float Scoutscale = Game.scale / 2.0f;
		
		public static final int SCOUT_DRAW_WIDTH = (int)(ScoutWidthDefault * Scoutscale);
		public static final int SCOUT_DRAW_HEIGHT = (int)(ScoutHeightDefault * Scoutscale);
		public static final int SCOUT_X_DRAW_OFFSET = (int)(36 * Scoutscale);
		public static final int SCOUT_Y_DRAW_OFFSET = (int)(24 * Scoutscale);
		
		public static final float SCOUT_HITBOX_WIDTH  = 24 * Scoutscale;
		public static final float SCOUT_HITBOX_HEIGHT = 24 * Scoutscale;
		public static final int SCOUT_X_DEAD_OFFSET = (int)( 6.6666667 * Scoutscale);
		public static final int SCOUT_Y_DEAD_OFFSET = (int)( 15.3333333 * Scoutscale);
		
		
		// MECHA MONSTER PROPERTIES
		public static final int MechaWidthDefault = 100;
		public static final int MechaHeightDefault = 100;
		
		public static final int MECHA_DRAW_WIDTH = (int)(MechaWidthDefault * Game.scale);
		public static final int MECHA_DRAW_HEIGHT = (int)(MechaWidthDefault * Game.scale);
		public static final int MECHA_X_DRAW_OFFSET = (int)(25 * Game.scale);
		public static final int MECHA_Y_DRAW_OFFSET = (int)(34 * Game.scale);
		
		public static final float MECHA_HITBOX_WIDTH  = 48 * Game.scale;
		public static final float MECHA_HITBOX_HEIGHT = 38 * Game.scale;
		public static final int MECHA_Y_DEAD_OFFSET = (int)( 10 * Game.scale);
		
		public static int GetSpriteAmount(int enemyType, int enemyState) {
			switch (enemyType) {
			case TV: 
				switch(enemyState) {
				case IDLE:
					return 15;
				case ALERT:
				case WALK:
					return 16;
				case ATK:
					return 13;
				case HURT:
					return 4;
				case DEAD:
					return 11;
				}
				
			case SCOUT:
				switch(enemyState) {
				case IDLE:
					return 6;
				case WALK:
					return 12;
				case ATK:
					return 13;
				case HURT:
					return 3;
				case DEAD:
					return 16;
				}
				
			case GOLEM:
				switch (enemyState) {
				case IDLE:
				case DEAD:
					return 4;
				case WALK:
					return 8;
				case ATK:
					return 9;
				case HURT:
					return 2;
				}
			}
			return 0;
		}
		
		public static int GetMaxHealth(int enemyType) {
			switch(enemyType) {
			case TV:
				return 50;
			case SCOUT:
				return 100;
			case GOLEM:
				return 300;
			default:
				return 1;
			}
		}
		
		public static float GetEnemyDamage(int enemyType) {
			switch(enemyType) {
			case TV:
				return 3;
			case SCOUT:
				return 3;
			case GOLEM:
				return 0.4f; // DPS 
			default:
				return 0;
			}
		}
	}
	
	public static class Environment{
		
		public static final double PlayingBGWidth = 346.6666666666667 * Game.scale;
		public static final double PlaingBGHeight = 240 * Game.scale;
		
		public static final int PLAYING_BG_WIDTH = (int)(PlayingBGWidth);
		public static final int PLAYING_BG_HEIGHT = (int)(PlaingBGHeight);
	}
	public static class UI{
		public static class Buttons{
			public static final int MenuButtonWidthDefault = 48;
			public static final int MenuButtonHeightDefault = 16;
			public static final int MENU_BUTTON_WIDTH = MenuButtonWidthDefault * (Game.scale + Game.extraScale);
			public static final int MENU_BUTTON_HEIGHT= MenuButtonHeightDefault * (Game.scale + Game.extraScale);
			
			public static final int PausedMenuButtonWidthDefault = 48;
			public static final int PausedMenuButtonHeightDefault = 16;
			public static final int PAUSED_MENU_BUTTON_WIDTH= PausedMenuButtonWidthDefault * (Game.scale + Game.extraScale);
			public static final int PAUSED_MENU_BUTTON_HEIGHT= PausedMenuButtonHeightDefault * (Game.scale + Game.extraScale);
		}
	}
	
	public static class Directions{
		
		public static final int LEFT = 0,
								UP = 1,
								RIGHT = 2,
								DOWN = 3;
	}
	public static class PlayerConstants{
		
		public static int weaponType = 1;
		
		public static final int IDLE = 0,
								WALK = 1,
								RUN = 2,
								SKIP = 3,
								JUMP = 4,
								FALL = 5,
								ATK0 = 6,
								ATK1 = 7,
								ATK2 = 8,
								ATK3 = 9,
								ATK4 = 10,
								FIREBALL = 11,
								THROW = 12,
								BOW = 13,
								SHIELD = 14,
								HURT = 15,
								DEAD = 16;
		
		public static int GetSpriteAmount(int playerAction) {
			
			switch(playerAction) {
			case JUMP: 
			case FALL:
				return 2;
			case SKIP:
			case BOW:
			case SHIELD:
			case HURT:
				return 4;
			case ATK0:
			case ATK1:
			case ATK2:
			case ATK3:
				return 5;
			case IDLE:
			case WALK:
			case RUN:
				return 6;
			case ATK4:
			case DEAD:
				return 7;
			case FIREBALL:
			case THROW:
				return 8;
			default:
				return 1;
			}
		}
		
		public static int GetPlayerDamage() {
			switch(weaponType) {
			case 0:
				return 10;
			case 1:
				return 20;
			default:
				return 0;
			}
		}
		
		public static float GetPlayerActionEnergyCost(int playerAction) {
			switch(playerAction) {
			case IDLE:
				return  0.09f;
			case WALK:
				return -0.03f;
			case RUN:
				return -0.07f;
			case JUMP: 
				return -0.09f;
			case ATK0:
			case ATK1:
			case ATK2:
			case ATK3:
			case ATK4:
				return -0.1f;
			case BOW:
				return -0.2f;
			default:
					return 0f;
			}
		}
	}
}
