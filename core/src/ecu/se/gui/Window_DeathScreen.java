package ecu.se.gui;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;

import ecu.se.Game;
import ecu.se.archive.Archiver;
import ecu.se.archive.TimeRecords;
import ecu.se.archive.TotalRecords;
import ecu.se.stats.Stats;

//TODO: Game over / Death screen
public class Window_DeathScreen extends Window{

	public Window_DeathScreen(GUI gui) {
		super(gui);
	}

	@Override
	protected void buildWindow() {
		ArrayList<Widget> widgetList = new ArrayList<Widget>();
		int halfwayX = (int)(GUI.defaultWidth * 0.5f);
		int halfwayY = (int)(GUI.defaultHeight * 0.5f);
		int backgroundX = (int)(halfwayX * 0.5f);
		int backgroundY = (int)(halfwayY * 0.5f);
		int bufferX = 50;
		
		
		Widget_Image img_background = new Widget_Image(backgroundX, backgroundY, halfwayX, halfwayY, this, "texture/misc/paper.jpg");
		img_background.setDefaultColor(Color.WHITE);
		
		Widget_Label lbl_title = new Widget_Label(backgroundX + bufferX, backgroundY + halfwayY - 25, 0, 0, parent, this.windowName, 50, Color.CLEAR, Color.BLACK);
		
		
		
		int fontSize = 35;
		float labelYChange = fontSize * 0.5f;
		float startLabelX = halfwayX - halfwayX * 0.5f, startLabelY = halfwayY - labelYChange + backgroundY;
		float currentY = 40;
		float currentX = 10;
		float attrButtonSize = 15;
		float xOffset = attrButtonSize * 1.25f;
		Widget_Button btn_newGame = new Widget_Button(backgroundX + bufferX, backgroundY + halfwayY - 400, halfwayX - bufferX * 2, 50, this, "New Game") {
			@Override
			public void mouseReleased(int mouseX, int mouseY) {
				gui.getGame().newGame();
				gui.setWindow(GUI.WINDOW_HUD);
			}
		};		
		
		widgetList.add(img_background);
		widgetList.add(lbl_title);
		widgetList.add(btn_newGame);
		
		
		
		
		
		
		
		
		
		for (int i = 0; i < TotalRecords.values().length; i++) {
			TotalRecords stat = TotalRecords.values()[i];

			widgetList.add(new Widget_Label(xOffset + currentX + startLabelX, startLabelY - currentY, 0, 0, this,
					stat.name(), fontSize, Color.BLACK, Color.WHITE));
			widgetList.add(new Widget_Label(xOffset + currentX + startLabelX + 300, startLabelY - currentY, 0, 0, this,
					"" + Archiver.getRecord(stat, true), fontSize, Color.BLACK, Color.WHITE));

			
			widgetList.add(new Widget_Label(halfwayX, startLabelY - currentY, 0, 0, this,
					stat.name(), fontSize, Color.BLACK, Color.WHITE));
			widgetList.add(new Widget_Label(halfwayX+300, startLabelY - currentY, 0, 0, this,
					"" + Archiver.getRecord(stat, false), fontSize, Color.BLACK, Color.WHITE));
			currentY += labelYChange;
		}
		

		
		widgets = widgetList.toArray(widgets);
	}
	
	@Override
	public void onResume() {
		Archiver.set(TimeRecords.TIME_IN_MENU, false);
		buildWindow();
	}

}
