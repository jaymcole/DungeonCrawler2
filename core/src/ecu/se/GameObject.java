package ecu.se;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;

public abstract class GameObject {
    
    protected float x,y,z;
    protected Texture texture;
    protected boolean alive;
    protected ObjectManager objectManager;
    protected Polygon bounds;
        
    public GameObject(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        alive = true;
        bounds = Utilities.getRectangleBounds(x, y, 20, 20);
    }
   
    public abstract void update(float deltaTime);
    
    public abstract void render(float deltaTime, SpriteBatch batch);
   
    public abstract void dispose();
    
   
 
    
    protected void kill() {
        this.alive = false;
        if(objectManager != null) 
            objectManager.remove(this);
        dispose();
    }
    
    public Polygon getBounds()
    {
    	return bounds;
    }
}
