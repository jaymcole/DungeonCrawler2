package ecu.se.gui2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import ecu.se.Game;
import ecu.se.Logger;

public class GuiButton extends GuiLabel{

	protected int hotkey;
	protected boolean hasHotkey;
	
	public GuiButton(String text) {
		super(text);
	}
	
	@Override
	public Component updateComponent(Component consumer, float deltaTime, int mouseX, int mouseY) {
		if (hasHotkey && Gdx.input.isKeyJustPressed(hotkey)) {
			onHotkey(mouseX, mouseY);
			onHotKeyReleased(mouseX, mouseY);
		}
		specialActions(deltaTime, mouseX, mouseY);
		return consumer;
	}

	@Override
	protected void renderComponent(SpriteBatch batch) {
		if (font == null) {
			Logger.Error(getClass(), "render", "Font is null"); 
			return;
		}
		// Center Text
		font.setColor(textColor);
		setTextJustify(justify);
		font.draw(batch, text, getChildX() + xOffset, getChildY()  + yOffset);
	}
	
		
	@Override
	protected void debugRenderComponent(ShapeRenderer shapeRenderer) {
		if (isHovering)
			shapeRenderer.setColor(Color.RED);
		else
			shapeRenderer.setColor(Color.GREEN);
		Rectangle rect = getBounds();
		shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
	}
	
	public void specialActions(float deltaTime, int mouseX, int mouseY) {}
	
	public void onHotkey(int mouseX, int mouseY) {
		mousePressed(mouseX, mouseY);
	};
	
	public void onHotKeyDown(int mouseX, int mouseY) {
		mouseDown(mouseX, mouseY);
	};
	
	public void onHotKeyReleased(int mouseX, int mouseY) {
		mouseReleased(mouseX, mouseY);
	};
	
	@Override
	protected void disposeComponent() {
		// TODO Auto-generated method stub
	}
}
