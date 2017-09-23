package ecu.se.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ecu.se.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
		config.width = LwjglApplicationConfiguration.getDesktopDisplayMode().width;
		config.height = LwjglApplicationConfiguration.getDesktopDisplayMode().height;
		config.vSyncEnabled = false;
		config.foregroundFPS = 120; // Setting this to 0 == uncapped fps
		config.backgroundFPS = 5;
		config.fullscreen = false;
		new LwjglApplication(new Game(), config);
	}
}
