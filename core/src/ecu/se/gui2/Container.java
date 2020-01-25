package ecu.se.gui2;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import ecu.se.Logger;
import ecu.se.assetManager.AssetManager;

public class Container extends Component{
	protected LinkedList<Component> children;
	protected Layout layout;
	
	public Container() {
		super();
		layout = Layout.Vertical;
		setLayout(Layout.Vertical);
		children = new LinkedList<Component>();
	}
	
	public void addChild(Component child) {
		if (child == this) {
			Logger.Error(getClass(), "addChild", "You can't add a container to itself. That doesn't make any kind of sense.");
		} else if (!children.contains(child)) {
			children.addFirst(child);
			child.parent = this;
			invalidate();
		} else
			Logger.Error(getClass(), "addChild", "Failed to add child of type:" + child.getClass().getSimpleName() + " to " + getClass().getSimpleName());
	}

	@Override
	protected Component updateComponent(Component consumer, float deltaTime, int mouseX, int mouseY) {
		for (Component child : children) {
			consumer = child.update(consumer, deltaTime, mouseX, mouseY);
		}
		return consumer;
	}

	@Override
	protected void renderComponent(SpriteBatch batch) {
		for (Component child : children) {
			child.render(batch);
		}
	}

	@Override
	protected void renderDebugComponent(ShapeRenderer renderer) {
		for (Component child : children) {
			child.renderDebug(renderer);
		}
	}

	@Override
	protected boolean packComponent() {
		if (layout == Layout.Vertical) 
			return packComponentVertical();
		return packComponentHorizontal();
	}
	
	// TODO: These two (packComponentHorizontal, packComponentVertical) methods need cleaning up.
	private boolean packComponentHorizontal() {
		validLayout = true;		
		float availableWidth=getContentBounds().width, availableHeight=getContentBounds().height;
		int NumberOfExpandingChildren = 0;
		int NumberOfAutoChildren = 0;
		int totalHeightUsed = 0;
		int totalWidthUsed = 0;
		for (Component child : children) {
			if (!child.AbsolutePositioning) {
				// All children are now using minimum bounds (this is important in the next loop)
				Rectangle childBounds = child.getPreferredSize();
				if (child.expand == Expand.Auto) {
					availableWidth -= childBounds.width;
					NumberOfAutoChildren++;
				}else if (child.expand == Expand.DoNotExpand) {
					availableWidth -= childBounds.width;		
				} else {
					NumberOfExpandingChildren++;
				}
			}
		}

		float currentY = getContentBounds().y;
		float currentX = getContentBounds().x;
		
		for (Component child : children) {
			if (!child.AbsolutePositioning) {
				Rectangle childBounds = new Rectangle();
				if (child.expand == Expand.DoNotExpand) {
					childBounds = child.getPreferredSize();
					// TODO: Looks like width isn't being calculated correctly - at least not for buttons.
				} else if(child.expand == Expand.Auto) {
					childBounds = child.getPreferredSize();
					
					// Temporary Hack to get width - width should be determined by 
					childBounds.height = getContentBounds().height;
				} else if (child.expand == Expand.ExpandAll) {
					childBounds.x = currentX;
					childBounds.y = currentY;
					childBounds.width = availableWidth / NumberOfExpandingChildren;
					childBounds.height = availableHeight;
				}
				
				childBounds.x = currentX;
				childBounds.y = currentY;
				if (!child.pack(childBounds)) 
					validLayout = false;
				totalWidthUsed += child.getMarginsBounds().width;
				totalHeightUsed += child.getMarginsBounds().height;
	
				currentX += childBounds.width;
			}
		}
		
		
		float spaceToShareAmongAutoElements = 0;
		if (layout == Layout.Vertical && NumberOfAutoChildren > 0) {
			if (totalHeightUsed < getContentBounds().height) {
				spaceToShareAmongAutoElements = (getContentBounds().height - totalHeightUsed) / NumberOfAutoChildren;
			}
			
		} else if (layout == Layout.Horizontal && NumberOfAutoChildren > 0) {
			if (totalWidthUsed < getContentBounds().width) {
				spaceToShareAmongAutoElements = (getContentBounds().width - totalWidthUsed) / NumberOfAutoChildren;
			}
		}
		
		Rectangle previousBounds = null;
		for (Component child : children) { 
			if (!child.AbsolutePositioning) {
				if (child.expand == Expand.Auto) {				
					if (layout == Layout.Vertical) {
						child.getMarginsBounds().height += spaceToShareAmongAutoElements;
						if (previousBounds != null) {
							child.getMarginsBounds().y = previousBounds.y + previousBounds.height;
						}
					} else if (layout == Layout.Horizontal) {
						child.getMarginsBounds().width += spaceToShareAmongAutoElements;
						if (previousBounds != null) {
							child.getMarginsBounds().x = previousBounds.x + previousBounds.width;
						}
					}
					child.pack(child.getMarginsBounds());
					previousBounds = child.getMarginsBounds();
				}
			}
		}

		return validLayout;
	}
	
	private boolean packComponentVertical() {
		validLayout = true;		
		float availableWidth=getContentBounds().width, availableHeight=getContentBounds().height;
		int NumberOfExpandingChildren = 0;
		int NumberOfAutoChildren = 0;
		int totalHeightUsed = 0;
		int totalWidthUsed = 0;
		for (Component child : children) {
			if (!child.AbsolutePositioning) {
				// All child are now using minimum bounds (this is important in the next loop)
				Rectangle childBounds = child.getPreferredSize();
				if (child.expand == Expand.Auto) {
					availableHeight -= childBounds.height;
					NumberOfAutoChildren++;
				}else if (child.expand == Expand.DoNotExpand) {
					availableHeight -= childBounds.height;		
				} else {
					NumberOfExpandingChildren++;
				}
			}
		}


		float currentY = getContentBounds().y;
		float currentX = getContentBounds().x;
		
		for (Component child : children) {
			if (!child.AbsolutePositioning) {

				Rectangle childBounds = new Rectangle();
				if (child.expand == Expand.DoNotExpand) {
					childBounds = child.getPreferredSize();
					// TODO: Looks like width isn't being calculated correctly - at least not for buttons.
				} else if(child.expand == Expand.Auto) {
					childBounds = child.getPreferredSize();
					
					// Temporary Hack to get width - width should be determined by 
					childBounds.width = getContentBounds().width;
				} else if (child.expand == Expand.ExpandAll) {
					childBounds.x = currentX;
					childBounds.y = currentY;
					childBounds.width = availableWidth;
					childBounds.height = availableHeight / NumberOfExpandingChildren;
				}
				
				childBounds.x = currentX;
				childBounds.y = currentY;
				if (!child.pack(childBounds)) 
					validLayout = false;
				totalWidthUsed += child.getMarginsBounds().width;
				totalHeightUsed += child.getMarginsBounds().height;
	
				currentY += childBounds.height;
			}
		}
		
		
		float spaceToShareAmongAutoElements = 0;
		if (layout == Layout.Vertical && NumberOfAutoChildren > 0) {
			if (totalHeightUsed < getContentBounds().height) {
				spaceToShareAmongAutoElements = (getContentBounds().height - totalHeightUsed) / NumberOfAutoChildren;
			}
			
		} else if (layout == Layout.Horizontal && NumberOfAutoChildren > 0) {
			if (totalWidthUsed < getContentBounds().width) {
				spaceToShareAmongAutoElements = (getContentBounds().width - totalWidthUsed) / NumberOfAutoChildren;
			}
		}
		
		Rectangle previousBounds = null;
		for (Component child : children) { 
			if (!child.AbsolutePositioning) {
				if (child.expand == Expand.Auto) {				
					if (layout == Layout.Vertical) {
						child.getMarginsBounds().height += spaceToShareAmongAutoElements;
						if (previousBounds != null) {
							child.getMarginsBounds().y = previousBounds.y + previousBounds.height;
						}
					} else if (layout == Layout.Horizontal) {
						child.getMarginsBounds().width += spaceToShareAmongAutoElements;
						if (previousBounds != null) {
							child.getMarginsBounds().x = previousBounds.x + previousBounds.width;
						}
					}
					child.pack(child.getMarginsBounds());
					previousBounds = child.getMarginsBounds();
				}
			}
			
		}

		return validLayout;
	}


	@Override
	protected void setPositionComponent(float x, float y) {
		for (Component child : children) {
			child.setPosition(x, y);
		}
	}

	@Override
	protected Rectangle calculateMinContentBoundsComponent() {
		float width = 0, height = 0;
		Rectangle childBounds;
		for (Component child : children) {
			childBounds = child.calculateMinContentBoundsComponent();
			width += childBounds.width;
			height += childBounds.height;
		}
		return new Rectangle(0,0,width, height);
	}
		
	// ---------------------------------------------------------------------------------------------------------------------
	// -------------------------------------------------Getters / Setters --------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------
	
	public void setLayout(Layout layout) {
		if(layout != this.layout)
			invalidate();
		this.layout = layout;
	}	
	
	public LinkedList<Component> getChildren() {
		return children;
	}

}
