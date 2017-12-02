package ecu.se.gui;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;

import ecu.se.Game;
import ecu.se.archive.Archiver;
import ecu.se.archive.TimeRecords;
import ecu.se.archive.TotalRecords;
import ecu.se.stats.Stats;

public class Window_PlayerStats extends Window {

	public Window_PlayerStats(GUI gui) {
		super(gui);
	}

	@Override
	protected void buildWindow() {
		widgets = new Widget[0];
		ArrayList<Widget> widgetList = new ArrayList<Widget>();
		int halfwayX = (int) (GUI.defaultWidth * 0.5f);
		int halfwayY = (int) (GUI.defaultHeight * 0.5f);
		int backgroundX = (int) (halfwayX * 0.5f);
		int backgroundY = (int) (halfwayY * 0.5f);
		int bufferX = 10;

		Widget_Image img_background = new Widget_Image(backgroundX, backgroundY, halfwayX, halfwayY, this,
				"texture/misc/white.png");
		img_background.setDefaultColor(new Color(Color.CHARTREUSE.r, Color.CHARTREUSE.g, Color.CHARTREUSE.b, 0.5f));
		widgetList.add(img_background);

		Widget_Label lbl_title = new Widget_Label(backgroundX + bufferX, backgroundY + halfwayY - 25, 0, 0, parent,
				this.windowName, 50, Color.CLEAR, Color.BLACK);
		widgetList.add(lbl_title);

		Widget_Button btn_close = new Widget_Button(backgroundX + backgroundX + bufferX, backgroundY + halfwayY - 30,
				100, 35, this, "Close") {
			@Override
			public void mouseReleased(int mouseX, int mouseY) {
				gui.closeWindow(GUI.WINDOW_HUD, GUI.WINDOW_HUD);
			}
		};
		widgetList.add(btn_close);

		int fontSize = 35;
		float labelYChange = fontSize * 0.5f;
		float startLabelX = halfwayX - halfwayX * 0.5f, startLabelY = halfwayY - labelYChange + backgroundY;
		float currentY = 40;
		float currentX = 10;
		float attrButtonSize = 15;
		float xOffset = attrButtonSize * 1.25f;
		System.out.println((halfwayY - backgroundY));
		Widget_Label lbl_AttrPoints = new Widget_Label(currentX + startLabelX, startLabelY - 20, 0, 0, this,
				"Attribute Points: " + Game.player.getRemainingAttributePoints(), fontSize, Color.BLACK, Color.WHITE);
		for (int i = 0; i < Stats.values().length; i++) {
			Stats stat = Stats.values()[i];
			if (stat.upgradeable) {
				Widget_Button attrButton = new Widget_Button(currentX + startLabelX, startLabelY - currentY,
						attrButtonSize, attrButtonSize, this, "+") {
					@Override
					public void mouseReleased(int mouseX, int mouseY) {
						if (Game.player.getRemainingAttributePoints() > 0) {
							Game.player.addAttributePoints(-1);
							Game.player.setBaseStat(Stats.values()[variableOne], Game.player.getStat(Stats.values()[variableOne]) + 1);
							Archiver.set(TotalRecords.ATTRIBUTE_POINTS_SPENT, 1);
							buildWindow();
						}
					}
				};
				
				if (Game.player.getAttributePoints() > 0) {
					attrButton.setMultiVariableOne(i);
					widgetList.add(attrButton);
				}
			}

			widgetList.add(new Widget_Label(xOffset + currentX + startLabelX, startLabelY - currentY, 0, 0, this,
					stat.name(), fontSize, Color.BLACK, Color.WHITE));
			widgetList.add(new Widget_Label(xOffset + currentX + startLabelX + 300, startLabelY - currentY, 0, 0, this,
					"" + gui.player.getStat(stat), fontSize, Color.BLACK, Color.WHITE));
			currentY += labelYChange;

			if (startLabelY - currentY < halfwayY - backgroundY) {
				currentY = 40;
				System.out.println("Passed bottoms");
				currentX += 475;
			}
		}
		widgetList.add(lbl_AttrPoints);

		widgets = widgetList.toArray(widgets);
	}

	@Override
	public void onResume() {
		Archiver.set(TimeRecords.TIME_IN_MENU, false);
		buildWindow();
	}

}
