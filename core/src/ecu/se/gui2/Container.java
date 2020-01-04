package ecu.se.gui2;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import ecu.se.Logger;
import ecu.se.assetManager.AssetManager;

public class Container extends Component{
	private LinkedList<Component> children;
	private GuiUtils.Layout layout;
//	private int Columns = 1, Rows = 1;
	
	public Container() {
		super();
		setBackgroundTexture(AssetManager.getTexture("texture/misc/white.png").getTexture());
		layout = GuiUtils.Layout.Horizontal;
		children = new LinkedList<Component>();
	}
	
	public void addChild(Component child) {
		if (!children.contains(child)) {
			children.add(child);
			child.parent = this;
			invalidate();
		} else
			Logger.Error(getClass(), "addChild", "Failed to add child of type:" + child.getClass().getSimpleName() + " to " + getClass().getSimpleName());
	}
	
	public void setLayout(GuiUtils.Layout layout) {
		if(layout != this.layout)
			invalidate();
		this.layout = layout;
	}

	@Override
	public boolean validate() {
		boolean valid = true;
		for (Component child : children) {
			child.validate();
			if (!child.isValidLayout)
				resize();
		}
		return valid;
	}
	

	public void resize() {	
//		if (parent == null)
//			calculateMinDimensions();
		LogComponentData();

		if (parent != null) {
			if (minimumWidth > parent.getChildAvailableWidth())
				Logger.Warning(getClass(), "resize", "Minimum Width is greater than parent's available space");
			if (minimumHeight > parent.getChildAvailableHeight())
				Logger.Warning(getClass(), "resize", "Minimum Width is greater than parent's available space");			
		}
		
		switch (layout) {
			case Horizontal:
				resizeHorizontal(children);
				break;
			case Vertical:
				resizeVertical(children);
				break;
			default:
				Logger.Error(getClass(), "Resize", "Missing case for \"" + layout.toString() + "\". Defaulting to " + GuiUtils.Layout.Horizontal );
		}
		Logger.Debug(getClass(), "resize", "Resizing components");
		LogComponentData();
	}
	
	
	public void resizeHorizontal(LinkedList<Component> children) {
		float previousX = getChildX();
		float previousY = getChildY();
		for (Component child : children) {
			child.setOriginX(previousX);
			child.setOriginY(previousY);
			child.setHeight(getChildAvailableHeight());
			child.setWidth(getChildAvailableWidth() / children.size());
			previousX += child.getWidth();
			if (child instanceof Container)
				((Container)child).resize();
			Logger.Debug(getClass(), "resizeHorizontal", "Placing child at:");
			Logger.Debug(getClass(), "resizeHorizontal", "\t X:" + child.getX() +", Y:" + child.getY());
		}		
	}
	
	public void resizeVertical(LinkedList<Component> children) {
		float previousX = getChildX();
		float previousY = getChildY();
		for (Component child : children) {
			child.setOriginX(previousX);
			child.setOriginY(previousY);
			child.setHeight(getChildAvailableHeight() / children.size());
			child.setWidth(getChildAvailableWidth());
			previousY += child.getHeight();
			if (child instanceof Container)
				((Container)child).resize();
			
			Logger.Debug(getClass(), "resizeVertical", "Placing child at:");
			Logger.Debug(getClass(), "resizeVertical", "\t X:" + child.getX() +", Y:" + child.getY());

		}		
	}
	
//	private float calculateCenter(float componentLength, float availableLength) {
//		return (availableLength * 0.5f) - (componentLength * 0.5f);
//	}
	
	
	@Override
	protected void renderComponent(SpriteBatch batch) {
		for (Component child : children) {
			child.render(batch);
		}		
	}

	@Override
	protected void debugRenderComponent(ShapeRenderer shapeRenderer) {
		for (Component child : children) {
			child.debugRenderComponent(shapeRenderer);
		}	
		
	}

	@Override
	protected void disposeComponent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean update(float deltaTime, int mouseX, int mouseY) {
		if (!isVisible)
			return false;
		boolean consumeUI = false;
		for (Component child : children) {
			if (child.update(deltaTime, mouseX, mouseY))
				consumeUI = true;
		}	
		return getBounds().contains(mouseX, mouseY) || consumeUI;
	}
	

	@Override
	public void calculateMinComponentDimensions() {
		minimumWidth = 0;
		minimumHeight = 0;
		if (children == null)
			return;
		
		for (Component child : children) {
			child.calculateMinDimensions();
		switch (layout) {
			case Horizontal:		
				minimumWidth += child.getWidth();
				if (child.getHeight() > minimumHeight)
					minimumHeight = child.getHeight();
				break;
			case Vertical:
				if (child.getWidth() > minimumWidth)
					minimumWidth = child.getWidth();
				minimumHeight += child.getHeight();
				break;
			default:
				Logger.Error(getClass(), "calculateMinDimensions", "Missing case for \"" + layout.toString() + "\". Setting minimum dimensions to 0." );
		}
	}
		
		
		
	}


}
