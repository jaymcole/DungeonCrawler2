package ecu.se.gui;

import com.badlogic.gdx.graphics.Color;

import ecu.se.Game;
import stats.Stats;

public class Window_HUD extends Window {

	@Override
	protected void buildWindow() {
		int extra = 14;
		widgets = new Widget[] { 
				// Player Health bar
				new Widget_ProgressBar(254, 1014 + extra, 606, 45, this, Color.FIREBRICK, Color.RED){
					@Override
					public void updateBar() {
						progress = Game.player.getHealth();
						max = Game.player.getStat(Stats.HEALTH);
					}
				},
				
				// Player Mana Bar
				new Widget_ProgressBar(254, 970 + extra, 438, 44, this, Color.SKY, Color.BLUE) {
					@Override
					public void updateBar() {
						progress = Game.player.getMana();
						max = Game.player.getStat(Stats.MANA);	
					}
				},
				
				//Player Primary
				new Widget_Button(1859, 535, 60, 55, this, "Prim") {
					@Override
					public void mousePressed() {
						System.out.println("Bitch please");
					}
					@Override
					public void mouseDown() {
					}
					@Override
					public void mouseReleased() {
						System.out.println("maaaan");
					}
				},
				
				//Player Secondary
				new Widget_Button(1859, 478, 60, 55, this, "Seco") {
					@Override
					public void mousePressed() {
						System.out.println("Secondary WOW");
					}
					@Override
					public void mouseDown() {
					}
					@Override
					public void mouseReleased() {
						System.out.println("unfortunate");
					}
				},
				
				// QUICKBAR (1-13)
				new Widget_Button(595, 2, 56, 62, this, "1") {
					@Override
					public void mousePressed() {
						System.out.println(":( 1");
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
				new Widget_Button(651, 2, 56, 62, this, "2") {
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
				new Widget_Button(708, 2, 56, 62, this, "3") {
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
				new Widget_Button(764, 2, 56, 62, this, "4") {
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
				new Widget_Button(821, 2, 56, 62, this, "5") {
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
				new Widget_Button(877, 2, 56, 62, this, "6") {
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
				new Widget_Button(933, 2, 56, 62, this, "7") {
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
				new Widget_Button(990, 2, 56, 62, this, "8") {
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
				new Widget_Button(1046, 2, 56, 62, this, "9") {
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
				new Widget_Button(1102, 2, 56, 62, this, "10") {
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
				new Widget_Button(1158, 2, 56, 62, this, "11") {
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
				new Widget_Button(1214, 2, 56, 62, this, "12") {
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
				new Widget_Button(1272, 2, 56, 62, this, "13") {
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
				
				// Test Label
				new Widget_Label(800, 80, 10, 10, this, "Wash The Queen", 50, Color.BLUE, Color.WHITE),
			
				
				
				// GUI OVERLAY
				new Widget_Image(0, 0, GUI.defaultWidth, GUI.defaultHeight, this, "texture/gui/hud.png")
		};

	}

}
