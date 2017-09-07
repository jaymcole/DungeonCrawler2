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
    
    protected Texture loadTexture(String filePath) {
        try {
            texture = new Texture(filePath);      
        } catch (GdxRuntimeException e) {
            System.err.println("Unable to load texture \""+ filePath +"\"");
            try {
                texture = new Texture(Globals.DEFAULT_TEXTURE);
            } catch (GdxRuntimeException e2){
                System.err.println("Failed to load default texture " + filePath + " - killing game object.");
                this.kill();
            }
        }
        return texture;
    }
    
    protected void kill() {
        objectManager.remove(this);
        dispose();
    }
}
