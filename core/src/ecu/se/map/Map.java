package ecu.se.map;

import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import ecu.se.Game;
import ecu.se.Globals;
import ecu.se.ObjectManager;

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
        currentLevel = 0;
        changeFloor(0);
//        changeFloor(1);
//        changeFloor(0);
        
    }
    
    public static void setFloor(int floor) {
    	changeFloor = true;
    	changeTo = floor;
    }
    
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
    
    public static int getCurrentLevel() {
    	return currentLevel;
    }
    
    public static void setScreenResolution(int screenWidth, int screenHeight) {
        tilesHorizontal = (screenWidth/Globals.TILE_PIXEL_WIDTH) + 2;
        tilesVertical = (screenHeight/Globals.TILE_PIXEL_HEIGHT) + 2;
    }    

    public static Floor getCurrentFloor() {
    	return currentFloor;
    }
}
