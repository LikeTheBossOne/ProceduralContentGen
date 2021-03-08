package main.map;

import java.awt.Point;

public class Room {

	public static int nextId = 0;

	private int id;
	private int x;
	private int y;
	private int width;
	private int height;
	
	
	public Room(int x, int y, int width, int height) {
		this.id = nextId;
		nextId++;
		
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	
	public static boolean bufferIntersects(Room room1, Room room2, int buffer) {
		int a = room2.getX() - (room1.getX() + room1.getWidth());
		int b = room1.getX() - (room2.getX() + room2.getWidth());
		if (a >= buffer || b >= buffer)
			return false;
		
		int c = room2.getY() - (room1.getY() + room1.getHeight());
		int d = room1.getY() - (room2.getY() + room2.getHeight());
		if (c >= buffer || d >= buffer)
			return false;
		
		return true;
	}
	
	public int getId() {
		return id;
	}
	
	public int getX() {
		return x;
	}


	public int getY() {
		return y;
	}


	public int getWidth() {
		return width;
	}


	public int getHeight() {
		return height;
	}

	public Point getCenter() {
		return new Point(x + width / 2, y + height / 2);
	}
	
	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Room other = (Room) obj;
		return this.getId() == other.getId();
	}
	
}
