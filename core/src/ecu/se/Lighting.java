package ecu.se;

import java.util.Iterator;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;

import ecu.se.lights.Light;
import ecu.se.map.Floor;
import ecu.se.map.Map;
import ecu.se.map.Tile;

public class Lighting {

	/** 
	 * Compiles a new instance of the default shader for this batch and returns it. If compilation
	 * was unsuccessful, GdxRuntimeException will be thrown.
	 * @return the default shader
	 */
	public static ShaderProgram createShader(String vert, String frag) {
		ShaderProgram prog = new ShaderProgram(vert, frag);
		if (!prog.isCompiled())
			throw new GdxRuntimeException("could not compile shader: " + prog.getLog());
		if (prog.getLog().length() != 0)
			Gdx.app.log("GpuShadows", prog.getLog());
		return prog;
	}
	
private int lightSize = 512;
	
	private float upScale = 1f; //for example; try lightSize=128, upScale=1.5f
		
	SpriteBatch batch;
	OrthographicCamera cam; 
	
	BitmapFont font;
	
	TextureRegion shadowMap1D; //1 dimensional shadow map
	TextureRegion occluders;   //occluder map
	
	FrameBuffer shadowMapFBO;
	FrameBuffer occludersFBO;
	
	Texture casterSprites;
	static Texture light;
	ShaderProgram shadowMapShader, shadowRenderShader;
		
	boolean additive = true;
	boolean softShadows = true;
	
	public Lighting () {
		lights = new LinkedList<Light>();
		updater = lights.iterator();
		
		ShaderProgram.pedantic = false;
		
		//read vertex pass-through shader
		final String VERT_SRC = Gdx.files.internal("shader/pass.vert").readString();
		
		// renders occluders to 1D shadow map
		shadowMapShader = createShader(VERT_SRC, Gdx.files.internal("shader/shadowMap.frag").readString());
		// samples 1D shadow map to create the blurred soft shadow
		shadowRenderShader = createShader(VERT_SRC, Gdx.files.internal("shader/shadowRender.frag").readString());
		
		//the occluders
		casterSprites = new Texture("humans/dhruv.png");
		//the light sprite
		light = new Texture("texture/items/potion_health.png");
		
		//build frame buffers
		occludersFBO = new FrameBuffer(Format.RGBA8888, lightSize, lightSize, false);
		occluders = new TextureRegion(occludersFBO.getColorBufferTexture());
		occluders.flip(false, true);
		
		//our 1D shadow map, lightSize x 1 pixels, no depth
		shadowMapFBO = new FrameBuffer(Format.RGBA8888, lightSize, 1, false);
		Texture shadowMapTex = shadowMapFBO.getColorBufferTexture();
		
		//use linear filtering and repeat wrap mode when sampling
		shadowMapTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		shadowMapTex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		
		//for debugging only; in order to render the 1D shadow map FBO to screen
		shadowMap1D = new TextureRegion(shadowMapTex);
		shadowMap1D.flip(false, true);
		
		font = new BitmapFont();
		
		cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.setToOrtho(false);
	}
	
	public void RenderLights(SpriteBatch batch, FrameBuffer lightingFBO) {
		this.batch = batch;
//		this.cam.position.set(Vector3.Zero);
		for(Light light : lights) {		
//			upScale = light.intensity;

			RenderOcclusionMap(light);
			RenderLight(light, lightingFBO);
		}
	}
	
	private void RenderOcclusionMap(Light l) {
		float mx = l.getPosition().x;
		float my = l.getPosition().y;
		
		//STEP 1. render light region to occluder FBO
		
		//bind the occluder FBO
		occludersFBO.begin();
		
		//clear the FBO
		Gdx.gl.glClearColor(0f,0f,0f,0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//set the orthographic camera to the size of our FBO
		cam.setToOrtho(false, occludersFBO.getWidth(), occludersFBO.getHeight());
		
		//translate camera so that light is in the center 
		cam.translate(mx - lightSize/2f, my - lightSize/2f);
		
		//update camera matrices
		cam.update();
		
		//set up our batch for the occluder pass
		batch.setProjectionMatrix(cam.combined);
		batch.setShader(null); //use default shader
		batch.begin();
		
		
		
		Floor floor = Map.getCurrentFloor();
		Tile[][] tiles = null;
		
		if (floor != null) {
			tiles = floor.getTiles();
		}
		
		tiles = floor.getTiles();
		if (tiles != null) {
			for (int i = 0; i < tiles.length; i++) {
				for (int j = 0; j < tiles[i].length; j++) {
					Tile tile = tiles[i][j];
					if (tile != null && tile.isWall)
						tile.render(batch);
//					else if (tile != null) {
//						for(GameObject item : tile.getObjects()) {
//							item.render(batch);
//						}
//					}
						
				}
			}
			
		}
		cam.translate(-mx + lightSize/2f, -my + lightSize/2f);
		//end the batch before unbinding the FBO
		batch.end();
		//unbind the FBO
		occludersFBO.end();
	}
	
	
	private void RenderLight(Light l, FrameBuffer lightingFBO) {
		float mx = l.getPosition().x;
		float my = l.getPosition().y;
		
		//STEP 2. build a 1D shadow map from occlude FBO
		
		//bind shadow map
		shadowMapFBO.begin();
		
		//clear it
		Gdx.gl.glClearColor(0f,0f,0f,0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		Gdx.gl.glClearColor(1f,1f,1f,1f);
		//set our shadow map shader
		batch.setShader(shadowMapShader);
		batch.begin();
		shadowMapShader.setUniformf("resolution", lightSize, lightSize);
		shadowMapShader.setUniformf("upScale", upScale);
		
		//reset our projection matrix to the FBO size
		cam.setToOrtho(false, shadowMapFBO.getWidth(), shadowMapFBO.getHeight());
		batch.setProjectionMatrix(cam.combined);
		
		//draw the occluders texture to our 1D shadow map FBO
		batch.draw(occluders.getTexture(), 0, 0, lightSize, shadowMapFBO.getHeight());

		//flush batch
		batch.end();
		
		//unbind shadow map FBO
		shadowMapFBO.end();
		
		//STEP 3. render the blurred shadows
		lightingFBO.begin();
		
		//reset projection matrix to screen
		cam.setToOrtho(false);
		cam.position.set(Game.player.getPosition());
		cam.update();
		batch.setProjectionMatrix(cam.combined);
		
		//set the shader which actually draws the light/shadow 
		batch.setShader(shadowRenderShader);
		batch.begin();
		
		shadowRenderShader.setUniformf("resolution", lightSize, lightSize);
		shadowRenderShader.setUniformf("softShadows", softShadows ? 1f : 0f);
		
		// clamping this is guaranteed to cause debuggin problems later
		shadowRenderShader.setUniformf("intensity", Utils.clamp(0,1, l.intensity));
		//set color to light
		batch.setColor(l.getColor());
		
		float finalSize = lightSize * upScale;
		
		//draw centered on light position
		batch.draw(shadowMap1D.getTexture(), mx-finalSize/2f, my-finalSize/2f, finalSize, finalSize);
		
		//flush the batch before swapping shaders
		batch.end();
		//reset color
		batch.setColor(Color.WHITE);
		lightingFBO.end();
	}

	
	public void debugRender(ShapeRenderer batch) {
		batch.setColor(Color.WHITE);
		batch.line(new Vector2(cam.position.x, cam.position.y), new Vector2(Game.player.x, Game.player.y));
		batch.setColor(Color.CYAN);
		batch.line(new Vector2(cam.position.x, cam.position.y), new Vector2(0,0));
		batch.setColor(Color.RED);
		batch.rect(cam.position.x+5, cam.position.y+5, cam.viewportWidth-10, cam.viewportHeight-10);
	}
	

	
	
	
	
	private static LinkedList<Light> lights;
	private static LinkedList<Light> waitlist;
	private static Iterator<Light> updater;
	
	
	
	
	
	
	
	/**
	 * Updates the lights and shader.
	 * 
	 * @param deltaTime
	 *            - Time since last render call.
	 */
	public static void updateLights(float deltaTime, Vector2 targetVector) {
		updater = lights.iterator();
		Light light;
		while (updater.hasNext()) {
			light = updater.next();
			light.update(deltaTime, targetVector);
		}
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

//	/**
//	 * Sets batch shader to the shader used for lighting.
//	 * 
//	 * @param batch
//	 */
//	public static void setShader(SpriteBatch batch) {
//		batch.setShader(shader);
//	}

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

//	/**
//	 * Toggle for turning on/off global lighting. 
//	 */
//	public static void toggleLights() {
//		lightsOn ^= true;
//	}

	/**
	 * Disposes the assets used by Lighting. Including: - ShaderProgram
	 */
	public static void dispose() {
//		shader.dispose();
	}
	
	
}
