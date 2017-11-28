package ecu.se.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ecu.se.Game;
import ecu.se.ObjectManager;
import ecu.se.map.Map;
import ecu.se.objects.ItemObject;

public class Widget_ItemSlot extends Widget_Button_Image {

	protected ItemObject item;
	private TextureRegion itemTexture;
	private float itemX, itemY;
	private int MPCX, MPCY; // Mouse Pressed Coordinates X and Y
	private boolean full;

	public Widget_ItemSlot(float x, float y, float width, float height, Window parent, TextureRegion defaultTexture,
			TextureRegion highlightTexture, TextureRegion activeTexture) {
		super(x, y, width, height, parent, defaultTexture, highlightTexture, activeTexture);
		itemX = x;
		itemY = y;
		full = false;
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
		if (activeWidget) {
			itemX = mouseX + mouseOffsetX;
			itemY = mouseY + mouseOffsetY;
		}
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY) {
		// Active item if it's an action
		if (mouseX == MPCX && mouseY == MPCY)
			System.out.println("Activate Item");
		// Move item (to the ground, to a new slot, etc)
		else if (item != null) {
			System.out.println("Mouse Released");
			for (Window window : GUI.getActiveWindows()) {
				for (Widget widget : window.getChildren()) {
					if (widget.contains(mouseX, mouseY)) {
						System.out.println("sekjfnbakjlfdljkbasjhkfhkjasfhkjlashjkdshf;lsafsdjhkhlf");
						if (widget != this && widget instanceof Widget_ItemSlot) {
							ItemObject movingObject = this.item;
							this.removeItem();
							((Widget_ItemSlot) widget).setItem(movingObject);
//							 return;
						}

					}
				}
			}
//			 dropItem(mouseX, mouseY);
		}
		itemX = x;
		itemY = y;
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.setColor(defaultColor);

		if (item != null)
			batch.setColor(Color.GOLD);
		batch.draw(defaultTexture, x, y, width, height);

		if (itemTexture != null) {
			batch.setColor(Color.WHITE);
			batch.draw(itemTexture, itemX, itemY, width, height);
		}

		if (highlight) {
			batch.setColor(highlightColor);
			batch.draw(highlightTexture, x, y, width, height);
		}

		if (activeWidget) {
			batch.setColor(activeColor);
			batch.draw(activeTexture, x, y, width, height);
		}

		if (useText) {
			font.setColor(textColor);
			font.draw(batch, text, textX, textY);
		}
	}

	/**
	 * Drops the item on the ground at world coordinates (x, y)
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
	public void onRemoveItem() {}

	public void setItem(ItemObject item) {
		// TODO: If this.item != null, send this.item inventory
		if (this.item != null) {
			((Window_Inventory)GUI.getWindow(GUI.WINDOW_INVENTORY)).insertItem(this.item);
		}
		
		
		ObjectManager.remove(item);
		this.item = item;
		itemX = x;
		itemY = y;
		this.itemTexture = item.getTextureRegion();
		full = true;
		onSetItem();
	}
	
	/**
	 * Called when a new item is set.
	 */
	public void onSetItem() {}
	
	public boolean isFull() {
		return full;
	}

}
