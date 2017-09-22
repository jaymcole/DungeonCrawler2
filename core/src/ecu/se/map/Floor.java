package ecu.se.map;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import ecu.se.Globals;
import ecu.se.Utilities;

public class Floor {
    private long seed;
    private Random random;
    private boolean generated = false;
    
    private int mapWidth = Globals.MAP_TILE_WIDTH;
    private int mapHeight = Globals.MAP_TILE_HEIGHT;
    private int tileWidth = Globals.TILE_PIXEL_WIDTH;
    private int tileHeight = Globals.TILE_PIXEL_HEIGHT;
    
    Tile[][] tiles;
    
    // TODO: Floor/Map uses an absurd amount of memory. This needs to be cleaned up.
    
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
    	System.out.println("Generate");
        generated = true;
        // Fill tiles array with walls.
        for(int i = 0; i < mapWidth; i++) {
            for(int j = 0; j < mapHeight; j++) {
            	System.out.println("Initializing tile (" + i + ", " + j + ")");
                tiles[i][j] = new Tile(i*tileWidth, j*tileHeight, tileWidth, tileHeight);
                tiles[i][j].setWall(true);
            }
        }
        System.out.println("Done initializing tiles");
        // Populate the map with walkable tiles.
        System.out.println("Starting new path");
        generatePath(random.nextInt(mapWidth), random.nextInt(mapHeight), null);
        while(totalPaths < (mapWidth*mapHeight) * Globals.MIN_PATH_DENSITY) {
            int x = random.nextInt(mapWidth);
            int y = random.nextInt(mapHeight);
            System.out.println("Starting new path (" + x + ", " + y + ")");

            generatePath(x,y, null);
        }
        System.out.println("Done making tiles.");
        
        ArrayList<ArrayList<Vector2>> islands = new ArrayList<ArrayList<Vector2>>();
        for(int i = 0; i < mapWidth; i++) {
            for(int j = 0; j < mapHeight; j++) {
//                System.out.print("Checking " + i + ", " + j );
                if(!checkLists(new Vector2(i, j), islands) && !tiles[i][j].getWall()) {
//                    System.out.print("\t IF \n");
                    ArrayList<Vector2> path = new  ArrayList<Vector2>();
                    islands.add(path);
                    searchPath(new Vector2(i, j), path);                    
                } else {
//                    System.out.print("\t else \n");
                }
            }
        }
        System.out.println("Done counting islands.");
        System.out.println("Total Islands=" + islands.size());

    }
    
    private int findDistance(Vector2 point1, Vector2 point2) {
   		return (int) (Math.abs(point2.x-point1.x) + Math.abs(point2.y-point1.y));
    }
        	
    
    private boolean checkLists(Vector2 coordinates, ArrayList<ArrayList<Vector2>> islands) {
        for(ArrayList<Vector2> lists : islands) {
            for(Vector2 v : lists) {
                if(v.equals(coordinates)) {
                    return true;
                    
                }
            }
        }
        return false;
    }
    
    
    // TODO: get the shortest path for each island and build a new
    // TODO: Find largest ISLAND
    
    private void searchPath(Vector2 coord, ArrayList<Vector2> path) { 
        if (path.contains(coord))
            return;
        
        if (inBounds((int)coord.x, (int)coord.y) && tiles[(int) coord.x][(int) coord.y] != null && !tiles[(int) coord.x][(int) coord.y].getWall()) {
            path.add(new Vector2(coord.x, coord.y));
            searchPath(Direction.translate(coord, Direction.NORTH, 1), path);
            searchPath(Direction.translate(coord, Direction.EAST, 1), path);
            searchPath(Direction.translate(coord, Direction.WEST, 1), path);
            searchPath(Direction.translate(coord, Direction.SOUTH, 1), path);
        } else {
            return;
        }
    }
    
    private void makeTileWalkable(int x, int y) {
        System.out.println("\tMaking walkable (" + x + ", " + y + ")");

        totalPaths++;
        tiles[x][y].setWall(false);
        tiles[x][y].setTexture(Utilities.loadTexture("texture/floor/castle_tile.jpg"));
        
        if(inBounds(x, y+1) && tiles[x][y+1].getWall())
            tiles[x][y+1].setTexture(Utilities.loadTexture("texture/wall/stonewall_short.png"));
        System.out.println("\tDone making walkable");

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
    	System.out.println("Initializing path at (" + x + ", " + y + ")");

        if(!checkDirection(x, y, dir))
            return;
        System.out.println("\tPassed checkDirection");
        if(!tiles[x][y].getWall() && random.nextInt(100) > Globals.CONTINUE_AFTER_OVERLAP_CHANCE)
            return;
        System.out.println("\tPassed tile check");

        makeTileWalkable(x, y);

        
//        if(inBounds(x, y+1) && tiles[x][y+1].getWall())
//            tiles[x][y+1].setTexture(Utilities.loadTexture("texture/wall/stonewall_short.png"));
        
        if(dir == null) {
        	System.out.println("\tDirection == null");
            tiles[x][y].setTexture(Utilities.loadTexture("texture/floor/castle_tile.jpg"));
            System.out.println("\tSetting texture");
            dir = Direction.randomDirection(random);
            System.out.println("\tDone getting random direction.");
            generatePath(x, y, dir);
            x += dir.x;
            y += dir.y;
        } else {
            System.out.println("\tDirection != null");
        }

        if(random.nextInt(100) <= Globals.CONTINUE_CHANCE) { 
            if(random.nextInt(100) <= Globals.TURN_CHANCE) {
                System.out.println("\tTURN");

                dir = Direction.turn(random, dir);
                x += dir.x;
                y += dir.y;
                generatePath(x, y, dir);
            } else if (random.nextInt(100) <= Globals.SPLIT_CHANCE) {
                System.out.println("\tSPLIT");
            	generatePath(x, y, dir);
                dir = Direction.turn(random, dir);
                x += dir.x;
                y += dir.y;
                generatePath(x, y, dir);
            } else {
                System.out.println("\tFORWARD");

                x += dir.x;
                y += dir.y;
                generatePath(x, y, dir);
            }
        } else {
            System.out.println("\tEND");

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
        
        return inBounds(x, y, 1, 1);     
    }
    
 // inBounds(x, y) returns true if x and y are within the map bounds.
    // bx and by = the number of tiles to leave as buffer.
    // Example: given a grid 10x10 and bx=1, by=2
    //  x=0 or x=9 would return false.
    //  y=0 or y=1 or y=9 or y=8 would return false.
    private boolean inBounds(int x, int y, int bx, int by) {
        if(x < bx || x >= mapWidth - bx )
            return false;
        else if (y < by || y >= mapHeight - by )
            return false;
        return true;
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
    
    public Tile getTile(int x, int y) {
        int xCoord = x/tileWidth;
        int yCoord = y/tileHeight;
        if(inBounds(xCoord, yCoord) && tiles[xCoord][yCoord] != null) {
            return tiles[xCoord][yCoord];
        }
        else {
            return null;
        }

    }
    
    public Vector2 getSpawn(int x, int y)
    {
        for(int i = 0; i < Globals.MAP_TILE_WIDTH; i++)
        {
            for(int n = 0; n < Globals.MAP_TILE_HEIGHT; n++)
                
            {
                if(!tiles[i][n].getWall())
                {
                    return new Vector2((i * tileWidth)+(tileWidth*0.5f),(n * tileHeight)+(tileHeight*0.5f));
                    
                }
            }
        }
        return null;
    }
}
