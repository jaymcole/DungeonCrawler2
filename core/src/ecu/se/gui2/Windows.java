package ecu.se.gui2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;

import ecu.se.Game;
import ecu.se.Logger;
import ecu.se.archive.Archiver;
import ecu.se.archive.TotalRecords;
import ecu.se.assetManager.AssetManager;
import ecu.se.gui.GUI;
import ecu.se.gui2.GuiUtils.Justify;
import ecu.se.objects.ActiveItem;

public class Windows {
	/**
	 * Convenience class for creating gui windows/elements
	 */
	
	public static Container CreateMainMenu (final GUI gui) {
		Container MainContainer = new Container();
		MainContainer.name = "Main Menu Container";
//		MainContainer.canConsumeUI = false;

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
		
		for(Component widget : MainContainer.getChildren()) {
			widget.setTextJustify(GuiUtils.Justify.Center);
			widget.setPadding(5);
			widget.setMargin(5);
			widget.setExpand(Expand.Auto);
			widget.setBackgroundColor(Color.GRAY);
			widget.setHighlightColor(Color.GREEN);
		}
		
		btn_play.setExpand(Expand.ExpandAll);
		btn_exit.setExpand(Expand.ExpandAll);
		
	
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


		Container stats = stats(gui);
		stats.setPadding(5);
		MainContainer.addChild(stats);
		
		for(Component widget : MainContainer.getChildren()) {
			widget.setTextJustify(GuiUtils.Justify.Center);
			widget.setFontSize(30);
		}		
	
		MainContainer.setLayout(Layout.Vertical);
		MainContainer.setPadding(GUI.convertX(45));
		MainContainer.setMargin(GUI.convertY(15));	
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
		MainContainer.addChild(row);
		for (int i = 0; i < TotalRecords.values().length; i++) {
			row = new Container();
			row.setLayout(Layout.Horizontal);
			TotalRecords stat = TotalRecords.values()[i];
			row.addChild(new GuiLabel(	"" + Archiver.getRecord(stat, true)));
			row.addChild(new GuiLabel("" + Archiver.getRecord(stat, false)));
			row.addChild(new GuiLabel(stat.name()));
			
			row.name = stat.name();

			for(Component widget : row.getChildren()) {
				widget.setMargin(2);
				widget.setBackgroundOpacity(0.1f);
				widget.showToolTip = false;
			}
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

		for(Component widget : MainContainer.getChildren()) {
			widget.setTextJustify(GuiUtils.Justify.Center);
			widget.setFontSize(25);
		}		
		
	
		MainContainer.setLayout(Layout.Vertical);
		MainContainer.setPadding(GUI.convertX(45));
		MainContainer.setMargin(GUI.convertY(15));	
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
		
		
		GUI.primaryItemSlot = new GuiItemSlot("Primary") {
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
		
		MainContainer.addChild(GUI.primaryItemSlot);
		MainContainer.addChild(GUI.secondaryItemSlot);
		
		MainContainer.setLayout(Layout.Vertical);
		MainContainer.setPadding(GUI.convertX(45));
		MainContainer.setMargin(GUI.convertY(15));	
//		MainContainer.pack(new Rectangle(
//				-Gdx.graphics.getWidth() * 0.5f,
//				-Gdx.graphics.getHeight() * 0.5f,
//				Gdx.graphics.getWidth(),
//				Gdx.graphics.getHeight()
//				));
		MainContainer.setPosition(-MainContainer.getMarginsBounds().width * .5f, -MainContainer.getMarginsBounds().height * .5f);

		MainContainer.pack(new Rectangle(
				0,0,0,0));
		MainContainer.setPosition(-10000,-10000);
		return MainContainer;
	}
	
	public static Container CreateSettings(GUI gui) {
		Container MainContainer = new Container();

		
		MainContainer.setLayout(Layout.Vertical);
		MainContainer.setPadding(GUI.convertX(45));
		MainContainer.setMargin(GUI.convertY(15));	
		MainContainer.pack(new Rectangle(0,0,GUI.convertX(800), GUI.convertY(500)));
		MainContainer.setPosition(-MainContainer.getMarginsBounds().width * .5f, -MainContainer.getMarginsBounds().height * .5f);
		return MainContainer;
	}
	
	public static Container CreateInventory(GUI gui) {
		Container MainContainer = new Container();

		
		MainContainer.setLayout(Layout.Vertical);
		MainContainer.setPadding(GUI.convertX(45));
		MainContainer.setMargin(GUI.convertY(15));	
		MainContainer.pack(new Rectangle(0,0,GUI.convertX(800), GUI.convertY(500)));
		MainContainer.setPosition(-MainContainer.getMarginsBounds().width * .5f, -MainContainer.getMarginsBounds().height * .5f);
		return MainContainer;	
	}
	
	public static Container CreatePlayerStats(GUI gui) {
		Container MainContainer = new Container();

		
		MainContainer.setLayout(Layout.Vertical);
		MainContainer.setPadding(GUI.convertX(45));
		MainContainer.setMargin(GUI.convertY(15));	
		MainContainer.pack(new Rectangle(0,0,GUI.convertX(800), GUI.convertY(500)));
		MainContainer.setPosition(-MainContainer.getMarginsBounds().width * .5f, -MainContainer.getMarginsBounds().height * .5f);
		return MainContainer;	
	}
	
	public static  Container CreatePauseWindow(GUI gui) {
		Container MainContainer = new Container();

		
		MainContainer.setLayout(Layout.Vertical);
		MainContainer.setPadding(GUI.convertX(45));
		MainContainer.setMargin(GUI.convertY(15));	
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
		
		
		for(Component widget : MainContainer.getChildren()) {
			widget.setTextJustify(GuiUtils.Justify.Center);
			widget.setPadding(5);
			widget.setMargin(5);
			widget.setBackgroundColor(Color.LIGHT_GRAY);
			
		}
		
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
		GUI.tooltip.setTextColor(Color.GOLD);
		GUI.tooltip.setTextJustify(GuiUtils.Justify.Center);
		GUI.tooltip.setBackgroundTexture(AssetManager.getTexture("texture/misc/white.png").getTexture());
		GUI.tooltip.setBackgroundOpacity(0.9f);
		GUI.tooltip.setBackgroundColor(Color.CORAL);
		GUI.tooltip.setPadding(5);		

		GUI.tooltip.setExpand(Expand.DoNotExpand);
		GUI.tooltipContainer.setExpand(Expand.DoNotExpand);
		
		GUI.tooltipContainer.addChild(GUI.tooltip);
		GUI.tooltipContainer.setBackgroundColor(Color.CORAL);
		GUI.tooltipContainer.setBackgroundOpacity(5.0f);
		GUI.tooltipContainer.setLayout(Layout.Vertical);	
		GUI.tooltipContainer.pack(new Rectangle(0,0,GUI.convertX(0), GUI.convertY(0)));
		
	}


}
