package ecu.se.gui2;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GuiSlider extends GuiProgressBar{

	
	
	
	public GuiSlider(float min, float max, float curVal) {
		super(min, max, curVal);
		renderText = false; 
		animate = false;
	}
	
	@Override
	protected void renderComponent(SpriteBatch batch) {

		batch.setColor(ProgressBarColor);
		batch.draw(progressBarTexture, 
				getChildX(), getChildY(), 
				contentWidth() * progressPercentage, contentHeight(), 
				-xTextureOffset,0,
				(((contentWidth() * progressPercentage) / progressBarTexture.getWidth()) - xTextureOffset), -contentHeight() / progressBarTexture.getHeight()
				);
		
		batch.setColor(foregroundColor);	
		
		if (renderText) {
			font.setColor(textColor);
			setTextJustify(justify);
			font.draw(batch, text, getChildX() + xTextOffset, getChildY() + yTextOffset);

		}
	}

}
