package ecu.se.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import ecu.se.GameObject;
import ecu.se.Lighting;

public class FadingLight extends Light{

	private float decay;

	public FadingLight(Vector3 position, Color color, float intensity, float decay) {
		super(position);
		this.color = color;
		this.baseIntensity = intensity;
		this.decay = decay;
	}

	public FadingLight(GameObject parent, Color color, float intensity, float decay) {
		super(parent, color, intensity);
		this.decay = decay;
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
		if (intensity < 0.00001f) {
			Lighting.removeLight(this);
		}
		
		if (hasParent) {
			if (parent != null) {
				this.position = parent.getPosition();
			}
		}
		
		distance = Vector2.dst2(position.x, position.y, targetVector.x, targetVector.y);
	}

}
