package ecu.se;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;

import ecu.se.assetManager.AssetManager;

/**
 *
 * A utilities class with useful methods.
 *
 */
public class Utils {
    
	public Utils() {}
	
    private static ShapeRenderer debugRenderer = new ShapeRenderer();
    private static Random random = new Random();
    
    /**
     * 
     * @return a random boolean
     */
    public static boolean randomBoolean() {
    	return random.nextBoolean();
    }
    
    /**
     * 
     * @return a random int
     */
    public static int getRandomInt() {
    	return random.nextInt();
    }
    
    /**
     * 
     * @param bound
     * @return a random int between 0 and bound
     */
    public static int getRandomInt(int bound) {
    	return random.nextInt(bound);
    }
    
    /**
     * Draws a debug line 
     * @param start
     * @param end
     * @param lineWidth
     * @param color
     * @param projectionMatrix
     */
    public static void DrawDebugLine(Vector2 start, Vector2 end, int lineWidth, Color color, Matrix4 projectionMatrix)
    {
        Gdx.gl.glLineWidth(lineWidth);
        debugRenderer.setProjectionMatrix(projectionMatrix);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugRenderer.setColor(color);
        debugRenderer.line(start, end);
        debugRenderer.end();
        Gdx.gl.glLineWidth(1);
    }
    
    /**
     * Draws a debug line
     * @param start
     * @param end
     * @param projectionMatrix
     */
    public static void DrawDebugLine(Vector2 start, Vector2 end, Matrix4 projectionMatrix)
    {
        Gdx.gl.glLineWidth(2);
        debugRenderer.setProjectionMatrix(projectionMatrix);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugRenderer.setColor(Color.WHITE);
        debugRenderer.line(start, end);
        debugRenderer.end();
        Gdx.gl.glLineWidth(1);
    }
    
    
    public static final int ALIGN_CENTERED 		= 0;
    public static final int ALIGN_BOTTOM_LEFT 	= 1;
    public static final int ALIGN_BOTTOM_RIGHT 	= 2;
    public static final int ALIGN_TOP_RIGHT 	= 3;
    public static final int ALIGN_TOP_LEFT		= 4;
    public static final int ALIGN_BOTTOM_CENTER = 5;
    
    /**
     * 
     * @param x
     * @param y
     * @param width
     * @param height
     * @param ALIGN
     * @return a bounding box with the specified settings/attributes
     */
    public static Polygon getRectangleBounds(float x, float y, float width, float height, int ALIGN) {

        Polygon poly = new Polygon();
        poly.setOrigin(0, 0);
        
        switch (ALIGN) {
            case ALIGN_CENTERED:

            	poly.setVertices(new float[]{
            			-(int)(width*0.5), -(int)(height*0.5),
            			(int)(width*0.5), -(int)(height*0.5),
            			(int)(width*0.5), (int)(height*0.5),
            			-(int)(width*0.5), (int)(height*0.5)
            			});
            	break;
            case ALIGN_BOTTOM_LEFT:
            	poly.setVertices(new float[]{
            			(int)(0), 0,
            			(int)(0), height,
            			(int)(width), (int)(0),
            			(int)(width), (int)(height)
            			});

            	poly.setVertices(new float[]{0, 0, width, 0, width, height, 0, height});
            	break;
            case ALIGN_BOTTOM_RIGHT:
//                poly.setOrigin(width, 0);

            	poly.setVertices(new float[]{0, 0, width, 0, width, height, 0, height});

            	break;
            case ALIGN_TOP_RIGHT:
//                poly.setOrigin(width, height);

            	poly.setVertices(new float[]{0, 0, width, 0, width, height, 0, height});
            	break;
            case ALIGN_TOP_LEFT:
//                poly.setOrigin(0, height);

            	poly.setVertices(new float[]{0, 0, width, 0, width, height, 0, height});
            	break; 
            case ALIGN_BOTTOM_CENTER:
            	poly.setVertices(new float[]{
            			-(int)(width*0.5), 0,
            			(int)(width*0.5), 0,
            			(int)(width*0.5), (int)(height*0.5),
            			-(int)(width*0.5), (int)(height*0.5)
            			});
            	break;
        }
        
        poly.setPosition(x, y);
        poly.setScale(1, 1);
        
        return poly;
    }
    
    /**
     * 
     * @param x
     * @param y
     * @param width
     * @param height
     * @param resolution
     * @return a bounding circle with the specified settings/attributes
     */
    public static Polygon getEllipseBounds(float x, float y, float width, float height, int resolution) {
    	float[] verts = new float[(int)(360.0f / resolution) * 2];
    	float currentAngle = 0;
    	float angleDelta = (360.0f / resolution);
    	for(int i = 0 ; i < (360/resolution)*2; i+=2) {
    		currentAngle += angleDelta;
    		verts[i] = (float) (Math.cos(Math.toRadians(currentAngle)) * width);
    		verts[i+1] = (float) (Math.sin(Math.toRadians(currentAngle)) * height);
    	}
    	
    	Polygon poly = new Polygon(verts);
    	poly.setOrigin(0, 0);
    	poly.setPosition(x, y);
    	poly.setScale(1, 1);
    	
    	
    	return poly;
    	
    }
    
    /**
     * ***Deprecated - Use AssetManager.getTexture(String filePath)
     * 		- Really only good now for watching the absurd amount of ram used when loading a new texture for every GameObject.
     * @param filePath
     * @return Returns a Texture object for the texture found at filePath.
     */
    public static Texture loadTexture(String filePath) {     
        if (Globals.USE_TEXTURE_MANAGER)
            return AssetManager.getTexture(filePath).getTexture();
        Texture texture;
        try {
            texture = new Texture(filePath);      
        } catch (GdxRuntimeException e) {
            System.err.println("Unable to load texture \""+ filePath +"\"");
            try {
                texture = new Texture(Globals.DEFAULT_TEXTURE);
            } catch (GdxRuntimeException e2){
                System.err.println("Failed to load default texture " + filePath + " - killing game object.");
                return null;
            }
        }
        return texture;
    }
    
    /**
     * 
     * @param currentLevel
     * @return The xp required to reach the next level.
     */
    public int xpRequiredToLevel(int currentLevel) {
    	return (int) Math.pow(currentLevel / Globals.XP_REQUIREMENT_CONSTANT, 2);
    }
    
    /**
     * Clamps a value between min and max.
     * @param min - The minimum value to return.
     * @param max - The maximum value to return.
     * @param val - The value to clamp.
     * @return Returns val when val is between min and max, otherwise returns min or max.
     */
    public static float clamp(float min, float max, float val)
    {
        if(val < min)
        {
            return min;
        }
        if(val > max)
        {
            return max;
        }
        return val;
    }
    
    /**
     * Clamps a value between min and max.
     * @param min - The minimum value to return.
     * @param max - The maximum value to return.
     * @param val - The value to clamp.
     * @return Returns val when val is between min and max, otherwise returns min or max.
     */
    public static double clamp(double min, double max, double val)
    {
        if(val < min)
        {
            return min;
        }
        if(val > max)
        {
            return max;
        }
        return val;
    }
    
    /**
     * Clamps a value between min and max.
     * @param min - The minimum value to return.
     * @param max - The maximum value to return.
     * @param val - The value to clamp.
     * @return Returns val when val is between min and max, otherwise returns min or max.
     */
    public static int clamp(int min, int max, int val)
    {
        if(val < min)
        {
            return min;
        }
        if(val > max)
        {
            return max;
        }
        return val;
    }
    
    public static boolean NullOrEmpty(String s) {
    	return (s == null || s.length() <= 0);
    }
    
}
