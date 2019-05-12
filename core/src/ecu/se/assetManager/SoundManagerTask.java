package ecu.se.assetManager;

import com.badlogic.gdx.audio.Sound;

public class SoundManagerTask {

	private Sound sound;
	private long id;
	private float decayRate;
	private float volume;
	private boolean taskComplete = false;
	private SoundTask endingTask;
	
	public SoundManagerTask (Sound sound, long id, float decayRate, float startVolume, SoundTask endingTask){
		this.sound = sound;
		this.id = id;
		this.decayRate = decayRate;
		this.volume = startVolume;
		this.endingTask = endingTask;
	}
	
	public void update(float deltaTime) {
		volume -= decayRate * deltaTime;
		sound.setVolume(id, volume);
		
		if (volume <= 0.0f || volume > 1.0f) {
			if (endingTask == SoundTask.STOP)
				sound.stop(id);
			else if (endingTask == SoundTask.PAUSE)
				sound.pause(id);
			taskComplete = true;
		}
	}
	
	public void stop() {
		sound.stop();
	}
	
	public boolean isTaskComplete() {
		return taskComplete;
	}
}
