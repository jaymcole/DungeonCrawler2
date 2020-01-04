package ecu.se.gui2;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import ecu.se.Logger;
import ecu.se.archive.Archiver;
import ecu.se.archive.TimeRecords;
import ecu.se.gui.GUI;

public abstract class Component {
	public float preferredWidth, preferredHeight;

	private float x, originX, y, originY;
	private float paddingT, paddingR, paddingB, paddingL;
	private float marginT, marginR, marginB, marginL;
	private float borderThickness;
	private float width, height;
	
	private Color backgroundTint = Color.WHITE;
	private static Color debugMarginBorderColor = Color.BLUE;
	private static Color debugPaddingBorderColor = Color.RED;
	private static Color debugBorderColor = Color.DARK_GRAY;
	
	
	private Texture background;
	private float backgroundOpacity = 0.5f;
	private boolean renderBackground = true;
		
	public Component parent;
	
	protected boolean isVisible = true;
	protected float minimumWidth;
	protected float minimumHeight;
	
	protected boolean isValidLayout;
		
	public Component () {
		Random random = new Random();
		setBackgroundColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255),1));
		borderThickness = 0;
		isVisible = true;
	}
	
	/**
	 * 
	 * @param deltaTime
	 * @param mouseX
	 * @param mouseY
	 * @return
	 */
	public boolean update(float deltaTime, int mouseX, int mouseY) {
		if (!isVisible)
			return false;
		
		Rectangle rectangle = getBounds();
		
		if (rectangle.contains(mouseX, mouseY)) {
			LogComponentData();
			//Logger.Debug(getClass(), "update", "");
		}
		return false;
	}
	
	public void act() {
		
	}
	
	
	public void render(SpriteBatch batch) {
		if (!isVisible)
			return;
		if (renderBackground && background != null) {
			Rectangle rect = getBounds();
			batch.setColor(backgroundTint);
			batch.draw(background, rect.x, rect.y, rect.width, rect.height);
			batch.setColor(Color.WHITE);
		}
		renderComponent(batch);
	}	
	
	protected abstract void renderComponent(SpriteBatch batch);
	
	
	public void calculateMinDimensions() {
		calculateMinComponentDimensions();
		minimumWidth += paddingL + paddingR + marginL + marginR + (borderThickness * 2);
	}
	protected abstract void calculateMinComponentDimensions();
	
	void invalidate() {
		isValidLayout = false;
	}
	
	public boolean validate() {
		return isValidLayout;
	}
	
	public void debugRender(ShapeRenderer shapeRenderer) {
		Rectangle rect;
		shapeRenderer.setColor(debugBorderColor);
		shapeRenderer.rect(getX(), getY(), getWidth(), getHeight());
		rect = getBounds();
		shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
		
		shapeRenderer.setColor(debugMarginBorderColor);
		shapeRenderer.rect(originX, originY, getMarginWidth(), getMarginHeight());
		rect = getMarginBounds();
		shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
		
		shapeRenderer.setColor(debugPaddingBorderColor);
		shapeRenderer.rect(getX() + getPaddingL(), getY() + getPaddingT(), getPaddedWidth(), getPaddedHeight());
		rect = getPaddedBounds();
		shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
		
		debugRenderComponent(shapeRenderer);
		shapeRenderer.setColor(Color.WHITE);
	}
	
	protected abstract void debugRenderComponent(ShapeRenderer shapeRenderer);
	
	public void dispose() {
		disposeComponent();
	}
	
	protected abstract void disposeComponent();
	
	
	public void setIsVisible(boolean visible) {
		isVisible = visible;
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
		return new Rectangle(getX(), getY(), getComponentWidth(), getComponentHeight());
	}
	public Rectangle getMarginBounds() {
		return new Rectangle(originX, originY, getMarginWidth(), getMarginHeight());
	}	
	public Rectangle getPaddedBounds() {
		return new Rectangle(getChildX(), getChildY(), getPaddedWidth(), getPaddedHeight());
	}
	
	protected float getComponentWidth() {
		return getWidth() - marginL - marginR;
	}
	public float getWidth() {
		return width;
	}
	public void setWidth(float w) {
		width = w;
	}
	
	protected float getComponentHeight() {
		return getHeight() - marginT - marginB;
	}
	public float getHeight() {
		return height;
	}
	public void setHeight(float h) {
		height = h;
	}
	public float getChildAvailableWidth() {
		return getPaddedWidth();
	}
	public float getChildAvailableHeight() {
		return getPaddedHeight();
	}
	public float getMinimumWidth() {
		return minimumWidth;
	}
	public float getMinimumHeight() {
		return minimumHeight;
	}
	
	/// X Coordinates
	protected float getX() {
		return (originX + marginL + borderThickness);
	}
	public float getChildX() {
		return getX() + paddingL;
	}
	public void setOriginX(float originX) {
		this.originX = originX;
	}
	
	/// Y Coordinates
	protected float getY() {
		return (originY + marginT + borderThickness);
	}
	public float getChildY() {
		return getY() + paddingL;
	}
	public void setOriginY(float originY) {
		this.originY = originY;
		
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
		return getWidth() - getMarginL() - getBorderThickness() - getPaddingL() - getPaddingR() - getBorderThickness() - getMarginR();
	}
	private float getPaddedHeight() {
		return getHeight() - getMarginT() - getBorderThickness() - getPaddingT() - getPaddingB() - getBorderThickness() - getMarginB();
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
		return getWidth();// - getMarginR() - getMarginL();
	}
	private float getMarginHeight() {
		return getHeight();// - getMarginT() - getMarginB();
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
		Logger.Debug(getClass(), "resize", "\t X: " + getX());
		Logger.Debug(getClass(), "resize", "\t Y: " + getY());
		Logger.Debug(getClass(), "resize", "\t originX: " + originX);
		Logger.Debug(getClass(), "resize", "\t originY: " + originY);
		Logger.Debug(getClass(), "resize", "\t Width: " + getWidth());
		Logger.Debug(getClass(), "resize", "\t Height: " + getHeight());
		Logger.Debug(getClass(), "resize", "\t AvailableChildWidth: " + getChildAvailableWidth());
		Logger.Debug(getClass(), "resize", "\t AvailableChildHeight: " + getChildAvailableHeight());
		Logger.Debug(getClass(), "resize", "\t Margins: T" + getMarginT() + ", R" + getMarginR()+", B" + getMarginB()+", L" + getMarginL());
		Logger.Debug(getClass(), "resize", "\t Padding: T" + getPaddingT() + ", R" + getPaddingR()+", B" + getPaddingB()+", L" + getPaddingL());
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
	
	
}
