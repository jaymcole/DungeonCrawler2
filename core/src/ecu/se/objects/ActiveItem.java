package ecu.se.objects;

import ecu.se.Game;
import ecu.se.GameObject;
import ecu.se.actions.Action;
import ecu.se.actors.Actor;

public class ActiveItem extends InteractableItem{

	protected Action action;
	
	public ActiveItem(float x, float y, String name, String path) {
		super(x, y, name, path);
	}
	
	public void setAction(Action a) {
		this.action = a;
	}
	
	public void onClick(GameObject otherObject) {
		if (otherObject instanceof Actor)
			action.setCaster((Actor)otherObject);
		
		if (otherObject == Game.player) {
//			((Window_Inventory)GUI.getWindow(GUI.WINDOW_INVENTORY)).insertItem(this);
		}
	}
	
	@Override
	public void update(float deltaTime) {
		if (action.getDelete())
			this.kill();
	}
	
	public Action getAction() {
		return action;
	}
	
	
}
