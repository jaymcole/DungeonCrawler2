package ecu.se.lights;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import ecu.se.GameObject;
import ecu.se.Lighting;
import ecu.se.Logger;

public class LightFading extends Light {

	public float decay;

	public LightFading(Vector3 position, Color color, float intensity, float decay, int type) {
		super(position);
		this.color = color;
		this.baseIntensity = intensity;
		this.decay = decay;
		this.type = type;
	}

	public LightFading(GameObject parent, Color color, float intensity, float decay, int type) {
		super(parent, color, intensity);
		this.decay = decay;
		this.type = type;
	}

	public void update(float deltaTime, Vector2 targetVector) {
		time += deltaTime;
		if (time > 0.1f) {
			time = 0;
			intensityPosition++;
			intensityPosition %= flickerTest.length;
		}

		baseIntensity *= decay;
		intensity = baseIntensity * (flickerTest[intensityPosition] * 0.5f);
//		intensity = flickerTest[intensityPosition] * decay;

		
		if (intensity < 0.00001f) {
			kill();
		}
		
		if (hasParent) {
			if (parent != null) {
				this.setPosition(parent.getPosition());
			}
		}
		
		distance = Vector2.dst2(getPosition().x, getPosition().y, targetVector.x, targetVector.y);
	}

}
