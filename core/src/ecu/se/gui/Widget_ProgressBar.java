package ecu.se.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import assetManager.AssetManager;

public class Widget_ProgressBar extends Widget {
	
	protected float progress;
	protected float max;
	
	protected Color backgroundColor;
	protected Color foregroundColor;
	
	
	protected static final int borderWidth = GUI.convertX(2);
	
//	protected Texture backgroundTexture;
//	protected Texture foregroundTexture;
	protected Texture texture;
	
	public Widget_ProgressBar(float x, float y, float width, float height, Window parent) {
		super(x, y, width, height, parent);
		texture = AssetManager.getTexture("texture/misc/white.png").getTexture();
		
		useText = true;
		
		backgroundColor = Color.MAGENTA;
		foregroundColor = Color.SKY;
		
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
		batch.draw(texture, x, y, width, height);
		
		batch.setColor(foregroundColor);
		float percent = progress / (max + 0.0f);
		batch.draw(texture, x + borderWidth, y + borderWidth, (width - (borderWidth * 2)) * percent, height - (borderWidth * 2));
		
		if (useText) {
			batch.setColor(textColor);
			setText((int)progress + "/" + (int)max);
			font.setColor(textColor);
			font.draw(batch, text, textX, textY);
		}
	}
	
	public void setProgress(float progress) {
		this.progress = progress;
	}
	
	public void setMax(float max) {
		this.max = max;
	}
	
	public void setDisplayText(boolean displayText) {
		this.useText = displayText;
	}

}
