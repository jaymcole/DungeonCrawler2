package ecu.se.objects;

import actions.Action;

public class ActiveItem extends InteractableItem{

	protected Action action;
	
	public ActiveItem(float x, float y, String name, String path) {
		super(x, y, name, path);
	}
	
	public void setAction(Action a) {
		this.action = a;
	}
	
	public Action getAction() {
		return action;
	}
	
	
}
