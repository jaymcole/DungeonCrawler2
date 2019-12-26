package ecu.se.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import ecu.se.Logger;
import ecu.se.archive.Archiver;
import ecu.se.archive.TimeRecords;

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
	
	protected boolean useBackground;
	
	protected Widget_Image background;
	
	
	public Window(GUI gui, Window parent) {
		this.gui = gui;
		this.parent = parent;
		useBackground = false;
		initWindow();
		buildWindow();
	}
	
	public Window(GUI gui) {
		this.gui = gui;
		this.parent = null;
		useBackground = false;
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
		act(deltaTime);
		boolean mouseUsed = false;
		if (widgets == null) {
			System.err.println("[" + windowName + "] widgets array was null");
		}
		
		for(Widget w : widgets) {
			if (w == null) {
				System.err.println("[" + windowName + "] widget was null: ");
			}
			if (w != null && w.update(deltaTime, mouseX, mouseY))
				 mouseUsed = true;
		}
		return mouseUsed;
	}
	
	public void act(float deltaTime) {};
	
	/**
	 * Renders all widgets associated with this window.
	 * @param batch
	 */
	public void render(SpriteBatch batch) {
		if (useBackground)
			background.render(batch);
		
		for(Widget w : widgets) {
			if (w != null)
				w.render(batch);
		}
	}
	
	/**
	 * Renders the debug view for this window.
	 * @param renderer
	 */
	public void debugRender(ShapeRenderer renderer) {
		if (useBackground)
			background.debugRender(renderer);
		
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
	
	public void setBackground(Widget_Image background) {
		this.background = background;
		if (background != null)
			useBackground = true;
	}
	
	public void toggleBackground() {
		this.useBackground ^= true;
		Logger.Debug(this.getClass(), "toggleBackground","Toggle background");
		if (this.background == null) {
			this.useBackground = false;
			System.err.println("[FAILED] " + windowName + " does not have a background to display." );
		}
	}
	
	/**
	 * 
	 * @return All child widgets of this Window.
	 */
	public Widget[] getChildren() {
		return widgets;
	}
	
}
