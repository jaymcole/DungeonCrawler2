package ecu.se.gui2;


import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import ecu.se.Game;
import ecu.se.Globals;
import ecu.se.Logger;
import ecu.se.assetManager.AssetManager;

public class GuiLabel extends Component{

	private String text;
	private BitmapFont font;
	public Color textColor;
	
	private float textWidth;
	private float textHeight;
	private GlyphLayout layout;
	
	private Color debugColor;
	private Random random;
//	private Texture test;
	private boolean isHovering = false;
	
	private float xOffset, yOffset;
	private GuiUtils.Justify justify;
	public GuiLabel(String text) {
		super();
		setMargin(5);
		
		setBackgroundTexture(AssetManager.getTexture("texture/misc/skulls.png").getTexture());
		setBackgroundColor(Color.RED);
		setBackgroundColor(Color.CYAN);
		setRenderBackground(true);
		
		textColor = Color.RED;
		layout = new GlyphLayout();
		font = AssetManager.getFont("font/OpenSansRegular.ttf", 150).getFont();
		setText(text);
		justify = GuiUtils.Justify.Right;
	}
	
	@Override
	public boolean update(float deltaTime, int mouseX, int mouseY) {
		if (!isVisible)
			return false;
		isHovering = getBounds().contains(mouseX, mouseY);
		return getBounds().contains(mouseX, mouseY);
	}
	
	public void setText(String text) {
		this.text = text;
		layout.setText(font, text);
		textWidth = layout.width;
		textHeight = layout.height;
	}

	@Override
	protected void renderComponent(SpriteBatch batch) {
		if (font == null) {
			Logger.Error(getClass(), "render", "Font is null"); 
			return;
		}
		
		font.setColor(Color.MAGENTA);
		// Center Text
		setTextJustify(justify);
		font.draw(batch, text, getX() + xOffset, getY()  + yOffset);
	}

	private void setTextJustify(GuiUtils.Justify mode) {
		yOffset = (getComponentHeight() * 0.5f) - (textHeight * 0.5f);
		switch (mode) {
		case Center:
			xOffset = (getComponentWidth() * 0.5f) - (textWidth * 0.5f);
			break;
		case Left:
			xOffset = 0;
			break;
		case Right:
			xOffset = getComponentWidth() - textWidth;
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

}
