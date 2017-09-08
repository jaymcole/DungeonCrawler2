package ecu.se;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.GdxRuntimeException;

public abstract class GameObject {
    
    protected float x,y,z;
    protected Texture texture;
    protected boolean alive;
    protected ObjectManager objectManager;
        
    public GameObject(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        alive = true;
    }
   
    public abstract void update(double d);
    
    public abstract void render(double d, SpriteBatch batch);
   
    public abstract void dispose();
    
    //TODO: This should be cleaned up.
    protected Texture loadTexture(String filePath) {
        this.texture = Utilities.loadTexture(filePath);
        if(texture == null) {
            this.kill();
            return null;
        }
        return texture;
    }
    
    protected void kill() {
        this.alive = false;
        if(objectManager != null) 
            objectManager.remove(this);
        dispose();
    }
}
