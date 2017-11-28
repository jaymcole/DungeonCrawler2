package actions;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import actors.Actor;
import actors.Team;
import archive.Archiver;
import archive.TotalRecords;
import ecu.se.GameObject;
import stats.Stats;

public abstract class Spell extends Action {
	private final int CHANNEL 	= 0;
	private final int CAST		= 1;
	private final int COOLDOWN	= 2;
	
	protected int currentStage = -1;

	protected float baseCastSpeed;
	protected float currentCastSpeed;
	
	protected float baseManaCost;
	protected float currentManaCost;
	
	protected float baseCooldown;
	protected float currentCooldown;
	
	protected float baseDamage;
	
	protected TextureRegion textureRegion;
	protected Actor caster;
	
	protected float targetX, targetY;
	protected GameObject target;
	
	protected float timer;
	
	protected Team team;
	
	protected boolean instantCast = false;
	
	public Spell(Actor caster) {
		this.caster = caster;
		active = false;
		this.team = caster.team;
		baseCastSpeed 	= 0;
		baseCooldown 	= 0;
		baseManaCost 	= 10f;
	}
	
	
	
	public void act(int x, int y) {
		if (active)
			return;
		Archiver.set(TotalRecords.ACTIONS_TAKEN, 1);
		calculateStats();
		// Initialize variables based on caster ability.
		
		timer = 0;
		targetX = x;
		targetY = y;
		
		// Check if caster has enough mana
		if (caster.getMana() < currentManaCost) {
			active = false;
			return;
		} else {
			caster.setMana(-currentManaCost);
		}
		
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
			Archiver.set(TotalRecords.SPELLS_CAST, 1);
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
	
	
	public void setBaseCooldown(float cooldown) { 
		this.baseCooldown = cooldown;
	}
	
	public void setBaseCastSpeed(float baseCastSpeed) { 
		this.baseCastSpeed = baseCastSpeed;
	}
	
	public void setBaseManaCost(float baseManaCost) { 
		this.baseManaCost = baseManaCost;
	}
	
	private void calculateStats() {
		currentCastSpeed = baseCastSpeed * caster.getStat(Stats.SPELL_CAST_SPEED);
		currentManaCost = baseManaCost * (1 - caster.getStat(Stats.MANA_COST));
		currentCooldown = baseCooldown * caster.getStat(Stats.SPELL_COOLDOWN);
	}
}
