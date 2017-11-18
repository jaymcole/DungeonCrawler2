package ecu.se.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import assetManager.AssetManager;

public class Widget_Image extends Widget{

	private Texture texture;
	
	public Widget_Image(float x, float y, float width, float height, Window parent, String texturePath) {
		super(x, y, width, height, parent);
		texture = AssetManager.getTexture(texturePath).getTexture();
	}

	@Override
	public boolean update(float deltaTime, int mouseX, int mouseY) {
		return false;
	}

	@Override
	public void render(SpriteBatch batch) {
//		batch.setBlendFunction( GL20.GL_SRC_ALPHA,  GL20.GL_SRC_ALPHA);
		batch.setColor(Color.WHITE);
		batch.draw(texture, x, y, width, height);
	}

}
