package ecu.se.gui;

import com.badlogic.gdx.graphics.Color;

import ecu.se.Game;
import stats.Stats;

public class Window_HUD extends Window {

	@Override
	protected void buildWindow() {		
		widgets = new Widget[] { 
				new Widget_ProgressBar(1000, 1000, 100, 100, this),
				new Widget_ProgressBar(500, 500, 100, 100, this){
					@Override
					public void updateBar() {
						progress = Game.player.getHealth();
						max = Game.player.getStat(Stats.HEALTH);
					}
				},
				
				new Widget_ProgressBar(200, 200, 500, 100, this) {
					@Override
					public void updateBar() {
						progress = Game.player.getMana();
						max = Game.player.getStat(Stats.MANA);	
					}
				},
				
				new Widget_Button(400, 600, 100, 100, this, "Ass") {
					@Override
					public void mousePressed() {
						System.out.println("mousePressed");
					}
					@Override
					public void mouseDown() {
						System.out.println("mouseDown");
					}
					@Override
					public void mouseReleased() {
						System.out.println("mouseReleased");
					}
				},
				new Widget_Label(100, 900, 10, 10, this, "Label Test", 50, Color.BLUE, Color.WHITE),	
		};

	}

}
