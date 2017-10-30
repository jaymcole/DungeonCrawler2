package ecu.se;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;

import ecu.se.objects.Light;

public class Lighting {
	
	private static ShaderProgram shader;
	private static Random rand;
	private static OrthographicCamera camera;
	
	private static ArrayList<Light> lights = new ArrayList<Light>();
    private static Iterator<Light> updater;
    private static Light light;
    private static int counter = 0; 
    private static int lightSize;
    private static int lightsPosition;
    private static Color color;
    private static Vector3 position;
    private static boolean lightsOn;
    
	public static void init(OrthographicCamera cam) {
		camera = cam;
		rand = new Random();
		ShaderProgram.pedantic = false;
		
	    shader = new ShaderProgram(
	            Gdx.files.internal("shader/light1.vert").readString(),
	            Gdx.files.internal("shader/light1.frag").readString());
	    if(!shader.isCompiled()) {
	        Gdx.app.log("[Lighting] Problem loading shader:", shader.getLog());
	    }

	}
	
	/**
	 * Updates the lights and shader.
	 * @param deltaTime - Time since last render call.
	 */
	public static void updateLights (float deltaTime) {
		updater = lights.iterator();
		counter = 0;
//		float oldZoom = camera.zoom;
//		camera.zoom = Globals.DEFAULT_CAMERA_ZOOM;
		shader.begin();
		
		if (lightsOn) 
			shader.setUniformf("ambientLight", 1f, 1f, 1f, 1f);
		else
			shader.setUniformf("ambientLight", 0f, 0f, 0f, 0f);
		
		
		while(updater.hasNext() && counter < Globals.MAX_LIGHTS) 
        {
            light = updater.next();
            if (light != null && light.on) {
            	light.update(deltaTime);
            	color = light.getColor();
            	
            	position = light.getPos();
            	
//            	position = (camera.position);
            	shader.setUniformf("lights["+counter+"].color", color.r, color.g, color.b, color.a);
            	shader.setUniformf("lights["+counter+"].position", position.x, position.y);
            	shader.setUniformf("lights["+counter+"].intensity", light.getIntensity());            	
            	counter++;
            }
        }
		
		
		shader.setUniformMatrix("view_matrix", camera.view);
		shader.setUniformi("totalLights", counter);
		shader.setUniformf("worldPos", camera.position.x, camera.position.y);
		shader.setUniformMatrix("u_projViewTrans", camera.view);
		shader.setUniformMatrix("inverseProjectionMatrix", camera.invProjectionView);
	    shader.end();
	}
	
	/**
	 * Sets batch shader to the shader used for lighting.
	 * @param batch
	 */
	public static void setShader(SpriteBatch batch) {
		batch.setShader(shader);
	}
	
	/**
	 * Deletes light.
	 * @param light
	 */
	public static void removeLight(Light light) {
		lights.remove(light);
	}
	
	public static void addLight(Light light) {
		lights.add(light);
	}
	
	public static void toggleLights() {
		lightsOn ^= true; ;
	}
	
	/**
	 * Disposes the assets used by Lighting.
	 * Including:
	 * 	- ShaderProgram
	 */
	public static void dispose() {
		shader.dispose();
	}	

}
