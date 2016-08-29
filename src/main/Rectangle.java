package main;

import main.Rectangle;

public class Rectangle {
	int x,  y , width, height;
	
	public Rectangle(int x, int y, int width, int height) {
		this.x = x ;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public boolean isOverlap(Rectangle r) {
		return x < r.x + r.width && x + width > r.x && y < r.y + r.height && y + height > r.y;
	}
}
