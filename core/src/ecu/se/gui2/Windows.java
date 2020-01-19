package ecu.se.gui2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import ecu.se.Game;
import ecu.se.Logger;
import ecu.se.archive.Archiver;
import ecu.se.archive.TotalRecords;
import ecu.se.assetManager.AssetManager;
import ecu.se.gui.GUI;
import ecu.se.gui2.GuiUtils.Expand;
import ecu.se.gui2.GuiUtils.Layout;
import ecu.se.objects.ActiveItem;

public class Windows {
	/**
	 * Convenience class for creating gui windows/elements
	 */
	
	public static Container CreateMainMenu (GUI gui) {
		Container mainMenu = new Container();
		
		GuiButton btn_play = new GuiButton("Play") {
			@Override
			public void mousePressed(int mouseX, int mouseY) {
				GUI.setWindow(GUI.WINDOW_HUD);
			}
		};
		
		GuiButton btn_newGame = new GuiButton("New Game") {
			@Override
			public void mousePressed(int mouseX, int mouseY) {
				gui.getGame().newGame();
				GUI.setWindow(GUI.WINDOW_HUD);
			}
		};
		
		GuiButton btn_settings = new GuiButton("Settings") {
			@Override
			public void mousePressed(int mouseX, int mouseY) {
				GUI.setWindow(GUI.WINDOW_SETTINGS);
			}
		};
		
		GuiButton btn_guiShowcase = new GuiButton("Gui Components") {
			@Override
			public void mousePressed(int mouseX, int mouseY) {
				GUI.setWindow(GUI.WINDOW_COMPONENT_TEST);
				Logger.Debug(getClass(), "CreateMainMenu", "OWWOWOWOO");
			}
		};
		
		GuiButton btn_stats = new GuiButton("Game Stats") {
			@Override
			public void mousePressed(int mouseX, int mouseY) {
				GUI.setWindow(GUI.WINDOW_GAME_STATS);
			}
		};

		GuiButton btn_exit = new GuiButton("Exit") {
			@Override
			public void mousePressed(int mouseX, int mouseY) {
				Game.currentState = Game.GAME_STATE_EXITING;
			}
		};
		
		
		mainMenu.addChild(btn_exit);
		mainMenu.addChild(btn_stats);
		mainMenu.addChild(btn_guiShowcase);
		mainMenu.addChild(btn_settings);
		mainMenu.addChild(btn_newGame);
		mainMenu.addChild(btn_play);
		
		
		for(Component widget : mainMenu.getChildren()) {
			widget.setTextJustify(GuiUtils.Justify.Center);
			widget.setPadding(5);
		}
	
		mainMenu.setLayout(GuiUtils.Layout.Vertical);
		mainMenu.setPadding(GUI.convertX(45));
		mainMenu.setMargin(GUI.convertY(15));
		mainMenu.setMaxWidth(GUI.convertX(800));
		mainMenu.setMaxHeight(GUI.convertY(500));
		mainMenu.calculateMinDimensions();
		
		mainMenu.resize();
		mainMenu.pack(-mainMenu.getWidth() * .5f, -mainMenu.getHeight() * .5f);
		
		return mainMenu;
	}

	public static Container CreateGameStats(GUI gui) {
		Container gameStats = new Container();

		Container stats = stats(gui);
		stats.setPadding(5);
		gameStats.addChild(stats);
		
		for(Component widget : gameStats.getChildren()) {
			widget.setTextJustify(GuiUtils.Justify.Center);
			widget.setFontSize(30);
		}		
	
		gameStats.setLayout(GuiUtils.Layout.Vertical);
		gameStats.setPadding(GUI.convertX(45));
		gameStats.setMargin(GUI.convertY(15));
		gameStats.setMaxWidth(GUI.convertX(800));
		gameStats.setMaxHeight(GUI.convertY(500));
		gameStats.calculateMinDimensions();
		
		gameStats.resize();
		gameStats.pack(-gameStats.getWidth() * .5f, -gameStats.getHeight() * .5f);
		return gameStats;
	}
	
	private static Container stats(GUI gui) {
		Container statsContainer = new Container();
		statsContainer.setLayout(Layout.Vertical);
		Container row = new Container();
		for (int i = 0; i < TotalRecords.values().length; i++) {
			row = new Container();
			row.setLayout(Layout.Horizontal);
			TotalRecords stat = TotalRecords.values()[i];
			row.addChild(new GuiLabel(stat.name()));
			row.addChild(new GuiLabel("" + Archiver.getRecord(stat, false)));
			row.addChild(new GuiLabel(	"" + Archiver.getRecord(stat, true)));
			
			row.name = stat.name();

			for(Component widget : row.getChildren()) {
				widget.setMargin(2);
				widget.setRenderBackground(false);
				widget.showToolTip = false;
			}
			statsContainer.addChild(row);
		}		

		row = new Container();
		row.addChild(new GuiLabel("Stats"));
		row.addChild(new GuiLabel("Current"));
		row.addChild(new GuiLabel("All Time"));
		statsContainer.addChild(row);

		return statsContainer;
	}
	
	public static Container CreateGameOver(GUI gui) {
		Container gameOver = new Container();
		
		
		Container buttonsBottom = new Container();
		buttonsBottom.setLayout(Layout.Horizontal);
		buttonsBottom.setExpandSettings(Expand.UseMinimumSize);
		GuiButton btn_newGame = new GuiButton("New Game") {
			@Override
			public void mouseReleased(int mouseX, int mouseY) {
				gui.getGame().newGame();
				GUI.setWindow(GUI.WINDOW_HUD);
			}
		};	
//		btn_newGame.setExpandSettings(Expand.UseMinimumSize);
		GuiButton btn_exit = new GuiButton("Quit") {
			@Override
			public void mouseReleased(int mouseX, int mouseY) {
				gui.getGame().newGame();
				GUI.setWindow(GUI.WINDOW_HUD);
			}
		};	
//		btn_exit.setExpandSettings(Expand.UseMinimumSize);
		
		buttonsBottom.addChild(btn_newGame);	
		buttonsBottom.addChild(btn_exit);	
		
		gameOver.addChild(buttonsBottom);		
		gameOver.addChild(stats(gui));

		for(Component widget : gameOver.getChildren()) {
			widget.setTextJustify(GuiUtils.Justify.Center);
			widget.setFontSize(25);
		}		
		
	
		gameOver.setLayout(GuiUtils.Layout.Vertical);
		gameOver.setPadding(GUI.convertX(45));
		gameOver.setMargin(GUI.convertY(15));
		gameOver.setMaxWidth(GUI.convertX(800));
		gameOver.setMaxHeight(GUI.convertY(500));
		gameOver.calculateMinDimensions();
		
		gameOver.resize();
		gameOver.pack(-gameOver.getWidth() * .5f, -gameOver.getHeight() * .5f);
		
		return gameOver;
	}

	public static Container CreateHUD(GUI gui) {
		Container container = new Container();
		
		gui.primaryItemSlot = new GuiItemSlot("Primary") {
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
		gui.secondaryItemSlot = new GuiItemSlot("Scondary") {
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
		
		container.addChild(gui.primaryItemSlot);
		container.addChild(gui.secondaryItemSlot);
		
		
		container.setLayout(GuiUtils.Layout.Vertical);
		container.calculateMinDimensions();
		
		container.resize();
		container.pack(-50, -50);
		
		return container;
	}
	
	public static Container CreateSettings(GUI gui) {
		return new Container();
	}
	
	public static Container CreateInventory(GUI gui) {
		return new Container();
	}
	
	public static Container CreatePlayerStats(GUI gui) {
		return new Container();
	}
	
	public static  Container CreatePauseWindow(GUI gui) {
		return new Container();
	}
	
	public static Container CreateComponentTest(GUI gui) {
		Container container = new Container();
		
		GuiButton btn_backToMainMenu = new GuiButton("Button - Return to Main Menu") {
			@Override
			public void mouseReleased(int mouseX, int mouseY) {
				GUI.setWindow(GUI.WINDOW_MAIN_MENU);
			}
		};
		
		GuiLabel label = new GuiLabel("Label Test");
		label.setTextJustify(GuiUtils.Justify.Center);
		
		GuiLabel label_useMinDimensions = new GuiLabel("Label - Use Minimum Size");
		label_useMinDimensions.setTextJustify(GuiUtils.Justify.Center);
		label_useMinDimensions.setExpandSettings(GuiUtils.Expand.UseMinimumSize);
		
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
		
		
		GuiDropDownMenu drop = new GuiDropDownMenu("Drop Down Menu");
		drop.addChild(new GuiLabel("Menu Item #1"));
		
		
		
		
		
		
		
		
		
		container.addChild(drop);
		container.addChild(progressBar2);
		container.addChild(progressBar);
		container.addChild(itemSlot);
		container.addChild(label_useMinDimensions);
		container.addChild(label);
		container.addChild(btn_backToMainMenu);
		
		
		for(Component widget : container.getChildren()) {
			widget.setTextJustify(GuiUtils.Justify.Center);
			widget.setPadding(5);
			widget.setBackgroundColor(Color.LIGHT_GRAY);
			
		}
		
		container.setLayout(GuiUtils.Layout.Vertical);
		container.setPadding(GUI.convertX(45));
		container.setMargin(GUI.convertY(15));
		container.setMaxWidth(GUI.convertX(800));
		container.setMaxHeight(GUI.convertY(500));
		container.calculateMinDimensions();
		
		container.resize();
		container.pack(-container.getWidth() * .5f, -container.getHeight() * .5f);
		
		return container;
	}
	




	public static void createTooltip(GUI gui) {
		GUI.tooltipContainer = new Container();

		GUI.tooltip = new GuiLabel("*Your tooltip here!*");
		GUI.tooltip.setTextColor(Color.GOLD);
		GUI.tooltip.setTextJustify(GuiUtils.Justify.Center);
		GUI.tooltip.setPadding(5);
		GUI.tooltip.setBackgroundTexture(AssetManager.getTexture("texture/misc/white.png").getTexture());
		GUI.tooltip.setBackgroundOpacity(1.0f);
		GUI.tooltip.setBackgroundColor(Color.CORAL);

		GUI.tooltip.setExpandSettings(GuiUtils.Expand.UseMinimumSize);
		GUI.tooltipContainer.setExpandSettings(GuiUtils.Expand.UseMinimumSize);
		
		GUI.tooltipContainer.addChild(GUI.tooltip);
		GUI.tooltipContainer.calculateMinDimensions();
		GUI.tooltipContainer.setMaxWidth(GUI.convertX(800));
		GUI.tooltipContainer.setMaxHeight(GUI.convertY(500));
		GUI.tooltipContainer.pack(0, 0);
		GUI.tooltipContainer.setBackgroundColor(Color.CORAL);
		GUI.tooltipContainer.setBackgroundOpacity(1.0f);
	}


}
