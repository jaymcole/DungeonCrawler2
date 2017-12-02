package ecu.se.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ecu.se.assetManager.AssetManager;

public class Widget_ProgressBar_Image extends Widget_ProgressBar {
	public Widget_ProgressBar_Image(float x, float y, float width, float height, Window parent, Color bgc, Color fgc, String foregroundTexture) {
		super(x, y, width, height, parent, bgc, fgc);
		texture = AssetManager.getTexture(foregroundTexture).getTextureRegion();
		
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
		texture.setRegion(texture, 0, 0, texture.getTexture().getWidth(), texture.getTexture().getHeight());
		batch.draw(texture, x, y, width, height);
		
		
		batch.setColor(foregroundColor);
		float percent = progress / (max + 0.0f);
		texture.setRegion(texture, 0, 0, (int)(texture.getTexture().getWidth() * percent), (int)(texture.getTexture().getHeight()));

		batch.draw(texture, x + borderWidth, y + borderWidth, (width - (borderWidth * 2)) * percent, height - (borderWidth * 2));
		
		
		if (useText) {
			batch.setColor(textColor);
			setText((int)progress + "/" + (int)max);
			font.setColor(textColor);
			font.draw(batch, text, textX, textY);
		}
	}
}
