package ecu.se.map;

import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import ecu.se.Game;
import ecu.se.GameObject;
import ecu.se.Globals;
import ecu.se.ObjectManager;
import ecu.se.actors.Actor;

/**
 * 
 * Manages all floors
 *
 */
public class Map {
    private static int tilesVertical = 5;
    private static int tilesHorizontal = 5;
    
    private static Tile[][] visibleTiles;
    private static ArrayList<Floor> floors;
    private static Floor currentFloor;
    private static int currentLevel;
    
    private static boolean changeFloor;
    private static int changeTo;
    
    public Map() {
    	setScreenResolution(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    	changeFloor = false;
        floors = new ArrayList<Floor>(200);
        currentLevel = -1;
        changeFloor(0);        
    }
    
    /**
     * Sets the floor to floor
     * @param floor
     */
    public static void setFloor(int floor) {
    	changeFloor = true;
    	changeTo = floor;
    }
    
    /**
     * Changes the floor to floor
     * @param floor
     */
    private static void changeFloor(int floor) {
    	changeFloor = false;
    	boolean movingDown = floor > currentLevel;
    	if (floor < 0)
    		return;
  
  
    	// Add floors until the desired floor is reached.
    	
    	while(floors.size() <= floor + 1) {
        	floors.add(new Floor());	
        }        
        
        if (currentFloor != null) 
        	currentFloor.save();
        
        //LinkedList<GameObject> actors = ObjectManager.getActors();
        //for(GameObject go : actors) {
        //	((Actor)go).dispose();
        //}
        
        
        ObjectManager.dispose();
        new ObjectManager();
        
        currentLevel = floor;
        currentFloor = floors.get(floor);	
        currentFloor.spawnMap();      
        
        if (movingDown)
        	Game.player.setPosition(getFloorUp());
        else 
        	Game.player.setPosition(getFloorDown());
    }

    
    private static LinkedList<Tile> wallsToRender;
    private static LinkedList<Tile> floorsToRender;
    /**
     * Updates all tiles
     * @param deltaTime
     * @param cameraX
     * @param cameraY
     */
    public static void update(float deltaTime, int cameraX, int cameraY) {
    	if (changeFloor)
    		changeFloor(changeTo);

    	wallsToRender = new LinkedList<Tile>();
    	floorsToRender = new LinkedList<Tile>();
    	 visibleTiles = currentFloor.getAdjacent(cameraX, cameraY, tilesHorizontal, tilesVertical);
         for(int i = 0; i < tilesHorizontal; i++) {
             for(int j = 0; j < tilesVertical; j++) {
                 if(visibleTiles[i][j] != null) {
                	visibleTiles[i][j].update(deltaTime);
                	 
                 	if (visibleTiles[i][j].isWall) {
                 		wallsToRender.add(visibleTiles[i][j]);
                 	} else {
                 		floorsToRender.add(visibleTiles[i][j]);
                 	}
                 }
             }
         }
    }
     
    /**
     * Renders all tiles
     * @param batch
     */
    public static void render(SpriteBatch batch) { 
        if(Globals.RENDER_ALL_TILES) {
            currentFloor.renderAll(batch);
            return;
        }

        for(Tile t : floorsToRender) {
        	t.render(batch);
        }
        
        for(Tile t : floorsToRender) {
        	t.renderDecals(batch);
        }

        for(Tile t : wallsToRender) {
        	t.render(batch);
        	t.renderDecals(batch);
        }
    }
    
    /** 
     * Renders the debug view
     * @param renderer
     * @param cameraX
     * @param cameraY
     */
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
    
    /**
     * Disposes all floors
     */
    public static void dispose() {
        for(Floor f : floors) {
            if(f != null)
                f.dispose();
        }
    }
    
    /**
     * 
     * @param x
     * @param y
     * @return a tile with index x,y
     * 			null if out of bounds
     */
    public static Tile getTileByIndex(int x, int y) {
    	return currentFloor.getTileByIndex(x, y);
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
    public static Vector2 getFloorUp() {
        return currentFloor.getFloorIn();
    }
    
    /**
     * 
     * @param x - x world coordinate
     * @param y - y world coordinate
     * @return The tile containing the staircase to move one floor down.
     */
    public static Vector2 getFloorDown() {
        return currentFloor.getFloorOut();
    }
    
    /**
     * 
     * @return the current floor level
     */
    public static int getCurrentLevel() {
    	return currentLevel;
    }
    
    /**
     * Sets the screen resolution - used for determining how many tiles to render
     * @param screenWidth
     * @param screenHeight
     */
    public static void setScreenResolution(int screenWidth, int screenHeight) {
        tilesHorizontal = (screenWidth/Globals.TILE_PIXEL_WIDTH) + 2;
        tilesVertical = (screenHeight/Globals.TILE_PIXEL_HEIGHT) + 2;
    }    

    /**
     * 
     * @return the current flor
     */
    public static Floor getCurrentFloor() {
    	return currentFloor;
    }
}
