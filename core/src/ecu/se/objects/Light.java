package ecu.se.objects;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import ecu.se.GameObject;
import ecu.se.Lighting;

public class Light implements Comparable<Light> {

	protected static float a = 1 / 26.0f;
	protected static float b = 2 / 26.0f;
	protected static float c = 3 / 26.0f;
	protected static float d = 4 / 26.0f;
	protected static float e = 5 / 26.0f;
	protected static float f = 6 / 26.0f;
	protected static float g = 7 / 26.0f;
	protected static float h = 8 / 26.0f;
	protected static float i = 9 / 26.0f;
	protected static float j = 10 / 26.0f;
	protected static float k = 11 / 26.0f;
	protected static float l = 12 / 26.0f;
	protected static float m = 13 / 26.0f;
	protected static float n = 14 / 26.0f;
	protected static float o = 15 / 26.0f;
	protected static float p = 16 / 26.0f;
	protected static float q = 17 / 26.0f;
	protected static float r = 18 / 26.0f;
	protected static float s = 19 / 26.0f;
	protected static float t = 20 / 26.0f;
	protected static float u = 21 / 26.0f;
	protected static float v = 22 / 26.0f;
	protected static float w = 23 / 26.0f;
	protected static float x = 24 / 26.0f;
	protected static float y = 25 / 26.0f;
	protected static float z = 1;

	public boolean on;
	public boolean delete = false;
	public float intensity;
	public int type;

	protected boolean hasParent;
	protected GameObject parent;
	protected float[] flickerTest = { m, m, n, m, m, o, m, m, o, m, m, n, o, n, m, m, o, n, q, n, m, m, o };
	protected Color color;
	protected float offsetX, offsetY;
	

	protected Vector3 position;
	protected float baseIntensity;
	protected float time;
	protected float distance;
	protected static Random rand = new Random();
	protected int intensityPosition = rand.nextInt(flickerTest.length);
	
	
	public Light(Vector3 position) {
		this.parent = null;
		this.hasParent = false;
		this.position = position;
		this.type = 1;
		on = true;
	}

	public Light(GameObject parent) {
		this.parent = parent;
		this.hasParent = true;
		this.position = parent.getPosition();
		this.type = 1;
		on = true;
	}
	
	public Light (GameObject parent, Color color, float intensity) {
		this.color = color;
		this.intensity = intensity;
		this.parent = parent;
		this.hasParent = true;
		this.position = parent.getPosition();
		this.type = 1;
		on = true;
	}
	
	public Light (GameObject parent, Color color, float intensity, int type) {
		this.color = color;
		this.intensity = intensity;
		this.parent = parent;
		this.hasParent = true;
		this.position = parent.getPosition();
		this.type = type;
		on = true;
	}

	public void update(float deltaTime, Vector2 targetVector) {
		time += deltaTime;
		if (time > 0.1f) {
			time = 0;
			intensityPosition++;
			intensityPosition %= flickerTest.length;
		}
		intensity = baseIntensity * (flickerTest[intensityPosition] * 0.5f);

		if (hasParent) {
			if (parent == null || !parent.isAlive()) {
				Lighting.removeLight(this);
			}
			this.position = parent.getPosition();
		}
		this.position.x += offsetX;
		this.position.y += offsetY;
		distance = Vector2.dst2(position.x, position.y, targetVector.x, targetVector.y);
	}

	public void setParent(GameObject parent) {
		this.parent = parent;
		hasParent = true;
	}
	
	public void setOffset(float x, float y) {
		offsetX = x;
		offsetY = y;
	}
	
	public void setOffset(Vector3 offset) {
		offsetX = offset.x;
		offsetY = offset.y;
	}

	public void setColor(Color c) {
		this.color = c;
	}

	public void setIntensity(float intensity) {
		this.baseIntensity = intensity;
	}

	public Color getColor() {
		return color;
	}

	public float getIntensity() {
		return intensity;
	}

	public Vector3 getPos() {
		return position;
	}
	
	public void setX(float x) {
		position.x = x;
	}
	
	public void setY(float y) {
		position.y = y;
	}

	public float getDistance() {
		return distance;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}


	@Override
	public int compareTo(Light arg0) {
		if (distance > arg0.getDistance())
			return 1;
		else if (distance == arg0.getDistance())
			return 0;
		return -1;
	}
}
