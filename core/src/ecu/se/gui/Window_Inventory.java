package ecu.se.gui;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ecu.se.Logger;
import ecu.se.assetManager.AssetManager;
import ecu.se.objects.ItemObject;

public class Window_Inventory extends Window {

	public Window_Inventory(GUI gui) {
		super(gui);
	}
	
//	private int[][] slots;
	private final int slotColumns	= 10;
	private final int slotRows 		= 10;

	@Override
	protected void buildWindow() {
		int halfwayX = (int)(GUI.defaultWidth * 0.5f);
		int halfwayY = (int)(GUI.defaultHeight * 0.5f);
		int backgroundX = (int)(halfwayX * 0.5f);
		int backgroundY = (int)(halfwayY * 0.5f);
		int bufferX = 50;
		
		ArrayList<Widget> widgetList = new ArrayList<Widget>();
		
		Widget_Image img_background = new Widget_Image(backgroundX, backgroundY, halfwayX, halfwayY, this, "texture/misc/paper.jpg");
		img_background.setDefaultColor(Color.WHITE);
		widgetList.add(img_background);
		Widget_Label lbl_title = new Widget_Label(backgroundX + bufferX, backgroundY + halfwayY - 25, 0, 0, parent, this.windowName, 50, Color.CLEAR, Color.BLACK);
		widgetList.add(lbl_title);
		
		Texture buttonTexture = AssetManager.getTexture("texture/gui/hotkey.png").getTexture();
		TextureRegion buttonDefault;
		TextureRegion buttonHighlight;
		TextureRegion buttonActive;
//		int rows = 10, cols = 10;
//		slots = new Widget_ItemSlot[slotRows][slotColumns];
//		slots = new int[slotRows][slotColumns];
		float startX = GUI.defaultWidth * .25f;
		float startY = GUI.defaultHeight * .25f;
		float buttonWidth = 40;
		float buttonHeight = 40;
		float xMargin = 5;
		float yMargin = 1;
		Logger.Debug(this.getClass(), "buildWindow",(halfwayY - backgroundY));
		for(int i = 0 ; i < slotRows; i++) {
			for(int j = 0; j < slotColumns; j++) {
				buttonDefault =   new TextureRegion(buttonTexture, 0, 0,   149, 159);
				buttonHighlight = new TextureRegion(buttonTexture, 0, 165, 149, 159);
				buttonActive =    new TextureRegion(buttonTexture, 0, 330, 149, 159);
				Widget_ItemSlot hotkey = new Widget_ItemSlot(startX + (buttonWidth + xMargin) * i, startY + (buttonHeight + yMargin) * j, buttonWidth, buttonHeight, this, buttonDefault, buttonHighlight, buttonActive);
				
				hotkey.setDefaultColor( Color.RED);
				hotkey.setActiveColor(Color.CYAN);
				hotkey.setHighlightColor(Color.CYAN);
//				slots[i][j] = widgetList.size();
				widgetList.add(hotkey);
			}
		}
		widgets = widgetList.toArray(widgets);
	}
	
	public void insertItem(ItemObject item) {
		for(Widget w : widgets) {
			if (w instanceof Widget_ItemSlot) {
				if (!((Widget_ItemSlot)w).isFull()) {
					((Widget_ItemSlot)w).setItem(item);
					return;
				}
			}
		}
	}
}
