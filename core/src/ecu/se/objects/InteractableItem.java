package ecu.se.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import assetManager.TextureAsset;

public class InteractableItem extends ItemObject {

	public InteractableItem(float x, float y, String name, String path) {
		super(x, y, name, path);
		
	}

	public void update(float deltaTime) {
		
	}

	public void render(SpriteBatch batch) {
		
	}

	public void dispose() {
		
	}

	public String getName() {
		return name;
	}
	
	public Vector3 getPosition() {
    	return new Vector3(x, y, 1);
    }
	
	public boolean itemUsed() {
		// Item is in use
		return true;
	}
	
	
}
