package ecu.se.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class Window {

	
	protected Widget[] widgets;
	
	public Window() {
		widgets = new Widget[0];
		buildWindow();
	}
	
	protected abstract void buildWindow();
	
	/**
	 * Updates all widgets in this window.
	 * @param deltaTime - The time between frames.
	 * @param mouseX - mouse cursor x coordinate.
	 * @param mouseY - mouse cursor y coordinate.
	 * @return True if a widget uses the mouse input. 
	 */
	public boolean update(float deltaTime, int mouseX, int mouseY) {
		boolean mouseUsed = false;
		for(Widget w : widgets) {
			if (w.update(deltaTime, mouseX, mouseY))
				 mouseUsed = true;
		}
		return mouseUsed;
	}
	
	/**
	 * Renders all widgets associated with this window
	 * @param batch
	 */
	public void render(SpriteBatch batch) {
		for(Widget w : widgets) {
			w.render(batch);
		}
	}
	
	public void debugRender(ShapeRenderer renderer) {
		for(Widget w : widgets) {
			w.debugRender(renderer);
		}
	}
}
