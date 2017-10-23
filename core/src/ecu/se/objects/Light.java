package ecu.se.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import ecu.se.GameObject;
import ecu.se.Lighting;

public class Light {

	private static float a = 1 / 26.0f;
	private static float b = 2 / 26.0f;
	private static float c = 3 / 26.0f;
	private static float d = 4 / 26.0f;
	private static float e = 5 / 26.0f;
	private static float f = 6 / 26.0f;
	private static float g = 7 / 26.0f;
	private static float h = 8 / 26.0f;
	private static float i = 9 / 26.0f;
	private static float j = 10 / 26.0f;
	private static float k = 11 / 26.0f;
	private static float l = 12 / 26.0f;
	private static float m = 13 / 26.0f;
	private static float n = 14 / 26.0f;
	private static float o = 15 / 26.0f;
	private static float p = 16 / 26.0f;
	private static float q = 17 / 26.0f;
	private static float r = 18 / 26.0f;
	private static float s = 19 / 26.0f;
	private static float t = 20 / 26.0f;
	private static float u = 21 / 26.0f;
	private static float v = 22 / 26.0f;
	private static float w = 23 / 26.0f;
	private static float x = 24 / 26.0f;
	private static float y = 25 / 26.0f;
	private static float z = 1;

	public boolean on;
	private boolean hasParent;
	private GameObject parent;

	private float[] flickerTest = { m, m, n, m, m, o, m, m, o, m, m, n, o, n, m, m, o, n, q, n, m, m, o };
	private Color color;
	private Vector3 offset;
	private Vector3 position;
	private float intensity;

	public Light(Vector3 position) {
		this.parent = null;
		this.hasParent = false;
		this.position = position;
		on = true;
	}

	public Light(GameObject parent) {
		this.parent = parent;
		this.hasParent = true;
		this.position = parent.getPosition();
		on = true;
	}

	public void update(float deltaTime) {
		if (hasParent) {
			if (parent == null) {
				Lighting.removeLight(this);
			}
			this.position = parent.getPosition();
		}
	}

	public void setOffset(Vector3 offset) {
		this.offset = offset;
	}

	public void setColor(Color c) {
		this.color = c;
	}

	public void setIntensity(float intensity) {
		this.intensity = intensity;
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
}
