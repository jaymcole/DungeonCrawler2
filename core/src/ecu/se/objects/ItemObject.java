package ecu.se.objects;

import com.badlogic.gdx.graphics.Texture;
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
		bounds = Utils.getRectangleBounds(x, y, textureRegion.getRegionWidth(), textureRegion.getRegionHeight(), Utils.ALIGN_BOTTOM_LEFT);
	}
	
	public ItemObject(float x, float y, String name, TextureRegion textureRegion) {
		super(x, y);
		this.name = name;
		this.textureRegion = textureRegion;
		bounds = Utils.getRectangleBounds(x, y, textureRegion.getRegionWidth(), textureRegion.getRegionHeight(), Utils.ALIGN_BOTTOM_LEFT);
	}

	public void update(float deltaTime) {
		
	}

	public void render(SpriteBatch batch) {
		batch.draw(textureRegion, x, y, textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
	}

	public void dispose() {
		
	}

	public String getName() {
		return name;
	}
	
}
