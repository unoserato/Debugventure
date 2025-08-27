package ui;

import static utilz.Constants.PlayerConstants.weaponType;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import entities.Player;
import gamestates.PlayingState;
import main.Game;
import utilz.LoadSave;

public class UserInterface {
	private PlayingState playingState;

	private int index = 0;

	public static int redPotionCount = 100;
	public static int bluePotionCount = 100;

	// ui button
	private BufferedImage mainAttackButton;
	private BufferedImage[] sword, bow, arrowUI;
	private BufferedImage[] redPotionButton, bluePotionButton, switchWeaponButton;
	public Circle[] buttons;
//	public Circle attackButtonCircleHitbox, switchButtonHitbox, redPotionHitbox, bluePotionHitbox;

	// BUTTON PROPERTIES
	private int atkSize = 64 * Game.scale;
	private int potionSize = 20 * Game.scale;

	private int potionRedOffsetX = Game.SCREEN_WIDTH - (Game.scale * 76);
	private int potionRedOffsetY = Game.SCREEN_HEIGHT - (Game.scale * 49);

	private int potionBlueOffsetX = Game.SCREEN_WIDTH - (Game.scale * 58);
	private int potionBlueOffsetY = Game.SCREEN_HEIGHT - (Game.scale * 70);

	private int switchWeaponOffsetX = Game.SCREEN_WIDTH - (Game.scale * 85);
	private int switchWeaponOffsetY = Game.SCREEN_HEIGHT - (Game.scale * 25);

	private int arrowUIXPos = Game.SCREEN_WIDTH - (Game.scale * 30);
	private int arrowUIYPos = Game.SCREEN_HEIGHT - (Game.scale * 85);
	private int arrowWidth = (Game.scale * 20);
	private int arrowHeight = (Game.scale * 20);

	public UserInterface(PlayingState playingState) {
		this.playingState = playingState;
		initVariables();
	}

	// UPDATE INDEX FOR CHANGING TRANSPARENCY
	public void update() {
		index = 0; // Default to 0

		for (Circle button : buttons) {
		    if (button.contains((int) playingState.getPlayer().getHitbox().x - playingState.xLevelOffset,
		                        (int) playingState.getPlayer().getHitbox().y)) {
		        index = 1; // Set index to 1 if any button contains the hitbox
		        break;     // Exit the loop early since we found a button
		    }
		}

//		DONT DELETE JUST IN CASE BUG
//		if (buttons[0].contains((int) playingState.getPlayer().getHitbox().x - playingState.xLevelOffset,
//				(int) playingState.getPlayer().getHitbox().y))
//			index = 1;
//		else if (buttons[1].contains((int) playingState.getPlayer().getHitbox().x - playingState.xLevelOffset,
//				(int) playingState.getPlayer().getHitbox().y))
//			index = 1;
//		else if (buttons[2].contains((int) playingState.getPlayer().getHitbox().x - playingState.xLevelOffset,
//				(int) playingState.getPlayer().getHitbox().y))
//			index = 1;
//		else if (buttons[3].contains((int) playingState.getPlayer().getHitbox().x - playingState.xLevelOffset,
//				(int) playingState.getPlayer().getHitbox().y))
//			index = 1;
//		else 
//			index = 0;
//		System.out.println(buttons[0].contains((int) playingState.getPlayer().getHitbox().x - playingState.xLevelOffset,
//				(int) playingState.getPlayer().getHitbox().y));

		if (weaponType == 0)
			mainAttackButton = sword[index];
		else
			mainAttackButton = bow[index];

	}

	private void initVariables() {
		sword = new BufferedImage[2];
		bow = new BufferedImage[2];
		arrowUI = new BufferedImage[2];
		redPotionButton = new BufferedImage[2];
		bluePotionButton = new BufferedImage[2];
		switchWeaponButton = new BufferedImage[2];

		// attack button
		sword[0] = LoadSave.getSprite(LoadSave.ATTACK_BUTTON_SWORD);
		sword[1] = LoadSave.getSprite(LoadSave.ATTACK_BUTTON_SWORD_1);
		bow[0] = LoadSave.getSprite(LoadSave.ATTACK_BUTTON_BOW);
		bow[1] = LoadSave.getSprite(LoadSave.ATTACK_BUTTON_BOW_1);
		// potion
		redPotionButton[0] = LoadSave.getSprite(LoadSave.POTION_RED);
		redPotionButton[1] = LoadSave.getSprite(LoadSave.POTION_RED_1);
		bluePotionButton[0] = LoadSave.getSprite(LoadSave.POTION_BLUE);
		bluePotionButton[1] = LoadSave.getSprite(LoadSave.POTION_BLUE_1);
		switchWeaponButton[0] = LoadSave.getSprite(LoadSave.SWITCH_WEAPON);
		switchWeaponButton[1] = LoadSave.getSprite(LoadSave.SWITCH_WEAPON_1);
		// arrow count
		arrowUI[0] = LoadSave.getSprite(LoadSave.ARROW_UI);
		arrowUI[1] = LoadSave.getSprite(LoadSave.ARROW_UI_1);

		buttons = new Circle[4]; 
		/* 0 = Attack button
		 * 1 = Switch weapon
		 * 2 = Red potion
		 * 3 = Blue potion
		 * */    
		buttons[0] = new Circle(Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT, 64 * Game.scale);
		buttons[1] = new Circle(switchWeaponOffsetX + potionSize / 2, switchWeaponOffsetY + potionSize / 2,
				potionSize / 2);
		buttons[2] = new Circle(potionRedOffsetX + potionSize / 2, potionRedOffsetY + potionSize / 2, potionSize / 2);
		buttons[3] = new Circle(potionBlueOffsetX + potionSize / 2, potionBlueOffsetY + potionSize / 2, potionSize / 2);
	}

	// RENDER BUTTONS TO SCREEN
	public void render(Graphics g) {
		int stringRedPotionXOffset = (redPotionCount > 10) ? Game.scale * 12 : Game.scale * 15;
		int stringBluePotionXOffset = (bluePotionCount > 10) ? Game.scale * 12 : Game.scale * 15;
		int stringArrowXOffset = (Player.arrowCount > 10) ? Game.scale * 10 : Game.scale * 10;

		g.drawImage(mainAttackButton, Game.SCREEN_WIDTH - atkSize, Game.SCREEN_HEIGHT - atkSize, atkSize, atkSize,
				null);

		g.setFont(new Font("Arial Black", Font.PLAIN, Game.scale * 10));

		g.drawImage(redPotionButton[index], potionRedOffsetX, potionRedOffsetY, potionSize, potionSize, null);
		if(redPotionCount == 0)
			g.setColor(Color.red);
		else
			g.setColor(Color.white);
		g.drawString(String.valueOf(redPotionCount), potionRedOffsetX + stringRedPotionXOffset,
				potionRedOffsetY + Game.scale * 6);
		if(bluePotionCount == 0)
			g.setColor(Color.red);
		else
			g.setColor(Color.white);
		g.drawImage(bluePotionButton[index], potionBlueOffsetX, potionBlueOffsetY, potionSize, potionSize, null);
		
		g.drawString(String.valueOf(bluePotionCount), potionBlueOffsetX + stringBluePotionXOffset,
				potionBlueOffsetY + Game.scale * 6);

		g.drawImage(arrowUI[index], arrowUIXPos, arrowUIYPos, arrowWidth, arrowHeight, null);
		if(Player.arrowCount == 0)
			g.setColor(Color.red);
		else
			g.setColor(Color.white);
		g.drawString(String.valueOf(Player.arrowCount), arrowUIXPos + stringArrowXOffset, arrowUIYPos + Game.scale * 6);

		g.drawImage(switchWeaponButton[index], switchWeaponOffsetX, switchWeaponOffsetY, potionSize, potionSize, null);

//		renderHitbox(g);
	}

	private void renderHitbox(Graphics g) {
//		attackButtonCircleHitbox.renderCircle(g);
//		switchButtonHitbox.renderCircle(g);
//		bluePotionHitbox.renderCircle(g);
//		redPotionHitbox.renderCircle(g);

		for (Circle print : buttons)
			print.renderCircle(g);
	}

}
