package ecu.se;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

import actors.Actor;
import actors.BasicEnemy;
import actors.RangedBadGuy;
import ecu.se.objects.FadingLight;
import ecu.se.objects.InteractableItem;
import ecu.se.objects.Light;

public class ObjectMaker {
	private static final int ORB_SIZE = 15;
	private static final int ORB_BRIGHTNESS = 300;
	private static final int HEALTH_PICKUP_VALUE = 15;
	private static final int MANA_PICKUP_VALUE = 15;

	
	public static GameObject createHealthOrb (float x, float y) {
		
		InteractableItem orb = new InteractableItem(x, y, ORB_SIZE, ORB_SIZE, "Health Orb", "texture/misc/orb_red.png") {
			@Override
			public void onCollision(GameObject otherObject) {
				if (otherObject == Game.player) {
					((Actor)otherObject).setHealth(HEALTH_PICKUP_VALUE);
					this.kill();
				}
			}
			
			@Override
			public void onClick(GameObject otherObject) {
				((Actor)otherObject).setHealth(HEALTH_PICKUP_VALUE);
				this.kill();
			}
			
			@Override
			public void die() {
				Lighting.removeLight(light);
				FadingLight fadeLight = new FadingLight(new Vector3(x, y, 0), Color.RED, ORB_BRIGHTNESS * 3, 0.99f, 2);
				Lighting.addLight(fadeLight);
			}
		};
		Light light = new Light(new Vector3(x, y, 0));
		light.setColor(Color.RED);
		light.type = 2;
		light.setIntensity(ORB_BRIGHTNESS);
		light.setParent(orb);
		Lighting.addLight(light);
		orb.setLight(light);
		return orb;
	}
	
	public static GameObject createManaOrb (float x, float y) {
		InteractableItem orb = new InteractableItem(x, y, ORB_SIZE, ORB_SIZE, "Health Orb", "texture/misc/orb_blue.png") {
			@Override
			public void onCollision(GameObject otherObject) {
				if (otherObject == Game.player) {
					((Actor)otherObject).setMana(MANA_PICKUP_VALUE);
					this.kill();
				}
			}
			
			@Override
			public void onClick(GameObject otherObject) {
				((Actor)otherObject).setMana(MANA_PICKUP_VALUE);
				this.kill();
			}
			
			@Override
			public void die() {
				Lighting.removeLight(light);
				FadingLight fadeLight = new FadingLight(new Vector3(x, y, 0), Color.BLUE, ORB_BRIGHTNESS * 3, 0.99f, 2);
				Lighting.addLight(fadeLight);
			}
		};
		Light light = new Light(new Vector3(x, y, 0));
		light.setColor(Color.BLUE);
		light.type = 2;
		light.setIntensity(ORB_BRIGHTNESS);
		light.setParent(orb);

		Lighting.addLight(light);
		orb.setLight(light);
		return orb;
	}
	
	public static Actor createMob(float x, float y) {
		return new RangedBadGuy(x,	y, 0,new String[] { "texture/spritesheet/grayguys.png" }, new int[] { 0 });
	}
	
	public static Actor createTestMob(float x, float y) {
		return new BasicEnemy(x,	y, 0,new String[] { "texture/spritesheet/grayguys.png" }, new int[] { 0 });
	}
}
