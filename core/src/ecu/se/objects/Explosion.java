package ecu.se.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ecu.se.GameObject;

public class Explosion extends GameObject{

	public Explosion(float x, float y, float force, float damage) {
		super(x, y);
	}

	@Override
	public void update(float deltaTime) {
		
	}

	@Override
	public void render(SpriteBatch batch) {
	}

	@Override
	public void dispose() {		
	}

}
