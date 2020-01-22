package ecu.se.gui2;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import ecu.se.Logger;
import ecu.se.Utils;

public class GuiButton extends GuiLabel{

	protected int hotkey;
	protected boolean hasHotkey;
	
	public GuiButton(String text) {
		super(text);
	}
	
	@Override
	public Component updateComponent(Component consumer, float deltaTime, int mouseX, int mouseY) {
//		Logger.Debug(getClass(), "updateComponent", "Updating: " + getClass().getSimpleName());
//		if (hasHotkey && Gdx.input.isKeyJustPressed(hotkey)) {
//			onHotkey(mouseX, mouseY);
//			onHotKeyReleased(mouseX, mouseY);
//		}
//		specialActions(deltaTime, mouseX, mouseY);
		return consumer;
	}

	@Override
	protected void renderComponent(SpriteBatch batch) {
		if (font == null) {
			Logger.Error(getClass(), "render", "Font is null"); 
			return;
		}
		if (Utils.NullOrEmpty(text))
			return;
		setTextJustify(justify);
		font.setColor(textColor);
		font.draw(batch, text, getContentBounds().x + xOffset, getContentBounds().y  + yOffset);
	}
	
		
	@Override
	protected void renderDebugComponent(ShapeRenderer shapeRenderer) {
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
}
