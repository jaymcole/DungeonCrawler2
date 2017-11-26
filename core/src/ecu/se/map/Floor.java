package ecu.se.map;

import java.util.LinkedList;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import ecu.se.Game;
import ecu.se.GameObject;
import ecu.se.Globals;
import ecu.se.Lighting;
import ecu.se.ObjectManager;
import ecu.se.objects.InteractableItem;
import ecu.se.objects.Light;

public class Floor {
	private long seed;
	private Random random;
	private boolean generated = false;

	private int mapWidth = Globals.MAP_TILE_WIDTH;
	private int mapHeight = Globals.MAP_TILE_HEIGHT;
	private int tileWidth = Globals.TILE_PIXEL_WIDTH;
	private int tileHeight = Globals.TILE_PIXEL_HEIGHT;

	private Tile[][] tiles;
	private LinkedList<Light> lights;
	private LinkedList<GameObject> floorObjects;

	private InteractableItem up;
	private InteractableItem down;

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
		lights = new LinkedList<Light>();
		generated = false;
	}

	public void save() {
		floorObjects = ObjectManager.getAllMapObjects();
	}

	public void spawnMap() {
		System.err.println("(Before SPAWN) Floor has " + lights.size() + " lights.");
		System.err.println("Spawning map");
		if (!generated)
			generate();

		// if (lights != null && !lights.isEmpty())
		Lighting.setLights(lights);

		// Load object/items/actor lights/etc
		if (floorObjects != null)
			for (GameObject object : floorObjects) {
				ObjectManager.add(object);
				if (object.getLight() != null)
					Lighting.addLight(object.getLight());
			}
		
		for(int i = 0; i < mapWidth; i++) {
			for(int j = 0; j < mapHeight; j++) {
				tiles[i][j].load();
			}
		}

		// Load player light
		if (Game.player != null && Game.player.getLight() != null) {
			System.err.println("Adding player light");
			Lighting.addLight(Game.player.getLight());
		}
		System.err.println("(after SPAWN) Floor has " + lights.size() + " lights.");
	}

	public void generate() {
		generated = true;
		Generate.generate(random, this);
	}

	public void generatedMap(Tile[][] tiles, LinkedList<Light> lights, InteractableItem up, InteractableItem down,
			int mapWidth, int mapHeight) {
		this.tiles = tiles;
		this.lights = lights;
		this.up = up;
		this.down = down;
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		getTile((int) up.getX(), (int) up.getY()).addObject(up);
		getTile((int) down.getX(), (int) down.getY()).addObject(down);
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
				if (inBounds(i + x, j + y) && tiles[i + x][j + y] != null) {
					tilesToRender[i][j] = tiles[i + x][j + y];
				}
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
		if (tiles == null)
			return;

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
			return tiles[0][0];
		}
	}

	public int getMapWidth() {
		return mapWidth;
	}

	public int getMapHeight() {
		return mapHeight;
	}

	/**
	 * 
	 * @return Staircase coordinates when moving down floors
	 */
	public Vector2 getFloorIn() {
		if (up == null) {
			return new Vector2();
		}
//		return new Vector2(up.getX() - (tileWidth * 0.5f), up.getY() - (tileHeight * 0.5f));
		return new Vector2(up.getX() + (tileWidth * 0.5f), up.getY() + (tileHeight * 0.5f));
		// return up.getPositionV2();
	}

	/**
	 * 
	 * @return Staircase coordinates when moving up floors
	 */
	public Vector2 getFloorOut() {
//		return new Vector2(down.getX() - (tileWidth * 0.5f), down.getY() - (tileHeight * 0.5f));
		return new Vector2(down.getX() + (tileWidth * 0.5f), down.getY() + (tileHeight * 0.5f));
		// return down.getPositionV2();
	}

	public LinkedList<Light> getlights() {
		return lights;
	}
}
