package ecu.se.map;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;

import ecu.se.GameObject;
import ecu.se.Utilities;

public class Tile {
    private int x, y, width, height;
    private Texture texture;
    private TextureRegion textureRegion;
    
    private boolean isWall;
    
    private ArrayList<GameObject> objects;
    private Polygon bounds;
    private ShapeRenderer shaper;
    private boolean flipX, flipY;
        
    public Tile(int x, int y, int width, int height) {
        this.x = x;// - (int)(width*0.5f);
        this.y = y;// - (int)(height*0.5f);
        this.width = width;
        this.height = height;
        shaper = new ShapeRenderer();
        objects = new ArrayList<GameObject>();
        bounds = Utilities.getRectangleBounds(x, y, width, height);
        
        
    }
    
    public void update(double d) {
        // TODO: finish tile update method
    }

    public void render(SpriteBatch batch) {
        
        if(texture != null) {
            batch.draw(texture, x, y, width, height);
        }
    }

    public void dispose() {
        if(texture!= null)
            texture.dispose();
        
        shaper.dispose();
        for(int i = 0; i < objects.size(); i++) {
            objects.get(i).dispose();
        }
        
        
        
    }
    
    public void setTexture(Texture texture) {
        this.texture = texture;
        this.textureRegion = new TextureRegion(texture);
        
    }
    
    public void setX (int x) { 
        this.x = x;
    }
    
    public void setY(int y) {
        this.y = y;
    }
    
    public Polygon getBounds() {
        return bounds;
    }
    
    public void setWall(boolean isWall) {
        this.isWall = isWall;
    }
    
    public boolean getWall() {
        return isWall;
    }
    
    
    public void setFlipX(boolean flip) {
        flipX = flip;
    }
    
    public void setFlipY(boolean flip) {
        flipY = flip;
    }
    
}
