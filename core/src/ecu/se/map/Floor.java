package ecu.se.map;

import java.util.LinkedList;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import ecu.se.Globals;
import ecu.se.Lighting;
import ecu.se.objects.Light;
import ecu.se.objects.Staircase;

public class Floor {
	private long seed;
	private Random random;
	private boolean generated = false;

//	private static final int EMPTY 	= 0; private static final String PRINT_EMPTY 	= " ";
//	private static final int WALL 	= 1; private static final String PRINT_WALL 	= "W";
//	private static final int FLOOR 	= 2; private static final String PRINT_FLOOR 	= "F"; 
//	private static final int ROOM 	= 3; private static final String PRINT_ROOM 	= "R";
//	private static final int SPAWN 	= 4; private static final String PRINT_SPAWN 	= "O";
//	private static final int PILLAR	= 5; private static final String PRINT_PILLAR 	= "P";
	

	private int mapWidth = Globals.MAP_TILE_WIDTH;
	private int mapHeight = Globals.MAP_TILE_HEIGHT;
	private int tileWidth = Globals.TILE_PIXEL_WIDTH;
	private int tileHeight = Globals.TILE_PIXEL_HEIGHT;
	
//	private static final int PATH_END_FACTOR = (int)((1.0f/((Math.max(Globals.MAP_TILE_HEIGHT, Globals.MAP_TILE_WIDTH)/10))) * (Globals.MAP_TILE_HEIGHT * Globals.MAP_TILE_WIDTH));
//	private static final int PATH_END_FACTOR = (int)(0.001f * (Globals.MAP_TILE_HEIGHT * Globals.MAP_TILE_WIDTH));
	
//	private Vector2 minTile = new Vector2(mapWidth + 1, mapHeight + 1);
//	private Vector2 maxTile = new Vector2(-1, -1);

	private Tile[][] tiles;
//	private int[][] map;
	private LinkedList<Light> lights;
	
	private Staircase up;
	private Staircase down;
		
	public Floor() {
		random = new Random();
		seed = random.nextLong();
		random = new Random(seed);
		init();
	}

	public Floor(long seed) {
		this.seed = seed;
		random = new Random(seed);
		init();
	}

	public Floor(int w, int h) {
		mapWidth = w;
		mapHeight = h;
		random = new Random();
		seed = random.nextLong();
		random = new Random(seed);
		init();
	}

	private void init() {
		spawnMap();
	}

	public void spawnMap() {
		//TODO: Set player to either up or down staircase coordinates
		//TODO: Load mobs and items into ObjectManager
		if (lights != null && !lights.isEmpty())
			Lighting.setLights(lights);
	}


	public void generate() {
		generated = true;
		Generate.generate(random, this);
	}
	
	public void generatedMap(Tile[][] tiles, LinkedList<Light> lights, Staircase up, Staircase down, int mapWidth, int mapHeight) {
		this.tiles = tiles;
		this.lights = lights;
		this.up = up;
		this.down = down;
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
	}
	
//	private void makeTileWalkable(int x, int y) {
//		if (tiles[x][y] == null) {
//			tiles[x][y] = new Tile(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
//			tiles[x][y].setTexture(Utils.loadTexture("texture/floor/castle_tile.jpg"));
//			tiles[x][y].setWall(false);
//		}
//	}

//	private void updateMaxMin(int x, int y) {
//		if (x < minTile.x) {
//			minTile.x = x;
//		} else if (x > maxTile.x) {
//			maxTile.x = x;
//		}
//
//		if (y < minTile.y) {
//			minTile.y = y;
//		} else if (y > maxTile.y) {
//			maxTile.y = y;
//		}
//	}

//	private boolean isTaken(BuildNode node) {
//		if (!inBounds(node.x + node.direction.x, node.y + node.direction.y))
//			return false;
//		return map[node.x + node.direction.x][node.y + node.direction.y] != EMPTY;
//	}



	public void renderAll(SpriteBatch batch) {
		for (int i = 0; i < mapWidth; i++) {
			for (int j = 0; j < mapHeight; j++) {
				if (tiles[i][j] != null)
					tiles[i][j].render(batch);
			}
		}
	}

	public Tile[][] getAdjacent(int x, int y, int width, int height) {
		x /= tileWidth;
		y /= tileHeight;

		x -= (int) (width * 0.5);
		y -= (int) (height * 0.5);

		Tile[][] tilesToRender = new Tile[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (inBounds(i + x, j + y) && tiles[i+x][j+y] != null) {
					tilesToRender[i][j] = tiles[i + x][j + y];
				} 
//				else {
//					tilesToRender[i][j] = null;
//				}
			}
		}

		return tilesToRender;
	}

	private boolean inBounds(int x, int y) {
		if (x < 0 || x >= mapWidth)
			return false;
		else if (y < 0 || y >= mapHeight)
			return false;
		return true;
	}

	public boolean getGenerated() {
		return generated;
	}

	public void dispose() {
		for (int i = 0; i < mapWidth; i++) {
			for (int j = 0; j < mapHeight; j++) {
				if (tiles[i][j] != null)
					tiles[i][j].dispose();
			}
		}
	}

	public Tile getTile(int x, int y) {
		int xCoord = x / tileWidth;
		int yCoord = y / tileHeight;
		if (inBounds(xCoord, yCoord) && tiles[xCoord][yCoord] != null) {
			return tiles[xCoord][yCoord];
		} else {
			return null;
		}
	}
	
	public int getMapWidth() {
		return mapWidth;
	}
	
	public int getMapHeight() {
		return mapHeight;
	}

	public Vector2 getFloorIn(int x, int y) {
//		return new Vector2((floorInX * tileWidth) + (tileWidth * 0.5f),
//				(floorInY * tileHeight) + (tileHeight * 0.5f));
		if (up == null) {
			System.err.println("Floor returned something ti should not have. (getFloorIn())");
			return new Vector2(); 
		}
		return up.getPositionV2();
	}

	public Vector2 getFloorOut(int x, int y) {
//		return new Vector2((floorOutX * tileWidth) + (tileWidth * 0.5f),
//				(floorOutY * tileHeight) + (tileHeight * 0.5f));
		return down.getPositionV2();
	}
}
