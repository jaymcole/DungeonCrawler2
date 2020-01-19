package ecu.se.gui2;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import ecu.se.Logger;
import ecu.se.Utils;
import ecu.se.archive.Archiver;
import ecu.se.archive.TimeRecords;
import ecu.se.assetManager.AssetManager;
import ecu.se.gui.GUI;

public abstract class Component {
	public float preferredWidth, preferredHeight;

	protected float originX, originY;
	private float paddingT, paddingR, paddingB, paddingL;
	private float marginT, marginR, marginB, marginL;
	private float borderThickness;
	protected float maxWidth;
	protected float maxHeight;
	
	private Color backgroundTint = Color.WHITE;
	private static Color debugMarginBorderColor = Color.BLUE;
	private static Color debugPaddingBorderColor = Color.RED;
	private static Color debugBorderColor = Color.WHITE;
	
	
	private Texture background;
	private float backgroundOpacity = 0.5f;
	private boolean renderBackground = true;
		
	public Component parent;
	
	protected boolean isVisible = true;
	protected float minimumWidth;
	protected float minimumHeight;
	
	protected boolean isValidLayout;
	
	public static float toolTipDelayTime = 0.01f;
	protected String tooltipText = null;
	protected ContextMenu contextMenu;
	
	protected float mouseOffsetX;
	protected float mouseOffsetY;
	protected boolean mouseHovering = false;
	protected float timeSpentHovering = 0;
	
	protected GuiUtils.Expand expandSettings = GuiUtils.Expand.ExpandAndFill;
	
	protected Color activeColor = Color.GREEN;
	protected boolean isActive = false;
	protected Color highlightColor = Color.CYAN;
	protected boolean highlight = false;
	protected Color defaultColor = Color.WHITE;
	
	// Optional parameters - may not be used by all gui elements
	protected GuiUtils.Justify justify = GuiUtils.Justify.Left;
	protected String text;
	protected BitmapFont font;
	protected Color textColor;
	protected float textWidth;
	protected float textHeight;
	protected GlyphLayout layout;
	protected boolean renderText = false;
	private int fontSize;
	private String fontStyle = "font/OpenSansRegular.ttf";
	
	public String name;
	
	public Component () {
		Random random = new Random();
		setBackgroundColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255),1));
		borderThickness = 0;
		isVisible = true;
		layout = new GlyphLayout();
		setFontSize(15);
		tooltipText = toString();
		textColor = Color.RED;
	}
	
	
	
	/**
	 * 
	 * @param deltaTime
	 * @param mouseX
	 * @param mouseY
	 * @return
	 */
	public Component update(Component consumer, float deltaTime, int mouseX, int mouseY) {		
		if (getBounds().contains(mouseX, mouseY)) {
			mouseHovering = true;
			timeSpentHovering += deltaTime;
			backgroundTint = highlightColor;
		} else {
			mouseHovering = false;
			timeSpentHovering = 0;
			backgroundTint = defaultColor;
		}

		if (showToolTip && timeSpentHovering > toolTipDelayTime && !Utils.NullOrEmpty(tooltipText)) {
			GUI.RenderToolTip = true;
			GUI.tooltip.setText(debugTooltipText());
		}
		consumer = updateComponent(consumer, deltaTime, mouseX, mouseY);
		
		return consumeInput(consumer, mouseX, mouseY);
	}
	
	protected abstract Component updateComponent(Component consumer, float deltaTime, int mouseX, int mouseY);
	
	protected boolean canConsumeUI = true;
	protected boolean showToolTip = true;
	private Component consumeInput(Component consumer, int mouseX, int mouseY) {
		if (this.getBounds().contains(mouseX, mouseY) && canConsumeUI && consumer == null) {
			consumer = this;
		}
		return consumer;
	}
	
	protected String debugTooltipText() {
		String text = "";
		if (!Utils.NullOrEmpty(name)) {
			text = name + " : " + getClass().getSimpleName() + "\n";
		}
		text += "Screen Coordinates: " + originX + ", " + originY + "\n";
		text += "MinimumWidth: " + minimumWidth + " MinimumHeight: " + minimumHeight + "\n";
		text += "maxWidth: " + maxWidth + " maxHeight: " + maxHeight + "\n";
		text += "width: " + getWidth() + " height: " + getHeight() + "\n";
		return text + debugTooltipTextComponent();
	}

	protected String debugTooltipTextComponent() {return "*" + getClass().getSimpleName() + " missing debug tooltip*";}
	
	public void act() {
		
	}
	
	/**
	 * The action this button performs WHEN the left mouse button is pressed AND over this button.
	 */
	public void mousePressed(int mouseX, int mouseY){
		Logger.Debug(getClass(), "mousePressed", "MousePressed unimplemented");		
	};

	
	/**
	 * The action this button performs while the left mouse button is down AND over this button.
	 */
	public void mouseDown(int mouseX, int mouseY){
		Logger.Debug(getClass(), "mouseDown", "mouseDown unimplemented");
	};

	
	/**
	 * The action this button performs WHEN the left mouse button is releases AND over this button.
	 */
	public void mouseReleased(int mouseX, int mouseY) {
		Logger.Debug(getClass(), "mouseReleased", "mouseDown unimplemented");
	}

	
	
	public void render(SpriteBatch batch) {
		if (!isVisible)
			return;
		if (renderBackground && background != null) {
			Rectangle rect = getPaddedBounds();
			
			if(mouseHovering)
				batch.setColor(highlightColor);
			else if (isActive)
				batch.setColor(activeColor);
			else
				batch.setColor(backgroundTint);
			
			batch.draw(background, rect.x, rect.y, rect.width, rect.height);
		}
		
		renderComponent(batch);
	}	
	
	protected abstract void renderComponent(SpriteBatch batch);
	
	
	public void calculateMinDimensions() {
		calculateMinComponentDimensions();
	}
	
	protected abstract void calculateMinComponentDimensions();
	
	void invalidate() {
		isValidLayout = false;
	}
	
	public boolean validate() {
		return isValidLayout;
	}
	
	public void setTextColor(Color c) {
		textColor = c;
	}
	
	public void setText(String text) {
		if (Utils.NullOrEmpty(text))
			text = "*Error: Missing tooltip.*";
		
		this.text = text;
		layout.setText(font, text);
		textWidth = layout.width;
		textHeight = layout.height;
		setTextJustify(justify);
	}
	
	public void debugRender(ShapeRenderer shapeRenderer) {
		Rectangle rect;
		shapeRenderer.setColor(debugBorderColor);
		rect = getBounds();
		shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
		
		shapeRenderer.setColor(debugMarginBorderColor);
		shapeRenderer.rect(originX, originY, getMarginWidth(), getMarginHeight());
		rect = getMarginBounds();
		shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
		
		shapeRenderer.setColor(debugPaddingBorderColor);
		shapeRenderer.rect(getChildX() - getPaddingL(), getChildY() - getPaddingT(), getPaddedWidth(), getPaddedHeight());
		rect = getPaddedBounds();
		shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
		
		debugRenderComponent(shapeRenderer);
	}
	
	protected abstract void debugRenderComponent(ShapeRenderer shapeRenderer);
	
	public void dispose() {
		disposeComponent();
	}
	
	protected abstract void disposeComponent();
	
	
	public void setIsVisible(boolean visible) {
		isVisible = visible;
	}
	
	
	public void setBackgroundOpacity(float opacity) {
		this.backgroundTint = new Color(backgroundTint.r, backgroundTint.g, backgroundTint.b, opacity);

	}
	public void setBackgroundColor(Color backgroundTint) {
		this.backgroundTint = new Color(backgroundTint.r, backgroundTint.g, backgroundTint.b, backgroundOpacity);
	}
	public void setBackgroundTexture(Texture background) {
		if (background != null) {
			this.background = background;
		} else {
			Logger.Error(getClass(), "setBackgroundTexture", "Failed to set background texture. Given texture was null.");
			renderBackground = false;
		}
	}	
	/**
	 * Note: disables background rendering if background texture is null.
	 * @param renderBackground
	 */
	public void setRenderBackground(boolean renderBackground) {
		if (renderBackground) {
			this.renderBackground = (background != null);
			if (!this.renderBackground)
				Logger.Error(getClass(), "setRenderBackground", "Failed to enable background. Background texture is null.");
		} else {
			this.renderBackground = renderBackground;
		}
	}	
	
	public Rectangle getBounds() {
		return new Rectangle(getChildX(), getChildY(), contentWidth(), contentHeight());
	}
	public Rectangle getMarginBounds() {
		return new Rectangle(originX, originY, getMarginWidth(), getMarginHeight());
	}	
	public Rectangle getPaddedBounds() {
		return new Rectangle(getChildX(), getChildY(), getPaddedWidth(), getPaddedHeight());
	}
	
	public float getChildX() {
		return originX + marginL + paddingL + borderThickness;
		
	}
	public float getChildY() {
		return originY + marginT + paddingT + borderThickness;
		
	}
	public void setOriginX(float x) {
		originX = x;
	}
	public void setOriginY(float y) {
		originY = y;
	}

	protected float contentWidth() {
		if (expandSettings == GuiUtils.Expand.ExpandAndFill)
			return maxWidth - getSurroundingWidth();
		else 
			return minimumWidth;
	}
	protected float contentHeight() {
		if (expandSettings == GuiUtils.Expand.ExpandAndFill)
			return maxHeight - getSurroundingHeight();
		else 
			return minimumHeight;
	}
	public float getChildAvailableWidth() {
		return getPaddedWidth();
	}
	public float getChildAvailableHeight() {
		return getPaddedHeight();
	}
	public float getWidth() {
		return getSurroundingWidth() + contentWidth() ;
	}
	protected float getSurroundingWidth() {
		return marginR + marginL + paddingR + paddingL + (borderThickness * 2);
	}
	public float getHeight() {
		return getSurroundingHeight() + contentHeight();
	}
	protected float getSurroundingHeight() {
		return marginT + marginB + paddingT + paddingB + (borderThickness * 2);
	}
	public float getMinimumWidth() {
		return minimumWidth;
	}
	public float getMinimumHeight() {
		return minimumHeight;
	}
	public void setMaxHeight(float max) {
		maxHeight = max;
	}	
	public void setMaxWidth(float max) {
		maxWidth = max;
	}

	

	// THICCNESS
	public void setBorderThickness (float thicc) {
		borderThickness = thicc;
	}
	private float getBorderThickness() {
		return borderThickness;
	}
	
	/// PADDING
	private float getPaddingT() {
		return paddingT;
	}
	private float getPaddingR() {
		return paddingR;
	}
	private float getPaddingB() {
		return paddingB;
	}
	private float getPaddingL() {
		return paddingL;
	}
	private float getPaddedWidth() {
		return getWidth() - getSurroundingWidth();
	}
	private float getPaddedHeight() {
		return getHeight() - getSurroundingWidth();
	}
	public void setPadding(float padding) {
		setPaddingH(padding);
		setPaddingV(padding);
	}
	public void setPaddingH(float padding) {
		setPaddingL(padding);
		setPaddingR(padding);
	}
	public void setPaddingV(float padding) {
		setPaddingT(padding);
		setPaddingB(padding);
	}
	public void setPaddingT(float paddingT) {
		this.paddingT = GUI.convertY(paddingT);
	}
	public void setPaddingR(float paddingR) {
		this.paddingR = GUI.convertY(paddingR);
	}
	public void setPaddingB(float paddingB) {
		this.paddingB = GUI.convertY(paddingB);
	}
	public void setPaddingL(float paddingL) {
		this.paddingL = GUI.convertY(paddingL);
	}

	/// MARGIN
	private float getMarginT() {
		return marginT;
	}
	private float getMarginR() {
		return marginR;
	}
	private float getMarginB() {
		return marginB;
	}
	private float getMarginL() {
		return marginL;
	}
	private float getMarginWidth() {
		return getWidth();
	}
	private float getMarginHeight() {
		return getHeight();
	}
	public void setMargin(float margin) {
		setMarginH(margin);
		setMarginV(margin);
	}
	public void setMarginH(float margin) {
		setMarginL(margin);
		setMarginR(margin);
	}
	public void setMarginV(float margin) {
		setMarginT(margin);
		setMarginB(margin);
	}
	public void setMarginT(float marginT) {
		this.marginT = GUI.convertY(marginT);
	}
	public void setMarginR(float marginR) {
		this.marginR = GUI.convertX(marginR);
	}
	public void setMarginB(float marginB) {
		this.marginB = GUI.convertY(marginB);
	}
	public void setMarginL(float marginL) {
		this.marginL = GUI.convertX(marginL);
	}

	
	public void LogComponentData() {
		Logger.Debug(getClass(), "resize", "Component Info");
		Logger.Debug(getClass(), "resize", "\t X: " + getChildX());
		Logger.Debug(getClass(), "resize", "\t Y: " + getChildY());
		Logger.Debug(getClass(), "resize", "\t originX: " + originX);
		Logger.Debug(getClass(), "resize", "\t originY: " + originY);
		Logger.Debug(getClass(), "resize", "\t Width: " + getWidth());
		Logger.Debug(getClass(), "resize", "\t Height: " + getHeight());
		Logger.Debug(getClass(), "resize", "\t Min Width: " + getMinimumWidth());
		Logger.Debug(getClass(), "resize", "\t Min Height: " + getMinimumHeight());
		Logger.Debug(getClass(), "resize", "\t AvailableChildWidth: " + getChildAvailableWidth());
		Logger.Debug(getClass(), "resize", "\t AvailableChildHeight: " + getChildAvailableHeight());
		Logger.Debug(getClass(), "resize", "\t Margins: T" + getMarginT() + ", R" + getMarginR()+", B" + getMarginB()+", L" + getMarginL());
		Logger.Debug(getClass(), "resize", "\t Padding: T" + getPaddingT() + ", R" + getPaddingR()+", B" + getPaddingB()+", L" + getPaddingL());
	}
	
	public void setClickable(boolean value) {
		canConsumeUI = value;
	}
	
	
	public void setExpandSettings(GuiUtils.Expand expandSettings) {
		this.expandSettings = expandSettings;
	}
	
	/**
	 * The action this window should perform when it loses focus.
	 * 		Should call Archiver.set with the appropriate TimeRecord
	 */
	public void onPause() {
		Archiver.set(TimeRecords.TIME_IN_MENU, false);
	}
	
	/**
	 * The action this window should perform when it gains focus.
	 * 		Should call Archiver.set with the appropriate TimeRecord
	 */
	public void onResume() {
		Archiver.set(TimeRecords.TIME_IN_MENU, false);
	}
	
	@Override
	public String toString() {
		return getClass().getName();
	}
	
	/**
	 * Sets activeColor to c
	 * @param c
	 */
	public void setActiveColor(Color c) {
		activeColor= c;
	}

	/**
	 * Sets highlightColor to c
	 * @param c
	 */
	public void setHighlightColor(Color c) {
		highlightColor = c;
	}
	
	public void setTextJustify(GuiUtils.Justify mode) {
		justify = mode;
	}
	
	public int getFontSize() {
		return fontSize;
	}
	
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
		font = AssetManager.getFont(fontStyle, fontSize).getFont();
		setTextJustify(justify);
		this.calculateMinDimensions();
	}
}
