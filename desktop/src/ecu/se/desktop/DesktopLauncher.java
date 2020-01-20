package ecu.se.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ecu.se.Game;
/**
 * 
 * Launches the game on desktop.
 */
public class DesktopLauncher {
	public static void main (String[] arg) {
		System.err.println("DesktopLauncher");
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
		config.width = LwjglApplicationConfiguration.getDesktopDisplayMode().width;
		config.height = LwjglApplicationConfiguration.getDesktopDisplayMode().height;
		config.vSyncEnabled = false;
		config.foregroundFPS = 120;
		config.backgroundFPS = 120;
		config.fullscreen = false;
		LwjglApplicationConfiguration.disableAudio=false;
		config.title = "Dungeon Crawler 2017";
		new LwjglApplication(new Game(), config);
//		new LwjglApplication(new GpuShadows(), config);
		
		
	}
}