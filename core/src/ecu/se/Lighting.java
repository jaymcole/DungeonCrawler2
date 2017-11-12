package ecu.se;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import ecu.se.objects.Light;

public class Lighting {

	private static ShaderProgram shader;
	private static OrthographicCamera camera;

	private static LinkedList<Light> lights = new LinkedList<Light>();
	private static LinkedList<Light> waitlist = new LinkedList<Light>();
	private static Iterator<Light> updater;
	private static Light light;
	private static int counter = 0;
	private static Color color;
	private static Vector3 position;
	private static boolean lightsOn;
	// private static GameObject renderTarget;

	public static void init(OrthographicCamera cam, GameObject rt) {
		camera = cam;
		ShaderProgram.pedantic = false;
		// renderTarget = rt;
		shader = new ShaderProgram(Gdx.files.internal("shader/light1.vert").readString(),
				Gdx.files.internal("shader/light1.frag").readString());
		if (!shader.isCompiled()) {
			Gdx.app.log("[Lighting] Problem loading shader:", shader.getLog());
		}

	}

	/**
	 * Updates the lights and shader.
	 * 
	 * @param deltaTime
	 *            - Time since last render call.
	 */
	public static void updateLights(float deltaTime, Vector2 targetVector) {
		updater = lights.iterator();

		while (updater.hasNext()) {
			light = updater.next();
			light.update(deltaTime, targetVector);
		}
		Collections.sort(lights);

		updater = lights.iterator();
		counter = 0;
		shader.begin();
		if (lightsOn)
			shader.setUniformf("ambientLight", 1f, 1f, 1f, 1f);
		else
			shader.setUniformf("ambientLight", 0f, 0f, 0f, 0f);

		while (updater.hasNext() && counter < Globals.MAX_LIGHTS) {
			light = updater.next();
			if (light != null && light.on) {
				color = light.getColor();
				position = light.getPos();
				shader.setUniformf("lights[" + counter + "].color", color.r, color.g, color.b, color.a);
				shader.setUniformf("lights[" + counter + "].position", position.x, position.y);
				shader.setUniformf("lights[" + counter + "].intensity", light.getIntensity());
				counter++;
			}
		}

		shader.setUniformMatrix("view_matrix", camera.view);
		shader.setUniformi("totalLights", counter);
		shader.setUniformf("worldPos", camera.position.x, camera.position.y);
		shader.setUniformMatrix("u_projViewTrans", camera.view);
		shader.setUniformMatrix("inverseProjectionMatrix", camera.invProjectionView);
		shader.end();

		updateLists();
	}

	private static boolean wipeList = false;
	private static boolean wipeWaitlist = false;
	private static void updateLists() {
		if (wipeList) {
			lights.removeAll(lights);
			wipeList = false;
		}

		if (wipeWaitlist) {
			waitlist.removeAll(waitlist);
			wipeWaitlist = false;
		}
		
		for (Light l : waitlist) {
			if (l.delete) 
				lights.remove(l);
			else 
				lights.add(l);
		}

		waitlist.removeAll(waitlist);
	}

	public static void setLights(LinkedList<Light> newLights) {
		waitlist = newLights;
		wipeList = true;
	}

	public static void addLight(Light light) {
		waitlist.add(light);
	}

	/**
	 * Sets batch shader to the shader used for lighting.
	 * 
	 * @param batch
	 */
	public static void setShader(SpriteBatch batch) {
		batch.setShader(shader);
	}

	// TODO: Add a waitlist for lights (like what ObjectManager has)
	/**
	 * Deletes light.
	 * 
	 * @param light
	 */
	public static void removeLight(Light light) {
		if (light != null) {
			light.delete = true;
			waitlist.add(light);
		}
	}

	public static void toggleLights() {
		lightsOn ^= true;
		;
	}

	/**
	 * Disposes the assets used by Lighting. Including: - ShaderProgram
	 */
	public static void dispose() {
		shader.dispose();
	}

}
