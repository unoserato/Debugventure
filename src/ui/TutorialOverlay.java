package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import entities.Player;
import gamestates.PlayingState;
import main.Game;

public class TutorialOverlay {

	private PlayingState playingState;
	
	private String text0 = "Use A and D keys to move Left and right";
	private String text1 = "Use spacebar to jump";
	private String text2 = "Stand in front of this chest and attack";
	private String text3 = "Kill the enemy using your weapons";
	private String text4 = "Stand in front of ";
	private String text5 = "the white door and press enter";
	private String text6 = "Press and hold shift to run";
	private String text7 = "Doing any action will require energy";
	private String text8 = "Use blue potion to repleshing energy";
	private String text9 = "Use red potion to repleshing health";
	private String text10 = "Enemy attacks has a chance to deal crit damage";
	private String text11 = "Level should be clear of enemies";
	

	public TutorialOverlay(PlayingState playingState) {
		this.playingState = playingState;
	}

	public void render(Graphics g, int xLevelOffset) {
//		g.setColor(new Color(255, 255, 255, 255));
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial Black", Font.BOLD, Game.scale * 10));

		g.drawString(text7, Game.scale * 10 - xLevelOffset, Game.scale * 100);
		g.drawString(text8, Game.scale * 10 - xLevelOffset, Game.scale * 110);
		g.drawString(text9, Game.scale * 10 - xLevelOffset, Game.scale * 120);

		g.drawString(text0, Game.scale * 10 - xLevelOffset, Game.scale * 150);
		g.drawString(text6, Game.scale * 10 - xLevelOffset, Game.scale * 160);
		g.drawString(text1, Game.scale * 10 - xLevelOffset, Game.scale * 170);
		
		
		g.setFont(new Font("Arial Black", Font.BOLD, (int)(Game.scale * 8.333333)));
		g.drawString(text2, Game.scale * 20 - xLevelOffset, Game.scale * 220);
		g.drawString(text3, Game.scale * 335 - xLevelOffset, Game.scale * 210);
		g.drawString(text10, Game.scale * 300 - xLevelOffset, Game.scale * 230);
		g.drawString(text4, Game.scale * 835 - xLevelOffset, Game.scale * 100);
		g.drawString(text5, Game.scale * 800 - xLevelOffset, Game.scale * 110);
		g.drawString(text11, Game.scale * 798 - xLevelOffset, Game.scale * 120);
	}
}
