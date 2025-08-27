package utilz;

import static utilz.Constants.Enemy.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import entities.Scout;
import entities.TV;
import main.Game;

public class LoadSave {

	public static String PLAYER = "PLAYER_NEW.png";
	public static String PLAYER_HEALTH_BAR = "HEALTH_BAR.png";
	public static String PLAYER_HEALTH_BAR_TRANSPARENT = "HEALTH_BAR1.png";

	public static String TILES = "STAGE_1_TILESET.png";
	public static String TILES2 = "STAGE_2_TILESET.png";
	public static String START_BUTTONS = "START_BUTTONS.png";
	public static String PAUSE_BUTTON = "PAUSE_BUTTON.png";
	public static String PAUSE_BACKGROUND = "PAUSE_BACKGROUND.png";
	public static String PAUSE_MENU_BUTTONS = "PAUSE_MENU_BUTTONS.png";

	public static String TV_MONSTER = "TV_MONSTER.png";
	public static String SCOUT_MONSTER = "SCOUT_MONSTER.png";
	public static String MECHA_STONE_GOLEM = "MECHA_STONE_GOLEM.png";

	public static String ARROW = "ARROW.png";
	public static String ATTACK_BUTTON_SWORD = "ATTACK_BUTTON_SWORD.png";
	public static String ATTACK_BUTTON_BOW = "ATTACK_BUTTON_BOW.png";
	public static String POTION_RED = "POTION_RED.png";
	public static String POTION_BLUE = "POTION_BLUE.png";
	public static String SWITCH_WEAPON = "SWITCH_WEAPON.png";
	public static String ARROW_UI = "ARROW_UI.png";
	// less opacity
	public static String ATTACK_BUTTON_SWORD_1 = "ATTACK_BUTTON_SWORD_1.png";
	public static String ATTACK_BUTTON_BOW_1 = "ATTACK_BUTTON_BOW_1.png";
	public static String POTION_RED_1 = "POTION_RED_1.png";
	public static String POTION_BLUE_1 = "POTION_BLUE_1.png";
	public static String SWITCH_WEAPON_1 = "SWITCH_WEAPON_1.png";
	public static String ARROW_UI_1 = "ARROW_UI_1.png";
	public static String RED_SCREEN_OVERLAY = "RED_SCREEN_OVERLAY.png";
	public static String YOU_DIED = "YOU_DIED.png";

	public static String CHEST = "CHEST.png";

	
	public static BufferedImage getSprite(String fileName) {

		BufferedImage image = null;
		InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);

		try {
			image = ImageIO.read(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return image;
	}

	public static BufferedImage[] GetAllLevels() {
		URL url = LoadSave.class.getResource("/level_stages");
		File file = null;

		try {
			file = new File(url.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		File[] files = file.listFiles();
		File[] filesSorted = new File[files.length];

		for (int i = 0; i < filesSorted.length; i++)
			for (int j = 0; j < files.length; j++) {
				if (files[j].getName().equals((i) + ".png"))
					filesSorted[i] = files[j];
			}

		BufferedImage[] images = new BufferedImage[filesSorted.length];
		for (int i = 0; i < images.length; i++)
			try {
				images[i] = ImageIO.read(filesSorted[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}

		return images;
	}
}
