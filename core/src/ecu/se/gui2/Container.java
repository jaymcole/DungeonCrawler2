package ecu.se.gui2;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import ecu.se.Logger;
import ecu.se.assetManager.AssetManager;
import ecu.se.gui2.GuiUtils.Expand;

public class Container extends Component{
	protected LinkedList<Component> children;
	protected GuiUtils.Layout layout;
	
	public Container() {
		super();
		setBackgroundTexture(AssetManager.getTexture("texture/misc/white.png").getTexture());
		layout = GuiUtils.Layout.Horizontal;
		children = new LinkedList<Component>();
		canConsumeUI = true;
	}
		
	@Override
	protected Component updateComponent(Component consumer, float deltaTime, int mouseX, int mouseY) {
		for (Component child : children) {
			consumer = child.update(consumer, deltaTime, mouseX, mouseY);
		}	
		return consumer;
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
	
	public void pack(float x, float y) {
		calculateMinDimensions();
		this.setOriginX(x);
		this.setOriginY(y);
		resize();
	}

	public void resize() {	
		if (parent != null) {
			if (minimumWidth > parent.getChildAvailableWidth())
				Logger.Warning(getClass(), "resize", "Minimum Width is greater than parent's available space");
			if (minimumHeight > parent.getChildAvailableHeight())
				Logger.Warning(getClass(), "resize", "Minimum Height is greater than parent's available space");			
		}
		
		calculateMinComponentDimensions();
						
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
	}	
	
	public void resizeHorizontal(LinkedList<Component> children) {
		float previousX = getChildX();
		float previousY = getChildY();
		
		float availableHorizontalSpace = getChildAvailableWidth();
		int autoExpandingChildren = 0;
		for(Component child : children) {
			if (child.expandSettings == GuiUtils.Expand.UseMinimumSize) {
				availableHorizontalSpace -= child.getMinimumWidth();
			} else {
				autoExpandingChildren++;
			}
		}
		calculateMinDimensions();
		for (Component child : children) {			
			if (child.expandSettings == GuiUtils.Expand.UseMinimumSize) {
				child.setOriginY(previousY + (contentHeight() * 0.5f) - (child.getHeight() * 0.5f));
				child.setOriginX(previousX);
				
				child.setMaxWidth(child.minimumWidth);
				child.setMaxHeight(getChildAvailableHeight());
			}else {
				child.setOriginY(previousY);
				child.setOriginX(previousX);
				child.setMaxWidth(availableHorizontalSpace / autoExpandingChildren);
				child.setMaxHeight(getChildAvailableHeight());
			}
			previousX += child.getWidth();
			
			if (child instanceof Container)
				((Container)child).resize();
		}
	}
	
	public void resizeVertical(LinkedList<Component> children) {
		float previousX = getChildX();
		float previousY = getChildY();
		
		
		float availableVerticalSpace = getChildAvailableHeight();
		int autoExpandingChildren = 0;
		for(Component child : children) {
			if (child.expandSettings == GuiUtils.Expand.UseMinimumSize) {
				availableVerticalSpace -= child.getMinimumHeight();
			} else {
				autoExpandingChildren++;
			}
		}
		
		for (Component child : children) {
			if (child.expandSettings == GuiUtils.Expand.UseMinimumSize) {
				child.setOriginX(previousX + (contentWidth() * 0.5f) - (child.getWidth() * 0.5f));
				child.setOriginY(previousY);
				
				child.setMaxHeight(child.minimumHeight);
				child.setMaxWidth(getChildAvailableWidth());
			}else {
				child.setOriginX(previousX);
				child.setOriginY(previousY);
				child.setMaxHeight(availableVerticalSpace / autoExpandingChildren);
				child.setMaxWidth(getChildAvailableWidth());
			}
			previousY += child.getHeight();
			
			if (child instanceof Container)
				((Container)child).resize();
		}		
	}
	
	
	
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
	public void calculateMinComponentDimensions() {
		minimumWidth = 5;
		minimumHeight = 5;
		if (children == null)
			return;
		
		for (Component child : children) {
			child.calculateMinDimensions();
			switch (layout) {
			case Horizontal:		
				if (child.getMinimumHeight() > minimumHeight)
					minimumHeight = child.getMinimumHeight();
				minimumWidth += child.getMinimumWidth();
				break;
			case Vertical:
				if (child.getMinimumWidth() > minimumWidth)
					minimumWidth = child.getMinimumWidth();
				minimumHeight += child.getMinimumHeight();
				break;
//				case Horizontal:		
//					if (child.getHeight() > minimumHeight)
//						minimumHeight = child.getHeight();
//					minimumWidth += child.getWidth();
//					break;
//				case Vertical:
//					if (child.getWidth() > minimumWidth)
//						minimumWidth = child.getWidth();
//					minimumHeight += child.getHeight();
//					break;
				default:
					Logger.Error(getClass(), "calculateMinDimensions", "Missing case for \"" + layout.toString() + "\". Setting minimum dimensions to 0." );
			}
		}
		
		minimumWidth += getSurroundingWidth();
		minimumHeight += getSurroundingHeight();
		
	}

	public LinkedList<Component> getChildren() {
		return children;
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY) {
		// TODO Auto-generated method stub
	}



}
