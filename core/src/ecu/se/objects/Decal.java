package ecu.se.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ecu.se.Utils;

public class Decal extends ItemObject{

	private float width, height;
	private float rotation;
	private Color color;
	private float alpha;
	
	public Decal(float x, float y, String name, TextureRegion textureRegion) {
		super(x, y, name, textureRegion);
		this.width = textureRegion.getRegionWidth();
		this.height = textureRegion.getRegionHeight();
		this.x -= width * 0.5f;
		this.y -= height * 0.5f;
		rotation = 0;
		alpha = 1.0f;
		color = Color.WHITE;
		bounds = Utils.getRectangleBounds(this.x, this.y, width, height, Utils.ALIGN_BOTTOM_LEFT);
	}
	
	public Decal(float x, float y, String name, TextureRegion textureRegion, float width, float height) {
		super(x, y, name, textureRegion);
		this.width = width;
		this.height = height;
		rotation = 0;
		color = Color.WHITE;
		alpha = 1.0f;
		bounds = Utils.getRectangleBounds(x, y, width, height, Utils.ALIGN_BOTTOM_LEFT);
	}
	
	public void render(SpriteBatch batch) {
		batch.setColor(color.r,color.g,color.b, alpha);
		batch.draw(textureRegion, x, y, width / 2, height / 2, width, height, 1f, 1f, rotation);
	}
	
	public void setRotation(float degrees) {
		rotation = degrees;
	}
	
	public void setAlpha(float a) {
		alpha = a;
	}
	
	public void setColor(Color c) {
		color = c;
	}

}
