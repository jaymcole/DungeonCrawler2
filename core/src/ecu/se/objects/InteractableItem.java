package ecu.se.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public abstract class InteractableItem extends ItemObject {

	public InteractableItem(float x, float y, String name) {
		super(x, y, name);
		
	}

	public abstract void update(float deltaTime);

	public abstract void render(SpriteBatch batch);

	public abstract void dispose();

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
