package ecu.se.actions;

import ecu.se.actors.Actor;

/**
 * 
 * Actions Actors can perform (spells, potions, etc).
 */
public abstract class Action {

	protected boolean active = false;
	protected Actor caster;
	protected boolean delete;

	public Action(Actor caster) {
		this.caster = caster;
		delete = false;
	}
	
	/**
	 * Performs and action.
	 * @param x: Mouse cursor X world coordinate.
	 * @param y: Mouse cursor Y world coordinate.
	 */
	public abstract void act(int x, int y);
	
	/**
	 * Update method for this action
	 * @param deltaTime - time since last frame
	 */
	public abstract void update(float deltaTime);

	/**
	 * 
	 * @return true if the Active is currently performing it's action.
	 */
	public boolean isActive() {
		return active;
	}
	
	/**
	 * The actor using this action.
	 * @param caster
	 */
	public void setCaster(Actor caster) {
		this.caster = caster;
	}
	
	/**
	 * 
	 * @return true if this action needs to be deleted for whatever reason.
	 */
	public boolean getDelete() {
		return delete;
	}
}
