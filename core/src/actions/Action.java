package actions;

import actors.Actor;

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
	
	public abstract void update(float deltaTime);

	public boolean isActive() {
		return active;
	}
	
	public void setCaster(Actor caster) {
		this.caster = caster;
	}
	
	public boolean getDelete() {
		return delete;
	}
}
