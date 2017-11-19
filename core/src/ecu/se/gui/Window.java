package ecu.se.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import archive.Archiver;
import archive.TimeRecords;

public abstract class Window {

	/**
	 * The name of this window.
	 * 		- Added automatically
	 */
	protected String windowName = "missing_name";
	
	/**
	 * All of the widgets that this windows owns (updates/renders).
	 */
	protected Widget[] widgets;
	
	/**
	 * parentGUI
	 */
	protected GUI gui;
	
	protected Window parent;
	
	public Window(GUI gui, Window parent) {
		this.gui = gui;
		this.parent = parent;
		initWindow();
		buildWindow();
	}
	
	public Window(GUI gui) {
		this.gui = gui;
		this.parent = null;
		initWindow();
		buildWindow();
	}
	
	/**
	 * Initial window setup required for all windows.
	 */
	private void initWindow() {
		widgets = new Widget[0];
		windowName = this.getClass().getSimpleName();
	}
	
	/**
	 * All Widgets for this window should initialized and added to widgets here.
	 */
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
		if (widgets == null) {
			System.err.println("[" + windowName + "] widgets array was null");
		}
		
		for(Widget w : widgets) {
			if (w == null) {
				System.err.println("[" + windowName + "] widget was null: ");
			}
			if (w.update(deltaTime, mouseX, mouseY))
				 mouseUsed = true;
		}
		return mouseUsed;
	}
	
	/**
	 * Renders all widgets associated with this window.
	 * @param batch
	 */
	public void render(SpriteBatch batch) {
		for(Widget w : widgets) {
			w.render(batch);
		}
	}
	
	/**
	 * Renders the debug view for this window.
	 * @param renderer
	 */
	public void debugRender(ShapeRenderer renderer) {
		for(Widget w : widgets) {
			w.debugRender(renderer);
		}
	}

	/**
	 * The action this window should perform when it loses focus.
	 * 		Should call Archiver.set with the appropriate TimeRecord
	 */
	public void onPause() {
		Archiver.set(TimeRecords.TIME_IN_MENU, false);
	}
	
	/**
	 * The action this window should perform when it gains focus.
	 * 		Should call Archiver.set with the appropriate TimeRecord
	 */
	public void onResume() {
		Archiver.set(TimeRecords.TIME_IN_MENU, false);
	}
	
}
