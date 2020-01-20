package ecu.se.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

import ecu.se.Game;
import ecu.se.Logger;
import ecu.se.actors.Player;
import ecu.se.gui2.Component;
import ecu.se.gui2.Container;
import ecu.se.gui2.GuiItemSlot;
import ecu.se.gui2.GuiLabel;
import ecu.se.gui2.Windows;
import ecu.se.objects.ActiveItem;

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
	public static final int WINDOW_GAME_STATS = 7;
	public static final int WINDOW_COMPONENT_TEST = 8;
	public static int currentWindow;

	/**
	 * The player - Used by widgets to update themselves and perform actions.
	 */
	public Player player;

	/**
	 * All windows used
	 */
	private static Container[] windows;

	public static Game game;
	
	public static Container tooltipContainer;
	public static GuiLabel tooltip;
	public static boolean RenderToolTip;

	public GUI(Player player, int screenWidth, int screenHeight, Game game) {
		GUI.game = game;
		this.player = player;
		halfWidth = (int) (screenWidth * 0.5);
		conversionX = screenWidth / defaultWidth;
		halfHeight = (int) (screenHeight * 0.5);
		conversionY = screenHeight / defaultHeight;

		hudCamera = new OrthographicCamera(screenWidth, screenHeight);
		
		windows = new Container[] {
				Windows.CreatePauseWindow(this), 
				Windows.CreateHUD(this), 
				Windows.CreateMainMenu(this), 
				Windows.CreateSettings(this), 
				Windows.CreateInventory(this), 
				Windows.CreatePlayerStats(this), 
				Windows.CreateGameOver(this), 
				Windows.CreateGameStats(this),
				Windows.CreateComponentTest(this)
		};
		
		setWindow(WINDOW_MAIN_MENU);
		Windows.createTooltip(this);
	}
	
	
	public static GuiItemSlot primaryItemSlot;
	public static GuiItemSlot secondaryItemSlot;
	public void setPrimaryItem(ActiveItem item) {
		primaryItemSlot.setItem(item);
	}
	
	public void setSecondaryItem(ActiveItem item) {
		secondaryItemSlot.setItem(item);
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
	Component consumer;

	
	public static boolean renderGui2Test = false;
	public static Container test;
	
	/**
	 * Updates the active windows.
	 * 
	 * @param deltaTime
	 */
	public void update(float deltaTime) {
		RenderToolTip = false;
		Vector3 mouse = hudCamera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		mouseX = (int) mouse.x;
		mouseY = (int) mouse.y;

		inputUsed = false;
		consumer = null;

		consumer = windows[currentWindow].update(consumer, deltaTime, mouseX, mouseY);
		if (consumer != null) {
			inputUsed = true;
			processInput(consumer, mouseX, mouseY);

		}
		
		if (RenderToolTip) {
			tooltipContainer.pack(mouseX, mouseY);
		}			
	}
	
	private void processInput(Component consumer, int mouseX, int mouseY) {
		if (Game.leftMouseState == Game.MOUSE_PRESSED) {
			consumer.mousePressed(mouseX, mouseY);
		} else if (Game.leftMouseState == Game.MOUSE_RELEASED) {
			consumer.mouseReleased(mouseX, mouseY);
		} else if (Game.leftMouseState == Game.MOUSE_DOWN) {
			consumer.mouseDown(mouseX, mouseY);
		}
	}
	
	
	

	/**
	 * Renders the appropriate window(s). May render more than one at a time.
	 * Example: The HUD should render behind the Player's inventory.
	 * 
	 * @param batch
	 */
	public void render(SpriteBatch batch) {		
		batch.setProjectionMatrix(hudCamera.projection);
//		if (Game.currentState == Game.GAME_STATE_PAUSED)
//			windows[WINDOW_PAUSED].render(batch);

//		windows[WINDOW_HUD].render(batch);
//
		if (currentWindow != WINDOW_HUD)
			windows[currentWindow].render(batch);

		batch.setColor(Color.WHITE);
		
		if (RenderToolTip)
			tooltipContainer.render(batch);
	}


	
	GuiLabel label = new GuiLabel("TEST LABEL");
	public static int density = 45;
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

//		if (Game.currentState == Game.GAME_STATE_PAUSED)
//			windows[WINDOW_PAUSED].debugRender(renderer);
		
		
//		renderer.line(-Gdx.graphics.getWidth(), -Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//		test.debugRender(renderer);
//		renderer.setColor(Color.MAGENTA);
//		renderer.circle(0, 0, 50);
//		renderer.line(0, 0, test.getChildX(), test.getChildY());
		if (RenderToolTip)
			tooltipContainer.debugRender(renderer);
//		}
		
		
		
//		renderGrid(renderer);
		
		
		renderer.setColor(Color.CYAN);
//		float testX = -test.getWidth() * .5f;
//		float testY = -test.getHeight() * .5f;
//		renderer.line(offsetX, offsetY, testX, testY);
		
	}
	
//	private void renderGrid(ShapeRenderer renderer) {
//		float hori = Gdx.graphics.getWidth() / density;
//		float vert = Gdx.graphics.getHeight() / density;
//		float offsetX = -Gdx.graphics.getWidth() * 0.5f;
//		float offsetY = -Gdx.graphics.getHeight() * 0.5f;
//		renderer.setColor(Color.GOLD);
//		for (int i = 0; i < hori; i++) {
//			renderer.line(i * density + offsetX, offsetY, i * density + offsetX, Gdx.graphics.getHeight());
//		}
//		
//		for (int i = 0; i < vert; i++) {
//			renderer.line(offsetX, i * density + offsetY, Gdx.graphics.getWidth() +offsetX, i * density + offsetY);
//		}
//	}
	

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

		Logger.Debug(GUI.class, "setWindow","Switch window from " + currentWindow + " to " + changeTo);

		if (changeTo == currentWindow) {
			return;
		}

		windows[changeTo].onResume();
		//if (windows[currentWindow] != null)
			windows[currentWindow].onPause();
		currentWindow = changeTo;
	}

	/**
	 * 
	 * @param windowIndex
	 * @return The window corresponding to windowIndex.
	 */
	public static Container getWindow(int windowIndex) {
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
	public static Container[] getActiveWindows() {
		if (currentWindow == WINDOW_HUD)
			return new Container[] { windows[WINDOW_HUD] };
		return new Container[] { windows[WINDOW_HUD], windows[currentWindow] };
	}

}
