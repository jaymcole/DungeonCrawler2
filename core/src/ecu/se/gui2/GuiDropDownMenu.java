package ecu.se.gui2;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import ecu.se.Logger;
import ecu.se.assetManager.AssetManager;
import ecu.se.gui2.GuiUtils.Expand;
import ecu.se.gui2.GuiUtils.Layout;

public class GuiDropDownMenu extends Container{

	private Container MenuItems;
	
	public GuiDropDownMenu(String text) {
		super();
		setBackgroundTexture(AssetManager.getTexture("texture/misc/white.png").getTexture());
		layout = GuiUtils.Layout.Horizontal;
		children = new LinkedList<Component>();
		MenuItems = new Container();
		MenuItems.setLayout(Layout.Vertical);
		GuiLabel title = new GuiLabel(text);
		title.setTextJustify(GuiUtils.Justify.Center);
		children.add(title);
		
		GuiLabel iconPlaceHolder = new GuiLabel("[*]");
		iconPlaceHolder.setTextJustify(GuiUtils.Justify.Center);
		iconPlaceHolder.setExpandSettings(Expand.UseMinimumSize);
		children.add(iconPlaceHolder);
	}
		
	@Override
	protected Component updateComponent(Component consumer, float deltaTime, int mouseX, int mouseY) {
		for (Component child : children) {
			// Passing true to disable mouse clicks on title and icon
			child.update(consumer, deltaTime, mouseX, mouseY);
		}	
		
		if (MenuItems.isActive) {
			MenuItems.update(consumer, deltaTime, mouseX, mouseY);
		}
		
		return consumer;//consumeInput(consumer, mouseX, mouseY);
	}
	
	public void addChild(Component child) {
		child.setExpandSettings(Expand.UseMinimumSize);
		MenuItems.addChild(child);
	}
	
	public void setLayout(GuiUtils.Layout layout) {
		Logger.Error(getClass(), "setLayout", "Cannot change layout for context menus.");
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
		MenuItems.pack(0, 0);
		MenuItems.pack(originX, originY - MenuItems.getHeight());
	}

	public void resize() {	
		if (parent != null) {
			if (minimumWidth > parent.getChildAvailableWidth())
				Logger.Warning(getClass(), "resize", "Minimum Width is greater than parent's available space");
			if (minimumHeight > parent.getChildAvailableHeight())
				Logger.Warning(getClass(), "resize", "Minimum Height is greater than parent's available space");			
		}

		resizeHorizontal(children);
	}	
	
	@Override
	protected void renderComponent(SpriteBatch batch) {
		for (Component child : children) {
			child.render(batch);
		}		
		
		if (isActive)
			MenuItems.render(batch);
	}
	
	public void mousePressed(int mouseX, int mouseY){
		this.isActive = !isActive;
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

	public LinkedList<Component> getChildren() {
		return children;
	}

	
	
}
