package ecu.se.map;

import java.util.LinkedList;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import actions.Potion;
import archive.Archiver;
import archive.TotalRecords;
import assetManager.AssetManager;
import ecu.se.DecalPicker;
import ecu.se.Game;
import ecu.se.GameObject;
import ecu.se.Globals;
import ecu.se.ObjectMaker;
import ecu.se.ObjectManager;
import ecu.se.Utils;
import ecu.se.objects.Decal;
import ecu.se.objects.InteractableItem;
import ecu.se.objects.Light;

public class Generate {
	private static int mapWidth = Globals.MAP_TILE_WIDTH;
	private static int mapHeight = Globals.MAP_TILE_HEIGHT;
	private static final int tileWidth = Globals.TILE_PIXEL_WIDTH;
	private static final int tileHeight = Globals.TILE_PIXEL_HEIGHT;

	private static final int EMPTY = 0;
	private static final String PRINT_EMPTY = " ";
	private static final int WALL = 91;
	private static final String PRINT_WALL = "W";
	private static final int FLOOR = 92;
	private static final String PRINT_FLOOR = "F";
	private static final int ROOM = 93;
	private static final String PRINT_ROOM = "R";
	private static final int SPAWN = 94;
	private static final String PRINT_SPAWN = "O";
	private static final int PILLAR = 95;
	private static final String PRINT_PILLAR = "P";

	private static LinkedList<BuildNode> remainingNodes;
	private static LinkedList<Light> lights;

	private static int[][] map;
	private static Tile[][] tiles;

	// private static Vector2 maxTile = new Vector2(mapWidth, mapHeight),
	// minTile = new Vector2(0, 0);
	private static InteractableItem up, down;

	private static Random random;

	private static int totalWalkable = 0;

	private static BuildNode minMapNode;
	private static BuildNode maxMapNode;

	public static void generate(Random random, Floor floor) {
		lights = new LinkedList<Light>();

		Generate.random = random;
		mapWidth = Globals.MAP_TILE_WIDTH;
		mapHeight = Globals.MAP_TILE_HEIGHT;
		minMapNode = new BuildNode(mapWidth, mapHeight, Direction.NORTH);
		maxMapNode = new BuildNode(0, 0, Direction.NORTH);

		totalWalkable = 3;
		Generate.random = random;
		remainingNodes = new LinkedList<BuildNode>();
		map = new int[mapWidth][mapWidth];
		down = new InteractableItem(0, 0, tileWidth, tileHeight, "Down Staircase", "texture/floor/staircase_down.png") {
			@Override
			public void onClick(GameObject otherObject) {
				if (otherObject == Game.player)
					Map.setFloor(Map.getCurrentLevel() + 1);
					Archiver.set(TotalRecords.STAIRS_TAKEN, 1);
			}
		};

		up = new InteractableItem(0, 0, tileWidth, tileHeight, "Down Staircase", "texture/floor/staircase_up.png") {
			@Override
			public void onClick(GameObject otherObject) {
				if (otherObject == Game.player)
					Map.setFloor(Map.getCurrentLevel() - 1);
					Archiver.set(TotalRecords.STAIRS_TAKEN, 1);
			}
		};

		generateSpawnRoom(new BuildNode(random.nextInt((int) (mapWidth * 0.5f)) + (int) (mapWidth * 0.25f),
				random.nextInt((int) (mapHeight * 0.5f)) + (int) (mapHeight * 0.25f),
				Direction.randomDirection(random)));

		while (!remainingNodes.isEmpty()) {
			generatePath(remainingNodes.getFirst());
		}

		map = minimizeMap();
		finalizeFloor(map, random, floor);
		finalizeTiles();

		boolean downStaircasePlace = false;
		while (!downStaircasePlace) {
			int x = random.nextInt(mapWidth);
			int y = random.nextInt(mapHeight);
			if (!tiles[x][y].isWall && (x != up.getX() && y != up.getY())) {
				downStaircasePlace = true;
				down.setPosition( ((int)(x * tileWidth + tileWidth * 0.5f)), ((int)(y * tileHeight + tileHeight * 0.5f)));
			}
		}

		// DONE BUILDING
		floor.generatedMap(tiles, lights, up, down, mapWidth, mapHeight);

		addPathNodes();
		printFloor();

		for (int i = 0; i < Utils.getRandomInt(15) + 10; i++) {
			boolean enemyPlaced = false;
			while (!enemyPlaced) {
				int x = random.nextInt(mapWidth);
				int y = random.nextInt(mapHeight);
				if (!tiles[x][y].isWall) {
					enemyPlaced = true;
					if (Vector2.dst(x * tileWidth + (Utils.getRandomInt(tileWidth)), y * tileHeight + Utils.getRandomInt(tileHeight), up.getX(), up.getY()) > 1000) {
						ObjectManager.add(ObjectMaker.createTestMob(x * tileWidth, y * tileHeight));
					}
				}
			}
		}
		// printTiles();
	}

	private static void addPathNodes() {
		for (int i = 0; i < mapWidth; i++) {
			for (int j = 0; j < mapHeight; j++) {
				tiles[i][j].pathNode = new PathNode(i, j, null, 0);
				if (tiles[i][j].isWall)
					tiles[i][j].pathNode.tileCost = 100;
			}
		}
	}

	public static void printTiles() {
		System.out.println("\n");
		for (int j = mapHeight - 1; j >= 0; j--) {
			for (int i = 0; i < mapWidth; i++) {
				System.out.print(tiles[i][j].pathNode.toString());
			}
			System.out.println(" ");
		}
		System.out.println("\n");
	}

	private static void finalizeTiles() {
		for (int i = 0; i < mapWidth; i++) {
			for (int j = 0; j < mapHeight; j++) {
				if (inBounds(i, j) && !tiles[i][j].isWall) {
					createWalls(i, j);
				}
			}
		}

		for (int i = 0; i < mapWidth; i++) {
			for (int j = 0; j < mapHeight; j++) {
				if (inBounds(i, j) && !tiles[i][j].isWall)
					createCorners(i, j);
			}
		}
	}

	private static void createCorners(int x, int y) {
		int xCoord = 0;
		int yCoord = 0;
		boolean insideCorner;
		Tile tile;
		if (inBounds(x, y)) {
			for (int i = 1; i < Direction.values().length; i += 2) {
				boolean ok = true;
				insideCorner = false;

				Direction dir = Direction.oppositeDirection(Direction.values()[i]);

				if (Direction.isExpanded(dir)) {
					Direction left = Direction.nextExpandedDirectionCCW(dir);
					Direction right = Direction.nextExpandedDirectionCW(dir);

					if (tiles[x + left.x][y + left.y].isWall && tiles[x + right.x][y + right.y].isWall) {
					} else if (!tiles[x + left.x][y + left.y].isWall && !tiles[x + right.x][y + right.y].isWall) {
						insideCorner = true;
					} else {
						ok = false;
					}
				}

				if (ok) {
					xCoord = x + dir.x;
					yCoord = y + dir.y;
					if (inBounds(xCoord, yCoord)) {
						tile = tiles[xCoord][yCoord];
						if (tile.isWall) {
							setTileWallTexture(tile, dir, insideCorner);
						}
					}
				}
			}
		}
	}

	private static void createWalls(int x, int y) {
		int xCoord = 0;
		int yCoord = 0;
		Tile tile;
		if (inBounds(x, y)) {
			for (int i = 0; i < Direction.values().length; i += 2) {
				boolean ok = true;
				Direction dir = Direction.oppositeDirection(Direction.values()[i]);

				if (Direction.isExpanded(dir)) {
					Direction left = Direction.nextExpandedDirectionCCW(dir);
					Direction right = Direction.nextExpandedDirectionCW(dir);
					if (tiles[x + left.x][y + left.y].isWall && tiles[x + right.x][y + right.y].isWall) {
					} else {
						ok = false;
					}
				}

				if (ok) {
					xCoord = x + dir.x;
					yCoord = y + dir.y;
					if (inBounds(xCoord, yCoord)) {
						tile = tiles[xCoord][yCoord];
						if (tile.isWall) {
							setTileWallTexture(tile, dir, false);
						}
					}
				}
			}
		}
	}

	private static void setTileWallTexture(Tile tile, Direction direction, boolean insideCorner) {
		if (direction == Direction.NORTH || direction == Direction.SOUTH)
			direction = Direction.oppositeDirection(direction);
		else if (direction == Direction.NORTHWEST)
			direction = Direction.nextExpandedDirectionCCW(Direction.nextExpandedDirectionCCW(direction));
		else if (direction == Direction.NORTHEAST)
			direction = Direction.nextExpandedDirectionCW(Direction.nextExpandedDirectionCW(direction));
		else if (direction == Direction.SOUTHEAST)
			direction = Direction.nextExpandedDirectionCCW(Direction.nextExpandedDirectionCCW(direction));
		else if (direction == Direction.SOUTHWEST)
			direction = Direction.nextExpandedDirectionCW(Direction.nextExpandedDirectionCW(direction));

		Texture tex = AssetManager.getTexture("texture/wall/walltest3.png").getTexture();
		float totalW = tex.getWidth();
		float totalH = tex.getHeight();
		float trWidth = totalW / 3.0f;
		float trHeight = totalH / 3.0f;

		float startX = (trWidth * (1 + direction.x)) / totalW;
		float startY = (trHeight * (1 + direction.y)) / totalH;
		float endX = startX + (trWidth / totalW);

		float endY = startY + (trHeight / totalH);
		TextureRegion tr = new TextureRegion(tex);
		tr.setRegion(tex);
		tr.setRegion(startX, startY, endX, endY);
		tile.addObject(new Decal(tile.getX(), tile.getY(), "WallBoi", tr, tileWidth, tileHeight));
	}

	private static void generateSpawnRoom(BuildNode node) {
		genRoom(node, 5, 5, true, false);
		node.x += node.direction.x * 2;
		node.y += Direction.nextDirectionCW(node.direction).y * 2;
		setMapInt(node, SPAWN, true);
	}

	private static int totalTiles = mapWidth * mapHeight;
	private final static int minTiles = (int) (totalTiles * 0.25);

	private static void generatePath(BuildNode node) {
		remainingNodes.remove(node);
		if (totalWalkable >= minTiles && random.nextInt(100) > totalTiles / (totalWalkable / 2.0f)) {
			genRoom(node, 2, 2, false, false);
			return;
		}

		switch (random.nextInt(5)) {
		case 0:
			genRoom(node, random.nextInt(5) + 2, random.nextInt(5) + 2, random.nextBoolean(), false);
			break;
		case 1:
			genStraightPath(node, random.nextInt(6) + 2, false, true, random.nextBoolean());
			break;
		case 2:
			if (random.nextBoolean()) {
				node.turnLeft();
			} else {
				node.turnRight();
			}
			genStraightPath(node, random.nextInt(6) + 2, false, true, random.nextBoolean());
			break;
		case 3:
			genStraightPath(node.copy(), random.nextInt(6) + 2, false, true, random.nextBoolean());

			node.turnLeft();
			genStraightPath(node.copy(), random.nextInt(6) + 2, false, true, random.nextBoolean());

			node.turnRight();
			node.turnRight();
			genStraightPath(node.copy(), random.nextInt(6) + 2, false, true, random.nextBoolean());
			break;
		default:
			break;
		}
	}

	private static void genRoom(BuildNode node, int width, int length, boolean extend, boolean intersect) {
		// setMapInt(node, SPAWN, true);
		width = Utils.clamp(3, 100, width);
		length = Utils.clamp(3, 100, length);

		BuildNode[] points = new BuildNode[4];

		points[0] = node.copy();
		points[1] = node.copy();
		points[1].forward(length);

		points[2] = node.copy();
		points[2].turnRight();
		points[2].forward(width);

		points[3] = points[2].copy();
		points[3].turnLeft();
		points[3].forward(length);
		BuildNode maxNode = points[3];
		BuildNode minNode = points[0];

		for (int i = 0; i < 4; i++) {
			maxNode = BuildNode.max(maxNode, points[i]);
			minNode = BuildNode.min(minNode, points[i]);
		}

		for (int i = 0; i <= maxNode.x - minNode.x; i++) {
			for (int j = 0; j <= maxNode.y - minNode.y; j++) {
				setMapInt(minNode.x + i, minNode.y + j, ROOM, false);
			}
			if (random.nextInt(100) > 50 && extend) {
				BuildNode temp = node.copy();
				if (random.nextBoolean()) {
					temp.x = random.nextInt(maxNode.x - minNode.x) + minNode.x;
					if (random.nextBoolean()) {
						temp.y = maxNode.y;
						temp.direction = Direction.NORTH;
					} else {
						temp.y = minNode.y;
						temp.direction = Direction.SOUTH;
					}
				} else {
					temp.y = random.nextInt(maxNode.y - minNode.y) + minNode.y;
					if (random.nextBoolean()) {
						temp.x = maxNode.x;
						temp.direction = Direction.EAST;
					} else {
						temp.x = minNode.x;
						temp.direction = Direction.WEST;
					}
				}
				genStraightPath(temp, random.nextInt(5) + 3, false, true, true);
			}
		}
	}

	private static int getType(BuildNode node) {
		return map[node.x][node.y];
	}

	private static void addNewNode(BuildNode node) {
		if (inBounds(node.x, node.y) && getType(node) == EMPTY)
			remainingNodes.add(node);
	}

	private static void genStraightPath(BuildNode node, int length, boolean extend, boolean intersect,
			boolean continueForward) {
		for (int i = 0; i < length; i++) {
			if (extend && random.nextInt(100) > 50) {
				BuildNode outNode = node.copy();
				outNode.direction = Direction.turn(random, node.direction);
				outNode.forward();
				addNewNode(outNode);
			}
			setMapInt(node, FLOOR, false);
			node.add(node.direction);
		}

		if (continueForward) {
			addNewNode(node);
		} else {
			genRoom(node, random.nextInt(4) + 2, random.nextInt(4) + 2, false, false);
		}
	}

	private static void setMapInt(BuildNode node, int floorType, boolean override) {
		setMapInt(node.x, node.y, floorType, override);
	}

	private static void setMapInt(int x, int y, int floorType, boolean override) {
		if (inBounds(x, y)) {
			if (map[x][y] == SPAWN)
				return;

			if (map[x][y] == EMPTY || override) {
				map[x][y] = floorType;
				if (x < minMapNode.x)
					minMapNode.x = x;
				if (x > maxMapNode.x)
					maxMapNode.x = x;

				if (y < minMapNode.y)
					minMapNode.y = y;
				if (y > maxMapNode.y)
					maxMapNode.y = y;
			}
		}
	}

	private static int[][] minimizeMap() {
		int buffer = 2;
		mapWidth = maxMapNode.x - minMapNode.x + 1;
		mapWidth += buffer * 2;
		mapHeight = maxMapNode.y - minMapNode.y + 1;
		mapHeight += buffer * 2;
		int[][] temp = new int[mapWidth][mapHeight];

		for (int i = 0; i < mapWidth - buffer * 2; i++) {
			for (int j = 0; j < mapHeight - buffer * 2; j++) {
				temp[i + buffer][j + buffer] = map[i + minMapNode.x][j + minMapNode.y];
			}
		}
		return temp;
	}

	private static void finalizeFloor(int[][] map, Random random, Floor floor) {
		tiles = new Tile[mapWidth][mapHeight];

		int temp = 0;
		int totalLights = 0;
		for (int i = 0; i < mapWidth; i++) {
			for (int j = 0; j < mapHeight; j++) {
				temp = map[i][j];

				switch (temp) {
				case FLOOR:
				case ROOM:
					if (random.nextInt(100) > 99) {
						createLight((int) (i * tileWidth + (tileWidth * 0.5f)),
								(int) (j * tileHeight + (tileHeight * 0.5f)));
						totalLights++;
					}
					makeTileWalkable(i, j);
					break;
				case SPAWN:
					makeTileWalkable(i, j);
					createLight((int) (i * tileWidth + (tileWidth * 0.5f)),
							(int) (j * tileHeight + (tileHeight * 0.5f)));
					totalLights++;
					up.setPosition(((int)(i * tileWidth + tileWidth * 0.5f)), ((int)(j * tileHeight + tileHeight * 0.5f)));

					break;
				default:
					makeTileWall(i, j);
					break;
				}
			}
		}
		Utils.print("Light Count: " + totalLights + "\n");
	}

	public static void printFloor() {
		System.out.println("\n");
		for (int j = mapHeight - 1; j >= 0; j--) {
			System.out.printf("%4s", "" + j);
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
				else
					System.out.print(map[i][j]);
			}
			System.out.println("");
		}
		System.out.println("\n");
	}

	private static void makeTileWall(int x, int y) {
		tiles[x][y] = new Tile(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
		tiles[x][y].setWall(true);
		// tiles[x][y].pathNode = new PathNode(x,y,null,0);
	}

	private static void makeTileWalkable(int x, int y) {
		if (!inBounds(x, y)) {
			System.err.println("Can't make tile walkable: x=" + x + ", y=" + y);
			return;
		}

		if (tiles[x][y] == null) {
			tiles[x][y] = new Tile(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
			tiles[x][y].setTexture(AssetManager.getTexture("texture/floor/castle_tile.jpg").getTextureRegion());

			if (random.nextInt(100) > 75) {
				Decal floorDecal = new Decal(x * tileWidth + random.nextInt(tileWidth),
						y * tileHeight + random.nextInt(tileHeight), "",
						AssetManager.getTexture(DecalPicker.getMossDecal()).getTextureRegion(), tileWidth, tileHeight);
				floorDecal.setRotation(random.nextInt(360));
				floorDecal.setAlpha(((random.nextInt(4)) / 10.0f) + 0.1f + (random.nextFloat() / 10.0f));
				tiles[x][y].addObject(floorDecal);
				
				if(Utils.getRandomInt(100) >= 97) {
					if (Utils.getRandomInt(100) > 51) 
						ObjectManager.add(ObjectMaker.createActiveItem(x * tileWidth + Utils.getRandomInt(tileWidth), y * tileHeight + Utils.getRandomInt(tileHeight), new Potion(null, Potion.POTION_HEALTH)));
					else
						ObjectManager.add(ObjectMaker.createActiveItem(x * tileWidth + Utils.getRandomInt(tileWidth), y * tileHeight + Utils.getRandomInt(tileHeight), new Potion(null, Potion.POTION_MANA)));
				}
				
				
			}

			// tiles[x][y].pathNode = new PathNode(x,y,null,0);
			tiles[x][y].setWall(false);
		}
	}

	private static void createLight(int x, int y) {
		Light light = new Light(new Vector3(x + random.nextInt(tileWidth), y + random.nextInt(tileWidth), 0));
		light.setColor(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1.0f));
		light.setIntensity(Globals.DEFAULT_LIGHT_INTENSITY);
		lights.add(light);
	}

	private static boolean inBounds(int x, int y) {
		if (x < 0 || x >= mapWidth)
			return false;
		else if (y < 0 || y >= mapHeight)
			return false;
		return true;
	}
}
