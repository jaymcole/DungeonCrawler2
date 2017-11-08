package ecu.se.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import ecu.se.GameObject;
import ecu.se.ObjectManager;

public abstract class ItemObject extends GameObject {

	protected String name;
	
	//Item has a name and position
	public ItemObject(float x, float y, String name) {
		super(x, y);
		this.name = name;
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
}
