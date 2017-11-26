package ecu.se.gui;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import archive.Archiver;
import archive.TimeRecords;
import assetManager.AssetManager;
import ecu.se.Game;
import ecu.se.objects.ItemObject;
import stats.Stats;

public class Window_HUD extends Window {
	public Window_HUD(GUI gui) {
		super(gui);
	}

	private Widget_ProgressBar prgbar_health;
	private Widget_ProgressBar prgbar_mana;
	
	private Widget_Button_Image btn_playerWindow;
	private Widget_Button_Image btn_playerInventory;
	private Widget_Button_Image btn_mainMenu;
	
	private Widget_Button btn_primaryAction;
	private Widget_Button btn_secondaryAction;
	
	@Override
	protected void buildWindow() {
		
		ArrayList<Widget> widgetsList = new ArrayList<Widget>();
		int extra = 14;	
		// Player Health bar
		prgbar_health = new Widget_ProgressBar(254, 1014 + extra, 606, 45, this, Color.FIREBRICK, Color.RED){
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
		
		
		Texture buttonTexture = AssetManager.getTexture("texture/gui/navButtons.png").getTexture();
		
		TextureRegion buttonDefault =   new TextureRegion(buttonTexture, 0, 0,   193, 44);
		TextureRegion buttonHighlight = new TextureRegion(buttonTexture, 0, 110, 193, 44);
		TextureRegion buttonActive =    new TextureRegion(buttonTexture, 0, 222, 193, 44);
		btn_playerWindow = new Widget_Button_Image(1355, GUI.defaultHeight - 46, 197, 46, this, buttonDefault, buttonHighlight, buttonActive) {
			@Override
			public void mouseReleased(int mouseX, int mouseY) {
				gui.setWindow(GUI.WINDOW_PLAYER_STATS);
			}
		};
		
		buttonDefault =   new TextureRegion(buttonTexture, 193, 0,   180, 45);
		buttonHighlight = new TextureRegion(buttonTexture, 193, 110, 180, 45);
		buttonActive =    new TextureRegion(buttonTexture, 193, 222, 180, 45);
		btn_playerInventory = new Widget_Button_Image(1550, GUI.defaultHeight - 46, 182, 46, this, buttonDefault, buttonHighlight, buttonActive) {
			@Override
			public void mouseReleased(int mouseX, int mouseY) {
				gui.setWindow(GUI.WINDOW_INVENTORY);
			}
		};
		
		buttonDefault =   new TextureRegion(buttonTexture, 373, 0,   185, 109);
		buttonHighlight = new TextureRegion(buttonTexture, 373, 110, 185, 109);
		buttonActive =    new TextureRegion(buttonTexture, 373, 222, 185, 109);
		btn_mainMenu = new Widget_Button_Image(1730, GUI.defaultHeight - 113, 186, 113, this, buttonDefault, buttonHighlight, buttonActive) {
			@Override
			public void mouseReleased(int mouseX, int mouseY) {
				gui.setWindow(GUI.WINDOW_MAIN_MENU);
			}
		};
				
		//Player Primary
		buttonDefault =   new TextureRegion(buttonTexture, 373, 0,   185, 109);
		buttonHighlight = new TextureRegion(buttonTexture, 373, 110, 185, 109);
		buttonActive =    new TextureRegion(buttonTexture, 373, 222, 185, 109);
		btn_primaryAction= new Widget_Button_Image(1859, 535, 60, 55, this, buttonDefault, buttonHighlight, buttonActive) {
			@Override
			public void mousePressed(int mouseX, int mouseY) {
				System.out.println("Bitch please");
			}
			@Override
			public void mouseReleased(int mouseX, int mouseY) {
				System.out.println("maaaan");
			}
		};
		
		//Player Secondary
		buttonDefault =   new TextureRegion(buttonTexture, 373, 0,   185, 109);
		buttonHighlight = new TextureRegion(buttonTexture, 373, 110, 185, 109);
		buttonActive =    new TextureRegion(buttonTexture, 373, 222, 185, 109);
		btn_secondaryAction = new Widget_Button_Image(1859, 478, 60, 55, this, buttonDefault, buttonHighlight, buttonActive) {
			@Override
			public void mousePressed(int mouseX, int mouseY) {
				System.out.println("Secondary WOW");
			}
			@Override
			public void mouseReleased(int mouseX, int mouseY) {
				System.out.println("unfortunate");
			}
		};
		
		widgetsList.add(prgbar_health);
		widgetsList.add(prgbar_mana);
		widgetsList.add(btn_playerWindow);
		widgetsList.add(btn_playerInventory);
		widgetsList.add(btn_mainMenu);
		widgetsList.add(btn_primaryAction);
		widgetsList.add(btn_secondaryAction);
		widgetsList.add(new Widget_Label(800, 80, 10, 10, this, "Wash The Queen", 50, Color.BLUE, Color.WHITE));

		buttonTexture = AssetManager.getTexture("texture/gui/hotkey.png").getTexture();
//		Widget_Button_Image[] hotkeys = new Widget_Button_Image[13];
		for(int i = 0; i < 13; i++) {
			buttonDefault =   new TextureRegion(buttonTexture, 0, 0,   149, 159);
			buttonHighlight = new TextureRegion(buttonTexture, 0, 165, 149, 159);
			buttonActive =    new TextureRegion(buttonTexture, 0, 330, 149, 159);
			Widget_ItemSlot hotkey = new Widget_ItemSlot(595 + (55 * i) + i + (int)(i * 0.3), 2, 56, 62, this, buttonDefault, buttonHighlight, buttonActive);
			
			hotkey.setDefaultColor( Color.RED);
			hotkey.setActiveColor(Color.CYAN);
			hotkey.setHighlightColor(Color.CYAN);
			hotkey.setItem(new ItemObject(0,0, "Sprite LOL", "texture/test/spritePlaceholder.png"));
			widgetsList.add(hotkey);
		}
		widgetsList.add(new Widget_Image(0, 0, GUI.defaultWidth, GUI.defaultHeight, this, "texture/gui/hud.png"));
		
		widgets = widgetsList.toArray(widgets);
		

	}

	@Override
	public void onPause() {
		Archiver.set(TimeRecords.TIME_IN_GAME, false);
	}
	
	@Override
	public void onResume() {
		Archiver.set(TimeRecords.TIME_IN_GAME, false);
	}
//		widgets = new Widget[] { 
//				prgbar_health,
//				prgbar_mana,
//				
//				btn_playerWindow,
//				btn_playerInventory,
//				btn_mainMenu,
//				
//				btn_primaryAction,
//				btn_secondaryAction,
//				
//				hotkey1,
//				hotkey2,
//				hotkey3,
//				hotkey4,
//				hotkey5,
//				hotkey6,
//				hotkey7,
//				hotkey8,
//				hotkey9,
//				hotkey10,
//				hotkey11,
//				hotkey12,
//				hotkey13,
//				
//				// Test Label
//				new Widget_Label(800, 80, 10, 10, this, "Wash The Queen", 50, Color.BLUE, Color.WHITE),
//				// GUI OVERLAY
//				new Widget_Image(0, 0, GUI.defaultWidth, GUI.defaultHeight, this, "texture/gui/hud.png")
//		};
	
//	// QUICKBAR (1-13) (Might could be moved to it's own window)
//	buttonDefault =   new TextureRegion(buttonTexture, 0, 0,   149, 159);
//	buttonHighlight = new TextureRegion(buttonTexture, 0, 165, 149, 159);
//	buttonActive =    new TextureRegion(buttonTexture, 0, 330, 149, 159);
//	hotkey1 = new Widget_Button_Image(595, 2, 56, 62, this, buttonDefault, buttonHighlight, buttonActive) {
//		@Override
//		public void mousePressed() {
//			System.out.println(":( 1");
//		}
//		@Override
//		public void mouseDown() {
//			System.out.println("mouseDown");
//		}
//		@Override
//		public void mouseReleased() {
//			System.out.println("mouseReleased");
//		}
//	};
//	
//	buttonDefault =   new TextureRegion(buttonTexture, 0, 0,   149, 159);
//	buttonHighlight = new TextureRegion(buttonTexture, 0, 165, 149, 159);
//	buttonActive =    new TextureRegion(buttonTexture, 0, 330, 149, 159);
//	hotkey2 = new Widget_Button_Image(651, 2, 56, 62, this, buttonDefault, buttonHighlight, buttonActive) {
//		@Override
//		public void mousePressed() {
//			System.out.println("mousePressed");
//		}
//		@Override
//		public void mouseDown() {
//			System.out.println("mouseDown");
//		}
//		@Override
//		public void mouseReleased() {
//			System.out.println("mouseReleased");
//		}
//	};		
//	
//	buttonDefault =   new TextureRegion(buttonTexture, 0, 0,   149, 159);
//	buttonHighlight = new TextureRegion(buttonTexture, 0, 165, 149, 159);
//	buttonActive =    new TextureRegion(buttonTexture, 0, 330, 149, 159);
//	hotkey3 = new Widget_Button_Image(708, 2, 56, 62, this, buttonDefault, buttonHighlight, buttonActive) {
//		@Override
//		public void mousePressed() {
//			System.out.println("mousePressed");
//		}
//		@Override
//		public void mouseDown() {
//			System.out.println("mouseDown");
//		}
//		@Override
//		public void mouseReleased() {
//			System.out.println("mouseReleased");
//		}
//	};
//	
//	buttonDefault =   new TextureRegion(buttonTexture, 0, 0,   149, 159);
//	buttonHighlight = new TextureRegion(buttonTexture, 0, 165, 149, 159);
//	buttonActive =    new TextureRegion(buttonTexture, 0, 330, 149, 159);
//	hotkey4 = new Widget_Button_Image(764, 2, 56, 62, this, buttonDefault, buttonHighlight, buttonActive) {
//		@Override
//		public void mousePressed() {
//			System.out.println("mousePressed");
//		}
//		@Override
//		public void mouseDown() {
//			System.out.println("mouseDown");
//		}
//		@Override
//		public void mouseReleased() {
//			System.out.println("mouseReleased");
//		}
//	};
//	
//	buttonDefault =   new TextureRegion(buttonTexture, 0, 0,   149, 159);
//	buttonHighlight = new TextureRegion(buttonTexture, 0, 165, 149, 159);
//	buttonActive =    new TextureRegion(buttonTexture, 0, 330, 149, 159);
//	hotkey5 = new Widget_Button_Image(821, 2, 56, 62, this, buttonDefault, buttonHighlight, buttonActive) {
//		@Override
//		public void mousePressed() {
//			System.out.println("mousePressed");
//		}
//		@Override
//		public void mouseDown() {
//			System.out.println("mouseDown");
//		}
//		@Override
//		public void mouseReleased() {
//			System.out.println("mouseReleased");
//		}
//	};
//	
//	buttonDefault =   new TextureRegion(buttonTexture, 0, 0,   149, 159);
//	buttonHighlight = new TextureRegion(buttonTexture, 0, 165, 149, 159);
//	buttonActive =    new TextureRegion(buttonTexture, 0, 330, 149, 159);
//	hotkey6 = new Widget_Button_Image(877, 2, 56, 62, this, buttonDefault, buttonHighlight, buttonActive) {
//		@Override
//		public void mousePressed() {
//			System.out.println("mousePressed");
//		}
//		@Override
//		public void mouseDown() {
//			System.out.println("mouseDown");
//		}
//		@Override
//		public void mouseReleased() {
//			System.out.println("mouseReleased");
//		}
//	};
//	
//	buttonDefault =   new TextureRegion(buttonTexture, 0, 0,   149, 159);
//	buttonHighlight = new TextureRegion(buttonTexture, 0, 165, 149, 159);
//	buttonActive =    new TextureRegion(buttonTexture, 0, 330, 149, 159);
//	hotkey7 = new Widget_Button_Image(933, 2, 56, 62, this, buttonDefault, buttonHighlight, buttonActive) {
//		@Override
//		public void mousePressed() {
//			System.out.println("mousePressed");
//		}
//		@Override
//		public void mouseDown() {
//			System.out.println("mouseDown");
//		}
//		@Override
//		public void mouseReleased() {
//			System.out.println("mouseReleased");
//		}
//	};
//	
//	buttonDefault =   new TextureRegion(buttonTexture, 0, 0,   149, 159);
//	buttonHighlight = new TextureRegion(buttonTexture, 0, 165, 149, 159);
//	buttonActive =    new TextureRegion(buttonTexture, 0, 330, 149, 159);
//	hotkey8 = new Widget_Button_Image(990, 2, 56, 62, this, buttonDefault, buttonHighlight, buttonActive) {
//		@Override
//		public void mousePressed() {
//			System.out.println("mousePressed");
//		}
//		@Override
//		public void mouseDown() {
//			System.out.println("mouseDown");
//		}
//		@Override
//		public void mouseReleased() {
//			System.out.println("mouseReleased");
//		}
//	};
//	
//	buttonDefault =   new TextureRegion(buttonTexture, 0, 0,   149, 159);
//	buttonHighlight = new TextureRegion(buttonTexture, 0, 165, 149, 159);
//	buttonActive =    new TextureRegion(buttonTexture, 0, 330, 149, 159);
//	hotkey9 = new Widget_Button_Image(1046, 2, 56, 62, this, buttonDefault, buttonHighlight, buttonActive) {
//		@Override
//		public void mousePressed() {
//			System.out.println("mousePressed");
//		}
//		@Override
//		public void mouseDown() {
//			System.out.println("mouseDown");
//		}
//		@Override
//		public void mouseReleased() {
//			System.out.println("mouseReleased");
//		}
//	};
//	
//	buttonDefault =   new TextureRegion(buttonTexture, 0, 0,   149, 159);
//	buttonHighlight = new TextureRegion(buttonTexture, 0, 165, 149, 159);
//	buttonActive =    new TextureRegion(buttonTexture, 0, 330, 149, 159);
//	hotkey10 = new Widget_Button_Image(1102, 2, 56, 62, this, buttonDefault, buttonHighlight, buttonActive) {
//		@Override
//		public void mousePressed() {
//			System.out.println("mousePressed");
//		}
//		@Override
//		public void mouseDown() {
//			System.out.println("mouseDown");
//		}
//		@Override
//		public void mouseReleased() {
//			System.out.println("mouseReleased");
//		}
//	};
//	
//	buttonDefault =   new TextureRegion(buttonTexture, 0, 0,   149, 159);
//	buttonHighlight = new TextureRegion(buttonTexture, 0, 165, 149, 159);
//	buttonActive =    new TextureRegion(buttonTexture, 0, 330, 149, 159);
//	hotkey11 = new Widget_Button_Image(1158, 2, 56, 62, this, buttonDefault, buttonHighlight, buttonActive) {
//		@Override
//		public void mousePressed() {
//			System.out.println("mousePressed");
//		}
//		@Override
//		public void mouseDown() {
//			System.out.println("mouseDown");
//		}
//		@Override
//		public void mouseReleased() {
//			System.out.println("mouseReleased");
//		}
//	};
//	
//	buttonDefault =   new TextureRegion(buttonTexture, 0, 0,   149, 159);
//	buttonHighlight = new TextureRegion(buttonTexture, 0, 165, 149, 159);
//	buttonActive =    new TextureRegion(buttonTexture, 0, 330, 149, 159);
//	hotkey12 = new Widget_Button_Image(1214, 2, 56, 62, this, buttonDefault, buttonHighlight, buttonActive) {
//		@Override
//		public void mousePressed() {
//			System.out.println("mousePressed");
//		}
//		@Override
//		public void mouseDown() {
//			System.out.println("mouseDown");
//		}
//		@Override
//		public void mouseReleased() {
//			System.out.println("mouseReleased");
//		}
//	};
//	
//	buttonDefault =   new TextureRegion(buttonTexture, 0, 0,   149, 159);
//	buttonHighlight = new TextureRegion(buttonTexture, 0, 165, 149, 159);
//	buttonActive =    new TextureRegion(buttonTexture, 0, 330, 149, 159);
//	hotkey13 = new Widget_Button_Image(1272, 2, 56, 62, this, buttonDefault, buttonHighlight, buttonActive) {
//		@Override
//		public void mousePressed() {
//			System.out.println("mousePressed");
//		}
//		@Override
//		public void mouseDown() {
//			System.out.println("mouseDown");
//		}
//		@Override
//		public void mouseReleased() {
//			System.out.println("mouseReleased");
//		}
//	};
}
