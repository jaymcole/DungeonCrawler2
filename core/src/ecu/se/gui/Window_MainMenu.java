package ecu.se.gui;

import com.badlogic.gdx.graphics.Color;

import ecu.se.Game;

public class Window_MainMenu extends Window{

	public Window_MainMenu(GUI gui) {
		super(gui);
	}

	@Override
	protected void buildWindow() {
		int halfwayX = (int)(GUI.defaultWidth * 0.5f);
		int halfwayY = (int)(GUI.defaultHeight * 0.5f);
		int backgroundX = (int)(halfwayX * 0.5f);
		int backgroundY = (int)(halfwayY * 0.5f);
		int bufferX = 50;
		int bufferY = 50;
		
		
		Widget_Image img_background = new Widget_Image(backgroundX, backgroundY, halfwayX, halfwayY, this, "texture/misc/white.png");
		img_background.setDefaultColor(new Color(Color.CHARTREUSE.r,Color.CHARTREUSE.g,Color.CHARTREUSE.b,0.5f));
		
		Widget_Label lbl_title = new Widget_Label(backgroundX + bufferX, backgroundY + halfwayY - 25, 0, 0, parent, this.windowName, 50, Color.CLEAR, Color.BLACK);
		
		Widget_Button btn_play = new Widget_Button(backgroundX + bufferX, backgroundY + halfwayY - 100, halfwayX - bufferX * 2, 50, this, "Play") {
			public void mouseReleased() {
				gui.setWindow(GUI.WINDOW_HUD);
			}
		};
		
		Widget_Button btn_newGame = new Widget_Button(backgroundX + bufferX, backgroundY + halfwayY - 200, halfwayX - bufferX * 2, 50, this, "New Game") {
			public void mouseReleased() {
				gui.getGame().create();
			}
		};
		
		Widget_Button btn_settings = new Widget_Button(backgroundX + bufferX, backgroundY + halfwayY - 300, halfwayX - bufferX * 2, 50, this, "Settings") {
			@Override
			public void mouseReleased() {
				gui.setWindow(GUI.WINDOW_SETTINGS);
			}
		};
		
		Widget_Button btn_exit = new Widget_Button(backgroundX + bufferX, backgroundY + halfwayY - 400, halfwayX - bufferX * 2, 50, this, "Exit") {
			@Override
			public void mouseReleased() {
				Game.currentState = Game.GAME_STATE_EXITING;
			}
		};
		
		
		widgets = new Widget[]{
				img_background,
				lbl_title,
				btn_play,
				btn_newGame,
				btn_settings,
				btn_exit
						
		};
	}

}
