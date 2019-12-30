package ecu.se.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ecu.se.Logger;
import ecu.se.Utils;
import ecu.se.assetManager.AssetManager;

public class Widget_Slider extends Widget {

	private float oldProgress;
	private float newProgress;
	private float min, max;
	private Widget_Button btn;
	private Texture slideTexture;
	public Widget_Slider(float x, float y, float width, float height, Window parent, float min, float max,
			float initialValue) {
		super(x, y, width, height, parent);
		this.oldProgress = Integer.MIN_VALUE;
		this.newProgress = Utils.clamp(min, max, initialValue);
		this.min = min;
		this.max = max;
		setText(initialValue + "");
		
		
		float percent = (initialValue - min) / (max - min);
		btn = new Widget_Button(x + (percent * width), y, 45, 45, this.parentWindow, "" + initialValue) {
			@Override
			public void mouseDown(int mouseX, int mouseY) {
				this.x = mouseX + (this.mouseOffsetX);
				((Widget_Slider)parentWidget).correctChildPosition(this);
			};
		};
		btn.texture = AssetManager.getTexture("humans/dhruv.png").getTexture();
		btn.useText = false;
		btn.setParentWidget(this);
		correctChildPosition(btn);
		
		slideTexture = AssetManager.getTexture("texture/misc/white2.png").getTexture();
	}

	@Override
	public boolean update(float deltaTime, int mouseX, int mouseY) {
		boolean clickConsumed = btn.update(deltaTime, mouseX, mouseY);
		float percent = (btn.x - this.x) / (width - btn.width);
		newProgress = ((Math.abs(max) + Math.abs(min)) * percent) + min;
		
		if (clickConsumed && oldProgress != newProgress) {
			onValueChange();
			oldProgress = newProgress;
			this.setText(newProgress + "");
		}
		return clickConsumed;
	}
	
	public void onValueChange() {
		Logger.Debug(this.getClass(), "onValueChange",newProgress);	
	};

	@Override
	public void render(SpriteBatch batch) {
		batch.draw(slideTexture, x, y + (btn.height * 0.5f) - 1, width, 2);
		btn.render(batch);
		if (useText) {
			font.setColor(textColor);
			font.draw(batch, text, textX, (textY) + this.height * 0.15f);
		}
	}
	
	public void correctChildPosition(Widget w) {
		w.setPosition(Utils.clamp(x, x + width - w.width, w.x), y);
	}
	
	public float getProgress() {
		return newProgress;
	}

}
