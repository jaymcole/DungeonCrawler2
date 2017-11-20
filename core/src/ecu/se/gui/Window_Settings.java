package ecu.se.gui;

import com.badlogic.gdx.graphics.Color;

import ecu.se.Lighting;

public class Window_Settings extends Window{

	public Window_Settings(GUI gui) {
		super(gui);
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
		Widget_Button btn_play = new Widget_Button(backgroundX + bufferX, backgroundY + halfwayY - 100, halfwayX - bufferX * 2, 50, this, "Some setting") {
			public void mouseReleased() {
				//TODO: Settings options
			}
		};
		
		
		Widget_Slider sld_attn_a = new Widget_Slider(backgroundX + bufferX, backgroundY + halfwayY - 200, halfwayX - bufferX * 2, 50, this, 0, 10, Lighting.getAttnA()) {
			@Override
			public void onValueChange() {
				Lighting.setAttnA(this.getProgress());
			}
		};
		
		Widget_Slider sld_attn_b = new Widget_Slider(backgroundX + bufferX, backgroundY + halfwayY - 300, halfwayX - bufferX * 2, 50, this, 0, 10, Lighting.getAttnB()) {
			@Override
			public void onValueChange() {
				Lighting.setAttnB(this.getProgress());
			}
		};
		
		Widget_Button btn_back = new Widget_Button(backgroundX + bufferX, backgroundY + halfwayY - 400, (halfwayX - bufferX * 2) * 0.25f, 50, this, "Back") {
			@Override
			public void mouseReleased() {
				gui.closeWindow(GUI.WINDOW_MAIN_MENU, GUI.WINDOW_PAUSED);
			}
		};
		
		Widget_Button btn_toggleBackground = new Widget_Button(backgroundX + bufferX +  btn_back.width, backgroundY + halfwayY - 400, (halfwayX - bufferX * 2) * 0.25f, 50, this, "Toggle Background") {
			@Override
			public void mouseReleased() {
				parentWindow.toggleBackground();
			}
		};
		
		this.setBackground(img_background);
		widgets = new Widget[]{
//				img_background,
				lbl_title,
				btn_play,
				sld_attn_a,
				sld_attn_b,
				btn_back,
				btn_toggleBackground
		};
		
	}

}
