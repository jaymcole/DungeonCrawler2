package ecu.se.gui2;


import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import ecu.se.Logger;
import ecu.se.assetManager.AssetManager;

public class GuiLabel extends Component{
	
	protected Color debugColor;
	protected Random random;
//	private Texture test;
	protected boolean isHovering = false;
	
	protected float xOffset, yOffset;
	
	public GuiLabel(String text) {
		super();		
		setBackgroundTexture(AssetManager.getTexture("texture/misc/white.png").getTexture());
		setBackgroundColor(Color.RED);
		setBackgroundColor(Color.CYAN);
		setRenderBackground(true);
		setText(text);
		renderText = true;
		name = "\"" + text + "\"";
	}
	
	

	@Override
	protected void renderComponent(SpriteBatch batch) {
		if (font == null) {
			Logger.Error(getClass(), "render", "Font is null"); 
			return;
		}
		// Center Text
		setTextJustify(justify);
		font.setColor(textColor);

		font.draw(batch, text, getChildX() + xOffset, getChildY()  + yOffset);
	}

	@Override
	public void setTextJustify(GuiUtils.Justify mode) {
		justify = mode;
		yOffset = (contentHeight() * 0.5f) + (textHeight * 0.5f);
		switch (mode) {
		case Center:
			xOffset = (contentWidth() * 0.5f) - (textWidth * 0.5f);
			break;
		case Left:
			xOffset = 0;
			break;
		case Right:
			xOffset = contentWidth() - textWidth;
			break;
		}
	}
	
	@Override
	protected void debugRenderComponent(ShapeRenderer shapeRenderer) {
		if (isHovering)
			shapeRenderer.setColor(Color.RED);
		else
			shapeRenderer.setColor(Color.GREEN);
		Rectangle rect = getBounds();
		shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
	}

	@Override
	public void calculateMinComponentDimensions() {
		minimumWidth = textWidth;
		minimumHeight = textHeight;
	}

	@Override
	protected void disposeComponent() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected String debugTooltipTextComponent() {
		return toString() + "\n" +
				"Justify: " + justify.toString() + "\n"
				+ "Font Size: " + getFontSize() + "\n"
				+ "Font Ascent: " + font.getAscent();
	}

	@Override
	protected Component updateComponent(Component consumer, float deltaTime, int mouseX, int mouseY) {
		return null;
	}
}
