package actors;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import actions.Action;
import archive.Archiver;
import archive.TotalRecords;
import assetManager.Animation;
import assetManager.AssetManager;
import ecu.se.DecalPicker;
import ecu.se.Game;
import ecu.se.GameObject;
import ecu.se.Lighting;
import ecu.se.ObjectMaker;
import ecu.se.ObjectManager;
import ecu.se.Utils;
import ecu.se.map.Direction;
import ecu.se.map.Map;
import ecu.se.objects.Decal;
import ecu.se.objects.InteractableItem;
import ecu.se.objects.Light;
import stats.Stats;
import stats.TempStatModifier;

/**
 * 
 * Actor: All game players. (npcs, enemies, player, etc)
 */
public abstract class Actor extends GameObject {

	protected String name;
	protected String spriteSheet;
	// protected Texture texture;
	protected float oldx = 0;
	protected float oldy = 0;
	protected Direction direction;

	protected int spriteWidth;// = 40;
	protected int spriteHeight;// = 48;
	// protected TextureRegion textureRegion;
	protected boolean awake;
	protected boolean invulnerable;
	protected boolean invisible;

	/**
	 * Used for movement modifiers like explosions.
	 */
	protected float externalForcesX;
	protected float externalForcesY;

	/**
	 * baseStats: All permanent stats. i.e. Allocated skill points from leveling
	 * up
	 */
	protected float[] baseStats = new float[Stats.values().length];

	/**
	 * modifierStats: Stats the change but do not require regular updates. i.e.
	 * Stat changes from equipped items.
	 */
	protected float[] modifierStats = new float[Stats.values().length];

	/**
	 * currentStats: The actual stats the game pulls from.
	 */
	protected float[] currentStats = new float[Stats.values().length];

	/**
	 * tempStatModifiers: Stats requiring regular updates. i.e. A poison debuff
	 * needs to damage the player every tic and likely has a limited life span
	 * that needs to be checked/updated every tic.
	 */
	protected LinkedList<TempStatModifier> tempStatModifiers;

	protected int healthbarWidth = 50;
	protected int healthbarHeight = 40;

	protected Action primaryAction;
	protected Action secondaryAction;

	protected ArrayList<Action> actions = new ArrayList<Action>();
	protected LinkedList<Action> activeActions = new LinkedList<Action>();
	protected LinkedList<Animation> animations = new LinkedList<Animation>();

	protected Vector2 currentSpeed;
	protected float currentHealth;
	protected float currentMana;

	protected int characterLevel;
	protected int characterXP;
	protected int xpToLevel;
	private int xpWorth = 5;
	protected int attributePoints;

	private Texture debugHealthBarTexture;
	protected float scaleX, scaleY;
	public Vector2 lookAt;

	public Actor(float x, float y, float z, String[] spriteSheets, int[] row) {
		super(x, y, z);
		for (int i = 0; i < Math.min(spriteSheets.length, row.length); i++) {
			Animation animation = new Animation(0, 0, 0, AssetManager.getSpriteSheet(spriteSheets[i]));
			animation.setRow(row[i]);
			animations.add(animation);
		}
		invulnerable = false;
		light = null;
		attributePoints = 0;
		invisible = false;
		debugHealthBarTexture = AssetManager.getTexture("texture/misc/white.png").getTexture();
		bounds = Utils.getRectangleBounds(x, y, 40, 80, Utils.ALIGN_CENTERED);
		bounds = Utils.getEllipseBounds(x, y, 25, 25, 20);
		tempStatModifiers = new LinkedList<TempStatModifier>();
		setDefaults();
		calculateStats();
		team = Team.MOB;
		currentHealth = getStat(Stats.HEALTH);
		currentMana = getStat(Stats.MANA);

		characterLevel = 0;
		characterXP = 0;
		xpToLevel = 25;

		Random random = new Random();
		currentHealth = random.nextInt(100);
		this.awake = false;
		lookAt = new Vector2(0, 0);
	}

	/**
	 * update: Handles low level functionality of Actors. Can be overridden if
	 * you're making a special Actor Ex. An Actor that teleports would need to
	 * override this function to replace the movement parts with specialized
	 * code.
	 * 
	 * @param deltaTime:
	 *            Time between each frame.
	 */
	public void update(float deltaTime) {
		updateStats(deltaTime);
		act(deltaTime);
		updateMovement(deltaTime);
		updateAnimations(deltaTime);
		updateActions(deltaTime);
		if (currentHealth <= 0)
			kill();
	}

	/**
	 * Called when the wake state changes from awake to asleep.
	 */
	public void onSleep() {
		this.awake = false;
	}

	/**
	 * Called when the wake state changes from sleep to awake. Example use: A
	 * mob may want to wake nearby mobs even if the player hasn't reached their
	 * wake bubble.
	 */
	public void onWake() {
		this.awake = true;
	}

	/**
	 * Updates the Actors stats to account for changes.
	 * 
	 * @param deltaTime
	 */
	protected void updateStats(float deltaTime) {
		finalizeModifiers();
		for (TempStatModifier stat : tempStatModifiers) {
			stat.update(deltaTime);
		}
		setHealth(deltaTime * getStat(Stats.HEALTH_REGEN));
		setMana(deltaTime * getStat(Stats.MANA_REGEN));
	}

	/**
	 * Updates the Actors position.
	 * 
	 * @param deltaTime
	 */
	protected void updateMovement(float deltaTime) {
		x += (currentSpeed.x);
		y += (currentSpeed.y);

		currentSpeed.x *= currentStats[Stats.MOVEMENT_DRAG.ordinal()] * deltaTime;
		currentSpeed.y *= currentStats[Stats.MOVEMENT_DRAG.ordinal()] * deltaTime;
		bounds.setPosition(x, y);
		bounds.setScale(getStat(Stats.SIZE), getStat(Stats.SIZE));
	}

	/**
	 * Updates the Actors animations.
	 * 
	 * @param deltaTime
	 */
	protected void updateAnimations(float deltaTime) {
		float angle = Direction.angleDeg(this.getPositionV2(), lookAt);

		if (!invisible) {
			for (Animation a : animations) {
				a.setScale(getStat(Stats.SIZE), getStat(Stats.SIZE));
				a.setRotation(angle);
				a.setIdle(idle);
				a.update(deltaTime);
				a.setXY((int) x, (int) y);
			}
		}
		bounds.setRotation((float) Math.toDegrees(angle));
		bounds.setRotation(angle);
		idle = true;
	}

	/**
	 * Updates the Actors actions (like spells).
	 * 
	 * @param deltaTime
	 */
	protected void updateActions(float deltaTime) {
		for (int i = 0; i < activeActions.size(); i++) {
			if (activeActions.get(i).isActive()) {
				activeActions.get(i).update(deltaTime);
			} else {
				activeActions.remove(i);
			}
		}
	}

	/**
	 * How and Actor needs to behave. Overhead (i.e. movement, animation, etc
	 * updates) are already handled by the Actor.java.
	 * 
	 * @param deltaTime:
	 *            Time between each frame.
	 */
	public abstract void act(float deltaTime);

	private static final int borderWidth = 1;
	private static final int barHeight = 2;

	@Override
	public void render(SpriteBatch batch) {
		for (Animation a : animations) {
			a.render(batch);
		}

		// Renders a healthbar
		batch.setColor(1.0f, 1.0f, 1.0f, 0.5f);
		batch.draw(debugHealthBarTexture, x - (int) (healthbarWidth * 0.5f) - borderWidth,
				y + healthbarHeight - borderWidth, healthbarWidth + borderWidth * 2, barHeight * 2 + borderWidth * 2);

		batch.setColor(1.0f, 0f, 0f, 0.5f);
		batch.draw(debugHealthBarTexture, x - (int) (healthbarWidth * 0.5f), y + healthbarHeight,
				healthbarWidth * (currentHealth / (currentStats[Stats.HEALTH.ordinal()] + 0.0f)), barHeight * 2);
		batch.setColor(Color.WHITE);

		// Renders a manabar
		batch.setColor(1.0f, 1.0f, 1.0f, 0.5f);
		batch.draw(debugHealthBarTexture, x - (int) (healthbarWidth * 0.5f) - borderWidth,
				y + healthbarHeight - borderWidth + barHeight * 2 + 2, healthbarWidth + borderWidth * 2,
				barHeight * 2 + borderWidth * 2);

		batch.setColor(0.0f, 0f, 1f, 0.5f);
		batch.draw(debugHealthBarTexture, x - (int) (healthbarWidth * 0.5f), y + healthbarHeight + barHeight * 2 + 2,
				healthbarWidth * (currentMana / (currentStats[Stats.MANA.ordinal()] + 0.0f)), barHeight * 2);
		batch.setColor(Color.WHITE);
	}

	/**
	 * Increases this actors level IF they've gained enough xp.
	 */
	private void levelUp() {
		while (characterXP > xpToLevel) {
			System.err.println("Level increases!");
			System.err.println("+3 Attribute Points");
			characterLevel++;
			characterXP -= xpToLevel;
			attributePoints += 3;
		}
	}

	/**
	 * Adds xp to this player.
	 * 
	 * @param xp
	 */
	public void addXP(int xp) {
		System.out.println("Giving " + xp + " xp to " + this.getClass().getSimpleName());
		characterXP += xp;
		System.out.println("XP: " + characterXP + " of " + xpToLevel);
		if (characterXP >= xpToLevel)
			levelUp();
	}

	@Override
	public float defend(GameObject attacker, Stats type, float damage) {
		if (invulnerable)
			damage = 0;
		
		
		if (attacker == Game.player) {
			Archiver.set(TotalRecords.DAM_GIVEN, Math.min(damage, currentHealth));
		} else if (this == Game.player) {
			Archiver.set(TotalRecords.DAM_TAKEN, Math.min(damage, currentHealth));
		}

		if (!awake)
			onWake();
		currentHealth -= damage;
		System.err.println("Defending against " + attacker.getClass().getSimpleName() + " (" + damage + " damage)");
		if (currentHealth <= 0) {
			this.kill();
			if (attacker instanceof Actor && attacker.isAlive()) {
				((Actor) attacker).addXP(xpWorth);
				xpWorth = 0;
			}
		}

		return damage;
	}

	/**
	 * 
	 * @return the name of the Actor.
	 */
	// TODO: Add a name generator for Actors
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of this Actor.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return the current health of this Actor.
	 */
	public float getHealth() {
		return currentHealth;
	}

	/**
	 * 
	 * @return an int version of current health.
	 */
	public int getHealthRounded() {
		return (int) currentHealth;
	}

	/**
	 * Adds amount to this Actors health. Caps the max based on the Actors
	 * Health stat.
	 * 
	 * @param currentHealth
	 */
	public void setHealth(float amount) {
		if (invulnerable)
			return;
		this.currentHealth += amount;
		this.currentHealth = Utils.clamp(0, getStat(Stats.HEALTH), this.currentHealth);
	}

	/**
	 * 
	 * @return this Actors current attribute points.
	 */
	public int getAttributePoints() {
		return attributePoints;
	}

	/**
	 * 
	 * @return this actors current mana
	 */
	public float getMana() {
		return currentMana;
	}

	/**
	 * Adds cm to currentMana. Does NOT directly set mana.
	 * 
	 * @param currentMana
	 */
	public void setMana(float cm) {
		currentMana += cm;
		currentMana = Utils.clamp(0, getStat(Stats.MANA), currentMana);
	}

	/**
	 * 
	 * @return this actors stats
	 */
	public float[] getStats() {
		return currentStats;
	}

	/**
	 * 
	 * @param stat - the stat to retrieve
	 * @return this actors stat corresponding to stat.
	 */
	public float getStat(Stats stat) {
		return currentStats[stat.ordinal()];
	}
	
	/**
	 * Sets a base stat.
	 * @param stat
	 * @param value
	 */
	public void setBaseStat(Stats stat, float value) {
		baseStats[stat.ordinal()] = value;
		calculateStats();
	}

	/**
	 * Sets a modifier stat.
	 * @param stat
	 * @param value
	 */
	public void setModifierStat(Stats stat, float value) {
		modifierStats[stat.ordinal()] = value;
		calculateStats();
	}

	/**
	 * Sets idle to idle
	 * @param idle
	 */
	public void setIdle(boolean idle) {
		this.idle = idle;
	}

	/**
	 * Used for pushing this Actor
	 * @param x
	 * @param y
	 */
	public void push(float x, float y) {
		currentSpeed.x += x;
		currentSpeed.y += y;
	}

	/**
	 * Moves this actor.
	 * @param deltaTime
	 * @param direction
	 * @param updateDirection
	 */
	public void move(float deltaTime, Direction direction, boolean updateDirection) {
		currentSpeed.x += (currentStats[Stats.MOVEMENT_ACCELERATION.ordinal()] * deltaTime) * direction.x;
		currentSpeed.x = Utils.clamp(-currentStats[Stats.MOVEMENT_SPEED.ordinal()],
				currentStats[Stats.MOVEMENT_SPEED.ordinal()], currentSpeed.x);

		currentSpeed.y += (currentStats[Stats.MOVEMENT_ACCELERATION.ordinal()] * deltaTime) * direction.y;
		currentSpeed.y = Utils.clamp(-currentStats[Stats.MOVEMENT_SPEED.ordinal()],
				currentStats[Stats.MOVEMENT_SPEED.ordinal()], currentSpeed.y);
		setIdle(false);
	}

	/**
	 * Moves this actor.
	 * @param deltaTime
	 * @param angle
	 * @param updateDirection
	 */
	public void move(float deltaTime, double angle, boolean updateDirection) {
		currentSpeed.x += (currentStats[Stats.MOVEMENT_ACCELERATION.ordinal()] * deltaTime) * Math.cos(angle);
		currentSpeed.x = Utils.clamp(-currentStats[Stats.MOVEMENT_SPEED.ordinal()],
				currentStats[Stats.MOVEMENT_SPEED.ordinal()], currentSpeed.x);

		currentSpeed.y += (currentStats[Stats.MOVEMENT_ACCELERATION.ordinal()] * deltaTime) * Math.sin(angle);
		currentSpeed.y = Utils.clamp(-currentStats[Stats.MOVEMENT_SPEED.ordinal()],
				currentStats[Stats.MOVEMENT_SPEED.ordinal()], currentSpeed.y);
		setIdle(false);
	}

	/**
	 * Sets defaults stats.
	 */
	public void setDefaults() {
		for (int i = 0; i < Stats.values().length; i++) {
			baseStats[i] = 1;
			modifierStats[i] = 0;
		}

		baseStats[Stats.HEALTH.ordinal()] = 100;
		baseStats[Stats.MANA.ordinal()] = 100;
		baseStats[Stats.MOVEMENT_DRAG.ordinal()] = 0.3f;
		baseStats[Stats.MOVEMENT_SPEED.ordinal()] = 50f;
		baseStats[Stats.MOVEMENT_ACCELERATION.ordinal()] = 200f;
		baseStats[Stats.SIZE.ordinal()] = 1f;
	}

	/**
	 * Recalculates this Actors stats.
	 */
	public void calculateStats() {
		for (int i = 0; i < Stats.values().length; i++) {
			if (Stats.values()[i].upgradeable)
				if (Stats.values()[i] != Stats.SIZE)
					currentStats[i] = baseStats[i] + modifierStats[i] + currentStats[Stats.SIZE.ordinal()];
				else
					currentStats[i] = baseStats[i] + modifierStats[i];
			else {
				currentStats[i] = baseStats[i] + modifierStats[i];
				for (Stats s : Stats.values()[i].grouping) {
					currentStats[i] += getStat(Stats.values()[s.ordinal()]);
				}
			}
			currentStats[i] *= Stats.values()[i].multiplier;
		}
	}

	public LinkedList<TempStatModifier> modifierChanges = new LinkedList<TempStatModifier>();

	/**
	 * Removes a temporary stat.
	 * @param stat
	 */
	public void removeTempStat(TempStatModifier stat) {
		stat.remove = true;
		modifierChanges.add(stat);
	}

	/**
	 * Adds a temporary stat changer.
	 * @param stat
	 */
	public void addTempStat(TempStatModifier stat) {
		stat.remove = false;
		modifierChanges.add(stat);
	}

	/**
	 * Updates the modifier lists.
	 */
	public void finalizeModifiers() {
		for (TempStatModifier stat : modifierChanges) {
			if (stat.remove)
				tempStatModifiers.remove(stat);
			else
				tempStatModifiers.add(stat);
		}
		modifierChanges.removeAll(modifierChanges);
	}

	/**
	 * 
	 * @return the amount of xp this actor currently has.
	 */
	public int getXP() {
		return characterXP;
	}

	/**
	 * 
	 * @return this actors current level
	 */
	public int getLevel() {
		return characterLevel;
	}

	/**
	 * 
	 * @return the total amount xp to reach the next level.
	 */
	public int getXPNeeded() {
		return xpToLevel;
	}

	/**
	 * Use action toward x, y
	 * 
	 * @param x
	 * @param y
	 */
	public void primaryAction(int x, int y) {
		if (primaryAction != null) {
			primaryAction.act(x, y);
		}
	}

	/**
	 * Use action toward x, y
	 * 
	 * @param x
	 * @param y
	 */
	public void secondaryAction(int x, int y) {
		if (secondaryAction != null) {
			secondaryAction.act(x, y);
		}
	}

	/**
	 * 
	 * @param Hotkey/Toolbar
	 *            Actions
	 * @param x
	 *            - Mouse cursor X world coordinate.
	 * @param y
	 *            - Mouse cursor Y world coordinate.
	 */
	public void doAction(int action, int x, int y) {
		if (actions.get(action) != null) {
			actions.get(action).act(x, y);
		}
	}

	/**
	 * Adds an active ability.
	 * @param action
	 */
	public void addActiveAction(Action action) {
		activeActions.add(action);
	}

	/**
	 * Adds an action.
	 * @param action
	 */
	public void addAction(Action action) {
		actions.add(action);
	}

	/**
	 * Sets the chracter level.
	 * 
	 * @param level
	 */
	public void setLevel(int level) {
		if (level < 0)
			level = 0;
		else
			characterLevel = level;
	}

	@Override
	protected void die() {
		if (this != Game.player) {
			Archiver.set(TotalRecords.MONSTERS_SLAIN, 1);
		}

		Map.getTile((int) x, (int) y).addObject(
				new Decal(x, y, "ass", AssetManager.getTexture(DecalPicker.getActorCorpse()).getTextureRegion()));

		if (Utils.getRandomInt(100) > 50) {
			GameObject hOrb = ObjectMaker.createHealthOrb(x + Utils.getRandomInt(50) - 25,
					y + Utils.getRandomInt(50) - 25);
			Map.getTile((int) hOrb.getX(), (int) hOrb.getY()).addObject(hOrb);
		}

		if (Utils.getRandomInt(100) > 50) {
			GameObject mOrb = ObjectMaker.createManaOrb(x + Utils.getRandomInt(50) - 25,
					y + Utils.getRandomInt(50) - 25);
			Map.getTile((int) mOrb.getX(), (int) mOrb.getY()).addObject(mOrb);
		}
	}

	/**
	 * Set primary action.
	 * 
	 * @param a
	 */
	public void setPrimaryAction(Action a) {
		primaryAction = a;
	}

	/**
	 * Set secondary action
	 * 
	 * @param a
	 */
	public void setSecondaryAction(Action a) {
		secondaryAction = a;
	}

	/**
	 * 
	 * @return the number of remaining attribute points.
	 */
	public int getRemainingAttributePoints() {
		return attributePoints;
	}

	/**
	 * Adds attribute points to this actor.
	 * 
	 * @param points
	 */
	public void addAttributePoints(int points) {
		attributePoints += points;
	}

	/**
	 * Disposes this actor
	 */
	@Override
	public void dispose() {

	}
}
