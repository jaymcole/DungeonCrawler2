package ecu.se.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import assetManager.AssetManager;
import assetManager.TextureAsset;
import ecu.se.GameObject;
import ecu.se.ObjectManager;

public class ItemObject extends GameObject {

	protected String name;
	protected Texture texture;
	
	
	//Item has a name and position
	public ItemObject(float x, float y, String name, String path) {
		super(x, y);
		this.name = name;
		this.texture = AssetManager.getTexture(path).getTexture();
	}

	public void update(float deltaTime) {
		
	}

	public void render(SpriteBatch batch) {
		batch.draw(texture, x, y, texture.getWidth(), texture.getHeight());
	}

	public void dispose() {
		
	}

	public String getName() {
		return name;
	}
	
}
