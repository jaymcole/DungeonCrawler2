package ecu.se.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ecu.se.assetManager.AssetManager;

public class Widget_ProgressBar_Image extends Widget_ProgressBar {
	
	private TextureRegion foregroundTexture;
	private TextureRegion backgroundTexture;

	public Widget_ProgressBar_Image(float x, float y, float width, float height, Window parent, Color bgc, Color fgc, String backgroundTexture, TextureBehavior backgroundBehavior, String foregroundTexture, TextureBehavior foregroundBehavior) {
		super(x, y, width, height, parent, bgc, fgc);
		this.foregroundTexture = AssetManager.getTexture(foregroundTexture).getTextureRegion();
		this.backgroundTexture = AssetManager.getTexture(backgroundTexture).getTextureRegion();
		useText = true;
		
		backgroundColor = bgc;
		foregroundColor = fgc;
		
		progress = 15;
		max = 100;
	}

	@Override
	public boolean update(float deltaTime, int mouseX, int mouseY) {
		updateBar();
		return false;
	}
	
	public void updateBar(){}

	@Override
	public void render(SpriteBatch batch) {
		
		
		batch.setColor(backgroundColor);
		backgroundTexture.setRegion(backgroundTexture, 0, 0, backgroundTexture.getTexture().getWidth(), backgroundTexture.getTexture().getHeight());
		batch.draw(backgroundTexture, x, y, width, height);
		
		
		batch.setColor(foregroundColor);
		float percent = progress / (max + 0.0f);
		foregroundTexture.setRegion(foregroundTexture, 0, 0, (int)(foregroundTexture.getTexture().getWidth() * percent), (int)(foregroundTexture.getTexture().getHeight()));
		batch.draw(foregroundTexture, x + borderWidth, y + borderWidth, (width - (borderWidth * 2)) * percent, height - (borderWidth * 2));
		
		
		if (useText) {
			batch.setColor(textColor);
			setText((int)progress + "/" + (int)max);
			font.setColor(textColor);
			font.draw(batch, text, textX, textY);
		}
	}
}
