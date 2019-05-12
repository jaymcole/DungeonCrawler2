package ecu.se.actions;

import ecu.se.Game;
import ecu.se.GameObject;
import ecu.se.actors.Actor;
import ecu.se.actors.Team;
import ecu.se.archive.Archiver;
import ecu.se.archive.TotalRecords;
import ecu.se.stats.Stats;

/**
 * 
 *	Spells can be used by Actors.
 */
public abstract class Spell extends Action {
	private final int CHANNEL 	= 0;
	private final int CAST		= 1;
	private final int COOLDOWN	= 2;
	
	protected int currentStage = -1;

	protected String sound_cast;
	protected String sound_loop;
	protected String sound_end;
	protected String texture_path;
	
	/**
	 * Base stats for the spell.
	 */
	protected float baseCastSpeed;
	protected float currentCastSpeed;
	
	protected float baseManaCost;
	protected float currentManaCost;
	
	protected float baseCooldown;
	protected float currentCooldown;
	
	protected float baseDamage;
	/**
	 * 
	 */
		
	/**
	 * The position the spell moves toward.
	 */
	protected float targetX, targetY;
	
	/**
	 * The GameObject that spell is being cast on.
	 */
	protected GameObject target;
	
	/**
	 * float for moving between phases.
	 */
	protected float timer;
	
	/**
	 * The team this spell belongs to.
	 */
	protected Team team;
	
	protected boolean instantCast = false;
	
	public Spell(Actor caster) {
		super(caster);
		active = false;
		this.team = caster.team;
		baseCastSpeed 	= 0;
		baseCooldown 	= 0;
		baseManaCost 	= 10f;
	}
	
	
	@Override
	public void act(int x, int y) {
		if (active)
			return;
		calculateStats();
		
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
			if (caster == Game.player)
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
	 * Spells that need to be channeled require some time before that can be cast.
	 * Override if an animation or something needs to happen while channeling, ignore otherwise.
	 */
	protected void channel(float deltaTime) {
		timer += deltaTime;
		if(timer >= currentCastSpeed) {
			currentStage++;
			timer = 0;
		}
	}
	
	/**
	 * What the spell needs to actually do.
	 * @param deltaTime
	 */
	protected abstract void cast(float deltaTime);

	/**
	 * Time phase before that spell can be cast again.
	 * @param deltaTime
	 */
	protected void cooldown(float deltaTime) {
		timer += deltaTime;
		if (timer >= currentCooldown) {
			timer = 0;
			active = false;
			currentStage++;
		}
	}
	
	/**
	 * Sets the baseCooldown to cooldown.
	 * @param cooldown
	 */
	public void setBaseCooldown(float cooldown) { 
		this.baseCooldown = cooldown;
	}
	
	/**
	 * Sets baseCastSpeed to baseCastSpeed
	 * @param baseCastSpeed
	 */
	public void setBaseCastSpeed(float baseCastSpeed) { 
		this.baseCastSpeed = baseCastSpeed;
	}
	
	/**
	 * Sets baseManaCost to baseManaCost
	 * @param baseManaCost
	 */
	public void setBaseManaCost(float baseManaCost) { 
		this.baseManaCost = baseManaCost;
	}
	
	/**
	 * Calculates the base costs for this spell based on the casters ability.
	 */
	private void calculateStats() {
		currentCastSpeed = baseCastSpeed * caster.getStat(Stats.SPELL_CAST_SPEED);
		currentManaCost = baseManaCost * (1 - caster.getStat(Stats.MANA_COST));
		currentCooldown = baseCooldown * caster.getStat(Stats.SPELL_COOLDOWN);
	}
}
