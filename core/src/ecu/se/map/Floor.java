package ecu.se.map;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ecu.se.Globals;
import ecu.se.Utilities;

public class Floor {
    private long seed;
    private Random random;
    private boolean generated = false;
    
    private int mapWidth = 100;
    private int mapHeight = 100;
    private int tileWidth = 128;
    private int tileHeight = 128;
    
    Tile[][] tiles;
    
    // TODO: Set tileWidth/Height based on texture??
    
    // TODO: Create variable floor sizes?
    
    // TODO: Add dispose function
    
    public Floor () {
        random = new Random();
        seed = random.nextLong();
        random = new Random(seed);
        init();
    }
    
    public Floor (long seed) {
        random = new Random(seed);
        seed = random.nextLong();
        init();
    }
    
    public Floor (int w, int h) {
        mapWidth = w;
        mapHeight = h;
        random = new Random();
        seed = random.nextLong();
        random = new Random(seed);
        init();
    }
    
    private void init() {
        tiles = new Tile[mapWidth][mapHeight];
        
    }
    
    
    // TODO: Render only the tiles that are visible on screen.
    public void render(SpriteBatch batch) {
        for(int i = 0; i < mapWidth; i++) {
            for(int j = 0; j < mapHeight; j++) {
                tiles[i][j].render(batch);
            }
        }
    }
    
    public void generate() {
        generated = true;
        // Fill tiles array with walls.
        for(int i = 0; i < mapWidth; i++) {
            for(int j = 0; j < mapHeight; j++) {
                tiles[i][j] = new Tile(i*tileWidth, j*tileHeight, tileWidth, tileHeight);
                tiles[i][j].setWall(true);
                //tiles[i][j].setTexture(Utilities.loadTexture("texture/wall/corner.png"));
            }
        }
        
        // Populate the map with walkable tiles.
        while(totalPaths < (mapWidth*mapHeight) * Globals.MIN_PATH_DENSITY) {
            generatePath(random.nextInt(mapWidth), random.nextInt(mapHeight), null);
        }
        
        // Set wall textures.
        for(int i = 0; i < mapWidth; i++) {
            for(int j = 0; j < mapHeight; j++) {
                if(countAdjacentWalkable(i, j) > 0 && tiles[i][j].getWall()) {
                    tiles[i][j].setTexture(Utilities.loadTexture("texture/wall/corner.png"));
                }
            }
        }
    }
    
    private int countAdjacentWalkable(int x, int y) {
        int counter = 0;
        for(int i = x-1; i < x+2; i++) {
            for(int j = y-1; j < y+2; j++) {
                if(inBounds(i, j))
                    if(!tiles[i][j].getWall())
                        counter++;
            }
        }
        return counter;
    }
    
    
    
    // TODO: bruh
    private Tile[][] getAdjacent(int x, int y, int width, int height) {
        return null;
    }
    
    private int totalPaths = 0;
    // generatePath(startX, startY, direction);
    //  startX and startY represent a tile to turn into a path.
    //  direction is the current direction of the path 
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
                //System.out.println("TURN\t" + x + ", " + y);
                dir = Direction.turn(random, dir);
                x += dir.x;
                y += dir.y;
                generatePath(x, y, dir);
            } else if (random.nextInt(100) <= Globals.SPLIT_CHANCE) {
                //System.out.println("SPLIT\t" + x + ", " + y);
                generatePath(x, y, dir);
                dir = Direction.turn(random, dir);
                x += dir.x;
                y += dir.y;
                generatePath(x, y, dir);
            } else {
                //System.out.println("Straight " + x + ", " + y);
                x += dir.x;
                y += dir.y;
                generatePath(x, y, dir);
            }
        } else {
            System.out.println("END\t" + x + ", " + y);
            return;
        }
    }
    
    // checkDirection(x, y, dir) returns true if one tile in the direction of dir (from x, y) 
    //  is within map bounds.
    private boolean checkDirection(int x, int y, Direction dir) {
        if(dir != null) {
            x += dir.x;
            y += dir.y;            
        }
        
        return inBounds(x, y);     
    }
    
 // inBounds(x, y) returns true if x and y are within the map bounds.
    private boolean inBounds(int x, int y) {
        if(x < 0 || x >= mapWidth )
            return false;
        else if (y < 0 || y >= mapHeight )
            return false;
        return true;
    }
    
    public boolean getGenerated() {
        return generated;
    }
}
