package ecu.se;

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

import ecu.se.lights.Light;

public class Lighting {

	private static ShaderProgram shader;
	private static OrthographicCamera camera;

	//TODO: Switch arraylist to sorted priority queue
	private static LinkedList<Light> lights;
	private static LinkedList<Light> waitlist;
	private static Iterator<Light> updater;
	private static Light light;
	private static int counter = 0;
	private static Color color;
	private static Vector3 position;
	private static boolean lightsOn;
	// private static GameObject renderTarget;
	
	private static float attenuationA = 0.5562134f;
	private static float attenuationB = 2.1183434f;

	public static void init(OrthographicCamera cam, GameObject rt) {
		camera = cam;
		ShaderProgram.pedantic = false;
		// renderTarget = rt;
		lights = new LinkedList<Light>();
		waitlist = new LinkedList<Light>();
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
				shader.setUniformi("lights[" + counter + "].type", light.type);
				shader.setUniformf("lights[" + counter + "].color", color.r, color.g, color.b, color.a);
				shader.setUniformf("lights[" + counter + "].position", position.x, position.y);
//				Logger.Debug(Lighting.class, "updateLights", "Position: " + position.toString());
				shader.setUniformf("lights[" + counter + "].intensity", light.intensity);
				counter++;
			}
		}
		shader.setUniformf("a", attenuationA);
		shader.setUniformf("b", attenuationB);
		shader.setUniformMatrix("view_matrix", camera.view);
		shader.setUniformi("totalLights", counter);
		shader.setUniformf("worldPos", camera.position.x, camera.position.y);
		shader.setUniformMatrix("u_projViewTrans", camera.view);
		shader.setUniformMatrix("inverseProjectionMatrix", camera.invProjectionView);
		shader.end();

		updateLists();
	}

	/**
	 * updates the current light list.
	 * 		Ensures lights are not added/removes while Lighting is updating the lights / packaging light information for the GPU.
	 */
	private static boolean wipeList = false;
	private static boolean wipeWaitlist = false;
	private static void updateLists() {
		if (wipeList) {
			lights.clear();
			wipeList = false;
		}

		if (wipeWaitlist) {
			waitlist.clear();
			wipeWaitlist = false;
		}
		
		for (Light l : waitlist) {
			if (l.delete) 
				lights.remove(l);
			else {
				Logger.Debug(Lighting.class, "setLights", "Adding new light to render list");
				Logger.Debug(Lighting.class, "setLights", "\t" + l.getPos().toString());
				lights.add(l);
			}
		}

		waitlist.clear();
		wipeList = false;
		wipeWaitlist = false;
	}

	public static LinkedList<Light> getLights() {
		return lights;
	}
	
	/**
	 * Sets the light list to newLights and clears the old list.
	 * @param newLights
	 */
	public static void setLights(LinkedList<Light> newLights) {
		Logger.Debug(Lighting.class, "setLights", "Adding " + newLights.size() + " new lights");
		if(waitlist != null)
			waitlist.clear();
		else
			waitlist = new LinkedList<Light>();
		waitlist.addAll(newLights);
		
		wipeList = true;
	}

	/**
	 * Adds a new light to the light list. Does NOT clear the old list.
	 * @param light
	 */
	public static void addLight(Light light) {
		Logger.Debug(Lighting.class, "setLights", "Adding new light to waitlist");
		Logger.Debug(Lighting.class, "setLights", "\t" + light.getPos().toString());
		
		
		light.delete = false;
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

	/**
	 * Toggle for turning on/off global lighting. 
	 */
	public static void toggleLights() {
		lightsOn ^= true;
	}

	/**
	 * Disposes the assets used by Lighting. Including: - ShaderProgram
	 */
	public static void dispose() {
		shader.dispose();
	}
	
	/**
	 * 
	 * @return attentuationA
	 */
	public static float getAttnA() {
		return attenuationA;
	}
	
	/**
	 * 
	 * @return attenuationB
	 */
	public static float getAttnB() {
		return attenuationB;
	}
	
	/**
	 * Sets attenuationA - used for adjusting light brightness
	 * @param val
	 */
	public static void setAttnA(float val) {
		attenuationA = val;
	}
	
	/**
	 * Sets attenuationB - used for adjusting light brightness
	 * @param val
	 */
	public static void setAttnB(float val) {
		attenuationB = val;
	}

}
