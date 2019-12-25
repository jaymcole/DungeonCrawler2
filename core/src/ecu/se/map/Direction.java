package ecu.se.map;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;

import ecu.se.Logger;

/**
 *
 * A utility class used through Dungeon Crawler.
 *
 */
public enum Direction {
    NORTH		("NORTH"	 , "N"	, 0, 	1, 	90),
    NORTHEAST	("NORTH EAST", "NE"	, 1,	1, 	45),
    EAST		("EAST"		 , "E"	, 1, 	0, 	0),
    SOUTHEAST	("SOUTH EAST", "SE"	, 1,	-1, 315),
    SOUTH		("SOUTH"	 , "S"	, 0, 	-1, 270),
    SOUTHWEST	("SOUTH WEST", "SW"	, -1, 	-1, 225),
    WEST		("WEST"		 , "W"	,-1, 	0, 	180),
	NORTHWEST	("NORTH WEST", "NW"	, -1, 	1, 	135);
    

    public String name, shorthand;
    public int x, y, degAngle;
    Direction(String name, String shorthand, int x, int y, int degAngle) {
        this.name = name;
        this.shorthand = shorthand;
        this.x = x;
        this.y = y;
        this.degAngle = degAngle;
    }
    
    //returns enums name
    public String getName()
    {
    	return this.name();
    }
    //returns shorthand from type Direction
    public String getShorthand()
    {
    	return shorthand;
    }
    //returns x from type Direction
    public int getX()
    {
    	return x;
    }
    //returns y from type Direction
    public int getY()
    {
    	return y;
    }
    //returns DegAngle from type Direction
    public int getDegAngle()
    {
    	return degAngle;
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
    	Direction newboi = Direction.values()[(directionValues + dir.ordinal()+1)%directionValues];
//    	System.out.print(dir.name );
//    	System.out.print(" --> ");
//    	Logger.Debug("NA", "NA",newboi.name);
    	return newboi;
    }
    
    /**
     * 
     * @param dir
     * @return Returns the next direction turning to the left (counter-clockwise) from dir.
     * 			Includes NE, NW, SE, SW
     */
    public static Direction nextExpandedDirectionCCW (Direction dir) {
    	Direction newboi = Direction.values()[(directionValues + dir.ordinal()-1)%directionValues];
//    	System.out.print(dir.name );
//    	System.out.print(" --> ");
//    	Logger.Debug("NA", "NA",newboi.name);
    	return newboi;
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
            default: return null;
        }
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
            default: return null;
        }
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
    public static Direction getDirectionTo(int x1, int y1, int x2, int y2) {
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
    
    public static Direction getExpandedDirection(double rads) {
    	int degree = (int) Math.toDegrees(rads);
    	degree %= 360;
    	degree = Math.abs(((int)((degree-22.5-90) / 45) % Direction.directionValues));
    	Logger.Debug("NA", "NA","Returnning " + degree);
    	return Direction.values()[degree];
    }
    
    /**
     * 
	 * NEEDS TO BE IMPLEMENTED!
     * @return returns NORTH
     */
    public Direction getExpandedDirectionTo(int x1, int y1, int x2, int y2) {
        return NORTH;
    }
    
    public static Direction directionTo(float x1, float y1, float x2, float y2)
    {
    	if (x1 > x2 && y1 > y2)
    	{
    		return SOUTHWEST;
    	}
    	else if (x1 < x2 && y1 > y2)
    	{
    		return SOUTHEAST;
    	}
    	else if (x1 > x2 && y1 < y2)
    	{
    		return NORTHWEST;
    	}
    	else
    	{
    		return NORTHEAST;
    	}
    }
    
    
    /**
     * 
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return The angle between (x1, y1) and (x2, y2) in radians.
     */
    public static float angleRad (float x1, float y1, float x2, float y2) {
    	float deltaX = x2 - x1;
    	float deltaY = y2 - y1;
    	return (float)Math.atan2(deltaY, deltaX);
    }
    
    /**
     * 
     * @param p1
     * @param p2
     * @return The angle between p1 and p2 in radians.
     */
    public static float angleRad (Vector2 p1, Vector2 p2) {
    	return angleRad(p1.x, p1.y, p2.x, p2.y);
    }
    
    /**
     * 
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return The angle between (x1, y1) and (x2, y2) in degrees.
     */
    public static float angleDeg(float x1, float y1, float x2, float y2) {
    	return (float)Math.toDegrees(angleRad(x1, y1, x2, y2));
    }
    
    /**
     * 
     * @param p1
     * @param p2
     * @return The angle between p1 and p2 in degrees.
     */
    public static float angleDeg(Vector2 p1, Vector2 p2) {
    	return (float)Math.toDegrees(angleRad(p1, p2));
    }
    
    /**
     * 
     * @param dir
     * @return true if dir is one of the expanded directions.
     */
    public static boolean isExpanded(Direction dir) {
    	switch (dir) {
    		case NORTHEAST:
    		case NORTHWEST:
    		case SOUTHEAST:
    		case SOUTHWEST:
    			return true;
    		default:
    			return false;
    	}
    }
}
