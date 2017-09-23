package ecu.se.map;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ecu.se.GameObject;
import ecu.se.Utils;

public class Tile extends GameObject{
    private int x, y, width, height;
    private Texture texture;
    private TextureRegion textureRegion;
    
    private boolean isWall;
    
    private ArrayList<GameObject> objects;
        
    public Tile(int x, int y, int width, int height) {
        super(x, y);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        objects = new ArrayList<GameObject>();
        bounds = Utils.getRectangleBounds(x, y, width, height, Utils.ALIGN_BOTTOM_LEFT);
    }
    
    @Override
    public void update(float deltaTime) {
        
    }

    public void render(SpriteBatch batch) {
        if(texture != null) {
            batch.draw(texture, x, y, width, height);
        }
        
        for(GameObject object : objects) {
            object.render(batch);
        }
    }

    public void dispose() {
        if(texture!= null)
            texture.dispose();
        
        for(GameObject object : objects) {
            object.dispose();
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
    
    public void setWall(boolean isWall) {
        this.isWall = isWall;
    }
    
    public boolean getWall() {
        return isWall;
    }
}
