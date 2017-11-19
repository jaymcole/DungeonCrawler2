package ecu.se.gui;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;

import archive.Archiver;
import archive.TimeRecords;
import stats.Stats;

public class Window_PlayerStats extends Window {

	public Window_PlayerStats(GUI gui) {
		super(gui);
	}

	@Override
	protected void buildWindow() {
		onResume();

	}

	@Override
	public void onResume() {
		Archiver.set(TimeRecords.TIME_IN_MENU, false);
		
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
			public void mouseReleased() {
				gui.closeWindow(GUI.WINDOW_HUD, GUI.WINDOW_HUD);
			}
		};
		widgetList.add(btn_close);
		
		
		int fontSize = 35;
		float labelYChange = fontSize * 0.5f;
		float startLabelX = halfwayX - halfwayX * 0.5f, startLabelY = halfwayY - labelYChange + backgroundY;
		float currentY = 40;
		float currentX = 10;
		
		System.out.println((halfwayY - backgroundY));
		for(int i = 0 ; i < Stats.values().length; i++) {
			widgetList.add(new Widget_Label(currentX + startLabelX, startLabelY - currentY, 0, 0, this, Stats.values()[i].name(), fontSize, Color.BLACK, Color.WHITE));
			widgetList.add(new Widget_Label(currentX + startLabelX + 300, startLabelY - currentY, 0, 0, this, "" + gui.player.getStat(Stats.values()[i]), fontSize, Color.BLACK, Color.WHITE));
			currentY += labelYChange;
			
			if (startLabelY - currentY < halfwayY - backgroundY) {
				currentY = 40;
				System.out.println("Passed bottoms");
				currentX += 475;
			}
			
		}
		
		widgets = widgetList.toArray(widgets);
	}

}
