package ecu.se.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import assetManager.AssetManager;
import ecu.se.Utils;

public class Widget_Label extends Widget{

	protected Color backgroundColor;
	protected Texture background;
	protected float xBuffer, yBuffer;
	
	public Widget_Label(float x, float y, float width, float height, Window parent, String text, int size, Color backgroundColor, Color textColor) {
		super(x, y, width, height, parent);
		this.backgroundColor = backgroundColor;
		this.textColor = textColor;
		background = AssetManager.getTexture("texture/misc/white.png").getTexture();
		font = AssetManager.getFont("font/font_jay.ttf", size).getFont();
		xBuffer = 2;
		yBuffer = 2;
		
		glyphLayout.setText(font, text);
		this.width = glyphLayout.width;
		this.height = glyphLayout.height;
		setText(text);
		bounds = Utils.getRectangleBounds(this.x, this.y, this.width, this.height, Utils.ALIGN_BOTTOM_LEFT);

	}

	@Override
	public boolean update(float deltaTime, int mouseX, int mouseY) {
		return false;
	}

	@Override
	public void render(SpriteBatch batch) {
		if (backgroundColor != null) {
			batch.setColor(backgroundColor);
			batch.draw(background, x - xBuffer, y - yBuffer, width + xBuffer * 2, height  + yBuffer * 2);
		}
		font.setColor(textColor);
		font.draw(batch, text, textX - xBuffer, textY - yBuffer);
	}
}
