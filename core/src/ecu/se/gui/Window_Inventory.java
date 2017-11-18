package ecu.se.gui;

import com.badlogic.gdx.graphics.Color;

public class Window_Inventory extends Window {

	public Window_Inventory(GUI gui) {
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
		
		
		Widget_Image img_background = new Widget_Image(backgroundX, backgroundY, halfwayX, halfwayY, this, "texture/misc/white.png");
		img_background.setDefaultColor(new Color(Color.CHARTREUSE.r,Color.CHARTREUSE.g,Color.CHARTREUSE.b,0.5f));
		Widget_Label lbl_title = new Widget_Label(backgroundX + bufferX, backgroundY + halfwayY - 25, 0, 0, parent, this.windowName, 50, Color.CLEAR, Color.BLACK);
		
		Widget_Button btn_close = new Widget_Button(backgroundX + bufferX, backgroundY + halfwayY - 100, halfwayX - bufferX * 2, 50, this, "Close") {
			public void mouseReleased() {
				gui.closeWindow(GUI.WINDOW_HUD, GUI.WINDOW_HUD);
			}
		};
		
		
		widgets = new Widget[]{
				img_background,
				lbl_title,
				btn_close
		};
		
	}

}
