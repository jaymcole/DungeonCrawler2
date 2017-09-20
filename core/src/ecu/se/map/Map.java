package ecu.se.map;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import ecu.se.Globals;

// TODO: Add all used (tiles that actually need to be rendered) to a list for rendering.
//      - To avoid having to check a render flag on a bunch of unused tiles.

// TODO: Cleanup Islands
//      - And/Or make passage ways connecting the islands.

// TODO: Generation may need to optimized significantly for it to be viable. Idk, someone with a slower computer
//          will have to give it a try ;);););) ... Or we just need to test it on whichever computer we intend on 
//          showing it with.

// TODO: Generate Monsters / Loot / Treasure

// IMPORTANT
// TODO: Dispose floors

public class Map {
    private int tilesVertical = 5;
    private int tilesHorizontal = 5;
    
    private Tile[][] visibleTiles;
    private ArrayList<Floor> floors;
    private Floor currentFloor;
    
    public Map() {
        floors = new ArrayList<Floor>();
        setFloor(0);
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
}
