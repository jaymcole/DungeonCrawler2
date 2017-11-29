package ecu.se.gui;

import com.badlogic.gdx.Gdx;
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
	
	protected int mouseOffsetX, mouseOffsetY;
	
	/**
	 * Multi-use variable to be used with new buttons.
	 */
	protected int variableOne;
	
	protected int hotkey;
	
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
	
	public void setHotkey (int key) {
		this.hotkey = key;
	}
	
	/**
	 * The action this button performs WHEN the left mouse button is pressed AND over this button.
	 */
	public void mousePressed(int mouseX, int mouseY){};
	
	/**
	 * The action this button performs while the left mouse button is down AND over this button.
	 */
	public void mouseDown(int mouseX, int mouseY){};
	
	/**
	 * The action this button performs WHEN the left mouse button is releases AND over this button.
	 */
	public void mouseReleased(int mouseX, int mouseY){};

	public void onHotkey(int mouseX, int mouseY) {
		mousePressed(mouseX, mouseY);
	};
	
	public void onHotKeyDown(int mouseX, int mouseY) {
		mouseDown(mouseX, mouseY);
	};
	
	public void onHotKeyReleased(int mouseX, int mouseY) {
		mouseReleased(mouseX, mouseY);
	};
	
//	private boolean hotkeyDown;
	
	@Override
	public boolean update(float deltaTime, int mouseX, int mouseY) {
		if (Gdx.input.isKeyJustPressed(hotkey)) {
			onHotkey(mouseX, mouseY);
			onHotKeyReleased(mouseX, mouseY);
			//			hotkeyDown = true;
		}
//		else if (Gdx.input.isKeyPressed(hotkey)) {
////			onHotKeyDown(mouseX, mouseY);
//			hotkeyDown = true;
//		}else if (!Gdx.input.isKeyPressed(hotkey) && !Gdx.input.isKeyJustPressed(hotkey) && hotkeyDown) {
//			onHotKeyReleased(mouseX, mouseY);
//			hotkeyDown = false;
//		}
		
		if (bounds.contains(mouseX, mouseY) || activeWidget) {
			highlight = true;
			
			if (Game.leftMouseState == Game.MOUSE_PRESSED) {
				mouseOffsetX = (int) (x - mouseX);
				mouseOffsetY = (int) (y - mouseY);
				mousePressed(mouseX, mouseY);
				activeWidget = true;
			}
			
//			if (!activeWidget) 
//				return true;
			
			if (Game.leftMouseState == Game.MOUSE_RELEASED) {
				mouseReleased(mouseX, mouseY);
				activeWidget = false;
				
			} else if (Game.leftMouseState == Game.MOUSE_DOWN)
				mouseDown(mouseX, mouseY);
			
			
			return true;
		} else {
			highlight = false;
		}
		
		if (Game.leftMouseState == Game.MOUSE_RELEASED) 
			activeWidget = false;
		
		specialActions(deltaTime, mouseX, mouseY);
		return false;
	}
	
	
	public void specialActions(float deltaTime, int mouseX, int mouseY) {
		
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
	
	public void setMultiVariableOne(int var) {
		variableOne = var;
	}
}