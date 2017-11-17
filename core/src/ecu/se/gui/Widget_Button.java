package ecu.se.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import assetManager.AssetManager;
import ecu.se.Game;

public abstract class Widget_Button extends Widget{
	
	protected Color defaultColor;
	protected Color highlightColor;
	protected Color activeColor;
	protected Color currentColor;
	protected Texture texture;
	protected Window parent;
	protected boolean activeWidget;

	public Widget_Button(float x, float y, float width, float height, Window parent, String text) {
		super(x, y, width, height, parent);
		texture = AssetManager.getTexture("texture/misc/white.png").getTexture();
		setText(text);
		activeWidget = false;
		defaultColor = Color.MAGENTA;
		highlightColor = Color.BROWN;
		activeColor = Color.SKY;
	}
	
	public Widget_Button(float x, float y, float width, float height, Window parent) {
		super(x, y, width, height, parent);
		texture = AssetManager.getTexture("texture/misc/white.png").getTexture();
		
		defaultColor = Color.MAGENTA;
		highlightColor = Color.BROWN;
		activeColor = Color.SKY;
	}
	
	public void mousePressed(){};
	public void mouseDown(){};
	public void mouseReleased(){};

	@Override
	public boolean update(float deltaTime, int mouseX, int mouseY) {
		if (bounds.contains(mouseX, mouseY)) {
			currentColor = highlightColor;
			
			if (Game.leftMouseState == Game.MOUSE_PRESSED) {
				mousePressed();
				activeWidget = true;
			} 
			
			if (!activeWidget)
				return true;
			
			if (Game.leftMouseState == Game.MOUSE_RELEASED) {
				mouseReleased();
				activeWidget = false;
			} else if (Game.leftMouseState == Game.MOUSE_DOWN)
				mouseDown();
			return true;
		} else {
			currentColor = defaultColor;
		}
		return false;
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.setColor(currentColor);
		batch.draw(texture, x, y, width, height);
		if (useText) {
			font.setColor(textColor);
			font.draw(batch, text, textX, textY);
		}
	}	
}