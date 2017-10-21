package ecu.se.map;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;

public enum Direction {
    NORTH		("NORTH"	 , "N"	, 0, 	1),
    NORTHEAST	("NORTH EAST", "NE"	, 1,	1),
    EAST		("EAST"		 , "E"	, 1, 	0),
    SOUTHEAST	("SOUTH EAST", "SE"	, 1,	-1),
    SOUTH		("SOUTH"	 , "S"	, 0, 	-1),
    SOUTHWEST	("SOUTH WEST", "SW"	, -1, 	-1),
    WEST		("WEST"		 , "W"	,-1, 	0),
	NORTHWEST	("NORTH WEST", "NW"	, -1, 	1);
    

    public String name, shorthand;
    public int x, y;
    Direction(String name, String shorthand, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }
    
    private static final int directionValues = Direction.values().length;
    
    /**
     * 
     * @param dir
     * @return Returns the Direction index value of dir.
     */
    public static int getIndexValue(Direction dir) {
    	for(int i = 0; i < directionValues; i++) {
    		if (dir == Direction.values()[i])
    			return i;
    	}
    	return 0;
    }
    
    /**
     * 
     * @param dir
     * @return returns the next direction turning to the right (clockwise) form dir.
     * 			Includes NE, NW, SE, SW
     */
    public static Direction nextExpandedDirectionCW (Direction dir) {
    	return Direction.values()[(getIndexValue(dir)+1)%directionValues];
    }
    
    /**
     * 
     * @param dir
     * @return Returns the next direction turning to the left (counter-clockwise) from dir.
     * 			Includes NE, NW, SE, SW
     */
    public static Direction nextExpandedDirectionCCW (Direction dir) {
    	return Direction.values()[(getIndexValue(dir)-1)%directionValues];
    }
    
    /**
     * 
     * @param dir - The starting Direction.
     * @return Returns the next clockwise direction from dir.
     *	Only includes N, E, S, W
     */
    public static Direction nextDirectionCW(Direction dir) {
        switch (dir) {
            case NORTH: return EAST;
            case EAST:  return SOUTH;
            case SOUTH: return WEST;
            case WEST:  return NORTH;
        }
        return null;
    }
    
    /**
     * 
     * @param dir - The starting Direction.
     * @return Returns the next counter-clockwise direction from dir.
     *	Only includes N, E, S, W
     */
    public static Direction nextDirectionCCW(Direction dir) {
        switch (dir) {
            case NORTH: return WEST;
            case WEST:  return SOUTH;
            case SOUTH: return EAST;
            case EAST:  return NORTH;
        }
        return null;
    }

    /**
     * 
     * @param random
     * @return Returns a random direction.
     * Includes expanded directions.
     */
    public static Direction randomExpandedDirection(Random random) {
        return Direction.values()[random.nextInt(directionValues)];
    }
    
    /**
     * 
     * @param random - The Random Object used to generate the return direction. Pass null to create a new random object.
     * @return Returns a random direction.
     * Does NOT include expanded directions.
     */
    public static Direction randomDirection(Random random) {
    	if(random == null) {
    		random = new Random();
    	}
    	
    	int dir = 0;
    	do {
    		dir = random.nextInt(directionValues);
    	} while (dir % 2 != 0);
        return Direction.values()[dir];
    }
    
    /**
     * 
     * @param dir - The starting Direction.
     * @return Returns a Direction that points in the opposite direction of dir.
     * 
     * Examples:
     * oppositeDirection(NORTH) return SOUTH
     * oppositeDirection(SOUTHEAST) return NORTHWEST
     * oppositeDirection(EAST) return WEST
     */
    public static Direction oppositeDirection(Direction dir) {
        switch (dir) {
            case NORTH: return SOUTH;
            case WEST:  return EAST;
            case SOUTH: return NORTH;
            case EAST:  return WEST;
            case NORTHEAST: return SOUTHWEST;
            case NORTHWEST:  return SOUTHEAST;
            case SOUTHEAST: return NORTHWEST;
            case SOUTHWEST:  return NORTHEAST;
        }
        return null;
    }
    
    /**
     * 
     * @param random - The Random used to generate random booleans.
     * - If random is null, a new Random object will be instantiated.
     * @param dir - the starting dir.
     * @return Returns the Direction to the left OR the right of dir.
     */
    public static Direction turn(Random random, Direction dir) {
    	if(random == null) {
    		random = new Random();
    	}
    	
        if (random.nextBoolean()) {
            return nextDirectionCCW(dir);
        } else {
            return nextDirectionCW(dir);
        }
    }
    
    /**
     * 
     * @param random - The Random used to generate random booleans.
     * - If random is null, a new Random object will be instantiated.
     * @param dir - the starting dir.
     * @return Returns the Direction to the left OR the right of dir.
     */
    public static Direction turnExpanded(Random random, Direction dir) {
    	if(random == null) {
    		random = new Random();
    	}
    	
        if (random.nextBoolean()) {
            return nextExpandedDirectionCW(dir);
        } else {
            return nextExpandedDirectionCW(dir);
        }
    }
    
    /**
     * 
     * @param start - The starting coordinates to translate
     * @param dir - The Direction to move in.
     * @param distance - The distance to move.
     * @return Translates the x/y coordinates form start by distance in the direction of dir.
     */
    public static Vector2 translate(Vector2 start, Direction dir, int distance) {
        return new Vector2(start.x + (dir.x * distance), start.y + (dir.y * distance));
    }
    
    /**
     * 
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return Returns the NON-Expanded Direction from (x1, y1), to (x2, y2).
     * i.e. only NORTH, EAST, SOUTH, WEST
     */
    public Direction getDirectionTo(int x1, int y1, int x2, int y2) {
        int deltaX = x1 - x2;
        int deltaY = y1 - y2;
        
        if (Math.abs(deltaX) < Math.abs(deltaY)) {
            if (deltaX > 0)
                return Direction.EAST;
            return Direction.WEST;
        } else {
            if(deltaY > 0)
                return Direction.NORTH;
            return Direction.SOUTH;
        }
    }
    
    /**
     * 
	 * NEEDS TO BE IMPLEMENTED!
     * @return returns NORTH
     */
    public Direction getExpandedDirectionTo(int x1, int y1, int x2, int y2) {
        return NORTH;
    }
}
