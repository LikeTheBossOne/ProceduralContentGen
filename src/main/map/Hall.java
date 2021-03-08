package main.map;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Hall {
	
	private List<Point> path;
	
	public Hall() {
		
		path = new ArrayList<Point>();
	}
	
	public void addToPath(Point p) {
		path.add(p);
	}
	
	public List<Point> getPath() {
		return path;
	}
}
