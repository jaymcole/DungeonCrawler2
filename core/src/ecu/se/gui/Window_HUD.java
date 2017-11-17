package ecu.se.gui;

import com.badlogic.gdx.graphics.Color;

import ecu.se.Game;
import stats.Stats;

public class Window_HUD extends Window {

	@Override
	protected void buildWindow() {
		Widget_Label label1 = new Widget_Label(0, 0, 10, 10, this, "AHGSYASYUYSAKHJGSA", 20, Color.BLUE, Color.WHITE);
		Widget_Label label2 = new Widget_Label(0, 0, 10, 10, this, "AHGSYASYUYSAKHJGSA", 40, Color.BLUE, Color.WHITE);
		Widget_Label label3 = new Widget_Label(0, 0, 10, 10, this, "AHGSYASYUYSAKHJGSA", 60, Color.BLUE, Color.WHITE);
		
		Widget_Label label4 = new Widget_Label(0, 0, 10, 10, this, "AHGSYASYUYSAKHJGSA", 80, Color.BLUE, Color.WHITE);
		Widget_Label label5 = new Widget_Label(0, 0, 10, 10, this, "AHGSYASYUYSAKHJGSA", 100, Color.BLUE, Color.WHITE);
		Widget_Label label6 = new Widget_Label(0, 0, 10, 10, this, "AHGSYASYUYSAKHJGSA", 120, Color.BLUE, Color.WHITE);
		Widget_Label label7 = new Widget_Label(0, 0, 10, 10, this, "AHGSYASYUYSAKHJGSA", 220, Color.BLUE, Color.WHITE);
		Widget_Label label8 = new Widget_Label(0, 0, 10, 10, this, "AHGSYASYUYSAKHJGSA", 320, Color.BLUE, Color.WHITE);
		Widget_Label label9 = new Widget_Label(0, 0, 10, 10, this, "AHGSYASYUYSAKHJGSA", 420, Color.BLUE, Color.WHITE);
		
		
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
				new Widget_Label(100, 900, 0, 0, this, "Label Test", 50, Color.BLUE, Color.BLACK),
				label1,
				label2,
				label3,
				label4,
				label5,
				label6,
				label7,
				label8
				
				
		};

	}

}
