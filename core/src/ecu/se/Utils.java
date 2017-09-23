package ecu.se;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.GdxRuntimeException;

import assetManager.AssetManager;

public class Utils {
    
    private static ShapeRenderer debugRenderer = new ShapeRenderer();
    
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
    
    
    public static final int ALIGN_CENTERED = 0;
    public static final int ALIGN_BOTTOM_LEFT = 1;
    public static final int ALIGN_BOTTOM_RIGHT = 2;
    public static final int ALIGN_TOP_RIGHT = 3;
    public static final int ALIGN_TOP_LEFT = 4;
    
    public static Polygon getRectangleBounds(float x, float y, float width, float height, int ALIGN) {
//        height *= 0.5f;
//        width *= 0.5f;
//        Polygon poly = new Polygon(new float[]{-width, -height, width, -height, width, height, -width, height});

        Polygon poly = new Polygon();
        
        switch (ALIGN) {
            case ALIGN_CENTERED:
                poly.setOrigin(width *0.5f, height*0.5f);
                break;
            case ALIGN_BOTTOM_LEFT:
                poly.setOrigin(0, 0);
                break;
            case ALIGN_BOTTOM_RIGHT:
                poly.setOrigin(width, 0);
                break;
            case ALIGN_TOP_RIGHT:
                poly.setOrigin(width, height);
                break;
            case ALIGN_TOP_LEFT:
                poly.setOrigin(0, height);
                break;                
        }
        poly.setVertices(new float[]{0, 0, width, 0, width, height, 0, height});
        poly.setPosition(x, y);
        poly.setScale(1, 1);
        
        return poly;
    }
    
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
    
    public static void println(Object caller, String string) {
        System.out.println("[" + caller.getClass().getName() + "] " + string);
    }
    
    public static void print(Object caller, String string ) {
        System.out.print("[" + caller.getClass().getName() + "] " + string);
    }
    
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
    
}
