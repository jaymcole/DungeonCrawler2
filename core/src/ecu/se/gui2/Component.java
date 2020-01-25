package ecu.se.gui2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

import ecu.se.Globals;
import ecu.se.Logger;
import ecu.se.Utils;
import ecu.se.archive.Archiver;
import ecu.se.archive.TimeRecords;
import ecu.se.assetManager.AssetManager;
import ecu.se.gui.GUI;

public abstract class Component {
	
	public Component parent;
	protected boolean canConsumeUI;
	public boolean showToolTip;
	
	public String name;
	private Texture background;
	private float backgroundOpacity = 1.0f;
	private boolean renderBackground = true;	

	private Texture cornerTexture;
	private Color cornerColor;
	private boolean renderCorners;
	private float cornerOpacity;

	
	protected boolean isVisible = true;	
	
	public static float toolTipDelayTime = 0.01f;
	protected String tooltipText = null;
	
	protected float mouseOffsetX;
	protected float mouseOffsetY;
	protected boolean mouseHovering = false;
	protected float timeSpentHovering = 0;
		
	protected Color 	bgColor = Color.WHITE;
	
	protected Color 	activeColor = Color.GREEN;
	private float 		ActiveOpacity;
	private boolean		ActiveEnabled = false;
	protected boolean 	isActive = false;
	
	protected Color 	highlightColor = Color.CYAN;
	private float 		HighlightOpacity;
	private boolean 	HighlightEnabled = false;
	protected boolean 	highlight = false;
	
	protected Color 	defaultColor = Color.WHITE;	
	
	protected boolean AbsolutePositioning;
	
	protected Expand expand;
	
	public Component () {
		MarginBounds = new Rectangle();
		PaddingBounds = new Rectangle();
		ContentBounds = new Rectangle();
		setBackgroundColor(Color.GRAY);
		isVisible = false;
		glyphLayout = new GlyphLayout();
		setFontSize(15);
		setExpand(Expand.ExpandAll);
		tooltipText = toString();
		textColor = Color.RED;
		
		canConsumeUI = false;
		showToolTip = false;
		AbsolutePositioning = false;
		cornerColor = Color.RED;
		setRenderCorners(false);
	}
	
	private int mX, mY;
	public Component update(Component consumer, float deltaTime, int mouseX, int mouseY) {	
		// Temp debug code
		if (ContentBounds.contains(mouseX, mouseY)) {
			debugMouseBounds = getContentBounds();
		} else if (PaddingBounds.contains(mouseX, mouseY)) {
			debugMouseBounds = getPaddingBounds();
		} else if (MarginBounds.contains(mouseX, mouseY)) {
			debugMouseBounds = getMarginsBounds();
		} else
			debugMouseBounds = null;
		
		mX = mouseX;
		mY = mouseY;
		
		if (PaddingBounds.contains(mouseX, mouseY)) {
			mouseHovering = true;
			timeSpentHovering += deltaTime;
			bgColor = highlightColor;
		} else {
			mouseHovering = false;
			timeSpentHovering = 0;
			bgColor = defaultColor;
		}

		if (showToolTip && timeSpentHovering > toolTipDelayTime && !Utils.NullOrEmpty(tooltipText)) {
			GUI.RenderToolTip = true;
			GUI.tooltip.setText(debugTooltipText());
		}
		consumer = updateComponent(consumer, deltaTime, mouseX, mouseY);
		
		return consumeInput(consumer, mouseX, mouseY);
	}
	private Component consumeInput(Component consumer, int mouseX, int mouseY) {
		if (getPaddingBounds().contains(mouseX, mouseY) && canConsumeUI && consumer == null) {
			consumer = this;
		}
		return consumer;
	}
	protected abstract Component updateComponent(Component consumer, float deltaTime, int mouseX, int mouseY);

	
	public void render(SpriteBatch batch) {
		if (isVisible && renderBackground && background != null) {
			Rectangle rect = PaddingBounds;
			
			if(mouseHovering && HighlightEnabled)
				batch.setColor(highlightColor);
			else if (isActive && ActiveEnabled)
				batch.setColor(activeColor);
			else
				batch.setColor(defaultColor);
			batch.draw(background, rect.x, rect.y, rect.width, rect.height);
		}

		renderComponent(batch);
		renderCorners(batch);
	}
	
	private float cornerSize = 15;
	private void renderCorners(SpriteBatch batch) {
		if (!renderCorners || cornerTexture == null)
			return;
		
		batch.setColor(cornerColor);
		//Bottom Left
		batch.draw(cornerTexture, getPaddingBounds().x, getPaddingBounds().y, cornerSize, cornerSize, 0, 0, 1, 1);
		
		//Bottom Right
		batch.draw(cornerTexture, getPaddingBounds().x + getPaddingBounds().width - cornerSize, getPaddingBounds().y, cornerSize, cornerSize, 1, 0, 0, 1);
		
		//Top Right
		batch.draw(cornerTexture, getPaddingBounds().x + getPaddingBounds().width - cornerSize, getPaddingBounds().y + getPaddingBounds().height - cornerSize, cornerSize, cornerSize, 1, 1, 0, 0);

		//Top Left
		batch.draw(cornerTexture, getPaddingBounds().x, getPaddingBounds().y + getPaddingBounds().height - cornerSize, cornerSize, cornerSize, 0, 1, 1, 0);
	}

	public void act() {};
	
	protected abstract void renderComponent(SpriteBatch batch);
	
	
	
	// ---------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------ Debug --------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------
	
	private float DebugAlpha = 0.1f;
	private Color MarginColor = new Color(	Color.GREEN.r,	Color.GREEN.g, 	Color.GREEN.b, 	DebugAlpha);
	private Color PaddingColor = new Color(	Color.BLUE.r,	Color.BLUE.g, 	Color.BLUE.b, 	DebugAlpha);
	private Color ContentColor = new Color(	Color.RED.r,	Color.RED.g, 	Color.RED.b, 	DebugAlpha);
	private Color DebugHighlightColor = Color.WHITE;
	protected Rectangle debugMouseBounds;
	public void renderDebug(ShapeRenderer renderer) {
		renderer.setAutoShapeType(true);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		if (Globals.GUI_DEBUG_RENDER_FILL)
			renderer.set(ShapeType.Filled);
		
		renderer.setColor(MarginColor);
//		renderer.line(0, 0, getMarginsBounds().x, getMarginsBounds().y);
		renderer.rect(getMarginsBounds().x, getMarginsBounds().y, getMarginsBounds().width, getMarginsBounds().height);
		
		
		renderer.setColor(PaddingColor);
//		renderer.line(0, 0, getPaddingBounds().x, getPaddingBounds().y);
		renderer.rect(getPaddingBounds().x, getPaddingBounds().y, getPaddingBounds().width, getPaddingBounds().height);
		
		
		renderer.setColor(ContentColor);
//		renderer.line(0, 0, getContentBounds().x, getContentBounds().y);
		renderer.rect(getContentBounds().x, getContentBounds().y, getContentBounds().width, getContentBounds().height);
		renderer.set(ShapeType.Line);
		
		if (debugMouseBounds != null) {
			renderer.setColor(DebugHighlightColor);
			renderer.rect(debugMouseBounds.x, debugMouseBounds.y, debugMouseBounds.width, debugMouseBounds.height);
		}
		renderDebugComponent(renderer);
		Gdx.gl.glDisable(GL20.GL_BLEND);

	}
	protected abstract void renderDebugComponent(ShapeRenderer renderer);
	
	protected String debugTooltipText() {
		String text = "";
		if (!Utils.NullOrEmpty(name)) {
			text = name + " : " + getClass().getSimpleName() + "\n";
		}
		text += "Mouse Coordiantes: (" + mX + ", " + mY + ")\n";
		text += "Content Bounds: " + ContentBounds.x + ", " + ContentBounds.y + ", " + ContentBounds.width + ", " + ContentBounds.height + "\n";
		text += "Padding Bounds: " + PaddingBounds.x + ", " + PaddingBounds.y + ", " + PaddingBounds.width + ", " + PaddingBounds.height + "\n";
		text += " Margin Bounds: " + MarginBounds.x + ", " + MarginBounds.y + ", " + MarginBounds.width + ", " + MarginBounds.height + "\n";

		return text + debugTooltipTextComponent();
	}
	protected String debugTooltipTextComponent() {return "*" + getClass().getSimpleName() + " missing debug tooltip*";}

	
	// ---------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------- User Input -----------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------
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
	
	// ---------------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------- Text --------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------
	protected GuiUtils.Justify justify = GuiUtils.Justify.Left;
	protected String text;
	protected BitmapFont font;
	protected Color textColor;
	protected float textWidth;
	protected float textHeight;
	protected GlyphLayout glyphLayout;
	protected boolean renderText = false;
	private int fontSize;
	private String fontStyle = "font/OpenSansRegular.ttf";
	
	public void setText(String text) {
		if (text == null) {
			Logger.Error(getClass(), "setText", "Will not set text to null value.");
			return;
		}
		
		this.text = text;
		glyphLayout.setText(font, text);
		textWidth = glyphLayout.width;
		textHeight = glyphLayout.height;
		setTextJustify(justify);
	}
	
	public void setFontSize(int fontSize) {
		Logger.Debug(getClass(), "setFontSize", "Setting font size to: " + fontSize);
		this.fontSize = fontSize;
		font = AssetManager.getFont(fontStyle, fontSize).getFont();
		setTextJustify(justify);
		invalidate();
	}
	
	public void setTextJustify(GuiUtils.Justify mode) {
		justify = mode;
	}
	
	public void setTextColor(Color c) {
		textColor = c;
	}
	
	
	// ---------------------------------------------------------------------------------------------------------------------
	// ----------------------------------------------Bounding Box Calculations----------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------
	protected boolean validLayout = false;
	
	private float marginTop, marginRight, marginBottom, marginLeft;
	private float paddingTop, paddingRight, paddingBottom, paddingLeft;
	
	protected float minimumWidth, minimumHeight;
	
	private Rectangle MarginBounds;
	private Rectangle PaddingBounds;
	private Rectangle ContentBounds;
	
	public void invalidate() {
		validLayout = false;
	}
	
	public boolean pack(Rectangle AbsoluteBounds) {
		calculateBoundsMargin(AbsoluteBounds);
		calculateBoundsPadding();
		calulcateBoundsContent();
		validLayout = packComponent();
		return validLayout;
	}
	protected abstract boolean packComponent();
	
	
	public void setPosition(float x, float y) {
		getMarginsBounds().x = x;
		getMarginsBounds().y = y;
		pack(getMarginsBounds());
		
	}
	protected abstract void setPositionComponent(float x, float y);
	
	
	public Rectangle getPreferredContentSize() {
		return calculateMinContentBounds();
	}
	public Rectangle getPreferredSize() {
		calculateMinContentBounds();
		calculateBoundsPaddingReverse();
		return calculateBoundsMarginReverse();
	}
	
	private Rectangle calculateBoundsMargin(Rectangle AbsoluteBounds) {
		MarginBounds.x = AbsoluteBounds.x;
		MarginBounds.y = AbsoluteBounds.y;
		MarginBounds.width = AbsoluteBounds.width;
		MarginBounds.height = AbsoluteBounds.height;
		return MarginBounds;
	}
	
	private Rectangle calculateBoundsMarginReverse() {
		MarginBounds.x = PaddingBounds.x - marginLeft;
		MarginBounds.y = PaddingBounds.y - marginBottom;
		MarginBounds.width = PaddingBounds.width + marginLeft + marginRight;
		MarginBounds.height = PaddingBounds.height + marginTop + marginBottom;
		return MarginBounds;
	}
	
	private Rectangle calculateBoundsPadding() {
		PaddingBounds.x = MarginBounds.x + marginLeft;
		PaddingBounds.y = MarginBounds.y + marginBottom;
		PaddingBounds.width = MarginBounds.width - marginLeft - marginRight;
		PaddingBounds.height = MarginBounds.height - marginTop - marginBottom;
		return PaddingBounds;
	}
	private Rectangle calculateBoundsPaddingReverse() {
		PaddingBounds.x = ContentBounds.x - paddingLeft;
		PaddingBounds.y = ContentBounds.y - paddingBottom;
		PaddingBounds.width = ContentBounds.width + paddingLeft + paddingRight;
		PaddingBounds.height = ContentBounds.height + paddingTop + paddingBottom;
		return PaddingBounds;

	}
	
	private Rectangle calulcateBoundsContent() {
		ContentBounds.x = PaddingBounds.x + marginLeft;
		ContentBounds.y = PaddingBounds.y + marginBottom;
		ContentBounds.width = PaddingBounds.width - marginLeft - marginRight;
		ContentBounds.height = PaddingBounds.height - marginTop - marginBottom;
		return ContentBounds;
	}
	
	private Rectangle calculateMinContentBounds() {
		ContentBounds = calculateMinContentBoundsComponent();
		return ContentBounds;
	}
	protected abstract Rectangle calculateMinContentBoundsComponent();
	
	
	public Rectangle getMarginsBounds() {
		return MarginBounds;
	}
	public Rectangle getPaddingBounds() {
		return PaddingBounds;
	}
	public Rectangle getContentBounds() {
		return ContentBounds;
	}
		
	
	
	
	
	// ---------------------------------------------------------------------------------------------------------------------
	// -------------------------------------------------Getters / Setters --------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------
	public Expand getExpand() {
		return expand;
	}
	public void setExpand(Expand ex) {
		if (ex != getExpand())
			invalidate();
		expand = ex;
	}	
	
	public void setHighlightEnabled(boolean enabled) {
		HighlightEnabled = enabled;
	}
	public void setActiveEnabled(boolean enabled) {
		ActiveEnabled = enabled;
	}
	
	public void setBackgroundTexture(Texture background) {
		if (background != null) {
			this.background = background;
		} else {
			Logger.Error(getClass(), "setBackgroundTexture", "Failed to set background texture. Given texture was null.");
			renderBackground = false;
		}
	}	
	public void setRenderBackground(boolean value) {
		renderBackground = value;
	}
	
	
	public void setCornerTexture(String texturePath) {
		Texture texture = AssetManager.getTexture(texturePath).getTexture();
		if (texture != null)
			setCornerTexture(texture);
	}
	public void setCornerTexture(Texture corner) {
		cornerTexture = corner;
	}
	public void setRenderCorners(boolean render) {
		if (render) {
			if (cornerTexture != null)
				renderCorners = render;
			else
				Logger.Error(getClass(), "SetRenderCorner", "Cannot enable rendering corner before setting corner texture.");			
		} else {
			renderCorners = false;
		}
	}
	
	
	private Color createColor(Color c, float opacity) {
		return new Color(c.r, c.g, c.b, opacity);
	}
	public void setBackgroundColor(Color backgroundTint) {
		this.defaultColor = createColor(backgroundTint, backgroundOpacity);
	}
	public void setCornerColor(Color color) {
		cornerColor = createColor(color, cornerOpacity);
	}
	public void setActiveColor(Color c) {
		activeColor = createColor(c, ActiveOpacity);
	}
	public void setHighlightColor (Color c) {
		highlightColor = createColor(c, HighlightOpacity);
	}
	
	
	
	public void setBackgroundOpacity(float opacity) {
		backgroundOpacity = opacity;
		setBackgroundColor(defaultColor);
	}
	public void setCornerOpacity(float opacity) {
		cornerOpacity = opacity;
		setCornerColor(cornerColor);
	}
	public void setActiveOpacity(float opacity) {
		ActiveOpacity = opacity;
		setActiveColor(activeColor);
	}
	public void setHighlighOpacity(float opacity) {
		HighlightOpacity = opacity;
		setHighlightColor(highlightColor);
	}
	
	public int getFontSize() {
		return fontSize;
	}
	
	
	/// PADDING
	public void setPadding(float padding) {
		setPaddingHorizontal(padding);
		setPaddingVertical(padding);
	}
	public void setPaddingHorizontal(float padding) {
		setPaddingL(padding);
		setPaddingR(padding);
	}
	public void setPaddingVertical(float padding) {
		setPaddingT(padding);
		setPaddingB(padding);
	}
	public void setPaddingT(float paddingT) {
		this.paddingTop = GUI.convertY(paddingT);
		invalidate();
	}
	public void setPaddingR(float paddingR) {
		this.paddingRight = GUI.convertY(paddingR);
		invalidate();
	}
	public void setPaddingB(float paddingB) {
		this.paddingBottom = GUI.convertY(paddingB);
		invalidate();
	}
	public void setPaddingL(float paddingL) {
		this.paddingLeft = GUI.convertY(paddingL);
		invalidate();
	}

	/// MARGIN
	public void setMargin(float margin) {
		setMarginHorizontal(margin);
		setMarginVertical(margin);
	}
	public void setMarginHorizontal(float margin) {
		setMarginL(margin);
		setMarginR(margin);
	}
	public void setMarginVertical(float margin) {
		setMarginT(margin);
		setMarginB(margin);
	}
	public void setMarginT(float marginT) {
		this.marginTop = GUI.convertY(marginT);
		invalidate();
	}
	public void setMarginR(float marginR) {
		this.marginRight = GUI.convertX(marginR);
		invalidate();
	}
	public void setMarginB(float marginB) {
		this.marginBottom = GUI.convertY(marginB);
		invalidate();
	}
	public void setMarginL(float marginL) {
		this.marginLeft = GUI.convertX(marginL);
		invalidate();
	}
}
