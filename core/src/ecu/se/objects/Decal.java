package ecu.se.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ecu.se.Utils;

public class Decal extends ItemObject{

	float width, height;
	
	public Decal(float x, float y, String name, TextureRegion textureRegion) {
		super(x, y, name, textureRegion);
		this.width = textureRegion.getRegionWidth();
		this.height = textureRegion.getRegionHeight();
		this.x -= width * 0.5f;
		this.y -= height * 0.5f;
		bounds = Utils.getRectangleBounds(this.x, this.y, width, height, Utils.ALIGN_BOTTOM_LEFT);
	}
	
	public Decal(float x, float y, String name, TextureRegion textureRegion, float width, float height) {
		super(x, y, name, textureRegion);
		this.width = width;
		this.height = height;
		bounds = Utils.getRectangleBounds(x, y, width, height, Utils.ALIGN_BOTTOM_LEFT);
	}
	
	public void render(SpriteBatch batch) {
		batch.draw(textureRegion, x, y, width, height);
	}

}
