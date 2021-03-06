package ecu.se.lights;

import java.util.LinkedList;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import ecu.se.GameObject;
import ecu.se.Globals;
import ecu.se.Lighting;
import ecu.se.Logger;

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
	protected float baseIntensity;
	protected float distance = 0.2f;
	public int type;

	protected boolean hasParent;
	protected GameObject parent;
	protected float[] flickerTest = { m, m, n, m, m, o, m, m, o, m, m, n, o, n, m, m, o, n, q, n, m, m, o };
	protected float[] flickerStyle;
	protected Color color;
	protected float offsetX, offsetY;

	private Vector3 position;
	protected float time;
	protected Random rand = new Random();
	protected int intensityPosition;
	
	public LinkedList<Vector3> positions = new LinkedList<Vector3>();
	
	
	
	public Light(Vector3 position) {
		this.parent = null;
		this.hasParent = false;
		this.setPosition(position);
		this.color = Color.WHITE;
		this.intensity = Globals.DEFAULT_LIGHT_INTENSITY;
		this.type = 1;
		on = true;
		init();
	}

	public Light(GameObject parent) {
		this.parent = parent;
		this.hasParent = true;
		this.setPosition(parent.getPosition());
		this.type = 1;
		on = true;
		init();
	}
	
	public Light (GameObject parent, Color color, float intensity) {
		this.color = color;
		this.intensity = intensity;
		this.parent = parent;
		this.hasParent = true;
		this.setPosition(parent.getPosition());
		this.type = 1;
		on = true;
		init();
	}
	
	public Light (GameObject parent, Color color, float intensity, int type) {
		this.color = color;
		this.intensity = intensity;
		this.parent = parent;
		this.hasParent = true;
		this.setPosition(parent.getPosition());
		this.type = type;
		on = true;
		init();
	}
	
	private void init() {
		flickerStyle = flickerTest;
		intensityPosition = rand.nextInt(flickerStyle.length);
	}

	public void update(float deltaTime, Vector2 targetVector) {
		time += deltaTime;
		if (time > 0.1f) {
			time = 0;
			intensityPosition++;
			intensityPosition %= flickerStyle.length;
		}
//		intensity = baseIntensity * (flickerStyle[intensityPosition] * 0.5f);
		intensity = flickerStyle[intensityPosition];
//		color = new Color(color.r, color.g, color.b, intensity);
		if (hasParent) {
			if (parent == null)
				kill();
			else 
				this.setPosition(new Vector3(parent.getPosition().x, parent.getPosition().y, parent.getPosition().z));
		}
		this.getPosition().x += offsetX;
		this.getPosition().y += offsetY;
		distance = Vector2.dst2(getPosition().x, getPosition().y, targetVector.x, targetVector.y);
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
		return getPosition();
	}
	
	public void setX(float x) {
		getPosition().x = x;
	}
	
	public void setY(float y) {
		getPosition().y = y;
	}

	public void setDistance(float distance) {
		this.distance = distance;
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
	
	public void kill() {
		Logger.Debug(Lighting.class, "kill", "Killing light");

		Lighting.removeLight(this);
		if (hasParent)
			parent.setLight(null);
	}


	@Override
	public int compareTo(Light arg0) {
		if (distance > arg0.getDistance())
			return 1;
		else if (distance == arg0.getDistance())
			return 0;
		return -1;
	}

	public Vector3 getPosition() {
		return position;
	}

	public void setPosition(Vector3 position) {
		this.position = position;
	}
}
