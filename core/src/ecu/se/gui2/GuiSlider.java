package ecu.se.gui2;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GuiSlider extends GuiProgressBar{

	public GuiSlider(float min, float max, float curVal) {
		super(min, max, curVal);
		renderText = false; 
		animate = false;
	}
}
