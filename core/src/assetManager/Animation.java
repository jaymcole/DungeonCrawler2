package assetManager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ecu.se.map.Direction;


public class Animation{
	private boolean hold = true;
	private int spriteHeight, spriteWidth, spriteX, spriteY, tRow, tColumn, frame,sRow, selectedRow;
	private TextureRegion textureRegion;
	private float rotation = 0;
	private int offsetX;
	private int offsetY;
	private float x, y;
	
	private float speed = 0.1f;
	private float time = 0;
	
	private boolean useExtendedDirections = false;
	
	public Animation (float x, float y, float z) {
		frame = 0;
		offsetX = 0;
		offsetY = 0;
	}

	public Animation(float x, float y, float z,SpriteAsset spriteAsset) {		
		tRow = spriteAsset.getSpriteRows();
		tColumn = spriteAsset.getSpriteColumns();
		spriteHeight = spriteAsset.getSpriteHeight();
		spriteWidth = spriteAsset.getSpriteWidth();
		textureRegion = new TextureRegion(spriteAsset.getTexture().getTexture());
		frame = 0;
		if (spriteAsset.numCol == 8) 
			useExtendedDirections = true;
		offsetX = (int) (spriteWidth * -0.5f);
		offsetY = 0;
	}
	
	public void rowSelect ( int aRow){
		if (useExtendedDirections) 
			spriteY= aRow * spriteHeight;
		else
			spriteY=(aRow/2)* spriteHeight;
	}
	
	 public void setIdle( boolean holding){
		 hold = holding;
	 }

	public void update(float deltaTime) {
		time+=deltaTime;
		if(hold){
			spriteX = 0;
			time = 0;
		} else if(time >= speed) {
	        frame++;
	        frame %= tRow-1;
	        time -= speed;
	        spriteX = frame * spriteWidth;
	    }		
		textureRegion.setRegion(spriteX, spriteY, spriteWidth, spriteHeight);
	}

	public void render(SpriteBatch batch) {
		batch.draw(textureRegion, x, y, spriteWidth * 0.5f, spriteHeight * 0.5f, spriteWidth, spriteHeight, 1, 1, rotation);
	}
	
	/**
	 * WARNING! Subtracts 90 degrees from rotation
	 * @param rotation
	 */
	public void setRotation(float rotation) {
		this.rotation = rotation - 90;
	}
	
	public float getSpriteWidth() {
		return spriteWidth;
	}
	
	public float getSpriteHeight() {
		return spriteHeight;
	}
	
	
	public void setNumRows(int r) {
		tRow = r;
	}
	
	public void setNumColumns(int c) {
		this.tColumn = c;
	}
	
	public void setSpriteH(int h) {
		this.spriteHeight = h;
	}
	
	public void setSpriteW(int w) {
		this.spriteWidth = w;
		offsetX = (int) (spriteWidth * -0.5f);
	}
	
	public void setTextureRegion(TextureRegion tr) {
		this.textureRegion = tr;
	}
	
	
	
	public void setExtendedDirection(boolean useDir) {
		useExtendedDirections = true;
	}

	public void dispose() {
		
	}
	
	public void setXY(int x, int y) {
		this.x = x + offsetX;
		this.y = y + offsetY;
	}
}
