package ui;

import java.awt.Color;
import java.awt.Graphics;

public class Circle {

	public int centerX; // x-coordinate of the center
	public int centerY; // y-coordinate of the center
	public int radius; // radius of the circle

	public Circle(int centerX, int centerY, int radius) {
		this.centerX = centerX;
		this.centerY = centerY;
		this.radius = radius;
	}

	// Method to check if a point is inside the circle
	public boolean contains(int pointX, int pointY) {
		int dx = pointX - centerX;
		int dy = pointY - centerY;
		return (dx * dx + dy * dy) <= (radius * radius);
	}

	public void renderCircle(Graphics g) {
		g.setColor(Color.red);
		g.drawOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
	}
	
	public Circle getCircle() {
		return this;
	}

}
