package ecu.se.map;

import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

import ecu.se.Globals;

public class Map {
    private static int tilesVertical = 5;
    private static int tilesHorizontal = 5;
    
    private static Tile[][] visibleTiles;
    private static ArrayList<Floor> floors;
    private static Floor currentFloor;
    private static TextureRegion background;
    private static int currentLevel = 0;
    
    //TODO: Bug: Occasionally crashes on generating map. Seems to happen more often when global map size is set high (500+)
    
    public Map() {
        floors = new ArrayList<Floor>(200);
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
    
    public static void render(SpriteBatch batch, int cameraX, int cameraY) { 
        if(Globals.RENDER_ALL_TILES) {
            currentFloor.renderAll(batch);
            return;
        }

        visibleTiles = currentFloor.getAdjacent(cameraX, cameraY, tilesHorizontal, tilesVertical);
        for(int i = 0; i < tilesHorizontal; i++) {
            for(int j = 0; j < tilesVertical; j++) {
                if(visibleTiles[i][j] != null) {
                    visibleTiles[i][j].render(batch);
                }
            }
        }
    }
    
    private static ShapeRenderer debugRenderer = new ShapeRenderer();
    public static void debugRender(Matrix4 projection, int cameraX, int cameraY) { 
        debugRenderer.begin(ShapeType.Line);
        debugRenderer.setProjectionMatrix(projection);
        debugRenderer.setColor(Color.RED);


        visibleTiles = currentFloor.getAdjacent(cameraX, cameraY, tilesHorizontal, tilesVertical);
        for(int i = 0; i < tilesHorizontal; i++) {
            for(int j = 0; j < tilesVertical; j++) {
                if(visibleTiles[i][j] != null) {
                    debugRenderer.polygon(visibleTiles[i][j].getBounds().getTransformedVertices());
                }
            }
        }
        
        debugRenderer.end();
    }
    
    public static void dispose() {
        for(Floor f : floors) {
            if(f != null)
                f.dispose();
        }
    }
    
    public static Tile currentTile(int x, int y) {
        return currentFloor.getTile(x, y);
    }
    
    public static Vector2 floorHelper(int x, int y) {
        return currentFloor.getSpawn(x,y);
    }
    
    public static void setScreenResolution(int screenWidth, int screenHeight) {
        tilesHorizontal = (screenWidth/Globals.TILE_PIXEL_WIDTH) + 2;
        tilesVertical = (screenHeight/Globals.TILE_PIXEL_HEIGHT) + 2;
    }    
    
    //TODO: Pathfinding! 
    public static LinkedList<Vector2> getPath(Vector2 from, Vector2 to) {
    	// Return a list of Vector2s. Should correspond to tile indices in tiles.
    	return null;
    }
}
