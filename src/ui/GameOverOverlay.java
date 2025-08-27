package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.security.KeyStore.LoadStoreParameter;

import gamestates.GameState;
import gamestates.PlayingState;
import main.Game;
import utilz.LoadSave;

public class GameOverOverlay {
	
	private PlayingState playingState;
	
	private BufferedImage image;
	
	public GameOverOverlay(PlayingState playingState) {
		this.playingState = playingState;
		image = LoadSave.getSprite(LoadSave.YOU_DIED);
	}
	
	public void render(Graphics g) {
		g.setColor(new Color(0,0,0,180));
		g.fillRect(0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
//	
//		g.setColor(Color.white);
//		g.drawString("Game Over", Game.SCREEN_WIDTH / 2, 150);
		g.setColor(new Color(255, 0, 0, 255));
		g.setFont(new Font("Arial Black", Font.PLAIN, 30));
		
		g.drawImage(image, 0, 0, Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT, null);
		
		g.drawString("Press Escape key to exit.....", Game.scale * 90, 400);
	}
	
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			playingState.resetAll();
			GameState.state = GameState.EXIT;
		}
	}
}
