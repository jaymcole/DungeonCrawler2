package ecu.se.lights;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import ecu.se.Lighting;
import ecu.se.Logger;

public class LightTimed extends Light{
	public float lifespanInSeconds;
	private float timeAlive;
	public Light lightOnDeath;
	
	public LightTimed(Vector3 position, Color color, float intensity, float lifespanInSeconds, int type, Light lightOnDeath) {
		super(position);
		this.color = color;
		this.baseIntensity = intensity;
		this.type = type;
		this.lifespanInSeconds = lifespanInSeconds; 
		this.lightOnDeath = lightOnDeath;
		timeAlive = 0;
	}


	public void update(float deltaTime, Vector2 targetVector) {
		time += deltaTime;		
		timeAlive += deltaTime;
		if (time > 0.1f) {
			time = 0;
			intensityPosition++;
			intensityPosition %= flickerTest.length;
		}
		intensity = baseIntensity * (flickerTest[intensityPosition] * 0.5f);

		if (timeAlive >= lifespanInSeconds) {
			if (lightOnDeath != null) {
				lightOnDeath.setPosition(this.getPosition());
				Lighting.addLight(lightOnDeath);
			}
			this.kill();
		}
		this.getPosition().x += offsetX;
		this.getPosition().y += offsetY;
		distance = Vector2.dst2(getPosition().x, getPosition().y, targetVector.x, targetVector.y);
	}
}
