package ecu.se.map;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ecu.se.Globals;
import ecu.se.Utilities;

public class Floor {
    private long seed;
    private Random random;
    private boolean generated = false;
    
    private int mapWidth = Globals.MAP_TILE_WIDTH;
    private int mapHeight = Globals.MAP_TILE_HEIGHT;
    private int tileWidth = 128;
    private int tileHeight = 128;
    
    Tile[][] tiles;
    
    // TODO: Set tile Width/Height based on texture??
    
    // TODO: Create variable floor sizes?
        
    public Floor () {
        random = new Random();
        seed = random.nextLong();
        random = new Random(seed);
        init();
    }
    
    public Floor (long seed) {
        this.seed = seed;
        random = new Random(seed);
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
    
    public void renderAll(SpriteBatch batch) {        
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
            }
        }
        
        // Populate the map with walkable tiles.
        while(totalPaths < (mapWidth*mapHeight) * Globals.MIN_PATH_DENSITY) {
            generatePath(random.nextInt(mapWidth), random.nextInt(mapHeight), null);
        }
        
        /*
        // Set wall textures.
        for(int i = 0; i < mapWidth; i++) {
            for(int j = 0; j < mapHeight; j++) {
                if(countAdjacentWalkable(i, j) > 0 && tiles[i][j].getWall()) {
                    tiles[i][j].setTexture(Utilities.loadTexture("texture/wall/corner.png"));
                }
            }
        }
        */
    }
    
    @SuppressWarnings("unused")
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
    
    
    
    public Tile[][] getAdjacent(int x, int y, int width, int height) {
        x /= tileWidth;
        y /= tileHeight;
        //System.out.println("Around: "+ x + " " + y);

        x -= (int)(width*0.5);
        y -= (int)(height*0.5);
        
        Tile[][] tilesToRender = new Tile[width][height];
        for(int i = 0; i < width; i++) {
            //System.out.print("\t");
            for(int j = 0; j < height; j++) {
                //System.out.print( "("+ (i+x) + ", " + (j+y) + ")  ");
                if(inBounds(i+x, j+y)) {
                    tilesToRender[i][j] = tiles[i+x][j+y];
                } else {
                    tilesToRender[i][j] = null;
                }
            }
            //System.out.println("");
        }
        
        return tilesToRender;
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
        tiles[x][y].setTexture(Utilities.loadTexture("texture/floor/castle_tile.jpg"));
        
        if(inBounds(x, y+1) && tiles[x][y+1].getWall())
            tiles[x][y+1].setTexture(Utilities.loadTexture("texture/wall/stonewall_short.png"));
        
        if(dir == null) {
            tiles[x][y].setTexture(Utilities.loadTexture("texture/floor/castle_tile.jpg"));
            dir = Direction.randomDirection(random);
            generatePath(x, y, dir);
            x += dir.x;
            y += dir.y;
        }
        
        if(random.nextInt(100) <= Globals.CONTINUE_CHANCE) { 
            if(random.nextInt(100) <= Globals.TURN_CHANCE) {
                dir = Direction.turn(random, dir);
                x += dir.x;
                y += dir.y;
                generatePath(x, y, dir);
            } else if (random.nextInt(100) <= Globals.SPLIT_CHANCE) {
                generatePath(x, y, dir);
                dir = Direction.turn(random, dir);
                x += dir.x;
                y += dir.y;
                generatePath(x, y, dir);
            } else {
                x += dir.x;
                y += dir.y;
                generatePath(x, y, dir);
            }
        } else {
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
    
    public void dispose() {
        for(int i = 0; i < mapWidth; i++) {
            for(int j = 0; j < mapHeight; j++) {
                tiles[i][j].dispose();
            }
        }
    }
}
