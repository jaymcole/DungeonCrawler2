package ecu.se.map;

import java.util.Collection;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import ecu.se.GameObject;
import ecu.se.Utils;
import ecu.se.objects.ItemObject;

public class Tile extends GameObject{
    private int x, y, width, height;
    private TextureRegion texture;
//    private TextureRegion textureRegion;
    
    private boolean isWall;
    
    private LinkedList<GameObject> objects;
    private LinkedList<GameObject> decals;
        
    public Tile(int x, int y,  int width, int height) {
        super(x, y);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        decals = new LinkedList<GameObject> ();
        objects = new LinkedList<GameObject>();
        bounds = Utils.getRectangleBounds(x, y, width, height, Utils.ALIGN_BOTTOM_LEFT);
    }
    
    @Override
    public void update(float deltaTime) {
    	for(GameObject object : decals) {
    		if (!object.isIdle())
    			object.update(deltaTime);
        }
        
        
        for(GameObject object : objects) {
        	if (!object.isIdle())
        		object.update(deltaTime);
        }
    }

    public void render(SpriteBatch batch) {
        if(texture != null) {
            batch.draw(texture, x, y, width, height);
        }
        for(GameObject g : decals) {
        	g.render(batch);
        }
        
        
        for(GameObject object : objects) {
            object.render(batch);
        }
    }
    
    public void debugRender(ShapeRenderer renderer) {
    	renderer.polygon(bounds.getTransformedVertices());
    	
    	for(GameObject g : decals) {
        	g.debugRender(renderer);
    	}
        
        for(GameObject object : objects) {
            object.debugRender(renderer);
        }
    }
    
    public void setTexture(TextureRegion texture) {
        this.texture = texture;        
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
    
    public void addObject(GameObject object) {
    	if (object instanceof ItemObject) {
    		objects.add(object);
    	}
    }
    
    public void remove(GameObject object) {
    	if (object instanceof ItemObject) {
    		objects.remove(object);
    	}
    }
    
    public void dispose() {
        for(GameObject object : objects) {
            object.dispose();
        }
        
        for(GameObject object : decals) {
            object.dispose();
        } 
    }

}
