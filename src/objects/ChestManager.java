package objects;

import static utilz.Constants.PlayerConstants.weaponType;

import java.awt.Graphics;
import java.util.ArrayList;

import gamestates.PlayingState;
import levels.Level;
import ui.ChestOverlay;

public class ChestManager {
	private PlayingState playingState;
	private ArrayList<Chest> chests; // Renamed for clarity
	private ChestOverlay chestOverlay;

	public ChestManager(PlayingState playingState) {
		this.playingState = playingState;
		this.chestOverlay = new ChestOverlay(playingState); // Create a single instance
	}

	// LOAD CHESTS
	public void loadChest(Level level) {
		chests = level.getChest();
	}

	public void render(Graphics g, int xLevelOffset) {
		for (Chest c : chests) {
			c.render(g, xLevelOffset);
		}
	}

	public void update() {
		for (Chest c : chests) {
			if (c.playerIntersectsChest(playingState.getPlayer().getHitbox())) {
				if (playingState.getPlayer().attack && weaponType == 0 && !c.isOpen) {
					chestOverlay.setChest(c); // Set the current chest in the overlay
					chestOverlay.setVisible(true); // Show the overlay
				}
			} else {
				// Optionally hide the overlay if the player is not interacting with the chest
//	                if (chestOverlay.isVisible()) {
//	                    chestOverlay.setVisible(false);
//	                }
			}
		}
	}
}
