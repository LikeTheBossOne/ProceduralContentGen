package main.map;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.SpanningTreeAlgorithm.SpanningTree;
import org.jgrapht.alg.spanning.KruskalMinimumSpanningTree;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;

import main.Application;
import main.enums.Tile;
import main.graphics.SpriteSheet;
import main.util.PointUtil;
import processing.core.PApplet;

public class FloorMap {
	PApplet sketch;
	
	private int width;
	private int height;
	
	private SpriteSheet tileSheet;
	
	private int[][] tileMap;
	
	
	public FloorMap(PApplet sketch, int width, int height, SpriteSheet tileSheet) {
		this.sketch = sketch;
		
		this.width = width;
		this.height = height;
		
		this.tileSheet = tileSheet;
		
		tileMap = new int[width][height];
	}
	
	public void generateMap(int seed, int roomAttempts, int minSize, int maxSize, int bufferSize, double percentExtraEdges, double houseChance) {
		Map<Integer, Room> rooms = new HashMap<Integer, Room>();
		
		final int minXVal = 1;
		final int minYVal = 1;
		final int maxXVal = Application.TILE_COUNT_X - 1;
		final int maxYVal = Application.TILE_COUNT_Y - 1;
		
		Random r;
		if (seed == -1) {
			r = new Random();
		} else {
			r = new Random(seed);
		}
		
		
		// Generate Rooms
		for (int i = 0; i < roomAttempts; i++) {
			int randWidth = minSize + r.nextInt(maxSize - minSize);
			int randHeight = minSize + r.nextInt(maxSize - minSize);
			int randX = r.nextInt(maxXVal - randWidth - minXVal) + minXVal;
			int randY = r.nextInt(maxYVal - randHeight - minYVal) + minYVal;
			
			Room possibleRoom = new Room(randX, randY, randWidth, randHeight);
			
			boolean canPlace = true;
			for (Room room : rooms.values()) {
				if (Room.bufferIntersects(room, possibleRoom, bufferSize)) {
					canPlace = false;
					break;
				}
			}
			if (canPlace) {
				rooms.put(possibleRoom.getId(), possibleRoom);
			}
			
		}
		
		
		// Create Delaunay triangulation
		Graph<Integer, Edge> g = new DefaultUndirectedWeightedGraph<Integer, Edge>(Edge.class);
		
		Room[] roomsValues = rooms.values().toArray(new Room[rooms.size()]);
		for (int i = 0; i < roomsValues.length; i++) {
			g.addVertex(roomsValues[i].getId());
		}
		
		int N = rooms.size();
		Point[] points = new Point[N];
		for (int i = 0; i < N; i++) {
			points[i] = roomsValues[i].getCenter();
		}
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				for (int k = 0; k < N; k++) {
					boolean isTriangle = true;
					for (int a = 0; a < N; a++) {
						if (a == i || a == j || a == k)
							continue;
						if (PointUtil.isInside(points[a], points[i], points[j], points[k])) {
							isTriangle = false;
							break;
						}
					}
					if (isTriangle) {
						Edge e1 = new Edge(roomsValues[i].getId(), roomsValues[j].getId());
						g.addEdge(roomsValues[i].getId(), roomsValues[j].getId(), e1);
						g.setEdgeWeight(roomsValues[i].getId(), roomsValues[j].getId(),
								PointUtil.distance(roomsValues[i].getX(), roomsValues[i].getY(), roomsValues[j].getX(), roomsValues[j].getY()));
						Edge e2 = new Edge(roomsValues[j].getId(), roomsValues[k].getId());
						g.addEdge(roomsValues[j].getId(), roomsValues[k].getId(), e2);
						g.setEdgeWeight(roomsValues[j].getId(), roomsValues[k].getId(),
								PointUtil.distance(roomsValues[j].getX(), roomsValues[j].getY(), roomsValues[k].getX(), roomsValues[k].getY()));
						Edge e3 = new Edge(roomsValues[k].getId(), roomsValues[i].getId());
						g.addEdge(roomsValues[k].getId(), roomsValues[i].getId(), e3);
						g.setEdgeWeight(roomsValues[k].getId(), roomsValues[i].getId(),
								PointUtil.distance(roomsValues[k].getX(), roomsValues[k].getY(), roomsValues[i].getX(), roomsValues[i].getY()));
					}
				}
			}
		}
		
		
		// Get MST
		KruskalMinimumSpanningTree<Integer, Edge> krusk = new KruskalMinimumSpanningTree<Integer, Edge>(g);
		SpanningTree<Edge> mst = krusk.getSpanningTree();
		
		Set<Edge> minimumEdges = mst.getEdges();
		
		// Add in Extra Edges
		int extraEdges = (int) Math.floor(minimumEdges.size() * percentExtraEdges);
		
		Edge[] allEdges = g.edgeSet().toArray(new Edge[g.edgeSet().size()]);
		while (extraEdges > 0) {
			if (minimumEdges.add(allEdges[r.nextInt(allEdges.length)])) {
				extraEdges--;
			}
		}
		
		for (Edge e : minimumEdges) {
			System.out.println(e.room1 + "   " + e.room2);
		}
		
		
		// Generate Halls based on Edges
		List<Hall> halls = new ArrayList<Hall>();
		for (Edge e : minimumEdges) {
			Room r1 = rooms.get(e.room1);
			Room r2 = rooms.get(e.room2);
			
			boolean horizontalFirst = r.nextBoolean();
			
			Point startingPoint;
			Point endingPoint;
			if (horizontalFirst) {
				startingPoint = new Point(r1.getCenter().x, r1.getY() + r.nextInt(r1.getHeight()));
				endingPoint = new Point(r2.getX() + r.nextInt(r2.getWidth()), r2.getCenter().y);
			} else {
				startingPoint = new Point(r1.getX() + r.nextInt(r1.getWidth()), r1.getCenter().y);
				endingPoint = new Point(r2.getCenter().x, r2.getY() + r.nextInt(r2.getHeight()));
			}

			
			Hall h = new Hall();
			if (horizontalFirst) {
				int currX = startingPoint.x;
				int currY = startingPoint.y;
				h.addToPath(new Point(currX, currY));
				
				while (currX != endingPoint.x) {
					if (startingPoint.x < endingPoint.x) {
						currX++;
					} else {
						currX--;
					}
					h.addToPath(new Point(currX, currY));
				}
				while (currY != endingPoint.y) {
					if (startingPoint.y < endingPoint.y) {
						currY++;
					} else {
						currY--;
					}
					h.addToPath(new Point(currX, currY));
				}
			} else {
				int currX = startingPoint.x;
				int currY = startingPoint.y;
				h.addToPath(new Point(currX, currY));
				
				while (currY != endingPoint.y) {
					if (startingPoint.y < endingPoint.y) {
						currY++;
					} else {
						currY--;
					}
					h.addToPath(new Point(currX, currY));
				}
				while (currX != endingPoint.x) {
					if (startingPoint.x < endingPoint.x) {
						currX++;
					} else {
						currX--;
					}
					h.addToPath(new Point(currX, currY));
				}
			}
			halls.add(h);
			
		}
		
		
		// Randomly place houses
		List<Point> houses = new ArrayList<Point>();
		for (Room room : rooms.values()) {
			for (int y = 0; y < room.getHeight(); y++) {
				for (int x = 0; x < room.getWidth(); x++) {
					if (r.nextDouble() < houseChance) {
						houses.add(new Point(room.getX() + x, room.getY() + y));
					}
				}
			}
		}
		
		
		// Generate Tile Map
		generateTiles(rooms.values(), halls, houses);
		
	}
	
	
	private void generateTiles(Collection<Room> rooms, Collection<Hall> halls, Collection<Point> houses) {
		final int wall = Tile.WallM.getIndex();
		
		
		// Turn everything to walls
		for (int y = 0; y < this.height; y++) {
			for (int x = 0; x < this.width; x++) {
				tileMap[x][y] = Tile.WallM.getIndex();
			}
		}
		
		// Place rooms
		for (Room room : rooms) {
			for (int y = 0; y < room.getHeight(); y++) {
				for (int x = 0; x < room.getWidth(); x++) {
					tileMap[x + room.getX()][y + room.getY()] = Tile.Ground.getIndex();
				}
			}
		}
		
		// Place Halls
		for (Hall hall : halls) {
			for (Point p : hall.getPath()) {
				tileMap[p.x][p.y]= Tile.Ground.getIndex(); 
			}
		}
		
		
		// Place objects
		for (Point house : houses) {
			tileMap[house.x][house.y] = Tile.Houses.getIndex();
		}
		
		// Post Process -> Wall
		int[][] tempMap = new int[this.width][this.height];
		for (int i = 0; i < this.width; i++) {
			int[] yArray = tileMap[i];
			tempMap[i] = new int[this.height];
			System.arraycopy(yArray, 0, tempMap[i], 0, this.height);
		}
		
		
		for (int y = 0; y < this.height; y++) {
			for (int x = 0; x < this.width; x++) {
				int tile = tempMap[x][y];
				
				if (tile != wall)
					continue;
				
				boolean hasLeftWall = x == 0 || tempMap[x-1][y] == wall;
				boolean hasRightWall = x == this.width-1 || tempMap[x+1][y] == wall;
				boolean hasDownWall = y == this.height-1 || tempMap[x][y+1] == wall;
				boolean hasUpWall = y == 0 || tempMap[x][y-1] == wall;
				
				if (!hasLeftWall && !hasRightWall && !hasUpWall && !hasDownWall)
					tileMap[x][y] = Tile.WallSingle.getIndex();
				else if (!hasLeftWall && !hasRightWall && !hasUpWall)
					tileMap[x][y] = Tile.WallSingleT.getIndex();
				else if (!hasLeftWall && !hasRightWall && !hasDownWall)
					tileMap[x][y] = Tile.WallSingleB.getIndex();
				else if (!hasLeftWall && !hasUpWall && !hasDownWall)
					tileMap[x][y] = Tile.WallSingleL.getIndex();
				else if (!hasUpWall && !hasRightWall && !hasDownWall)
					tileMap[x][y] = Tile.WallSingleR.getIndex();
				else if (!hasLeftWall && !hasRightWall)
					tileMap[x][y] = Tile.WallSingleTB.getIndex();
				else if (!hasDownWall && !hasUpWall)
					tileMap[x][y] = Tile.WallSingleLR.getIndex();
				else if (!hasLeftWall && !hasDownWall)
					tileMap[x][y] = Tile.WallBL.getIndex();
				else if (!hasLeftWall && !hasUpWall)
					tileMap[x][y] = Tile.WallTL.getIndex();
				else if (!hasRightWall && !hasDownWall)
					tileMap[x][y] = Tile.WallBR.getIndex();
				else if (!hasRightWall && !hasUpWall)
					tileMap[x][y] = Tile.WallTR.getIndex();
				else if (!hasLeftWall)
					tileMap[x][y] = Tile.WallML.getIndex();
				else if (!hasRightWall)
					tileMap[x][y] = Tile.WallMR.getIndex();
				else if (!hasUpWall)
					tileMap[x][y] = Tile.WallT.getIndex();
				else if (!hasDownWall)
					tileMap[x][y] = Tile.WallB.getIndex();
			}
		}
		
		
		
		
	}
	
	public void generateRandomMap() {
		Random r = new Random();
		for (int y = 0; y < this.height; y++) {
			for (int x = 0; x < this.width; x++) {
				tileMap[x][y] = r.nextInt(Tile.SIZE.getIndex());
			}
		}
	}
	
	public Tile getTile(int x, int y) {
		return Tile.valueOf(tileMap[x][y]);
	}
	
	
	
	public void draw() {
		for (int y = 0; y < this.height; y++) {
			for (int x = 0; x < this.width; x++) {
				sketch.image(tileSheet.getSprite(tileMap[x][y]), x * Application.TILE_SIZE, y * Application.TILE_SIZE);
			}
		}
	}
}
