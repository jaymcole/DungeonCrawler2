package actions;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import actors.Actor;
import actors.Stats;
import ecu.se.GameObject;

public abstract class Spell extends Action{
	public static final int CHANNEL 	= 0;
	public static final int CAST		= 1;
	public static final int COOLDOWN	= 2;
	protected int currentStage = -1;

	protected float baseCastSpeed;
	protected float currentCastSpeed;
	
	protected float baseManaCost;
	protected float currentManaCost;
	
	protected float baseCooldown;
	protected float currentCooldown;
	
	protected TextureRegion textureRegion;
	protected Actor caster;
	
	protected float targetX, targetY;
	protected GameObject target;
	
	protected float timer;
	
	protected boolean instantCast = false;
	
	public Spell(Actor caster) {
		this.caster = caster;
		active = false;
	}
	
	public void act(int x, int y) {
		if (active)
			return;
		// Initialize variables based on caster ability.
		currentCastSpeed = baseCastSpeed * caster.getStat(Stats.SPELL_CAST_SPEED);
		currentManaCost = baseManaCost * caster.getStat(Stats.MANA_COST);
		currentCooldown = baseCooldown * caster.getStat(Stats.SPELL_COOLDOWN);
		timer = 0;
		targetX = x;
		targetY = y;
		
		// Check if caster has enough mana
		if (caster.getMana() < currentManaCost) {
			active = false;
			System.err.println("Insufficient Mana!!\n\t Caster Mana currently sitting at " + caster.getMana());
			//TODO: Print message somewhere indicating insufficient mana.
			return;
		} else {
			caster.setMana(-currentManaCost);
		}
		System.out.println("Casting: " + this.getClass().getName());
		
		if (instantCast)
			currentStage = CAST;
		else
			currentStage = CHANNEL;
		active = true;
		caster.addActiveAction(this);
	}
	
	@Override
	public void update(float deltaTime) {
		switch (currentStage) {
		case CHANNEL:
			channel(deltaTime);
			break;
		case CAST:
			if (target != null) {
				targetX = target.getX();
				targetY = target.getY();
			}
			cast(deltaTime);
			break;
		case COOLDOWN:
			cooldown(deltaTime);
			break;
		default:
			active = false;
			break;
		}
	}
	
	/**
	 * Override if an animation or something needs to happen while channeling, ignore otherwise.
	 */
	protected void channel(float deltaTime) {
		timer += deltaTime;
		if(timer >= currentCastSpeed) {
			currentStage++;
			timer = 0;
		}
	}
	
	protected abstract void cast(float deltaTime);

	protected void cooldown(float deltaTime) {
		timer += deltaTime;
		if (timer >= currentCooldown) {
			timer = 0;
			active = false;
			currentStage++;
		}
	}
	
	
	
}
