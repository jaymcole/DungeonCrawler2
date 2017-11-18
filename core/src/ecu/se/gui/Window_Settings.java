package ecu.se.gui;

import com.badlogic.gdx.graphics.Color;

import ecu.se.Game;

public class Window_Settings extends Window{

	public Window_Settings(GUI gui) {
		super(gui);
		// TODO Auto-generated constructor stub
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
		Widget_Button btn_play = new Widget_Button(backgroundX + bufferX, backgroundY + halfwayY - 100, halfwayX - bufferX * 2, 50, this, "Some setting") {
			public void mouseReleased() {
				//TODO: Settings options
			}
		};
		
		Widget_Button btn_settings = new Widget_Button(backgroundX + bufferX, backgroundY + halfwayY - 200, halfwayX - bufferX * 2, 50, this, "Some setting") {
			@Override
			public void mouseReleased() {
				//TODO: Settings options
			}
		};
		
		Widget_Button btn_back = new Widget_Button(backgroundX + bufferX, backgroundY + halfwayY - 300, (halfwayX - bufferX * 2) * 0.25f, 50, this, "Back") {
			@Override
			public void mouseReleased() {
				gui.closeWindow(GUI.WINDOW_MAIN_MENU, GUI.WINDOW_PAUSED);
			}
		};
		
		
		widgets = new Widget[]{
				img_background,
				lbl_title,
				btn_play,
				btn_settings,
				btn_back
		};
		
	}

}
