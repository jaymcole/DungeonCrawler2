package ecu.se;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import ecu.se.map.Map;
import ecu.se.objects.TestObject;



public class Game extends ApplicationAdapter {
	SpriteBatch batch;
	
	private float deltaTime;
	private ObjectManager objectManager;
	private int screenHeight, screenWidth;
	private Map map;
	
	private BitmapFont font;
	OrthographicCamera camera;
	
	@Override
	public void create () {
		deltaTime = TimeUtils.millis();
		objectManager = new ObjectManager();
		map = new Map();

		screenHeight = Gdx.graphics.getHeight();
		screenWidth = Gdx.graphics.getWidth();
		camera = new OrthographicCamera(screenWidth, screenHeight);
		batch = new SpriteBatch();
		objectManager.add(new TestObject(0f,0f,0f, "texture/test/test_face_red.png"));
		
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/jay_font.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 48;
		parameter.characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.,!?";
		//e.g. abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.,!?: 
		// These characters should not repeat! 

		font = generator.generateFont(parameter);
		generator.dispose();
		
		//font = new Font("jay_messy.ttl", Font.PLAIN, 23);
	}

	
	@Override
	public void render () {
	    
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		deltaTime = Gdx.graphics.getDeltaTime();
		objectManager.update(deltaTime);
		camera.update();
		
		batch.begin();
		batch.setProjectionMatrix(camera.combined);
		objectManager.render(deltaTime, batch);
		
		map.render(batch);
		
		if(Globals.DEBUG) {
		    font.setColor(Color.WHITE);
		    font.draw(batch, "DEV Build", (int)(camera.position.x-screenWidth*0.5+15), (int)(-15+camera.position.y+screenHeight*0.5));
		    
		    
		    font.draw(batch, "Map Center", 10, -5);
            
		}
		
		batch.end();
		
		if(Globals.DEBUG) {
            Utilities.DrawDebugLine(new Vector2(0,-50), new Vector2(0,50), camera.combined);
            Utilities.DrawDebugLine(new Vector2(-50,0), new Vector2(50,0), camera.combined);
        }
		
		input();
		
	}
	
	public void input() {
	    if(Gdx.input.isKeyPressed(Input.Keys.W)){
	        camera.translate(0, Globals.CAMERA_SCROLL_SPEED_Y_AXIS * camera.zoom);
	    } if(Gdx.input.isKeyPressed(Input.Keys.A)){
            camera.translate(-Globals.CAMERA_SCROLL_SPEED_X_AXIS * camera.zoom, 0);
        } if(Gdx.input.isKeyPressed(Input.Keys.S)){
            camera.translate(0, -Globals.CAMERA_SCROLL_SPEED_Y_AXIS * camera.zoom);
        } if(Gdx.input.isKeyPressed(Input.Keys.D)){
            camera.translate(Globals.CAMERA_SCROLL_SPEED_X_AXIS * camera.zoom, 0);
        }
        
        if(Gdx.input.isKeyPressed(Input.Keys.E)){
            camera.zoom += 1;
        }if(Gdx.input.isKeyPressed(Input.Keys.Q)){
            camera.zoom -= 1;
        }
           
	}
	
	@Override
	public void dispose () {
	    batch.dispose();
		objectManager.dispose();
	}
	
}
