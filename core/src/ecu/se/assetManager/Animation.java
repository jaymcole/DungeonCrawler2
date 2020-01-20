package ecu.se.assetManager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ecu.se.GameObject;

/**
 * 
 * Handles changing/picking/rendering animation frames.
 */
public class Animation extends GameObject {
	private boolean hold = true;
	private int totalFramesPassed = 0;
	private int totalFramesAvailable;
	private int spriteHeight, spriteWidth, spriteX, spriteY, totalCol, totalRow, currentCol, currentRow;
	private TextureRegion textureRegion;
	private float rotation = 0;
	private int offsetX;
	private int offsetY;
	private float x, y;
	private float scaleX, scaleY;
	

	private float speed = 0.05f;
	private float time = 0;
	
	private boolean useAllRows = false;
	
	// -1 cycles means loop forever
	private float cycles = -1;
	private int endingFrame = -1;

	public Animation(float x, float y, float z) {
		super(x, y, z);
		currentCol = 0;
		offsetX = 0;
		offsetY = 0;
	}

	public Animation(float x, float y, float z, SpriteAsset spriteAsset) {
		super(x, y, z);
		totalCol = spriteAsset.getSpriteColumns();
		totalRow = spriteAsset.getSpriteRows();
		spriteHeight = spriteAsset.getSpriteHeight();
		spriteWidth = spriteAsset.getSpriteWidth();
		textureRegion = new TextureRegion(spriteAsset.getTexture().getTexture());
		currentCol = 0;
		currentRow = 0;

		
		totalFramesAvailable = totalCol * totalRow;

		offsetX = -(int) (spriteWidth * 0.5);
		offsetY = -(int) (spriteHeight * 0.5);

		scaleX = 1;
		scaleY = 1;
	}

	/**
	 * Sets the row to aRow
	 * 
	 * @param aRow
	 */
	public void setRow(int aRow) {
		spriteY = aRow * spriteHeight;
	}

	/**
	 * Sets idle to holding.
	 * 
	 * @param holding
	 */
	public void setIdle(boolean holding) {
		hold = holding;
	}

	@Override
	public void update(float deltaTime) {
		time += deltaTime;
		if (hold) {
			spriteX = 0;
			time = 0;
		} else if (time >= speed) {
			currentCol++;
			
			if (currentCol > totalCol) {
				if (useAllRows) {
					currentRow++;
					currentCol = 0;
					currentRow %= totalRow;
				}
				currentCol %= totalCol;
			}
			
			time -= speed;
			totalFramesPassed++;
			
			if (totalFramesPassed > endingFrame)
				this.kill();

			spriteX = currentCol * spriteWidth;
			spriteY = currentRow * spriteHeight;
		}
		textureRegion.setRegion(spriteX, spriteY, spriteWidth, spriteHeight);
	}

	@Override
	public void render(SpriteBatch batch) {
		
		offsetX = 0;
		offsetY = 0;

		offsetX = -(int) (spriteWidth * 0.5);
		offsetY = -(int) (spriteHeight * 0.5);

		batch.draw(textureRegion, x, y, spriteWidth * 0.5f, spriteHeight * 0.5f, spriteWidth, spriteHeight, scaleX,
				scaleY, rotation);
	}

	/**
	 * WARNING! Subtracts 90 degrees from rotation
	 * 
	 * @param rotation
	 */
	public void setRotation(float rotation) {
		this.rotation = rotation - 90;
	}

	/**
	 * 
	 * @return this animations frame width.
	 */
	public float getSpriteWidth() {
		return spriteWidth;
	}

	/**
	 * 
	 * @return the animations frame height.
	 */
	public float getSpriteHeight() {
		return spriteHeight;
	}

	/**
	 * Sets this animations textureRegion to tr.
	 * @param tr
	 */
	public void setTextureRegion(TextureRegion tr) {
		this.textureRegion = tr;
	}

	/**
	 * Sets the offset this animation should use.
	 * @param x
	 * @param y
	 */
	public void setOffset(float x, float y) {
		System.err.println("Setting offset!!!");
		offsetX = (int) x;
		offsetY = (int) y;
	}

	/**
	 * Sets this animations position to (x, y)
	 * @param x
	 * @param y
	 */
	public void setXY(int x, int y) {
		this.x = x + offsetX;
		this.y = y + offsetY;
	}

	/**
	 * Sets the scale this animation should use for rendering.
	 * @param x
	 * @param y
	 */
	public void setScale(float x, float y) {
		scaleX = x;
		scaleY = y;
	}

	/**
	 * 
	 * @return the current frame column being rendered.
	 */
	public int getCurrentColumn() {
		return currentCol;
	}

	/**
	 * 
	 * @return the total number of frame columns.
	 */
	public int getTotalColumns() {
		return totalCol;
	}
	
	/**
	 * Sets the number of times the animation should play before completion;
	 * -1 cycles means loops forever.
	 * @param cycles - the number of times this animation should play
	 */
	public void setLoopCycles(float cycles) {
		
		if (useAllRows) {
			endingFrame = (int)(totalFramesAvailable * cycles);			
		} else {
			endingFrame = (int)(totalCol * cycles);
		}
		
	}
	
	/**
	 * Determines if the animation should move to the next row or go back to the beginning of the current row.
	 * @param useAllRows
	 */
	public void setUseAllRows(boolean useAllRows) {
		this.useAllRows = useAllRows;
		setLoopCycles(cycles);
	}
	
	public void setSpeed(float speed) {
		this.speed = speed;
	}

	@Override
	public void dispose() {
	}
}
