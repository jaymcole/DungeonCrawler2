package ecu.se.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ecu.se.GameObject;

public class WorldObject extends GameObject{

	public WorldObject(float x, float y, float z, Texture texture) {
		super(x, y, z);
		this.texture = texture;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float deltaTime, SpriteBatch batch) {
		batch.draw(texture, x, y);
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
	

}
