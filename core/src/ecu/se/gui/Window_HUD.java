package ecu.se.gui;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import archive.Archiver;
import archive.TimeRecords;
import assetManager.AssetManager;
import ecu.se.Game;
import ecu.se.map.Map;
import ecu.se.objects.ActiveItem;
import ecu.se.objects.ItemObject;
import stats.Stats;

public class Window_HUD extends Window {
	public Window_HUD(GUI gui) {
		super(gui);
	}

	private Widget_Label lbl_characterLevel;
	private Widget_Label lbl_dungeonFloor;
	private Widget_Label lbl_xp;
	
	private Widget_ProgressBar prgbar_health;
	private Widget_ProgressBar prgbar_mana;
	private Widget_ProgressBar prgbar_xp;

	private Widget_Button_Image btn_playerWindow;
	private Widget_Button_Image btn_playerInventory;
	private Widget_Button_Image btn_mainMenu;

	private Widget_ItemSlot btn_primaryAction;
	private Widget_ItemSlot btn_secondaryAction;

	@Override
	protected void buildWindow() {

		ArrayList<Widget> widgetsList = new ArrayList<Widget>();
		int extra = 14;
		// Player Health bar
		prgbar_health = new Widget_ProgressBar(254, 1014 + extra, 606, 45, this, Color.FIREBRICK, Color.RED) {
			@Override
			public void updateBar() {
				progress = Game.player.getHealth();
				max = Game.player.getStat(Stats.HEALTH);
			}
		};

		// Player Mana Bar
		prgbar_mana = new Widget_ProgressBar(254, 970 + extra, 438, 44, this, Color.SKY, Color.BLUE) {
			@Override
			public void updateBar() {
				progress = Game.player.getMana();
				max = Game.player.getStat(Stats.MANA);
			}
		};

		// Player XP Bar
		prgbar_xp = new Widget_ProgressBar_Image(8, 874, 200, 200, this, new Color(0.3f,0.3f,0.3f,0.5f), Color.ORANGE, "texture/misc/circle.png") {
			@Override
			public void updateBar() {
				progress = Game.player.getXP();
				max = Game.player.getXPNeeded();
			}
		};
		
		lbl_characterLevel = new Widget_Label(80, 940, 0, 0, this, "Level", 40, Color.CLEAR, Color.CHARTREUSE) {
			@Override
			public boolean update(float deltaTime, int mouseX, int mouseY) {
				setText("Level - " + Game.player.getLevel());
				return false;
			}
		};
		
		lbl_dungeonFloor = new Widget_Label(80, 920, 0, 0, this, "Floor", 40, Color.CLEAR, Color.CHARTREUSE) {
			@Override
			public boolean update(float deltaTime, int mouseX, int mouseY) {
				setText("Floor - " + Map.getCurrentLevel());
				return false;
			}
		};
		
		lbl_xp = new Widget_Label(90, 895, 0, 0, this, "xp", 40, Color.CLEAR, Color.CHARTREUSE) {
			@Override
			public boolean update(float deltaTime, int mouseX, int mouseY) {
				setText(Game.player.getXP() + "/" + Game.player.getXPNeeded());
				return false;
			}
		};
		

		Texture buttonTexture = AssetManager.getTexture("texture/gui/navButtons.png").getTexture();

		TextureRegion buttonDefault = new TextureRegion(buttonTexture, 0, 0, 193, 44);
		TextureRegion buttonHighlight = new TextureRegion(buttonTexture, 0, 110, 193, 44);
		TextureRegion buttonActive = new TextureRegion(buttonTexture, 0, 222, 193, 44);
		btn_playerWindow = new Widget_Button_Image(1355, GUI.defaultHeight - 46, 197, 46, this, buttonDefault,
				buttonHighlight, buttonActive) {
			@Override
			public void mouseReleased(int mouseX, int mouseY) {
				gui.setWindow(GUI.WINDOW_PLAYER_STATS);
			}
		};

		buttonDefault = new TextureRegion(buttonTexture, 193, 0, 180, 45);
		buttonHighlight = new TextureRegion(buttonTexture, 193, 110, 180, 45);
		buttonActive = new TextureRegion(buttonTexture, 193, 222, 180, 45);
		btn_playerInventory = new Widget_Button_Image(1550, GUI.defaultHeight - 46, 182, 46, this, buttonDefault,
				buttonHighlight, buttonActive) {
			@Override
			public void mouseReleased(int mouseX, int mouseY) {
				gui.setWindow(GUI.WINDOW_INVENTORY);
			}
		};

		buttonDefault = new TextureRegion(buttonTexture, 373, 0, 185, 109);
		buttonHighlight = new TextureRegion(buttonTexture, 373, 110, 185, 109);
		buttonActive = new TextureRegion(buttonTexture, 373, 222, 185, 109);
		btn_mainMenu = new Widget_Button_Image(1730, GUI.defaultHeight - 113, 186, 113, this, buttonDefault,
				buttonHighlight, buttonActive) {
			@Override
			public void mouseReleased(int mouseX, int mouseY) {
				gui.setWindow(GUI.WINDOW_MAIN_MENU);
			}
		};

		// Player Primary
		buttonDefault = new TextureRegion(buttonTexture, 373, 0, 185, 109);
		buttonHighlight = new TextureRegion(buttonTexture, 373, 110, 185, 109);
		buttonActive = new TextureRegion(buttonTexture, 373, 222, 185, 109);
		btn_primaryAction = new Widget_ItemSlot(1859, 535, 60, 55, this, buttonDefault, buttonHighlight, buttonActive) {
			@Override
			public void onRemoveItem() {
				Game.player.setPrimaryAction(null);
			}

			@Override
			public void onSetItem() {
				if (this.item instanceof ActiveItem)
					Game.player.setPrimaryAction(((ActiveItem) item).getAction());
				else
					onRemoveItem();
			}
		};

		// Player Secondary
		buttonDefault = new TextureRegion(buttonTexture, 373, 0, 185, 109);
		buttonHighlight = new TextureRegion(buttonTexture, 373, 110, 185, 109);
		buttonActive = new TextureRegion(buttonTexture, 373, 222, 185, 109);
		btn_secondaryAction = new Widget_ItemSlot(1859, 478, 60, 55, this, buttonDefault, buttonHighlight,
				buttonActive) {
			@Override
			public void onRemoveItem() {
				Game.player.setSecondaryAction(null);
			}

			@Override
			public void onSetItem() {
				if (this.item instanceof ActiveItem)
					Game.player.setSecondaryAction(((ActiveItem) item).getAction());
				else
					onRemoveItem();
			}
		};

		widgetsList.add(prgbar_health);
		widgetsList.add(prgbar_mana);
		widgetsList.add(prgbar_xp);


		widgetsList.add(btn_playerWindow);
		widgetsList.add(btn_playerInventory);
		widgetsList.add(btn_mainMenu);
		widgetsList.add(btn_primaryAction);
		widgetsList.add(btn_secondaryAction);
		widgetsList.add(new Widget_Label(800, 80, 10, 10, this, "Wash The Queen", 50, Color.BLUE, Color.WHITE));

		buttonTexture = AssetManager.getTexture("texture/gui/hotkey.png").getTexture();
		for (int i = 0; i < 13; i++) {
			buttonDefault = new TextureRegion(buttonTexture, 0, 0, 149, 159);
			buttonHighlight = new TextureRegion(buttonTexture, 0, 165, 149, 159);
			buttonActive = new TextureRegion(buttonTexture, 0, 330, 149, 159);
			Widget_ItemSlot hotkey = new Widget_ItemSlot(595 + (55 * i) + i + (int) (i * 0.3), 2, 56, 62, this,
					buttonDefault, buttonHighlight, buttonActive) ;

			hotkey.setDefaultColor(Color.RED);
			hotkey.setActiveColor(Color.CYAN);
			hotkey.setHighlightColor(Color.CYAN);
			hotkey.setItem(new ItemObject(0, 0, "Sprite LOL", "texture/test/spritePlaceholder.png"));
			
			hotkey.setHotkey(Input.Keys.NUM_1 + i);
			
			
			
			widgetsList.add(hotkey);
		}
		widgetsList.add(new Widget_Image(0, 0, GUI.defaultWidth, GUI.defaultHeight, this, "texture/gui/hud.png"));

		widgetsList.add(lbl_characterLevel);
		widgetsList.add(lbl_dungeonFloor);
		widgetsList.add(lbl_xp);
		
		widgets = widgetsList.toArray(widgets);

	}

	public void setPrimary(ItemObject item) {
		btn_primaryAction.setItem(item);
	}

	public void setSecondary(ItemObject item) {
		btn_secondaryAction.setItem(item);
	}

	@Override
	public void onPause() {
		Archiver.set(TimeRecords.TIME_IN_GAME, false);
	}

	@Override
	public void onResume() {
		Archiver.set(TimeRecords.TIME_IN_GAME, false);
	}
}
