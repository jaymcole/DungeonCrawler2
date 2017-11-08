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
    private int tilesVertical = 5;
    private int tilesHorizontal = 5;
    
    private Tile[][] visibleTiles;
    private ArrayList<Floor> floors;
    private Floor currentFloor;
    private TextureRegion background;
    private int currentLevel = 0;
    
    public Map() {
        floors = new ArrayList<Floor>(200);
        setFloor(currentLevel);
        
    }
    
    public void setFloor(int floor) {
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
    
    public void render(SpriteBatch batch, int cameraX, int cameraY) { 
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
    
    private ShapeRenderer debugRenderer = new ShapeRenderer();
    public void debugRender(Matrix4 projection, int cameraX, int cameraY) { 
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
    
    public void dispose() {
        for(Floor f : floors) {
            if(f != null)
                f.dispose();
        }
    }
    
    public Tile currentTile(int x, int y) {
        return currentFloor.getTile(x, y);
    }
    
    public Vector2 floorHelper(int x, int y) {
        return currentFloor.getSpawn(x,y);
    }
    
    public void setScreenResolution(int screenWidth, int screenHeight) {
        tilesHorizontal = (screenWidth/Globals.TILE_PIXEL_WIDTH) + 2;
        tilesVertical = (screenHeight/Globals.TILE_PIXEL_HEIGHT) + 2;
    }    
    
    //TODO: Pathfinding! 
    public LinkedList<Vector2> getPath(Vector2 from, Vector2 to) {
    	// Return a list of Vector2s. Should correspond to tile indices in tiles.
    	return null;
    }
}
