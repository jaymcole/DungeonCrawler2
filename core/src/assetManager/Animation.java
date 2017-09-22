package assetManager;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ecu.se.GameObject;

public class Animation extends GameObject{

	
	private int height, width, spriteX, spriteY, tRow, tColumn, frame;
//	private SpriteAsset spriteAsset;
	private Texture texture;
	private TextureRegion textureRegion;
	
	// Requirements:
	//	- Add parent, position updates based on parent position
	// 	- Load sprite sheet
	// 	- Render frames from sprite sheet
	//  - Add life span / end condition
	//  - Remove itself after ending animation
	//	- Load a shader(?)	
	
	public Animation(float x, float y, float z,SpriteAsset spriteAsset) {
		super(x, y, z);
		tRow = spriteAsset.getSpriteRows();
		tColumn = spriteAsset.getSpriteColumns();
		height = spriteAsset.getSpriteHeight();
		width = spriteAsset.getSpriteWidth();
		texture = spriteAsset.getTexture().getTexture();
		textureRegion = spriteAsset.getTexture().getTextureRegion();
		frame = 0;
	}

	@Override
	public void update(float deltaTime) {
		spriteX = frame * width;
		
		textureRegion.setRegion(spriteX, spriteY, width, height);
		frame++;
		frame %= tRow;
	}

	@Override
	public void render(float deltaTime, SpriteBatch batch) {
		batch.draw(textureRegion, x, y);
	}

	@Override
	public void dispose() {
		
	}
	
	private void setParent(GameObject object) {
		
	}

}
