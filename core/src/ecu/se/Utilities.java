package ecu.se;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class Utilities {
    
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
    
    public static Polygon getRectangleBounds(float cx, float cy, float width, float height) {
        height *= 0.5f;
        width *= 0.5f;
        
        Polygon poly = new Polygon(new float[]{-width, -height, width, -height, width, height, -width, height});
        poly.setOrigin(cx, cy);
        poly.setScale(1, 1);
        
        return poly;
    }
    
    public static Texture loadTexture(String filePath) {
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
