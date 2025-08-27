package gamestates;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Timer;

import main.Game;
import utilz.LoadSave;

public class StoryState extends State implements StateMethods {

	private BufferedImage[] slideShow;
	private int slideIndex = 0;
//	private float alpha = 1f; // Alpha value for transparency
	private int i = 0;

	public StoryState(Game game) {
		super(game);
		loadImages();
	}

	private void loadImages() {
		slideShow = new BufferedImage[5];

		for (int i = 0; i < slideShow.length; i++) {
			String path = "story/" + i + ".png";
			slideShow[i] = LoadSave.getSprite(path);
		}
	}

	@Override
	public void update() {
//		System.out.println(i++);
		i++;

		if (i >= 1500) {
			if (slideIndex < slideShow.length) {
				slideIndex++;
			}
			i -= 1300;
		}

		if (slideIndex > 4)
			GameState.state = GameState.PLAYING;

	}

	@Override
	public void render(Graphics g) {
//		// Set the alpha composite for the graphics context
//		Graphics2D g2d = (Graphics2D) g;
//		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

		// Draw the current slide
		g.drawImage(slideShow[slideIndex], 0, 0, Game.SCREEN_WIDTH, Game.SCREEN_WIDTH, null);
		g.drawString("Press enter to skip", Game.SCREEN_WIDTH - Game.scale*50, Game.SCREEN_HEIGHT - Game.scale*10);

//		// Reset the composite to draw other elements without transparency
//		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
			GameState.state = GameState.PLAYING;

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
