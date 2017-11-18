package ecu.se.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import assetManager.AssetManager;
import ecu.se.Game;

public abstract class Widget_Button extends Widget{	
	/**
	 * The color/tint to use when button is highlighted
	 */
	protected Color highlightColor;
	
	/**
	 * The color/tint to use when button is active
	 */
	protected Color activeColor;
	
	/**
	 * The texture render this button with
	 */
	protected Texture texture;
	
	/**
	 * True when the mouse is over this button AND clicking
	 */
	protected boolean activeWidget;
	
	/**
	 * True when the mouse is over this button
	 */
	protected boolean highlight;
	

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
	
	/**
	 * The action this button performs WHEN the left mouse button is pressed AND over this button.
	 */
	public void mousePressed(){};
	
	/**
	 * The action this button performs while the left mouse button is down AND over this button.
	 */
	public void mouseDown(){};
	
	/**
	 * The action this button performs WHEN the left mouse button is releases AND over this button.
	 */
	public void mouseReleased(){};

	@Override
	public boolean update(float deltaTime, int mouseX, int mouseY) {
		
		if (bounds.contains(mouseX, mouseY)) {
			highlight = true;
			
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
			highlight = false;
		}
		
		if (Game.leftMouseState == Game.MOUSE_RELEASED) 
			activeWidget = false;
		
		return false;
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.setColor(defaultColor);
	
		if (highlight)
			batch.setColor(highlightColor);
		
		if (activeWidget)
			batch.setColor(activeColor);
		
		batch.draw(texture, x, y, width, height);
		
		if (useText) {
			font.setColor(textColor);
			font.draw(batch, text, textX, textY);
		}
	}	
	

	
	/**
	 * Sets activeColor to c
	 * @param c
	 */
	public void setActiveColor(Color c) {
		activeColor= c;
	}

	/**
	 * Sets highlightColor to c
	 * @param c
	 */
	public void setHighlightColor(Color c) {
		highlightColor = c;
	}
}