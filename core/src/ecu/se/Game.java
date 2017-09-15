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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import actors.Player;
import ecu.se.map.Map;
import ecu.se.objects.TestObject;



public class Game extends ApplicationAdapter {
	SpriteBatch batch;
	
	private float deltaTime;
	private ObjectManager objectManager;
	private int screenHeight, screenWidth;
	private Map map;
	private int camX, camY;
	private BitmapFont font;
	OrthographicCamera camera;
	Player player;
	
	// DEBUG THINGS - Needs to be deleted later
    private ShapeRenderer shaperRenderer;
	
	@Override
	public void create () {
		deltaTime = TimeUtils.millis();
		objectManager = new ObjectManager();
		map = new Map();

	
		
		screenHeight = Gdx.graphics.getHeight();
		screenWidth = Gdx.graphics.getWidth();
		camera = new OrthographicCamera(screenWidth, screenHeight);
		camX = 0; camY =0;
		camera.position.set(camX, camY, 0);
		
		batch = new SpriteBatch();
		//objectManager.add(new TestObject(0f,0f,0f, "texture/test/test_face_red.png"));
		player = new Player(map.floorHelper(0,0).x, map.floorHelper(0,0).y, 0, map);
		
		
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/jay_font.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 48;
		parameter.characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.,!?";
		//e.g. abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.,!?: 
		// These characters should not repeat! 

		font = generator.generateFont(parameter);
		generator.dispose();
		
		shaperRenderer = new ShapeRenderer();
		
	}
	
	@Override
	public void render () {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		deltaTime = Gdx.graphics.getDeltaTime();
		objectManager.update(deltaTime);
		player.update(deltaTime);
		camera.update();
		batch.begin();
		batch.setProjectionMatrix(camera.combined);
		
		map.render(batch, camX, camY);
		objectManager.render(deltaTime, batch);
		player.render(deltaTime, batch);
		
		if(Globals.DEBUG) {
		    font.setColor(Color.WHITE);
		    font.draw(batch, "DEV Build", (int)(camera.position.x-screenWidth*0.5+15), (int)(-15+camera.position.y+screenHeight*0.5));
		    
		    shaperRenderer.begin(ShapeType.Line);
		    
		    int radius = 25;
		    shaperRenderer.ellipse((int)(camera.viewportWidth*0.5-radius*0.5), (int)(camera.viewportHeight*0.5-radius*0.5), 25, 25);
		    shaperRenderer.end();
		    
		    font.draw(batch, "Map Center", 10, -5);
            
		}
		
		batch.end();
		
		if(Globals.DEBUG) {
            Utilities.DrawDebugLine(new Vector2(0,-50), new Vector2(0,50), camera.combined);
            Utilities.DrawDebugLine(new Vector2(-50,0), new Vector2(50,0), camera.combined);
        }
		
		input();
		
	}
	int floor = 0;
	public void input() {
	    // Pan Camera\
	    /*
	    if(Gdx.input.isKeyPressed(Input.Keys.W)){
	        camY += Globals.CAMERA_SCROLL_SPEED_Y_AXIS * camera.zoom;
	    } if(Gdx.input.isKeyPressed(Input.Keys.A)){
	        camX -= Globals.CAMERA_SCROLL_SPEED_X_AXIS * camera.zoom;
        } if(Gdx.input.isKeyPressed(Input.Keys.S)){
            camY -= Globals.CAMERA_SCROLL_SPEED_Y_AXIS * camera.zoom;
        } if(Gdx.input.isKeyPressed(Input.Keys.D)){
            camX += Globals.CAMERA_SCROLL_SPEED_X_AXIS * camera.zoom;
        }
        */
        camera.position.set(player.x, player.y, 0);
         player.input(deltaTime);
        // Zoom camera
        if(Gdx.input.isKeyPressed(Input.Keys.E)){
            camera.zoom += 1;
        }if(Gdx.input.isKeyPressed(Input.Keys.Q)){
            camera.zoom -= 1;
        }
        
        
        // Generate new floor
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)){
            floor++;
            map.setFloor(floor);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)){
            floor--;
            if(floor < 0)
                floor = 0;
            map.setFloor(floor);
        }
        
        
        
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)){
            Globals.RENDER_ALL_TILES ^= true; 
        }
           
	}
	
	@Override
	public void dispose () {
	    batch.dispose();
		objectManager.dispose();
		map.dispose();
	}
	
}
