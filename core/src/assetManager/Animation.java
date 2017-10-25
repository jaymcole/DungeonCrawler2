package assetManager;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ecu.se.GameObject;

public class Animation extends GameObject{

	
	private boolean hold = true;
	private int height, width, spriteX, spriteY, tRow, tColumn, frame,sRow, selectedRow;
//	private SpriteAsset spriteAsset;
	private Texture texture;
	private TextureRegion textureRegion;
	
	private int offsetX;
	private int offsetY;
	
	private float speed = 0.1f;
	private float time = 0;
	
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
		
		offsetX = (int) (width * -0.5f);
		offsetY = 0;
	}
	
	public void rowSelect ( int aRow){
		spriteY=(aRow/2)* height;
	}
	 public void setIdle( boolean holding){
		 hold = holding;
	 }

	@Override
	public void update(float deltaTime) {
		time+=deltaTime;
		
		if(hold){
			spriteX = 0;
		} else if(time >= speed) {
	        frame++;
	        frame %= tRow-1;
	        time -= speed;
	        spriteX = frame * width;
	    }		
		textureRegion.setRegion(spriteX, spriteY, width, height);
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.draw(textureRegion, x, y);
	}

	@Override
	public void dispose() {
		
	}
	
	private void setParent(GameObject object) {
		
	}

	public void setXY(int x, int y) {
		this.x = x + offsetX;
		this.y = y + offsetY;
	}
}
