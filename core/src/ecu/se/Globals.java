package ecu.se;

import ecu.se.assetManager.AssetManager;
import ecu.se.assetManager.FontAsset;
import ecu.se.gui.GUI;

/**
 *
 * Globals Settings - provides a convenient centralized locations for global setting values.
 *
 */
public class Globals {
    public static final String VERSION = " MISSING";
    
    public static boolean DEBUG = false;
    
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
    
    public static final int MAP_TILE_WIDTH = 1000;
    public static final int MAP_TILE_HEIGHT = 1000;
    public static final int TILE_PIXEL_WIDTH = 128;//128;
    public static final int TILE_PIXEL_HEIGHT= 128;//128;
    
    public static final String SPRITE_INFORMATION_EXTENSION = "spriteInfo";

    
    public static final float XP_REQUIREMENT_CONSTANT = 0.05f;
    
    //AI
    public static int MAX_PATH_LENGTH = 100; //Pathfinding length;
    
    
    //LIGHTING
    public static int MAX_LIGHTS = 200; //Must also be changed in the shader
    public static float DEFAULT_LIGHT_INTENSITY = 900;
    
    // DEBUG Settings
    // All of these will probably have to be cleaned up (removed along with whatever functions use them) later.
    public static boolean RENDER_ALL_TILES = false;
    public static boolean USE_TEXTURE_MANAGER = true;
    
    
    private static FontAsset _defaultFontAsset;
    public static FontAsset defaultFontAsset() {
    	if (_defaultFontAsset == null)
    		_defaultFontAsset = AssetManager.getFont("font/font_jay.ttf", (int) (400 * GUI.conversionX));
    	return _defaultFontAsset;
    }
    
}
