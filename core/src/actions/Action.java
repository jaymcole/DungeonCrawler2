package actions;

import actors.Actor;

public abstract class Action {

	protected boolean active = false;
	protected Actor caster;

	public Action(Actor caster) {
		this.caster = caster;
	}
	
	/**
	 * Performs and action.
	 * @param x: Mouse cursor X world coordinate.
	 * @param y: Mouse cursor Y world coordinate.
	 */
	public abstract void act(int x, int y);
	
	public abstract void update(float deltaTime);

	public boolean isActive() {
		return active;
	}
}
