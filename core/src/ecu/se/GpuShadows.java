package ecu.se;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
  * Per-pixel shadows on GPU: https://github.com/mattdesl/lwjgl-basics/wiki/2D-Pixel-Perfect-Shadows
  * @author mattdesl */
public class GpuShadows implements ApplicationListener {	
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
	
	private int lightSize = 256;
	
	private float upScale = 1f; //for example; try lightSize=128, upScale=1.5f
		
	SpriteBatch batch;
	OrthographicCamera cam; 
	
	BitmapFont font;
	
	TextureRegion shadowMap1D; //1 dimensional shadow map
	TextureRegion occluders;   //occluder map
	
	FrameBuffer shadowMapFBO;
	FrameBuffer occludersFBO;
	
	Texture casterSprites;
	Texture light;
	
	ShaderProgram shadowMapShader, shadowRenderShader;
	
	Array<Light> lights = new Array<Light>();
	
	boolean additive = true;
	boolean softShadows = true;
	
	class Light {
		
		float x, y;
		Color color;
		
		public Light(float x, float y, Color color) {
			this.x = x;
			this.y = y;
			this.color = color;
		}
	}
	
	@Override
	public void create() {
		batch = new SpriteBatch();
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
		
		Gdx.input.setInputProcessor(new InputAdapter() {
			
			public boolean touchDown(int x, int y, int pointer, int button) {
				float mx = x;
				float my = Gdx.graphics.getHeight() - y;
				lights.add(new Light(mx, my, randomColor()));
				return true;
			}
			
			public boolean keyDown(int key) {
				if (key==Keys.SPACE){
					clearLights();
					return true;
				} else if (key==Keys.A){
					additive = !additive;
					return true;
				} else if (key==Keys.S){ 
					softShadows = !softShadows;
					return true;
				}
				return false;
			}
		});
		
		clearLights();
	}

	@Override
	public void resize(int width, int height) {
		cam.setToOrtho(false, width, height);
		batch.setProjectionMatrix(cam.combined);
	}

	@Override
	public void render() {
		//clear frame
		Gdx.gl.glClearColor(0.25f,0.25f,0.25f,1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		float mx = Gdx.input.getX();
		float my = Gdx.graphics.getHeight() - Gdx.input.getY();
				
		if (additive)
			batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
		
		for (int i=0; i<lights.size; i++) {
			Light o = lights.get(i);
			if (i==lights.size-1) {
				o.x = mx;
				o.y = my;
			}	
			renderLight(o);
		}
		
		if (additive)
			batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		//STEP 4. render sprites in full colour		
		batch.begin();
		batch.setShader(null); //default shader
		
		
		batch.draw(casterSprites, 0, 0);
		
		//DEBUG RENDERING -- show occluder map and 1D shadow map
		batch.setColor(Color.BLACK);
		batch.draw(occluders, Gdx.graphics.getWidth()-lightSize, 0);
		batch.setColor(Color.WHITE);
		batch.draw(shadowMap1D, Gdx.graphics.getWidth()-lightSize, lightSize+5);
		
		//DEBUG RENDERING -- show light 
		batch.draw(light, mx-light.getWidth()/2f, my-light.getHeight()/2f); //mouse
		batch.draw(light, Gdx.graphics.getWidth()-lightSize/2f-light.getWidth()/2f, lightSize/2f-light.getHeight()/2f);
		
		//draw FPS
//		font.drawMultiLine(batch, "FPS: "+Gdx.graphics.getFramesPerSecond()
//				+"\n\nLights: "+lights.size
//				+"\nSPACE to clear lights"
//				+"\nA to toggle additive blending"
//				+"\nS to toggle soft shadows", 10, Gdx.graphics.getHeight()-10);
		
		batch.end();
	}
	
	void clearLights() {
		lights.clear();
		lights.add(new Light(Gdx.input.getX(), Gdx.graphics.getHeight()-Gdx.input.getY(), Color.WHITE));
	}
	
	static Color randomColor() {
		float intensity = (float)Math.random() * 0.5f + 0.5f;
		return new Color((float)Math.random(), (float)Math.random(), (float)Math.random(), intensity);
	}
	
	void renderLight(Light o) {
		float mx = o.x;
		float my = o.y;
		
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
		// ... draw any sprites that will cast shadows here ... //
		batch.draw(casterSprites, 0, 0);
		
		//end the batch before unbinding the FBO
		batch.end();
		
		//unbind the FBO
		occludersFBO.end();
		
		//STEP 2. build a 1D shadow map from occlude FBO
		
		//bind shadow map
		shadowMapFBO.begin();
		
		//clear it
		Gdx.gl.glClearColor(0f,0f,0f,0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
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
		
		//reset projection matrix to screen
		cam.setToOrtho(false);
		batch.setProjectionMatrix(cam.combined);
		
		//set the shader which actually draws the light/shadow 
		batch.setShader(shadowRenderShader);
		batch.begin();
		
		shadowRenderShader.setUniformf("resolution", lightSize, lightSize);
		shadowRenderShader.setUniformf("softShadows", softShadows ? 1f : 0f);
		//set color to light
		batch.setColor(o.color);
		
		float finalSize = lightSize * upScale;
		
		//draw centered on light position
		batch.draw(shadowMap1D.getTexture(), mx-finalSize/2f, my-finalSize/2f, finalSize, finalSize);
		
		//flush the batch before swapping shaders
		batch.end();
		
		//reset color
		batch.setColor(Color.WHITE);
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}