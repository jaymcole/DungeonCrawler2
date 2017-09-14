package ecu.se.map;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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
    
    private ArrayList<Floor> floors;
    private Floor currentFloor;
    
    public Map() {
        floors = new ArrayList<Floor>(200);
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
    
    public void render(SpriteBatch batch) {
        currentFloor.render(batch);
    }
    
}
