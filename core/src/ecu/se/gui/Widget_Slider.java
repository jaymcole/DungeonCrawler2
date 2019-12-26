package ecu.se.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ecu.se.Logger;
import ecu.se.Utils;

public class Widget_Slider extends Widget {

	private float oldProgress;
	private float newProgress;
	private float min, max;
	private Widget_Button btn;
	public Widget_Slider(float x, float y, float width, float height, Window parent, float min, float max,
			float initialValue) {
		super(x, y, width, height, parent);
		this.oldProgress = -123;
		this.newProgress = initialValue;
		this.min = min;
		this.max = max;
		setText(initialValue + "");
		btn = new Widget_Button(x, y, 15, 15, this.parentWindow, "" + initialValue) {
			@Override
			public void mouseDown(int mouseX, int mouseY) {
				this.x = mouseX + (this.mouseOffsetX);
				((Widget_Slider)parentWidget).correctChildPosition(this);
//				this.bounds.setPosition(this.x, this.y);
			};
		};
		btn.useText = false;
		btn.setParentWidget(this);
	}

	@Override
	public boolean update(float deltaTime, int mouseX, int mouseY) {
		boolean used = btn.update(deltaTime, mouseX, mouseY);
		float percent = (btn.x - this.x) / (width - btn.width);
		newProgress = ((Math.abs(max) + Math.abs(min)) * percent) + min;
		
		if (used && oldProgress != newProgress) {
			onValueChange();
			oldProgress = newProgress;
			this.setText(newProgress + "");
		}
		return used;
	}
	
	public void onValueChange() {
		Logger.Debug(this.getClass(), "onValueChange",newProgress);	};

	@Override
	public void render(SpriteBatch batch) {
		btn.render(batch);
		if (useText) {
			font.setColor(textColor);
			font.draw(batch, text, textX, (textY) + this.height * 0.15f);
		}
	}
	
	public void correctChildPosition(Widget w) {
		w.setPosition(Utils.clamp(x, x + width - w.width, w.x), y );
	}
	
	public float getProgress() {
		return newProgress;
	}

}
