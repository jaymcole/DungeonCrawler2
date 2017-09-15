package ecu.se.map;

import java.util.Random;

public enum Direction {
    NORTH("NORTH",0, 1),
    EAST("EAST", 1, 0),
    SOUTH("SOUTH", 0, -1),
    WEST("WEST",-1, 0);
    

    String name;
    public int x, y;
    Direction(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }
    
    public static Direction nextDirectionCW(Direction dir) {
        switch (dir) {
            case NORTH: return EAST;
            case EAST:  return SOUTH;
            case SOUTH: return WEST;
            case WEST:  return NORTH;
        }
        return null;
    }
    
    public static Direction nextDirectionCCW(Direction dir) {
        switch (dir) {
            case NORTH: return WEST;
            case WEST:  return SOUTH;
            case SOUTH: return EAST;
            case EAST:  return NORTH;
        }
        return null;
    }

    public static Direction randomDirection(Random random) {
        return Direction.values()[random.nextInt(Direction.values().length)];
    }
    
    public static Direction oppositeDirection(Direction dir) {
        switch (dir) {
            case NORTH: return SOUTH;
            case WEST:  return EAST;
            case SOUTH: return NORTH;
            case EAST:  return WEST;
        }
        return null;
    }
    
    public static Direction turn(Random random, Direction dir) {
        if (random.nextBoolean()) {
            return nextDirectionCCW(dir);
        } else {
            return nextDirectionCW(dir);
        }
    }
}
