package seabattle;

import java.awt.Point;
import java.util.ArrayList;

public class Ship {

	private ArrayList<Point> points;
	private int count = 0;
	
	public Ship() {
		points = new ArrayList<>();
	}
	
	public void addPoint(int x, int y) {
		points.add(new Point(x, y));
	}
	
	public void clear() {
		points.clear();
		count = 0;
	}
	
	public ArrayList<Point> getPoints() {
		return points;
	}
	
	public void attack(int x, int y) {
		for (int i = 0; i < points.size(); i++) {
			if (points.get(i).x == x && points.get(i).y == y) {
				count++;
			}
		}
	}
	
	public boolean isDead() {
		return count == points.size();
	}
}
