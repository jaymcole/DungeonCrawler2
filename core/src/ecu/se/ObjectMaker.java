package ecu.se;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

import actions.Action;
import actions.Potion;
import actions.Spell;
import actions.Spell_Explosion;
import actions.Spell_Fireball;
import actions.Spell_Teleport;
import actors.Actor;
import actors.BasicEnemy;
import actors.RandomBasicEnemy;
import actors.RangedBadGuy;
import actors.TeleportEnemy;
import ecu.se.gui.GUI;
import ecu.se.gui.Window_Inventory;
import ecu.se.objects.ActiveItem;
import ecu.se.objects.FadingLight;
import ecu.se.objects.InteractableItem;
import ecu.se.objects.Light;
import stats.Stats;

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
		InteractableItem orb = new InteractableItem(x - ORB_SIZE, y, ORB_SIZE, ORB_SIZE, "Health Orb", "texture/misc/orb_blue.png") {
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
		BasicEnemy mob;
		if (Utils.randomBoolean()) {
			mob = new BasicEnemy(x,	y, 0,new String[] { "texture/spritesheet/grayguys.png" }, new int[] { 0 });			
		} else {
			mob = new RandomBasicEnemy(x,	y, 0,new String[] { "texture/spritesheet/grayguys.png" }, new int[] { 0 });	
		}
		
		
		mob.setBaseStat(Stats.BASE_CONSTITUION, 		100);
		mob.setBaseStat(Stats.BASE_DEXTERITY, 			1);
		mob.setBaseStat(Stats.BASE_INTELLIGENCE, 		10);
		mob.setBaseStat(Stats.BASE_MAGICAL_RESISTANCE, 	1);
		mob.setBaseStat(Stats.BASE_PHYSICAL_RESISTANCE, 1);
		mob.setBaseStat(Stats.BASE_SPEED, 				1);
		mob.setBaseStat(Stats.BASE_STRENGTH, 			1);		
		mob.setBaseStat(Stats.MANA_REGEN, 3 + Utils.getRandomInt(5));
		
		Spell_Fireball primary = new Spell_Fireball(mob);		
		primary.setBaseCooldown(10);
		mob.setPrimaryAction(primary);
		
		return mob;
	}
	
	public static ActiveItem createActiveItem(float x, float y, Action action) {
		
		//TODO: Find a reasonable default action icon.
		String texturePath = "texture/items/scroll_blank.png";
		if (action instanceof Spell) {
			if (action instanceof Spell_Fireball) {
				texturePath = "texture/items/scroll_fireball.png";
			} else if (action instanceof Spell_Teleport) {
				texturePath = "texture/items/scroll_teleport.png";
			} else {
				texturePath = "texture/items/scroll_blank.png";
			}
		} else if (action instanceof Potion) {
			if (((Potion)action).getPotionType() == Potion.POTION_HEALTH)
				texturePath = "texture/items/potion_health.png";
			else 
				texturePath = "texture/items/potion_mana.png";
		}
		
		
		
		ActiveItem a = new ActiveItem(x,y, "Action Scroll", texturePath);
		a.setAction(action);
		a.setSize(40, 40);
		return a;
	}
	
}
