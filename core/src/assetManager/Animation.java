package assetManager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ecu.se.GameObject;

public class Animation extends GameObject{

	// Requirements:
	//	- Add parent, position updates based on parent position
	// 	- Load sprite sheet
	// 	- Render frames from sprite sheet
	//  - Add life span / end condition
	//  - Remove itself after ending animation
	//	- Load a shader(?)	
	
	public Animation(float x, float y, float z) {
		super(x, y, z);
	}

	@Override
	public void update(float deltaTime) {
		
	}

	@Override
	public void render(float deltaTime, SpriteBatch batch) {
		
	}

	@Override
	public void dispose() {
		
	}
	
	private void setParent(GameObject object) {
		
	}

}
