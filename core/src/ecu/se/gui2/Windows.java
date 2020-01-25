package ecu.se.gui2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

import ecu.se.Game;
import ecu.se.Logger;
import ecu.se.actors.Player;
import ecu.se.archive.Archiver;
import ecu.se.archive.TotalRecords;
import ecu.se.assetManager.AssetManager;
import ecu.se.gui.GUI;
import ecu.se.gui2.GuiUtils.Justify;
import ecu.se.objects.ActiveItem;
import ecu.se.stats.Stats;

public class Windows {
	
	private static Color CornerColor = Color.DARK_GRAY;
	private static float CornerOpacity = 0.75f;
	
	private static Color BackgroundColor = Color.BLACK;
	private static float BackgroundOpacity= 0.5f;
	
	private static Color HightlightColor = Color.CYAN;
	private static float HighlighOpacity = 0.5f;
			
	private static Color ActiveColor = Color.GREEN;
	private static float ActiveOpacity = 0.5f;
	
	
	
	private static void applyDefaultSettings(Component comp, boolean applyToContainer) {
		if (comp instanceof Container) {
			for(Component widget : ((Container)comp).getChildren()) {
				applyDefaultSettings(widget, false);
			}
			if (applyToContainer)
				applyDefaultSettingsToContainer(comp);
		} else {
			applyDefaultSettingsToComponent(comp);	
		}
	}
	
	private static void applyDefaultSettingsToComponent(Component comp) {
		comp.setBackgroundTexture(AssetManager.getTexture("texture/misc/white.png").getTexture());
		comp.setBackgroundColor(BackgroundColor);
		comp.setBackgroundOpacity(BackgroundOpacity);
		
		comp.setCornerTexture("texture/gui/corner_tl.png");
		comp.setRenderCorners(true);
		comp.setCornerColor(CornerColor);
		comp.setCornerOpacity(CornerOpacity);
		
		comp.setActiveColor(ActiveColor);
		comp.setActiveOpacity(ActiveOpacity);
		comp.setActiveEnabled(true);
		
		comp.setHighlightColor(HightlightColor);
		comp.setHighlighOpacity(HighlighOpacity);
		comp.setHighlightEnabled(true);

		
		comp.showToolTip = true;
		comp.canConsumeUI = true;
		comp.setExpand(Expand.ExpandAll);
		comp.setFontSize(15);
		comp.setTextJustify(Justify.Center);
		comp.isVisible = true;
		comp.setPadding(5);
		comp.setMargin(5);	
	}
	
	private static void applyDefaultSettingsToContainer(Component comp) {
		comp.setBackgroundTexture(AssetManager.getTexture("texture/misc/white.png").getTexture());
		comp.setBackgroundColor(BackgroundColor);
		comp.setBackgroundOpacity(BackgroundOpacity);
		
		comp.setRenderCorners(false);
		comp.setActiveEnabled(false);
		comp.setHighlightEnabled(false);
		
		comp.showToolTip = false;
		comp.canConsumeUI = true;
		comp.setExpand(Expand.ExpandAll);
		comp.setFontSize(15);
		comp.isVisible = true;
	}
	
	
	
	/**
	 * Convenience class for creating gui windows/elements
	 */
	
	public static Container CreateMainMenu (final GUI gui) {
		Container MainContainer = new Container();
		MainContainer.name = "Main Menu Container";

		GuiButton btn_play = new GuiButton("Play") {
			@Override
			public void mouseReleased(int mouseX, int mouseY) {
				GUI.setWindow(GUI.WINDOW_HUD);
			}
		};
		
		GuiButton btn_newGame = new GuiButton("New Game") {
			@Override
			public void mouseReleased(int mouseX, int mouseY) {
				gui.getGame().newGame();
				GUI.setWindow(GUI.WINDOW_HUD);
			}
		};
		
		GuiButton btn_settings = new GuiButton("Settings") {
			@Override
			public void mouseReleased(int mouseX, int mouseY) {
				GUI.setWindow(GUI.WINDOW_SETTINGS);
			}
		};
		
		GuiButton btn_debugsettings = new GuiButton("Debug Settings") {
			@Override
			public void mouseReleased(int mouseX, int mouseY) {
				GUI.setWindow(GUI.WINDOW_DEBUG_SETTINGS);
			}
		};
		
		GuiButton btn_guiShowcase = new GuiButton("Gui Components") {
			@Override
			public void mouseReleased(int mouseX, int mouseY) {
				GUI.setWindow(GUI.WINDOW_COMPONENT_TEST);
				Logger.Debug(getClass(), "CreateMainMenu", "OWWOWOWOO");
			}
		};
		
		GuiButton btn_stats = new GuiButton("Game Stats") {
			@Override
			public void mouseReleased(int mouseX, int mouseY) {
				GUI.setWindow(GUI.WINDOW_GAME_STATS);
			}
		};

		GuiButton btn_exit = new GuiButton("Exit") {
			@Override
			public void mouseReleased(int mouseX, int mouseY) {
				Game.currentState = Game.GAME_STATE_EXITING;
			}
		};
		MainContainer.addChild(btn_play);
		MainContainer.addChild(btn_newGame);
		MainContainer.addChild(btn_settings);
		MainContainer.addChild(btn_debugsettings);
		MainContainer.addChild(btn_guiShowcase);
		MainContainer.addChild(btn_stats);		
		MainContainer.addChild(btn_exit);

		
		applyDefaultSettings(MainContainer, true);
		MainContainer.setPadding(2);
		MainContainer.setMargin(2);
		MainContainer.setLayout(Layout.Vertical);
		MainContainer.pack(new Rectangle(0,0,GUI.convertX(800), GUI.convertY(500)));
		MainContainer.setPosition(-MainContainer.getMarginsBounds().width * .5f, -MainContainer.getMarginsBounds().height * .5f);
		return MainContainer;
	}

	public static Container CreateGameStats(GUI gui) {
		Container MainContainer = new Container();
		MainContainer.name = "Game Stats Container";

		applyDefaultSettingsToContainer(MainContainer);

		Container stats = stats(gui);
		stats.setPadding(5);
		MainContainer.addChild(stats);
		
		MainContainer.setRenderBackground(false);
		MainContainer.setLayout(Layout.Vertical);
//		MainContainer.setPadding(GUI.convertX(45));
//		MainContainer.setMargin(GUI.convertY(15));	
		MainContainer.pack(new Rectangle(0,0,GUI.convertX(800), GUI.convertY(500)));
		MainContainer.setPosition(-MainContainer.getMarginsBounds().width * .5f, -MainContainer.getMarginsBounds().height * .5f);
		return MainContainer;
	}
	
	private static Container stats(GUI gui) {
		Container MainContainer = new Container();
		MainContainer.name = "Stats Container";

		MainContainer.setLayout(Layout.Vertical);
		Container row = new Container();
		row.setLayout(Layout.Horizontal);
		row.addChild(new GuiLabel("All Time"));
		row.addChild(new GuiLabel("Current"));
		row.addChild(new GuiLabel("Stats"));
		for (Component child : row.getChildren()) {
			child.setTextJustify(Justify.Center);
		}
		
		MainContainer.addChild(row);
		applyDefaultSettingsToComponent(MainContainer);
		MainContainer.setActiveEnabled(false);
		MainContainer.setHighlightEnabled(false);
		

		for (int i = 0; i < TotalRecords.values().length; i++) {
			row = new Container();
			row.setLayout(Layout.Horizontal);
			final TotalRecords rowStat = TotalRecords.values()[i];
			
			
			GuiLabel totalStats = new GuiLabel("" + Archiver.getRecord(rowStat, true)) {
				private TotalRecords stat = rowStat;
				protected Component updateComponent(Component consumer, float deltaTime, int mouseX, int mouseY) {
					if (!text.equalsIgnoreCase(Archiver.getRecord(stat, true) + ""))
						setText(Archiver.getRecord(stat, true) + "");
					return consumer;
				}

			};
			row.addChild(totalStats);
			
			
			GuiLabel currentStats = new GuiLabel("" + Archiver.getRecord(rowStat, false)) {
				private TotalRecords stat = rowStat;
				protected Component updateComponent(Component consumer, float deltaTime, int mouseX, int mouseY) {
					if (!text.equalsIgnoreCase(Archiver.getRecord(stat, false) + ""))
						setText(Archiver.getRecord(stat, false) + "");
					return consumer;
				}

			};
			row.addChild(currentStats);
			row.addChild(new GuiLabel(rowStat.name()));
			row.name = rowStat.name();
			row.setRenderCorners(false);
			for (Component child : row.getChildren()) {
				child.setTextJustify(Justify.Center);
			}
			
			
			applyDefaultSettingsToComponent(row);

			row.setPadding(0);
			row.setMargin(0);
			row.setMarginVertical(2);
			MainContainer.addChild(row);
		}		
		MainContainer.addChild(row);
		
		MainContainer.setLayout(Layout.Vertical);
		MainContainer.pack(new Rectangle(0,0,GUI.convertX(800), GUI.convertY(500)));
		MainContainer.setPosition(-MainContainer.getMarginsBounds().width * .5f, -MainContainer.getMarginsBounds().height * .5f);
		return MainContainer;
	}
	
	public static Container CreateGameOver(final GUI gui) {
		Container MainContainer = new Container();
		MainContainer.name = "Game Over Container";

		
		Container buttonsBottom = new Container();
		buttonsBottom.setLayout(Layout.Horizontal);
		
		GuiButton btn_newGame = new GuiButton("New Game") {
			@Override
			public void mouseReleased(int mouseX, int mouseY) {
				gui.getGame().newGame();
				GUI.setWindow(GUI.WINDOW_HUD);
			}
		};	
		
		btn_newGame.setTextJustify(Justify.Center);
		GuiButton btn_exit = new GuiButton("Quit") {
			@Override
			public void mouseReleased(int mouseX, int mouseY) {
				Game.currentState = Game.GAME_STATE_EXITING;
			}
		};	
		btn_exit.setTextJustify(Justify.Center);
		
		buttonsBottom.addChild(btn_exit);	
		buttonsBottom.addChild(btn_newGame);	
		Container stats = stats(gui);
		stats.addChild(buttonsBottom);
		MainContainer.addChild(stats);

		applyDefaultSettingsToComponent(btn_exit);
		applyDefaultSettingsToComponent(btn_newGame);
		applyDefaultSettingsToContainer(MainContainer);

		MainContainer.setLayout(Layout.Vertical);
		MainContainer.setPadding(GUI.convertX(3));
//		MainContainer.setMargin(GUI.convertY(15));	
		MainContainer.pack(new Rectangle(0,0,GUI.convertX(800), GUI.convertY(500)));
		MainContainer.setPosition(-MainContainer.getMarginsBounds().width * .5f, -MainContainer.getMarginsBounds().height * .5f);		
		return MainContainer;
	}
	
	public static Container CreateDebugSettings(final GUI gui) {
		Container MainContainer = new Container();
		MainContainer.name = "Debug Settings Container";

		GuiButton btn_newGame = new GuiButton("Return to main menu") {
			@Override
			public void mouseReleased(int mouseX, int mouseY) {
				GUI.setWindow(GUI.WINDOW_MAIN_MENU);
			}
		};	
		
		MainContainer.addChild(btn_newGame);
		
		
		applyDefaultSettings(MainContainer, true);

		MainContainer.setLayout(Layout.Vertical);
		MainContainer.setPadding(GUI.convertX(45));
		MainContainer.setMargin(GUI.convertY(15));	
		MainContainer.pack(new Rectangle(0,0,GUI.convertX(800), GUI.convertY(500)));
		MainContainer.setPosition(-MainContainer.getMarginsBounds().width * .5f, -MainContainer.getMarginsBounds().height * .5f);
		return MainContainer;
	}

	public static Container CreateHUD(GUI gui) {
		Container MainContainer = new Container();
		MainContainer.name = "HUD Container";
		MainContainer.canConsumeUI = false;
		MainContainer.showToolTip = false;
		MainContainer.AbsolutePositioning = true;
		MainContainer.setBackgroundOpacity(0.0f);
		MainContainer.setRenderBackground(false);
		
		Container actionContainer = new Container();
		GUI.primaryItemSlot = new GuiItemSlot("Primary") {
			@Override
			public void mouseReleased(int mouseX, int mouseY) {
				Game.player.primaryAction(mouseX, mouseY);
			}
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
		GUI.primaryItemSlot.canConsumeUI = false;
		GUI.primaryItemSlot.showToolTip = false;
		
		GUI.secondaryItemSlot = new GuiItemSlot("Scondary") {
			@Override
			public void mouseReleased(int mouseX, int mouseY) {
				Game.player.secondaryAction(mouseX, mouseY);
			}
			
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
		GUI.secondaryItemSlot.canConsumeUI = false;
		GUI.secondaryItemSlot.showToolTip = false;
		
		actionContainer.AbsolutePositioning=true;
		actionContainer.setLayout(Layout.Vertical);
		actionContainer.setExpand(Expand.DoNotExpand);
		
		actionContainer.addChild(GUI.primaryItemSlot);
		actionContainer.addChild(GUI.secondaryItemSlot);
		actionContainer.pack(new Rectangle(350,350,50,100));
		MainContainer.addChild(actionContainer);
		
		
		
		
		
		
		Player player = Game.player;
		GuiProgressBar healthBar = new GuiProgressBar(0, player.getStat(Stats.HEALTH), player.getHealth()) {
			@Override
			public void updateBar() {
				setAllValues(0, Game.player.getHealth(), Game.player.getStat(Stats.HEALTH));
			}
		};
		healthBar.setPadding(15);
		healthBar.ProgressBarColor = Color.RED;
		healthBar.name = "Health Bar";
		healthBar.pack(new Rectangle(-1200, -600, 575, 25));
		healthBar.expand = Expand.DoNotExpand;
		healthBar.AbsolutePositioning = true;
		healthBar.setTextJustify(Justify.Right);
		healthBar.setTextColor(Color.BLUE);
		MainContainer.addChild(healthBar);
		
		GuiProgressBar manaBar = new GuiProgressBar(0, player.getStat(Stats.HEALTH), player.getHealth()) {
			@Override
			public void updateBar() {
				setAllValues(0, Game.player.getMana(), Game.player.getStat(Stats.MANA));
			}
		};
		manaBar.setPadding(15);
		manaBar.ProgressBarColor = Color.BLUE;
		manaBar.name = "Mana Bar";
		manaBar.expand = Expand.DoNotExpand;
		manaBar.pack(new Rectangle(-1200, -635, 600, 25));
		manaBar.AbsolutePositioning = true;
		manaBar.setTextJustify(Justify.Right);
		MainContainer.addChild(manaBar);

		GuiProgressBar experienceBar = new GuiProgressBar(0, player.getXPNeeded(), player.getXP()) {
			@Override
			public void updateBar() {
				setAllValues(0, Game.player.getXP(), Game.player.getXPNeeded());
			}
		};
		experienceBar.setPadding(15);
		experienceBar.ProgressBarColor = Color.ORANGE;
		experienceBar.name = "Experience Bar";
		experienceBar.expand = Expand.DoNotExpand;
		experienceBar.pack(new Rectangle(-1200, -670, 625, 25));
		experienceBar.AbsolutePositioning = true;
		experienceBar.setBackgroundColor(Color.CORAL);
		experienceBar.setTextJustify(Justify.Right);
		MainContainer.addChild(experienceBar);

		
		float startX = 500, startY = 500;
		
		GuiButton PlayerMenu = new GuiButton("Player") {
			@Override
			public void mouseReleased(int mouseX, int mouseY) {
				GUI.setWindow(GUI.WINDOW_PLAYER_STATS);
				Logger.Debug(getClass(), "mouseReleased", "HELLO");
			}
		};
		PlayerMenu.AbsolutePositioning = true;
		PlayerMenu.pack(new Rectangle(startX, startY, 100, 50));
		
		GuiButton InventoryMenu = new GuiButton("Inventory"){
			@Override
			public void mouseReleased(int mouseX, int mouseY) {
				GUI.setWindow(GUI.WINDOW_INVENTORY);
				Logger.Debug(getClass(), "mouseReleased", "HELLO");

			}
		};
		InventoryMenu.AbsolutePositioning = true;
		startX += 105;
		InventoryMenu.pack(new Rectangle(startX, startY, 100, 50));

		GuiButton MainMenu = new GuiButton("Main Menu"){
			@Override
			public void mouseReleased(int mouseX, int mouseY) {
				GUI.setWindow(GUI.WINDOW_MAIN_MENU);
				Logger.Debug(getClass(), "mouseReleased", "HELLO");

			}
		};		
		MainMenu.AbsolutePositioning = true;
		startX += 105;
		MainMenu.pack(new Rectangle(startX, startY, 100, 50));

		
		MainContainer.addChild(PlayerMenu);
		MainContainer.addChild(InventoryMenu);
		MainContainer.addChild(MainMenu);
		
		
		applyDefaultSettings(MainContainer, false);
		
		MainContainer.setLayout(Layout.Vertical);
		MainContainer.setPadding(GUI.convertX(1));
		MainContainer.setMargin(GUI.convertY(2));	
		MainContainer.pack(new Rectangle(
				-Gdx.graphics.getWidth() * 0.5f,
				-Gdx.graphics.getHeight() * 0.5f,
				Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight()
				));
		return MainContainer;
	}
	
	public static Container CreateSettings(GUI gui) {
		Container MainContainer = new Container();

		for(Component widget : MainContainer.getChildren()) {
			applyDefaultSettings(widget, true);
		}	
		MainContainer.setLayout(Layout.Vertical);
		MainContainer.setPadding(GUI.convertX(45));
		MainContainer.setMargin(GUI.convertY(15));	
		MainContainer.pack(new Rectangle(0,0,GUI.convertX(800), GUI.convertY(500)));
		MainContainer.setPosition(-MainContainer.getMarginsBounds().width * .5f, -MainContainer.getMarginsBounds().height * .5f);
		return MainContainer;
	}
	
	public static Container CreateInventory(GUI gui) {
		Container MainContainer = new Container();

		applyDefaultSettings(MainContainer, true);

		MainContainer.setLayout(Layout.Vertical);	
		MainContainer.pack(new Rectangle(0,0,GUI.convertX(800), GUI.convertY(500)));
		MainContainer.setPosition(-MainContainer.getMarginsBounds().width * .5f, -MainContainer.getMarginsBounds().height * .5f);
		return MainContainer;	
	}
	
	public static Container CreatePlayerStats(GUI gui) {
		Container MainContainer = new Container();

		applyDefaultSettings(MainContainer, true);
	
		MainContainer.setLayout(Layout.Vertical);
		MainContainer.pack(new Rectangle(0,0,GUI.convertX(800), GUI.convertY(500)));
		MainContainer.setPosition(-MainContainer.getMarginsBounds().width * .5f, -MainContainer.getMarginsBounds().height * .5f);
		return MainContainer;	
	}
	
	public static  Container CreatePauseWindow(GUI gui) {
		Container MainContainer = new Container();

		applyDefaultSettings(MainContainer, true);
	
		MainContainer.setLayout(Layout.Vertical);
		MainContainer.pack(new Rectangle(0,0,GUI.convertX(800), GUI.convertY(500)));
		MainContainer.setPosition(-MainContainer.getMarginsBounds().width * .5f, -MainContainer.getMarginsBounds().height * .5f);
		return MainContainer;	
	}
	
	public static Container CreateComponentTest(GUI gui) {
		Container MainContainer = new Container();
		
		GuiButton btn_backToMainMenu = new GuiButton("Button - Return to Main Menu") {
			@Override
			public void mouseReleased(int mouseX, int mouseY) {
				GUI.setWindow(GUI.WINDOW_MAIN_MENU);
			}
		};
		
		GuiLabel label = new GuiLabel("Label Test");
		label.setTextJustify(GuiUtils.Justify.Center);
		label.name = "Label Test";
		
		GuiLabel label_useMinDimensions = new GuiLabel("Label - Use Minimum Size");
		label_useMinDimensions.setTextJustify(GuiUtils.Justify.Center);
		label_useMinDimensions.setExpand(Expand.DoNotExpand);
		label_useMinDimensions.name = "label_useMinDimensions";
		
		GuiItemSlot itemSlot = new GuiItemSlot("Item Slot test");
		
		GuiProgressBar progressBar = new GuiProgressBar(0f, 100f, 75f) {
			@Override
			public void act(){
				this.changeValue(Gdx.graphics.getDeltaTime() * 20);
				if (getCurrentValue() >= getMaxValue() ) {
					this.setValue(getMinValue());
				}
			}
			
			
			@Override
			protected String debugTooltipTextComponent() {
				return "Minimum: " + getMinValue()
						+ "\nMaximum: " + getMaxValue()
						+ "\nValue: " + getCurrentValue();
			}
		};
		progressBar.animationSpeedFactor = 1.45f;
		
		GuiProgressBar progressBar2 = new GuiProgressBar(-10f, 100f, 75f) {
			@Override
			public void act(){
				
				this.changeValue(Gdx.graphics.getDeltaTime() * 10);
				if (getCurrentValue() >= getMaxValue() ) {
					this.setValue(getMinValue());
				}
			}
			
			@Override
			protected String debugTooltipTextComponent() {
				return "Minimum: " + getMinValue()
						+ "\nMaximum: " + getMaxValue()
						+ "\nValue: " + getCurrentValue();
			}
		};
		progressBar2.animationSpeedFactor = 1.45f;
		progressBar2.textAsPercentage = false;
		
		MainContainer.addChild(progressBar2);
		MainContainer.addChild(progressBar);
		MainContainer.addChild(itemSlot);
		MainContainer.addChild(label_useMinDimensions);
		MainContainer.addChild(label);
		MainContainer.addChild(btn_backToMainMenu);
		
		applyDefaultSettings(MainContainer, true);

		MainContainer.setLayout(Layout.Vertical);
		MainContainer.setPadding(GUI.convertX(45));
		MainContainer.setMargin(GUI.convertY(15));	
		MainContainer.pack(new Rectangle(-GUI.convertX(800)*0.5f,-GUI.convertY(500)*0.5f,GUI.convertX(800), GUI.convertY(500)));
		MainContainer.setPosition(-MainContainer.getMarginsBounds().width * .5f, -MainContainer.getMarginsBounds().height * .5f);		
		return MainContainer;	
	}
	




	public static void createTooltip(GUI gui) {
		GUI.tooltipContainer = new Container();

		GUI.tooltip = new GuiLabel("*Your tooltip here!*");
		applyDefaultSettingsToComponent(GUI.tooltip);
		GUI.tooltip.setTextColor(Color.GOLD);
		GUI.tooltip.setTextJustify(GuiUtils.Justify.Center);
		GUI.tooltip.setBackgroundTexture(AssetManager.getTexture("texture/misc/white.png").getTexture());
		GUI.tooltip.setBackgroundOpacity(0.9f);
		GUI.tooltip.setPadding(10);		

		GUI.tooltip.setExpand(Expand.DoNotExpand);
		GUI.tooltipContainer.setExpand(Expand.DoNotExpand);
		
		GUI.tooltipContainer.addChild(GUI.tooltip);
		GUI.tooltipContainer.setBackgroundColor(Color.CORAL);
		GUI.tooltipContainer.setBackgroundOpacity(5.0f);
		GUI.tooltipContainer.setLayout(Layout.Vertical);	
		GUI.tooltipContainer.pack(new Rectangle(0,0,GUI.convertX(0), GUI.convertY(0)));
		
		
	}


}
