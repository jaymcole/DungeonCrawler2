package ecu.se.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

public class Window_PauseScreen extends Window{

	@Override
	protected void buildWindow() {
		Widget_Label label = new Widget_Label(0, 0, GUI.defaultWidth,GUI.defaultHeight , this, "PAUSED", 500, null, Color.CHARTREUSE);
		
		
		widgets = new Widget[]{
				new Widget_Image(0, 0, GUI.defaultWidth, GUI.defaultHeight, this, "texture/misc/gray_trans.png"),
				label
		};
	}

}
