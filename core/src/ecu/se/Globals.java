package ecu.se;

public class Globals {
    public static final String VERSION = " MISSING";
    
    public static final boolean DEBUG = true;
    
    // Render Levels.
    public static final float FLOOR =    0.0f;
    public static final float WALLS =    1.0f;
    public static final float PLAYER =   2.0f;
    public static final float NPC =      2.0f;
    
    public static final String DEFAULT_TEXTURE = "default.png";
    
    public static final int CAMERA_SCROLL_SPEED_X_AXIS = 5;
    public static final int CAMERA_SCROLL_SPEED_Y_AXIS = 5;
    
    
    
    // Map Gen Settings (probably as a percent)
    public static final int TURN_CHANCE = 5;
    public static final int SPLIT_CHANCE = 30;
    public static final int CONTINUE_CHANCE = 95;
    public static final int CONTINUE_AFTER_OVERLAP_CHANCE = 45;
    
    public static final float MIN_PATH_DENSITY = 0.3f;
    
}
