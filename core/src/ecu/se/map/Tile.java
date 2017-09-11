package ecu.se.map;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
    
    private boolean flipX, flipY;
        
    public Tile(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        
        objects = new ArrayList<GameObject>();
        bounds = Utilities.getRectangleBounds(x, y, width, height);
        
        
        
    }
    
    public void update(double d) {
        
    }

    public void render(SpriteBatch batch) {
        //batch.draw(texture, x, y);        
        batch.draw(texture, x, y, width, height, textureRegion.getRegionX(), textureRegion.getRegionY(), textureRegion.getRegionWidth(), textureRegion.getRegionHeight(), flipX, flipY);
    }

    public void dispose() {
        texture.dispose();
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