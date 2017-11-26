package ecu.se.map;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import assetManager.AssetManager;
import ecu.se.GameObject;
import ecu.se.Lighting;
import ecu.se.Utils;
import ecu.se.objects.ItemObject;

public class Tile extends GameObject{
    private int x, y, width, height;
    private TextureRegion texture;
    
    public boolean isWall;
    
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
        
        texture = new TextureRegion();	
        texture.setRegion(AssetManager.getTexture("texture/misc/black.jpg").getTexture());
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
        	batch.setColor(Color.WHITE);
            batch.draw(texture, x, y, width, height);
        }
        
    }
    
    public void renderDecals(SpriteBatch batch) {
    	for(GameObject g : decals) {
    		batch.setColor(Color.WHITE);
        	g.render(batch);
        }
        
        for(GameObject object : objects) {
        	batch.setColor(Color.WHITE);
            object.render(batch);
        }
    }
    
    public void debugRender(ShapeRenderer renderer) {
    	if (isWall)
    		renderer.setColor(Color.RED);
    	else
    		renderer.setColor(Color.FOREST);
    	
    	
    	renderer.polygon(bounds.getTransformedVertices());
    	
    	for(GameObject g : decals) {
        	g.debugRender(renderer);
    	}
        
        for(GameObject object : objects) {
            object.debugRender(renderer);
        }
    }
    
    public LinkedList<GameObject> getObjects() {
    	return objects;
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
//    	if (object instanceof ItemObject) {
    		objects.remove(object);
//    	}
    }
    
    public void load() {
    	for(GameObject g : objects) {
    		if (g.getLight() != null)
    			Lighting.addLight(g.getLight());
    	}
    	
    	for(GameObject g : decals) {
    		if (g.getLight() != null)
    			Lighting.addLight(g.getLight());
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
