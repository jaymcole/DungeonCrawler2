package ecu.se;

public class Globals {
    public static final String VERSION = " MISSING";
    
    public static boolean DEBUG = true;
    
    // Render Levels.
    public static final float Z_LEVEL_FLOOR =    0.0f;
    public static final float Z_LEVEL_WALLS =    1.0f;
    public static final float Z_LEVEL_PLAYER =   2.0f;
    public static final float Z_LEVEL_NPC =      2.0f;
    
    public static final String DEFAULT_TEXTURE = "default.png";
    
    public static final int CAMERA_SCROLL_SPEED_X_AXIS = 5;
    public static final int CAMERA_SCROLL_SPEED_Y_AXIS = 5;
    public static final float DEFAULT_CAMERA_ZOOM = 1.0f;
    
    
    // Map Gen Settings (probably as a percent)
    public static final int TURN_CHANCE = 5;
    public static final int SPLIT_CHANCE = 30;
    public static final int CONTINUE_CHANCE = 95;
    public static final int CONTINUE_AFTER_OVERLAP_CHANCE = 45;
    
    public static final float MIN_PATH_DENSITY = 0.3f;
    
    public static final int MAP_TILE_WIDTH = 100;
    public static final int MAP_TILE_HEIGHT = 100;
    public static final int TILE_PIXEL_WIDTH = 128;
    public static final int TILE_PIXEL_HEIGHT= 128;
    
    public static final String SPRITE_INFORMATION_EXTENSION = "spriteInfo";
    
    //LIGHTING
    public static int MAX_LIGHTS = 50; //Must also be changed in the shader
    public static float DEFAULT_LIGHT_INTENSITY = 300;
    
    // DEBUG Settings
    // All of these will probably have to be cleaned up (removed along with whatever functions use them) later.
    public static boolean RENDER_ALL_TILES = false;
    public static boolean USE_TEXTURE_MANAGER = true;
    
    
    
    
}
