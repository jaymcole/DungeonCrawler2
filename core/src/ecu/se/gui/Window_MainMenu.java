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
		int bufferX = 90;
		
		
		Widget_Image img_background = new Widget_Image(backgroundX, backgroundY, halfwayX, halfwayY, this, "texture/misc/paper.jpg");
		img_background.setDefaultColor(Color.WHITE);
		
		Widget_Label lbl_title = new Widget_Label(backgroundX + bufferX, backgroundY + halfwayY - 25, 0, 0, parent, this.windowName, 50, Color.CLEAR, Color.BLACK);
		
		Widget_Button btn_play = new Widget_Button(backgroundX + bufferX, backgroundY + halfwayY - 100, halfwayX - bufferX * 2, 50, this, "Play") {
			@Override
			public void mouseReleased(int mouseX, int mouseY) {
				GUI.setWindow(GUI.WINDOW_HUD);
			}
		};
		
		Widget_Button btn_newGame = new Widget_Button(backgroundX + bufferX, backgroundY + halfwayY - 200, halfwayX - bufferX * 2, 50, this, "New Game") {
			@Override
			public void mouseReleased(int mouseX, int mouseY) {
				gui.getGame().newGame();
				GUI.setWindow(GUI.WINDOW_HUD);
			}
		};
		
		Widget_Button btn_settings = new Widget_Button(backgroundX + bufferX, backgroundY + halfwayY - 300, halfwayX - bufferX * 2, 50, this, "Settings") {
			@Override
			public void mouseReleased(int mouseX, int mouseY) {
				GUI.setWindow(GUI.WINDOW_SETTINGS);
			}
		};
		
		Widget_Button btn_exit = new Widget_Button(backgroundX + bufferX, backgroundY + halfwayY - 400, halfwayX - bufferX * 2, 50, this, "Exit") {
			@Override
			public void mouseReleased(int mouseX, int mouseY) {
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
