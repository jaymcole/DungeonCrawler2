package ecu.se.map;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import ecu.se.Globals;
import ecu.se.Lighting;
import ecu.se.Utils;
import ecu.se.objects.Light;

public class Floor {
	private long seed;
	private Random random;
	private boolean generated = false;

	private static final int EMPTY 	= 0; private static final String PRINT_EMPTY 	= " ";
	private static final int WALL 	= 1; private static final String PRINT_WALL 	= "W";
	private static final int FLOOR 	= 2; private static final String PRINT_FLOOR 	= "F"; 
	private static final int ROOM 	= 3; private static final String PRINT_ROOM 	= "R";
	private static final int SPAWN 	= 4; private static final String PRINT_SPAWN 	= "O";
	private static final int PILLAR	= 5; private static final String PRINT_PILLAR 	= "P";

	private int mapWidth = Globals.MAP_TILE_WIDTH;
	private int mapHeight = Globals.MAP_TILE_HEIGHT;
	private int tileWidth = Globals.TILE_PIXEL_WIDTH;
	private int tileHeight = Globals.TILE_PIXEL_HEIGHT;
	
	private static final int PATH_END_FACTOR = (int)(0.001f * (Globals.MAP_TILE_HEIGHT * Globals.MAP_TILE_WIDTH));

	private int totalWalkable = 0;
	private Vector2 minTile = new Vector2(mapWidth + 1, mapHeight + 1);
	private Vector2 maxTile = new Vector2(-1, -1);

	private Tile[][] tiles;
	private int[][] map;

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
		map = new int[mapWidth][mapHeight];
		buildChances();
	}

	public int tempSpawnX = 0;
	public int tempSpawnY = 0;

	public void generate() {
		generated = true;
		Direction currentDirection = Direction.randomDirection(random);
		int startX = random.nextInt(mapWidth);
		int startY = random.nextInt(mapHeight);
		tempSpawnX = startX;
		tempSpawnY = startY;

		intersection(new BuildNode(startX, startY, currentDirection));
		map[tempSpawnX][tempSpawnY] = SPAWN;

		tempSpawnX -= minTile.x;
		tempSpawnY -= minTile.y;

		mapWidth = (int) (maxTile.x - minTile.x);
		mapHeight = (int) (maxTile.y - minTile.y);
		int[][] temp = new int[mapWidth][mapHeight];
		for (int i = 0; i < mapWidth; i++) {
			for (int j = 0; j < mapHeight; j++) {
				temp[i][j] = map[(int) (i + minTile.x)][(int) (j + minTile.y)];
			}
		}
		map = temp;

		printFloor();
		finalizeFloor();
	}

	private void finalizeFloor() {
		tiles = new Tile[mapWidth][mapHeight];
		int temp = 0;
		for (int i = 0; i < mapWidth; i++) {
			for (int j = 0; j < mapHeight; j++) {
				temp = map[i][j];
				switch (temp) {
				case EMPTY:
					break;
				case WALL:
					break;
				case FLOOR:
				case ROOM:
				case SPAWN:
					makeTileWalkable(i, j);
					break;
				}
			}
		}

	}

	public void printFloor() {
		String bar = "";
		for (int i = 0; i < (maxTile.x - minTile.x) + 11; i++) {
			bar += "-";
		}

		System.out.println("minTile=" + minTile.toString());
		System.out.println("maxTile=" + maxTile.toString());
		System.out.println("\n");
		for (int j = mapHeight - 1; j >= 0; j--) {
			System.out.printf("%4s",""+ j);
			System.out.print("    ");
			for (int i = 0; i < mapWidth; i++) {
				if (map[i][j] == EMPTY)
					System.out.print(PRINT_EMPTY);
				else if (map[i][j] == SPAWN)
					System.out.print(PRINT_SPAWN);
				else if (map[i][j] == FLOOR)
					System.out.print(PRINT_FLOOR);
				else if (map[i][j] == WALL)
					System.out.print(PRINT_WALL);
				else if (map[i][j] == ROOM)
					System.out.print(PRINT_ROOM);
				else if (map[i][j] == PILLAR)
					System.out.print(PRINT_PILLAR);
			}
			System.out.println("");
		}
		System.out.println("\n");
	}

	private static final float BUILD_STRAIGHT 		= 3f;
	private static final float BUILD_TURN 			= 0.1f;
	private static final float BUILD_ROOM_SMALL 	= 0.1f;
	private static final float BUILD_INTERSECTION 	= 0.1f;
	
	
	private static int[] probabilities = new int[4];
	private void buildChances() {
		probabilities[0] = (int) (100 * BUILD_STRAIGHT);
		probabilities[1] = (int) (100 * BUILD_TURN + probabilities[0]);
		probabilities[2] = (int) (100 * BUILD_ROOM_SMALL + probabilities[1]);
		probabilities[3] = (int) (100 * BUILD_INTERSECTION + probabilities[2]);
	}
	
	
	public void genPath(BuildNode node) {
		if (null == node)
			return;
		if (!inBounds(node.x, node.y))
			return;

		// if (isTaken(new BuildNode(node.x + node.direction.x, node.y +
		// node.direction.y, node.direction)))
		// return;

		// if (isTaken(node))
		// return;

		if (random.nextInt(100) > 89) {
			createLight(node.x * tileWidth, node.y * tileHeight);
			Utils.println(this, "\tCreate Light");
		}
		setTile(node.x, node.y, FLOOR);
		
		if (endPath()){
			int option = random.nextInt(probabilities[probabilities.length-1]);
			for(int i = 0; i < probabilities.length; i++) {
				if (option < probabilities[i]) {
					option = i;
					break;
				}
			}	
//			int cases = 7;
			switch (option) {
			case 0:
				straight(node, random.nextInt(10), true);
				break;
			case 1:
				turn(node);
				break;
			case 2:
				roomSmall(node);
				break;
			case 3:
				intersection(node);
				break;
			default:
				Utils.println(this, node.toString() + " END ");
			}
		} else if (map[node.x][node.y] == FLOOR || map[node.x][node.y] == EMPTY){
			roomSmall(node);
		}
	}
	
	private boolean endPath() {
		return (random.nextInt((totalWalkable / PATH_END_FACTOR) + 1) < 100);
	}

	// Something fishy is going on with xCoord and yCoord
	private void straight(BuildNode node, int length, boolean keepGoing) {
		int xCoord = node.x, yCoord = node.y;
		for (int i = 0; i < length; i++) {
			xCoord = node.x + (i * node.direction.x);
			yCoord = node.y + (i * node.direction.y);
			if (inBounds(xCoord, yCoord)) {
				setTile(xCoord, yCoord, FLOOR);
			} else {
				return;
			}
		}
		if (keepGoing)
			straightContinued(new BuildNode(xCoord, yCoord, node.direction));
		return;
	}

	private void straight(BuildNode node, int length, int type, boolean keepGoing) {
		int xCoord = node.x, yCoord = node.y;
		for (int i = 0; i < length; i++) {
			xCoord = node.x + (i * node.direction.x);
			yCoord = node.y + (i * node.direction.y);
			if (inBounds(xCoord, yCoord)) {
				setTile(xCoord, yCoord, type);
			} else {
				return;
			}
		}	
		if (keepGoing)
			straightContinued(new BuildNode(xCoord, yCoord, node.direction));
		return;
	}
	
	private void straightContinued(BuildNode node) {
		if (random.nextInt(100) > 75)  
			turn(node);
		else 
			genPath(node);
	}

	private void intersection(BuildNode node) {
		setTile(node.x, node.y, FLOOR);
		straight(new BuildNode(node.x, node.y, Direction.nextDirectionCCW(node.direction)), random.nextInt(10), true);
		straight(new BuildNode(node.x, node.y, Direction.nextDirectionCW(node.direction)), random.nextInt(10), true);
		straight(new BuildNode(node.x, node.y, node.direction), random.nextInt(10), true);
		straight(new BuildNode(node.x, node.y, Direction.oppositeDirection(node.direction)), random.nextInt(10), true);
	}

	private void turn(BuildNode node) {
		Direction dir = Direction.turn(random, node.direction);
		straight(new BuildNode(node.x + dir.x, node.y + dir.y, Direction.nextDirectionCCW(node.direction)), 2, true);
	}

	// Add Offset
	private void roomSmall(BuildNode node) {
		int width = random.nextInt(2) + 2;
		int length = random.nextInt(8) + 2;
		int startX = node.x + (width * Direction.nextDirectionCCW(node.direction).x);
		int startY = node.y + (width * Direction.nextDirectionCCW(node.direction).y);
		
		startX += Direction.nextDirectionCW(node.direction).x;
		startY += Direction.nextDirectionCW(node.direction).y;
		straight(new BuildNode(startX, startY, node.direction), length, ROOM, false);
		for (int i = 1; i < (width * 2) -1; i++) {
			startX += Direction.nextDirectionCW(node.direction).x;
			startY += Direction.nextDirectionCW(node.direction).y;
			straight(new BuildNode(startX, startY, node.direction), length, ROOM, false);
		}
		startX += Direction.nextDirectionCW(node.direction).x;
		startY += Direction.nextDirectionCW(node.direction).y;
		straight(new BuildNode(startX, startY, node.direction), length, ROOM, false);
		
		
		
		if (random.nextInt(100) > 75) {
			straight(
					new BuildNode(startX + (int) (width * random.nextFloat()),
							startY + (int) (length * random.nextFloat()), Direction.randomDirection(random)),
					random.nextInt(5) + 1, true);
		}

	}

	private void setTile(int x, int y, int type) {
		updateMaxMin(x, y);
		totalWalkable++;
		if (type == FLOOR && map[x][y] == ROOM) 
			return;
		map[x][y] = type;
	}

	private void makeTileWall(int x, int y) {
		if (tiles[x][y] == null) {
			tiles[x][y] = new Tile(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
			tiles[x][y].setTexture(Utils.loadTexture("texture/floor/stonewall_short.png"));
			tiles[x][y].setWall(true);
		}
	}

	private void makeTileWalkable(int x, int y) {
		if (tiles[x][y] == null) {
			tiles[x][y] = new Tile(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
			tiles[x][y].setTexture(Utils.loadTexture("texture/floor/castle_tile.jpg"));
			tiles[x][y].setWall(false);
		}
	}

	private void updateMaxMin(int x, int y) {
		if (x < minTile.x) {
			minTile.x = x;
		} else if (x > maxTile.x) {
			maxTile.x = x;
		}

		if (y < minTile.y) {
			minTile.y = y;
		} else if (y > maxTile.y) {
			maxTile.y = y;
		}
	}

	private boolean isTaken(BuildNode node) {
		if (!inBounds(node.x + node.direction.x, node.y + node.direction.y))
			return false;
		return map[node.x + node.direction.x][node.y + node.direction.y] != EMPTY;
	}

	private void createLight(int x, int y) {
		Light light = new Light(new Vector3(x, y, 0));
		light.setColor(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1.0f));
		light.setIntensity(200);
		Lighting.addLight(light);
	}

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
				} else {
					tilesToRender[i][j] = null;
				}
			}
		}

		return tilesToRender;
	}

	private boolean inBounds(int x, int y, int bx, int by) {
		if (x < bx || x >= mapWidth - bx)
			return false;
		else if (y < by || y >= mapHeight - by)
			return false;
		return true;
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

	public Vector2 getSpawn(int x, int y) {
		return new Vector2((tempSpawnX * tileWidth) + (tileWidth * 0.5f),
				(tempSpawnY * tileHeight) + (tileHeight * 0.5f));

		// for(int i = 0; i < Globals.MAP_TILE_WIDTH; i++)
		// {
		// for(int n = 0; n < Globals.MAP_TILE_HEIGHT; n++)
		//
		// {
		// if(!tiles[i][n].getWall())
		// {
		// return new Vector2((i * tileWidth)+(tileWidth*0.5f),(n *
		// tileHeight)+(tileHeight*0.5f));
		//
		// }
		// }
		// }
		// return null;
	}
}
