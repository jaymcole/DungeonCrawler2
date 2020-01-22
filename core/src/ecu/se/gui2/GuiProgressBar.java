package ecu.se.gui2;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import ecu.se.Logger;
import ecu.se.Utils;
import ecu.se.assetManager.AssetManager;

public class GuiProgressBar extends Component{

	protected Texture progressBarTexture;
	protected TextureRegion progressBarRegion;
	protected Color ProgressBarColor = Color.GREEN;
	
	protected TextureRegion foreground;
	protected Color foregroundColor = Color.WHITE;
	
	protected float minValue;
	protected float maxValue;
	protected float currentValue;
	protected float progressPercentage;
	protected float xTextOffset;
	protected float yTextOffset;
	
	protected float xTextureOffset = 0;
	protected float yTextureOffset = 0;
	
	protected boolean animate = true;
	protected int animateReset;
	public float animationSpeedFactor = 1.0f;
	
	public boolean textAsPercentage = true;
	
	public GuiProgressBar(float min, float max, float curVal) {
		super();
		minValue = min;
		maxValue = max;
		foregroundColor = Color.CYAN;
		setBackgroundTexture(AssetManager.getTexture("texture/test/test_face_red.png").getTexture());
		
		Texture temp = (AssetManager.getTexture("texture/gui/progressbar.png").getTexture());
		temp.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
		progressBarRegion = new TextureRegion(temp);
		progressBarRegion.setRegion(temp);
		progressBarTexture = temp;
		animateReset = progressBarTexture.getWidth();
		
		foreground = new TextureRegion(AssetManager.getTexture("texture/wall/stonewall_short.png").getTexture());
		setValue(curVal);
		renderText = true;
	}
	
	@Override
	protected Component updateComponent(Component consumer, float deltaTime, int mouseX, int mouseY) {
		if (animate) {
			xTextureOffset += deltaTime * animationSpeedFactor;
			xTextureOffset %= animateReset;
			tooltipText = "hello: " + xTextOffset + " / " + animateReset;
		}
		act();
		return consumer;//consumeInput(consumer, mouseX, mouseY);
	}

	@Override
	protected void renderComponent(SpriteBatch batch) {

		batch.setColor(ProgressBarColor);
		batch.draw(progressBarTexture, 
				getContentBounds().x, getContentBounds().y, 
				getContentBounds().width * progressPercentage, getContentBounds().height, 
				-xTextureOffset,0,
				(((getContentBounds().width * progressPercentage) / progressBarTexture.getWidth()) - xTextureOffset), -getContentBounds().height / progressBarTexture.getHeight()
				);
		
		batch.setColor(foregroundColor);	
		
		if (renderText) {
			font.setColor(textColor);
			setTextJustify(justify);
			font.draw(batch, text, getContentBounds().x + xTextOffset, getContentBounds().y + yTextOffset);

		}
	}
	
	@Override
	public void setTextJustify(GuiUtils.Justify mode) {
		justify = mode;
		yTextOffset = (getContentBounds().height * 0.5f) + (textHeight * 0.5f);
		switch (mode) {
		case Center:
			xTextOffset = (getContentBounds().width * 0.5f) - (textWidth * 0.5f);
			break;
		case Left:
			xTextOffset = 0;
			break;
		case Right:
			xTextOffset = getContentBounds().width - textWidth;
			break;
		}
	}
	
	public float getProgressPercentage() {
		return progressPercentage;
	}
	
	public float getCurrentValue() {
		return currentValue;
	}
	
	public float getMaxValue() {
		return maxValue;
	}
	
	public float getMinValue() {
		return minValue;
	}
	
	/**
	 * Changes currentValue by val
	 * @param val
	 */
	public void changeValue(float val) {
		setValue(currentValue + val);
	}
	
	/**
	 * Sets the currentValue to val.
	 * Also calculates and assigns progressPercentage
	 * @param val
	 */
	public void setValue(float val) {
		currentValue = Utils.clamp(minValue, maxValue, val);
		calculatePercentage();
		updateText();
	}
	
	public void updateText() {
		if(textAsPercentage) {
			setText(Math.round(progressPercentage*100) + "%");
		} else {
			setText(Math.round(currentValue) + " / " + maxValue);			
		}
	}
	
	private void calculatePercentage () {
		float total = maxValue - minValue;
		progressPercentage = (currentValue - minValue) / total;
	}

	@Override
	protected void renderDebugComponent(ShapeRenderer renderer) {}

	@Override
	protected boolean packComponent() {return true;}

	@Override
	protected void setPositionComponent(float x, float y) {}

	@Override
	protected Rectangle calculateMinContentBoundsComponent() {		
		glyphLayout.setText(font, getMaxValue() + "");
		float tempTextWidth = glyphLayout.width;
		float tempTextHeight = glyphLayout.height;
		return new Rectangle(0,0,tempTextWidth, tempTextHeight);
	}

}
