package ecu.se.map;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import ecu.se.Globals;
import ecu.se.Utilities;


// TODO: 

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
                tiles[i][j].render(batch);
            }
        }
    }
    
    private void init() {
        tiles = new Tile[mapWidth][mapHeight];
        generate();
    }
    
    private void generate() {
        // Fill tiles array with walls.
        for(int i = 0; i < mapWidth; i++) {
            for(int j = 0; j < mapHeight; j++) {
                tiles[i][j] = new Tile(i*tileWidth, j*tileHeight, tileWidth, tileHeight);
                tiles[i][j].setWall(true);
                tiles[i][j].setTexture(Utilities.loadTexture("texture/wall/corner.png"));
            }
        }
        
        while(totalPaths < (mapWidth*mapHeight) * Globals.MIN_PATH_DENSITY) {
            generatePath(random.nextInt(mapWidth), random.nextInt(mapHeight), null);
        }
        // Set wall textures.
        for(int i = 0; i < mapWidth; i++) {
            for(int j = 0; j < mapHeight; j++) {
            }
        }
    }
    
    private int totalPaths = 0;
    // generatePath(startX, startY, direction);
    //  startX and startY represent a tile to turn into a path.
    //  direction is the current direction of the path 
    //      0 = up;
    //      1 = right;
    //      2 = down;
    //      3 = left;
    //      tiles[0][0] represents the top left most tile.
    private void generatePath(int x, int y, Direction dir) {
        if(!checkDirection(x, y, dir))
            return;
        if(!tiles[x][y].getWall() && random.nextInt(100) > Globals.CONTINUE_AFTER_OVERLAP_CHANCE)
            return;
            
        
        totalPaths++;
        tiles[x][y].setWall(false);
        tiles[x][y].setTexture(Utilities.loadTexture("texture/floor/grass.png"));
        
        if(dir == null) {
            tiles[x][y].setTexture(Utilities.loadTexture("texture/test/test_face_red.png"));
            dir = Direction.randomDirection(random);
            generatePath(x, y, dir);
            x += dir.x;
            y += dir.y;
        }
        

        if(random.nextInt(100) <= Globals.CONTINUE_CHANCE) { 
            if(random.nextInt(100) <= Globals.TURN_CHANCE) {
                System.out.println("TURN\t" + x + ", " + y);
                dir = Direction.turn(random, dir);
                x += dir.x;
                y += dir.y;
                generatePath(x, y, dir);
            } else if (random.nextInt(100) <= Globals.SPLIT_CHANCE) {
                System.out.println("SPLIT\t" + x + ", " + y);
                generatePath(x, y, dir);
                dir = Direction.turn(random, dir);
                x += dir.x;
                y += dir.y;
                generatePath(x, y, dir);
            } else {
                System.out.println("Straight " + x + ", " + y);
                x += dir.x;
                y += dir.y;
                generatePath(x, y, dir);
            }
        } else {
            System.out.println("END\t" + x + ", " + y);
            return;
        }
    }
    
    private boolean checkDirection(int x, int y, Direction dir) {
        if(dir != null) {
            x += dir.x;
            y += dir.y;            
        }
        
        if(x > mapWidth-1 || x < 0 || y > mapHeight-1 || y < 0) {
            return false;
        }
        return true;        
    }
    

}
