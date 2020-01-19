package ecu.se.gui2;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ecu.se.Game;
import ecu.se.Logger;
import ecu.se.ObjectManager;
import ecu.se.map.Map;
import ecu.se.objects.ActiveItem;
import ecu.se.objects.ItemObject;

public class GuiItemSlot extends GuiButton{

	protected ItemObject item;
	private TextureRegion itemTexture;
	private float itemX, itemY;
	private int MPCX, MPCY; // Mouse Pressed Coordinates X and Y
	private boolean full;
	
	public GuiItemSlot(String text) {
		super(text);
	}
	
	@Override
	public void mousePressed(int mouseX, int mouseY) {
		MPCX = mouseX;
		MPCY = mouseY;
		itemX = mouseX + mouseOffsetX;
		itemY = mouseY + mouseOffsetY;
	}

	@Override
	public void mouseDown(int mouseX, int mouseY) {
		if (isActive) {
			itemX = mouseX + mouseOffsetX;
			itemY = mouseY + mouseOffsetY;
		}
	}

	public void act(int mouseX, int mouseY) {
		if (item instanceof ActiveItem) {
			((ActiveItem) item).getAction().act(mouseX, mouseY);
			if (!item.isAlive())
				removeItem();

		}
	}

	@Override
	public void specialActions(float deltaTime, int mouseX, int mouseY) {
		if (item != null)
			item.update(deltaTime);
		if (item != null && !item.isAlive()) 
			removeItem();
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY) {
		// Active item if it's an action
		if (mouseX == MPCX && mouseY == MPCY)
			act(mouseX, mouseY);
		// Move item (to the ground, to a new slot, etc)
		else if (item != null) {
			Logger.Debug(this.getClass(), "mouseReleased","Mouse Released");
//			for (Window window : GUI.getActiveWindows()) {
//				for (Widget widget : window.getChildren()) {
//					if (widget.contains(mouseX, mouseY)) {
//						if (widget != this && widget instanceof Widget_ItemSlot) {
//							ItemObject movingObject = this.item;
//							this.removeItem();
//							((Widget_ItemSlot) widget).setItem(movingObject);
//							// return;
//						}
//
//					}
//				}
//			}
			// dropItem(mouseX, mouseY);
		}
		itemX = getChildX();
		itemY = getChildY();
	}
	
	/**
	 * Drops the item on the ground at world coordinates (x, y)
	 * 
	 * @param mouseX
	 * @param mouseY
	 */
	public void dropItem(int mouseX, int mouseY) {
		item.setPosition(
				(int) (Game.player.getX() + mouseX - item.getBounds().getBoundingRectangle().getWidth() * 0.5f),
				(int) (Game.player.getY() + mouseY - item.getBounds().getBoundingRectangle().getHeight() * 0.5f));
		Map.getTile((int) Game.player.getX(), (int) Game.player.getY()).addObject(item);
		this.removeItem();
	}
	
	/**
	 * Removes the item from this slot.
	 */
	public void removeItem() {
		item = null;
		itemTexture = null;
		full = false;
		onRemoveItem();
	}

	/**
	 * Called when an item is removed.
	 */
	public void onRemoveItem() {
	}

	public void setItem(ItemObject item) {
		// TODO: If this.item != null, send this.item inventory
		if (this.item != null) {
//			((Window_Inventory) GUI.getWindow(GUI.WINDOW_INVENTORY)).insertItem(this.item);
		}

		ObjectManager.remove(item);
		this.item = item;
		itemX = getChildX();
		itemY = getChildY();
		this.itemTexture = item.getTextureRegion();
		full = true;
		onSetItem();
	}

	/**
	 * Called when a new item is set.
	 */
	public void onSetItem() {
	}

	public boolean isFull() {
		return full;
	}

}
