package gamestates;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import audio.AudioPlayer;
import main.Game;
import ui.MenuButton;
import ui.PauseMenuButtons;

public class State {
	protected Game game;
	
	public State(Game game) {
		this.game = game;
	}
	
	public Game getGame() {
		return game;
	}
	
	public void setGameState(GameState state) {
		switch(state) {
		case MENU -> game.getAudioPlayer().playSong(AudioPlayer.MENU);
		case PLAYING -> game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex());
		}
		GameState.state = state;
	}
	
	public boolean isIn(MouseEvent e, MenuButton mb) {
//		System.out.println("HELLO");
		return mb.getHitbox().contains(e.getX(), e.getY());
	}
	
// pause hitbox in playing state
	public boolean isIn(MouseEvent e, Rectangle hitbox) {
		return hitbox.contains(e.getX(),e.getY());
	}
}
