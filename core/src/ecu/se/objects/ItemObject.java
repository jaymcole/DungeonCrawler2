package ecu.se.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import assetManager.AssetManager;
import ecu.se.GameObject;
import ecu.se.Utils;

public class ItemObject extends GameObject {

	protected String name;
	protected TextureRegion textureRegion;

	
	
	//Item has a name and position
	public ItemObject(float x, float y, String name, String path) {
		super(x, y);
		this.name = name;
		this.textureRegion = AssetManager.getTexture(path).getTextureRegion();
		this.width = textureRegion.getRegionWidth();
		this.height = textureRegion.getRegionHeight();
		bounds = Utils.getRectangleBounds(x, y, width, height, Utils.ALIGN_BOTTOM_LEFT);
//		bounds = Utils.getRectangleBounds(x - width*0.5f, y- height*0.5f, width, height, Utils.ALIGN_CENTERED);

	}
	
	public ItemObject(float x, float y, String name, TextureRegion textureRegion) {
		super(x, y);
		this.name = name;
		this.textureRegion = textureRegion;
		this.width = textureRegion.getRegionWidth();
		this.height = textureRegion.getRegionHeight();
		bounds = Utils.getRectangleBounds(x, y, width, height, Utils.ALIGN_BOTTOM_LEFT);
//		bounds = Utils.getRectangleBounds(x - width*0.5f, y- height*0.5f, width, height, Utils.ALIGN_CENTERED);

	}
	
	public ItemObject(float x, float y, float width, float height, String name, String path) {
		super(x, y);
		this.name = name;
		this.textureRegion = AssetManager.getTexture(path).getTextureRegion();
		this.width = width;
		this.height = height;
		bounds = Utils.getRectangleBounds(x - width*0.5f, y - height*0.5f, width, height, Utils.ALIGN_CENTERED);

	}
	
	public ItemObject(float x, float y, float width, float height, String name, TextureRegion textureRegion) {
		super(x, y);
		this.name = name;
		this.textureRegion = textureRegion;
		this.width = width;
		this.height = height;
		bounds = Utils.getRectangleBounds(x - width*0.5f, y - height*0.5f, width, height, Utils.ALIGN_CENTERED);
	}
	
	public void update(float deltaTime) {
//		bounds = Utils.getRectangleBounds(x - width*0.5f, y - height*0.5f, width, height, Utils.ALIGN_CENTERED);
	}

	public void render(SpriteBatch batch) {
		batch.setColor(Color.WHITE);
		batch.draw(textureRegion, x - width*0.5f, y - height*0.5f, width, height);
		
//		batch.setColor(Color.MAGENTA);
//		batch.draw(textureRegion, x - width, y - height, width, height);
//		batch.draw(textureRegion, x, y, width, height);
	}

	public void dispose() {
		
	}

	public String getName() {
		return name;
	}
	
	
	public TextureRegion getTextureRegion() {
		return textureRegion;
	}
}
