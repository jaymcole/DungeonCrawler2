package ecu.se.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.Glyph;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

import assetManager.AssetManager;
import assetManager.FontAsset;
import ecu.se.Utils;

public abstract class Widget {
	
	protected float x, y, width, height;
	protected Polygon bounds;
	protected Window parent;
	protected BitmapFont font;
	protected boolean useText;
	protected String text;
	protected Color textColor;
	
	protected GlyphLayout glyphLayout;
	protected float textX, textY;
	
	
	public Widget (float x, float y, float width, float height, Window parent) {
		this.x = GUI.getProportionalX(x);
		this.y = GUI.getProportionalY(y);
		this.width = GUI.convertX(width);
		this.height = GUI.convertY(height);
		this.parent = parent;
		
		useText = false;
		textColor = Color.WHITE;	
		glyphLayout = new GlyphLayout();
		font = AssetManager.getFont("font/font_jay.ttf", 100).getFont();
//		font.getData().setScale(GUI.conversionX, GUI.conversionY);
		bounds = Utils.getRectangleBounds(this.x, this.y, this.width, this.height, Utils.ALIGN_BOTTOM_LEFT);
	}
		
	public abstract boolean update (float deltaTime, int mouseX, int mouseY);
	public abstract void render(SpriteBatch batch);
	
	/**
	 * Renders this widgets bounding box.
	 * @param renderer
	 */
	public void debugRender (ShapeRenderer renderer) {
		renderer.polygon(bounds.getTransformedVertices());
	}
	
	/**
	 * 
	 * @return The window holding this widget.
	 */
	public Window getParent() {
		return parent;
	}
	
	
	/**
	 * Sets the text to rendered over the widget.
	 * Warning - Not Widgets support text.
	 * @param text
	 */
	public void setText(String text) {
		glyphLayout.setText(font, text);
		this.text = text;
		useText = true;
		textX = (int)(x + ((width - glyphLayout.width) * 0.5));
		textY = (int)(y + ((height + glyphLayout.height) * 0.5));
	}
	
	/**
	 * Sets the font to be used by this widget.
	 * @param font
	 */
	public void setFont(BitmapFont font) {
		this.font = font;
	}
}
