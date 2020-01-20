package ecu.se.assetManager;

import java.util.LinkedList;

public class SoundManager {

	
	
	private static LinkedList<SoundManagerTask> tasks = new LinkedList<SoundManagerTask>();
	
	public static void update(float deltaTime) {
		SoundManagerTask task;
		for(int i = 0 ; i < tasks.size(); i++) {
			task = tasks.get(i);
			task.update(deltaTime);
			if (task.isTaskComplete())
				tasks.remove(task);
		}
	}
	
	public static void addTask(SoundManagerTask task) {
		tasks.add(task);
	}	
	
	public static void stopAll() {
		SoundManagerTask task;
		for(int i = 0 ; i < tasks.size(); i++) {
			task = tasks.get(i);
			task.stop();
			tasks.remove(task);
		}
	}
	
	
}
