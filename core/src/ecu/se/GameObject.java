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
        bounds = Utils.getRectangleBounds(x, y, 20, 20, Utils.ALIGN_BOTTOM_CENTER);
        
    }
    
    public GameObject(float x, float y) {
        this.x = x;
        this.y = y;
        this.z = Globals.Z_LEVEL_FLOOR;
        alive = true;
        bounds = Utils.getRectangleBounds(x, y, 5, 5, Utils.ALIGN_BOTTOM_CENTER);
        
    }
   
    public abstract void update(float deltaTime);
    
    public abstract void render(SpriteBatch batch);
   
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
    
    public boolean isAlive() {
        return alive;
    }
}
