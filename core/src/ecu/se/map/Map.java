package ecu.se.map;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ecu.se.Globals;
import ecu.se.Utilities;

// TODO: Add all used (tiles that actually need to be rendered) to a list for rendering.
//      - To avoid having to check a render flag on a bunch of unused tiles.

// TODO: Cleanup Islands
//      - And/Or make passage ways connecting the islands.

// TODO: Generation may need to optimized significantly for it to be viable. Idk, someone with a slower computer
//          will have to give it a try ;);););) ... Or we just need to test it on whichever computer we intend on 
//          showing it with.

// TODO: Generate Monsters / Loot / Treasure

// IMPORTANT
// TODO: Decide whether to save generated maps. Obviously we'll be able to generate the maps again using a seed, 

public class Map {
    
    
    
    private ArrayList<Floor> floors;
    private Floor currentFloor;
    public Map() {
        floors = new ArrayList<Floor>();
        floors.add(new Floor(150, 150));
        currentFloor = floors.get(0);
        currentFloor.generate();
    }
    
    public void render(SpriteBatch batch) {
        currentFloor.render(batch);
    }
}
