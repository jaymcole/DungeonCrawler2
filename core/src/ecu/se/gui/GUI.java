package ecu.se.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

import ecu.se.Game;
import ecu.se.actors.Player;

public class GUI {

	/**
	 * The Orthographic Camera used to render windows.
	 */
	private OrthographicCamera hudCamera;

	/**
	 * Variables used to convert between varying display resolutions.
	 */
	public static final float defaultWidth = 1920.0f;
	public static final float defaultHeight = 1080.0f;
	private static int halfWidth, halfHeight;
	public static float conversionX;
	public static float conversionY;

	/**
	 * Windows index location used in windows Used for rending and switching to
	 * the correct window.
	 */
	public static final int WINDOW_PAUSED = 0;
	public static final int WINDOW_HUD = 1;
	public static final int WINDOW_MAIN_MENU = 2;
	public static final int WINDOW_SETTINGS = 3;
	public static final int WINDOW_INVENTORY = 4;
	public static final int WINDOW_PLAYER_STATS = 5;
	public static final int WINDOW_GAME_OVER = 6;
	public static int currentWindow;

	/**
	 * The player - Used by widgets to update themselves and perform actions.
	 */
	public Player player;

	/**
	 * All windows used
	 */
	private static Window[] windows;

	public static Game game;

	public GUI(Player player, int screenWidth, int screenHeight, Game game) {
		GUI.game = game;
		this.player = player;
		halfWidth = (int) (screenWidth * 0.5);
		conversionX = screenWidth / defaultWidth;
		halfHeight = (int) (screenHeight * 0.5);
		conversionY = screenHeight / defaultHeight;

		hudCamera = new OrthographicCamera(screenWidth, screenHeight);

		windows = new Window[] { new Window_PauseScreen(this), new Window_HUD(this), new Window_MainMenu(this),
				new Window_Settings(this), new Window_Inventory(this), new Window_PlayerStats(this), new Window_DeathScreen(this) };
		setWindow(WINDOW_MAIN_MENU);
	}

	/**
	 * Mouse coordinates corrected for the screen position
	 */
	private int mouseX;
	private int mouseY;

	/**
	 * True if a widget used to mouse input
	 */
	boolean inputUsed;

	/**
	 * Updates the active windows.
	 * 
	 * @param deltaTime
	 */
	public void update(float deltaTime) {
		Vector3 mouse = hudCamera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		mouseX = (int) mouse.x;
		mouseY = (int) mouse.y;
		inputUsed = false;

		// Updates the currently displayed window.
		inputUsed = windows[currentWindow].update(deltaTime, mouseX, mouseY);

		if (Game.currentState == Game.GAME_STATE_PAUSED)
			windows[WINDOW_PAUSED].update(deltaTime, mouseX, mouseY);

		// Updates the HUD IF it wasn't already updated AND
		if (currentWindow != WINDOW_HUD)
			if (windows[WINDOW_HUD].update(deltaTime, mouseX, mouseY) || inputUsed)
				inputUsed = true;
	}

	/**
	 * Renders the appropriate window(s). May render more than one at a time.
	 * Example: The HUD should render behind the Player's inventory.
	 * 
	 * @param batch
	 */
	public void render(SpriteBatch batch) {
		batch.setProjectionMatrix(hudCamera.projection);

		if (Game.currentState == Game.GAME_STATE_PAUSED)
			windows[WINDOW_PAUSED].render(batch);

		windows[WINDOW_HUD].render(batch);

		if (currentWindow != WINDOW_HUD)
			windows[currentWindow].render(batch);

		batch.setColor(Color.WHITE);
	}

	/**
	 * Renders the debug view. i.e., widget bounds + widget (x,y)
	 * 
	 * @param renderer
	 */
	public void debugRender(ShapeRenderer renderer) {
		renderer.setColor(Color.GOLDENROD);
		renderer.setProjectionMatrix(hudCamera.projection);
		if (windows[currentWindow] != null) {
			windows[currentWindow].debugRender(renderer);
		}

		if (Game.currentState == Game.GAME_STATE_PAUSED)
			windows[WINDOW_PAUSED].debugRender(renderer);
	}

	/**
	 * @param x
	 * @return A corrected x coordinate for the current screen resolution.
	 */
	public static int convertX(int x) {
		return (int) (x * conversionX);
	}

	/**
	 * @param y
	 * @return A corrected y coordinate for the current screen resolution.
	 */
	public static int convertY(int y) {
		return (int) (y * conversionY);
	}

	/**
	 * @param x
	 * @return A corrected x coordinate for the current screen resolution.
	 */
	public static float convertX(float x) {
		return (int) (x * conversionX);
	}

	/**
	 * @param y
	 * @return A corrected y coordinate for the current screen resolution.
	 */
	public static float convertY(float y) {
		return (int) (y * conversionY);
	}

	/**
	 * @param x
	 * @return A corrected x coordinate for the current screen resolution IN
	 *         screen coordinates. Note: (0,0) in screen coordinates is the
	 *         middle of the screen.
	 */
	public static int getProportionalX(int x) {
		return convertX(x) - halfWidth;
	}

	/**
	 * @param y
	 * @return A corrected y coordinate for the current screen resolution IN
	 *         screen coordinates. Note: (0,0) in screen coordinates is the
	 *         middle of the screen.
	 */
	public static int getProportionalY(int y) {
		return convertY(y) - halfHeight;
	}

	/**
	 * @param x
	 * @return A corrected x coordinate for the current screen resolution IN
	 *         screen coordinates. Note: (0,0) in screen coordinates is the
	 *         middle of the screen.
	 */
	public static float getProportionalX(float x) {
		return convertX(x) - halfWidth;
	}

	/**
	 * @param y
	 * @return A corrected y coordinate for the current screen resolution IN
	 *         screen coordinates. Note: (0,0) in screen coordinates is the
	 *         middle of the screen.
	 */
	public static float getProportionalY(float y) {
		return convertY(y) - halfHeight;
	}

	/**
	 * 
	 * @return The Orthographic Camera used by the GUI
	 */
	public OrthographicCamera getCamera() {
		return hudCamera;
	}

	/**
	 * Determines whether Game should block input to the player. Example: If a
	 * button is pressed, the player's character should not perform an action
	 * based on that input.
	 * 
	 * @return true if a widget used the mouse action
	 */
	public boolean mouseUsed() {
		return inputUsed;
	}

	/**
	 * Sets the current window.
	 * 
	 * @param changeTo
	 *            - The window to switch to. window should be one the
	 *            GUI.WINDOW_* integers. Warning: Does not check if window is
	 *            valid.
	 */
	public static void setWindow(int changeTo) {
		if (changeTo < 0 || changeTo >= windows.length) {
			System.err.println("An invalid window integer was passed to setWindow() in GUI.java.");
			System.err.println("Int passed: " + changeTo);
			return;
		}

		System.out.println("Switch window from " + currentWindow + " to " + changeTo);

		if (changeTo == currentWindow) {
			return;
		}

		windows[changeTo].onResume();
		windows[currentWindow].onPause();
		currentWindow = changeTo;
	}

	/**
	 * 
	 * @param windowIndex
	 * @return The window corresponding to windowIndex.
	 */
	public static Window getWindow(int windowIndex) {
		if (windowIndex < windows.length && windows != null)
			return windows[windowIndex];
		return null;
	}

	/**
	 * Closes the top most window OR sets currentWindow to window if no other
	 * windows are open. Example: If WINDOW_SETTINGS is open, calling this
	 * method closes it.
	 * 
	 * @param window
	 */
	public void closeWindow(int closeTo, int openTo) {
		if (currentWindow == WINDOW_HUD) {
			setWindow(openTo);
		} else {
			setWindow(closeTo);
		}
	}

	/**
	 * 
	 * @return the core game object.
	 */
	public Game getGame() {
		return game;
	}

	/**
	 * 
	 * @return All visible windows Note: Window_HUD is always returned last.
	 */
	public static Window[] getActiveWindows() {
		if (currentWindow == WINDOW_HUD)
			return new Window[] { windows[WINDOW_HUD] };
		return new Window[] { windows[WINDOW_HUD], windows[currentWindow] };
	}

}
