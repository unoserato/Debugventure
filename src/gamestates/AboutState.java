package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import main.Game;
import utilz.LoadSave;

public class AboutState extends State implements StateMethods, MouseWheelListener {

	// image properties
	private int imageWidth = Game.SCREEN_WIDTH, imageHeight = Game.SCREEN_HEIGHT * 2; // image dimensions
	private float yImagePos = 0; // Current position
	private double velocity = 0; // Current velocity for inertia
	private final double damping = 0.95; // Damping factor for inertia
	private float maxImageUpScroll;
	private float speed = 5 * Game.scale;
	private BufferedImage image;
	
	// scroller properties
	private int scrollerHeight = 80;
	private float scrollerPos = 0;
	private int maxScrollerPos = Game.SCREEN_HEIGHT - scrollerHeight;
	private float scrollerPercent;
	

	public AboutState(Game game) {
		super(game);
		image = LoadSave.getSprite("ABOUT_PAGE.png");
		maxImageUpScroll = -imageHeight + Game.SCREEN_HEIGHT; // Set max scroll limit
	}

	@Override
	public void update() {

		// for scroller in right side
		scrollerPercent = Math.abs(yImagePos / maxImageUpScroll);
		scrollerPos = maxScrollerPos * scrollerPercent;
		
		// Update yPos based on velocity
		yImagePos += (int) velocity;
		
		// Apply damping to velocity
		velocity *= damping;

		// Stop the scrolling if the velocity is very low
		if (Math.abs(velocity) < 0.1) {
			velocity = 0; // Stop the inertia
		}

		// Constrain yPos within the bounds
		if (yImagePos > 0) {
			yImagePos = 0; // Reset if at the top
			velocity = 0; // Stop inertia
		}
		if (yImagePos < maxImageUpScroll) {
			yImagePos = maxImageUpScroll; // Reset if at the bottom
			velocity = 0; // Stop inertia
		}
	}

	@Override
	public void render(Graphics g) {
		// Draw the image based on the current yPos
		g.setColor(new Color(255,255,255));
		g.drawImage(image, 0, (int)yImagePos, imageWidth, imageHeight, null);
		g.fillRect(Game.SCREEN_WIDTH - 5, (int)scrollerPos, 5, scrollerHeight);
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {
	
	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			GameState.state = GameState.MENU; // Return to menu on ESC
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int notches = e.getWheelRotation();
		// Adjust velocity based on the scroll direction
		velocity += (-notches) * speed; // Change 20 to adjust scroll speed
	}
}