package assetManager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class Animation{
	private boolean hold = true;
	private int height, width, spriteX, spriteY, tRow, tColumn, frame,sRow, selectedRow;
	private TextureRegion textureRegion;
	
	private int offsetX;
	private int offsetY;
	private float x, y;
	
	private float speed = 0.1f;
	private float time = 0;
	
	public Animation (float x, float y, float z) {
		frame = 0;
		offsetX = 0;
		offsetY = 0;
	}

	public Animation(float x, float y, float z,SpriteAsset spriteAsset) {		
		tRow = spriteAsset.getSpriteRows();
		tColumn = spriteAsset.getSpriteColumns();
		height = spriteAsset.getSpriteHeight();
		width = spriteAsset.getSpriteWidth();
		textureRegion = new TextureRegion(spriteAsset.getTexture().getTexture());
//		textureRegion = spriteAsset.getTexture().getTextureRegion();
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

	public void update(float deltaTime) {
		time+=deltaTime;
		
		if(hold){
			spriteX = 0;
			time = 0;
		} else if(time >= speed) {
	        frame++;
	        frame %= tRow-1;
	        time -= speed;
	        spriteX = frame * width;
	    }		
		textureRegion.setRegion(spriteX, spriteY, width, height);
	}

	public void setNumRows(int r) {
		tRow = r;
	}
	
	public void setNumColumns(int c) {
		this.tColumn = c;
	}
	
	public void setSpriteH(int h) {
		this.height = h;
	}
	
	public void setSpriteW(int w) {
		this.width = w;
		offsetX = (int) (width * -0.5f);
	}
	
	public void setTextureRegion(TextureRegion tr) {
		this.textureRegion = tr;
	}
	
	public void render(SpriteBatch batch) {
		batch.draw(textureRegion, x, y);
	}

	public void dispose() {
		
	}
	
	public void setXY(int x, int y) {
		this.x = x + offsetX;
		this.y = y + offsetY;
	}
}
