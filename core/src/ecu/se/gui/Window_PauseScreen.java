package ecu.se.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import ecu.se.Game;

public class Window_PauseScreen extends Window {
	
	private float time;
	private Widget_Label label;
	
	private final float transitionSpeedMultiplier = 4;
	private Color pauseTextColor;
	
	public Window_PauseScreen(GUI gui) {
		super(gui);
		pauseTextColor = Color.CHARTREUSE;
	}

	@Override
	protected void buildWindow() {		
		label = new Widget_Label(100, 100,
				Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), this, "PAUSED", 100, null, Color.CHARTREUSE);
		
		widgets = new Widget[] {
				new Widget_Image(0, 0, GUI.defaultWidth, GUI.defaultHeight, this, "texture/misc/gray_trans.png"),
				label };
	}
	
	@Override 
	public void act(float deltaTime) {
		time += deltaTime * transitionSpeedMultiplier;
		label.textColor = new Color(pauseTextColor.r, pauseTextColor.g, pauseTextColor.b, 0.5f + (float)Math.sin(time) * 0.5f);
	}

	@Override
	public void onPause() {
		if (Game.currentState != Game.GAME_STATE_EXITING)
			Game.currentState = Game.GAME_STATE_RUNNING;
	}

	@Override
	public void onResume() {
		if (Game.currentState != Game.GAME_STATE_EXITING)
			Game.currentState = Game.GAME_STATE_PAUSED;
		time = 0.0f;
	}

}
