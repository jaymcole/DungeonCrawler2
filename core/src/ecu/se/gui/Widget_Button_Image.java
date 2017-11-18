package ecu.se.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Widget_Button_Image extends Widget_Button{

	protected TextureRegion defaultTexture;
	protected TextureRegion highlightTexture;
	protected TextureRegion activeTexture;
	
	public Widget_Button_Image(float x, float y, float width, float height, Window parent, TextureRegion defaultTexture, TextureRegion highlightTexture, TextureRegion activeTexture) {
		super(x, y, width, height, parent);
		this.defaultTexture = defaultTexture;
		this.highlightTexture = highlightTexture;
		this.activeTexture = activeTexture;
		
		defaultColor = Color.WHITE;
	}
	
	@Override
	public void render(SpriteBatch batch) {
		batch.setColor(defaultColor);
	
		batch.draw(defaultTexture, x, y, width, height);

		if (highlight) {
			batch.setColor(highlightColor);
			batch.draw(highlightTexture, x, y, width, height);
		}
		
		if (activeWidget) {
			batch.setColor(activeColor);
			batch.draw(activeTexture, x, y, width, height);
		}
		
		if (useText) {
			font.setColor(textColor);
			font.draw(batch, text, textX, textY);
		}
	}
	

}
