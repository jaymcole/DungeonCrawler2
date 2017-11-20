package ecu.se.map;

import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import ecu.se.Globals;

public class Map {
    private static int tilesVertical = 5;
    private static int tilesHorizontal = 5;
    
    private static Tile[][] visibleTiles;
    private static ArrayList<Floor> floors;
    private static Floor currentFloor;
    private static int currentLevel;
    
    
    public Map() {
        floors = new ArrayList<Floor>(200);
        currentLevel = 0;
        setFloor(currentLevel); 
    }
    
    public static void setFloor(int floor) {
        if(floor < 0) 
            floor = 0;
        if(floors.size() <= floor) { 
            for(int i = floors.size(); i <= floor; i++) {
                floors.add(null);
            }
        }
        if ( floors.get(floor) == null) {
            floors.add(floor, new Floor());            
        }
        currentFloor = floors.get(floor);
        if (!currentFloor.getGenerated()) {
            currentFloor.generate();
        }    
        
    }
    
    private static LinkedList<Tile> wallsToRender;
    private static LinkedList<Tile> decalsToRender;
    public static void render(SpriteBatch batch, int cameraX, int cameraY) { 
        if(Globals.RENDER_ALL_TILES) {
            currentFloor.renderAll(batch);
            return;
        }

        wallsToRender = new LinkedList<Tile>();
        decalsToRender = new LinkedList<Tile>();
        visibleTiles = currentFloor.getAdjacent(cameraX, cameraY, tilesHorizontal, tilesVertical);
        for(int i = 0; i < tilesHorizontal; i++) {
            for(int j = 0; j < tilesVertical; j++) {
                if(visibleTiles[i][j] != null) {
                	if (visibleTiles[i][j].isWall) {
                		wallsToRender.add(visibleTiles[i][j]);
                	} else {
                		visibleTiles[i][j].render(batch);
                		decalsToRender.add(visibleTiles[i][j]);
                	}
                }
            }
        }
        
        for(Tile t : decalsToRender) {
        	t.renderDecals(batch);;
        }
        
        for(Tile t : wallsToRender) {
        	t.render(batch);
        	t.renderDecals(batch);
        }
        
        
    }
    
    public static void debugRender(ShapeRenderer renderer, int cameraX, int cameraY) { 

    	renderer.setColor(Color.SKY);
        visibleTiles = currentFloor.getAdjacent(cameraX, cameraY, tilesHorizontal, tilesVertical);
        for(int i = 0; i < tilesHorizontal; i++) {
            for(int j = 0; j < tilesVertical; j++) {
                if(visibleTiles[i][j] != null) {
                	visibleTiles[i][j].debugRender(renderer);
                }
            }
        }
    }
    
    public static void dispose() {
        for(Floor f : floors) {
            if(f != null)
                f.dispose();
        }
    }
    
    /**
     * 
     * @param x - x world coordinate
     * @param y - y world coordinate
     * @return the tile that contains position (x, y)
     */
    public static Tile getTile(int x, int y) {
        return currentFloor.getTile(x, y);
    }
    
    /**
     * 
     * @param x - x world coordinate
     * @param y - y world coordinate
     * @return The tile with the staircase to move one floor up.
     */
    public static Vector2 getFloorUp(int x, int y) {
        return currentFloor.getFloorIn(x,y);
    }
    
    /**
     * 
     * @param x - x world coordinate
     * @param y - y world coordinate
     * @return The tile containing the staircase to move one floor down.
     */
    public static Vector2 getFloorDown(int x, int y) {
        return currentFloor.getFloorOut(x,y);
    }
    
    public static void setScreenResolution(int screenWidth, int screenHeight) {
        tilesHorizontal = (screenWidth/Globals.TILE_PIXEL_WIDTH) + 2;
        tilesVertical = (screenHeight/Globals.TILE_PIXEL_HEIGHT) + 2;
    }    
    
    //TODO: Pathfinding! 
//    public static LinkedList<Vector2> getPath(Vector2 from, Vector2 to) {
//    	//Return a list of Vector2s. Should correspond to tile indices in tiles.
//    	LinkedList<Vector2> path = new LinkedList<Vector2>();
//    	LinkedList<Vector2> open = new LinkedList<Vector2>();
//    	LinkedList<Vector2> closed = new LinkedList<Vector2>();
//    	open.add(from);
//    	float gCost, hCost, fCost;
//    	while(true)
//    	{
//    		open.add(currentFloor.getAdjacent((int)from.x, (int)from.y, currentFloor.getMapWidth(), currentFloor.getMapHeight()));
//    	}
//    	while(true) {
//    		for(;;)
//    		{
//    			break;
//    		}
//    		break;
//    	}
//    	return null;
//    	
//    }
    	
   /* if we wanted a 2D array:::
    public static float[][] getPath(Vector2 from, Vector2 to)
    {
    	int w = currentFloor.getWidth();
    	int h = currentFloor.getHeight();
    	float fromX = from.x;
    	float fromY = from.y;
    	float toX = to.x;
    	float toY = to.y;
    	
    	float[][] pathArray = new float[w][h];
    	pathArray[(int) fromX][(int) fromY] = 1;
    	pathArray[(int) toX][(int) toY] = 1;
    	
    	float gCost, hCost, fCost;
    	return null;
    	
    	
    }*/
}
