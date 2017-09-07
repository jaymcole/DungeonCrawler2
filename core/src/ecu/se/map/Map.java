package ecu.se.map;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Map {
    long seed;
    private static Random random;
    Tile[][] tiles;
    private int mapWidth = 25;
    private int mapHeight = 25;
    private int tileWidth = 128;
    private int tileHeight = 128;
    
    public Map() {
        random = new Random();
        seed = random.nextLong();
        random = new Random(seed);
        init();
    }
    
    public Map(long seed) {
        this.seed = seed;
        random = new Random(seed);
        init();
    }
    
    
    
    public void render(SpriteBatch batch) {
        for(int i = 0; i < mapWidth; i++) {
            for(int j = 0; j < mapHeight; j++) {
                if(tiles[i][j].alive) {
                    tiles[i][j].render(batch);
                }
            }
        }
    }
    
    private void init() {
        tiles = new Tile[mapWidth][mapHeight];
        generate();
    }
    
    private void generate() {
        for(int i = 0; i < mapWidth; i++) {
            for(int j = 0; j < mapHeight; j++) {
                Tile tile = new Tile();
                tile.x = i * tileWidth;
                tile.y = j * tileHeight;
                if(random.nextInt(100) > 50) {
                    tile.alive = true;
                    tiles[i][j] = tile;
                } else {
                    tile.alive = false;
                    tiles[i][j] = tile;
                }
            }
        }
    }
}
