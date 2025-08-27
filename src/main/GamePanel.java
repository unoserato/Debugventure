package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import inputs.KeyboardInputs;
import inputs.MouseInputs;

public class GamePanel extends JPanel{
	
	// class instances
	private MouseInputs mouseInputs;
	private Game game;
	
	
	
	public GamePanel(Game game) {
		this.game = game;
		mouseInputs = new MouseInputs(this);
		
		setPanelSize();
		this.setBackground(Color.gray);
		addKeyListener(new KeyboardInputs(this));
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
		addMouseWheelListener(mouseInputs);
	}
	
	public void setPanelSize() {
		Dimension size = new Dimension(Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
	}
	
	public void update() {
		
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		game.render(g);
	}

	public Game getGame() {
		return game;
	}
}
