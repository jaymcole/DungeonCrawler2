package ecu.se.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;

import ecu.se.Utils;
import ecu.se.assetManager.AssetManager;

public abstract class Widget {
	
	protected float x, y, width, height;
	protected Polygon bounds;
	protected Window parentWindow;
	protected Widget parentWidget;
	protected BitmapFont font;
	
	protected boolean useText;
	protected boolean useBackground;
	
	protected String text;
	protected Color textColor;
	
	protected Color defaultColor;
	
	protected GlyphLayout glyphLayout;
	protected float textX, textY;	
	
	public Widget (float x, float y, float width, float height, Window parentWindow) {
		this.x = GUI.getProportionalX(x);
		this.y = GUI.getProportionalY(y);
		this.width = GUI.convertX(width);
		this.height = GUI.convertY(height);
		this.parentWindow = parentWindow;
		useBackground = true;
		defaultColor = Color.WHITE;
		useText = false;
		textColor = Color.WHITE;	
		glyphLayout = new GlyphLayout();
		font = AssetManager.getFont("font/font_jay.ttf", 50).getFont();
		bounds = Utils.getRectangleBounds(this.x, this.y, this.width, this.height, Utils.ALIGN_BOTTOM_LEFT);
	}
		
	
	/**
	 * Updates this widget
	 * @param deltaTime
	 * @param mouseX
	 * @param mouseY
	 * @return
	 */
	public abstract boolean update (float deltaTime, int mouseX, int mouseY);
	
	/**
	 * Renders this widget.
	 * @param batch
	 */
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
	public Window getParentWindow() {
		return parentWindow;
	}
	
	/**
	 * 
	 * @return The parent widget 
	 * Warning - this may be null.
	 */
	public Widget getParentWidget() {
		return parentWidget;
	}
	
	/**
	 * Sets parentWidget to parent.
	 * @param parent
	 */
	public void setParentWidget(Widget parent) {
		this.parentWidget = parent;
	}
	
	
	/**
	 * Sets the text to rendered over the widget.
	 * Warning - Not Widgets support text.
	 * @param text
	 */
	public void setText(String text) {
		this.text = text;
		glyphLayout.setText(font, this.text);
		textX = (int)(x + ((width - glyphLayout.width) * 0.5f));
		textY = (int)(y + ((height + glyphLayout.height) * 0.5f));
//		bounds.setPosition(x, y);
		bounds = Utils.getRectangleBounds(this.x, this.y, this.width, this.height, Utils.ALIGN_BOTTOM_LEFT);
		useText = true;
	}
	
	/**
	 * Sets the font to be used by this widget.
	 * @param font
	 */
	public void setFont(BitmapFont font) {
		this.font = font;
	}
	
	/**
	 * Sets defaultColor to c
	 * @param c
	 */
	public void setDefaultColor(Color c) {
		defaultColor = c;
	}
	
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
		this.bounds.setPosition(x, y);
	}
	
	public void toggleBackground() {
		useBackground ^= true;
	}
	
	public boolean contains(int x, int y) {
		return bounds.contains(x, y);
	}
}
