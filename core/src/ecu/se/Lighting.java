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
    
	public static void init(OrthographicCamera cam) {
		camera = cam;
		rand = new Random();
		ShaderProgram.pedantic = false;
		
	    shader = new ShaderProgram(
	            Gdx.files.internal("shader/light1.vert").readString(),
//	    		DefaultShader.getDefaultVertexShader(),
	            Gdx.files.internal("shader/light1.frag").readString());
	    if(!shader.isCompiled()) {
	        Gdx.app.log("[Lighting] Problem loading shader:", shader.getLog());
	    }
	    
//	    lightSize = shader.getUniformSize("LightSource");
//	    lightsPosition = shader.getUniformLocation("lights");
//	    System.out.println("\n\nlightSize=" + lightSize);
//	    System.out.println("lightsPosition=" + lightsPosition);
//		
//	    System.out.println("ALL attributes");
//	    System.out.println("\tAttr\t\tType\tSize\tLoc");
//	    for(String s : shader.getAttributes()) {	    	
//	    	System.out.println("\t" + s + "\t" + shader.getAttributeType(s) + "\t" + shader.getAttributeSize(s) + "\t" + shader.getAttributeLocation((s)));
//	    }
//	    
//	    System.out.println("ALL uniforms");
//	    System.out.println("\tUnif\t\tType\tSize\tLoc");
//	    for(String s : shader.getUniforms()) {	
//	    	System.out.println("\t" + s + "\t" + shader.getUniformType(s) + "\t" + shader.getUniformSize(s) + "\t" + shader.getUniformLocation((s)));
//	    	//System.out.println("\t" + s);
//	    }
//	    
//	    System.out.println("\nVariable Size");	    
//	    int v_color = shader.getAttributeSize("v_color");
//	    int v_texCoords = shader.getAttributeSize("v_texCoords");
//	    int worldPosition = shader.getAttributeSize("worldPosition");
//	    int u_texture = shader.getAttributeSize("u_texture");
//	    System.out.println("v_color=" + v_color);
//	    System.out.println("v_texCoords=" + v_texCoords);
//	    System.out.println("worldPosition=" + worldPosition);
//	    System.out.println("u_texture=" + u_texture);
//
//	    System.out.println("\n\nVariable Positions");
//	    v_color = shader.getUniformLocation("v_color");
//	    v_texCoords = shader.getUniformLocation("v_texCoords");	    
//	    worldPosition = shader.getUniformLocation("worldPosition");
//	    u_texture = shader.getUniformLocation("u_texture");
//	    System.out.println("v_color=" + v_color);
//	    System.out.println("v_texCoords=" + v_texCoords);
//	    System.out.println("worldPosition=" + worldPosition);
//	    System.out.println("u_texture=" + u_texture);
	    
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
	
	/**
	 * Disposes the assets used by Lighting.
	 * Including:
	 * 	- ShaderProgram
	 */
	public static void dispose() {
		shader.dispose();
	}	
	
	public static void printLog() {
		System.out.println(shader.getLog());
	}
}
