package ecu.se;

import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

import actors.Player;
import actors.TestActor;
import archive.Archiver;
import assetManager.AssetManager;
import ecu.se.gui.HUD;
import ecu.se.map.Map;
import ecu.se.objects.Light;

public class Game extends ApplicationAdapter {
	SpriteBatch batch;
	
	private float deltaTime;
	private ObjectManager objectManager;
	private int screenHeight, screenWidth;
	private Map map;
	private OrthographicCamera camera;
	private Player player;
	private HUD hud;
	//private Lighting lighting;
	
	private float zoom = Globals.DEFAULT_CAMERA_ZOOM;
	
	// DEBUG OBJECT(S)
	private ShapeRenderer shaperRenderer;
	private Light light;
	// END DEBUG OBJECT(S)
	
	@Override
	public void create () {
	    deltaTime = TimeUtils.millis();
	    screenHeight = Gdx.graphics.getHeight();
	    screenWidth = Gdx.graphics.getWidth();
	    objectManager = new ObjectManager();
	    map = new Map();
	    map.setScreenResolution(screenWidth, screenHeight);
	    player = new Player(map.floorHelper(0,0).x, map.floorHelper(0,0).y, 0, map, camera, "texture/spritesheet/adventuretime_sprites.png");
	    objectManager.setPlayer(player);
	    
	    Random random  = new Random();
	    for(int  i = 0; i < 50; i++) {
	        objectManager.add(new TestActor(random.nextInt(Globals.MAP_TILE_WIDTH * 128), random.nextInt(Globals.MAP_TILE_HEIGHT * 128), 0, map, "texture/spritesheet/adventuretime_sprites.png"));	        
	    }
	    hud = new HUD(player, screenWidth, screenHeight);
	    camera = new OrthographicCamera(screenWidth, screenHeight);
		batch = new SpriteBatch();
		shaperRenderer = new ShapeRenderer();

		// RECORDS
		Archiver.startArchiver();
	    Lighting.init(hud.getCamera());
		Lighting.setShader(batch);
		
		
		for(int i = 0 ; i < 32; i++) {
			light = new Light(new Vector3(i*50,i*50,0));
//			light = new Light(player.getPosition());
			light.setColor(new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 1.0f));
			//light.setColor(new Color(111f,111f,111f, 1.0f));
			light.setIntensity(25);
			Lighting.addLight(light);
			
		}
		
		

	}
	
	// Update all game objects
	public void update() {
	    deltaTime = Gdx.graphics.getDeltaTime();
        objectManager.update(deltaTime);
        player.update(deltaTime);
        camera.update();
        camera.zoom = zoom;
        Lighting.updateLights(deltaTime);
	}
	
	private static Random rand = new Random();
	
	@Override
	public void render () {
	    input(); // JUST MOVED THIS FROM THE BOTTOM TO THE TOP
		
	    update();
	    
	    //TEST
	    
	    
	    Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//	    camera.zoom = zoom;
		batch.begin();
		batch.setProjectionMatrix(camera.combined);
		
		Lighting.setShader(batch);
		map.render(batch, (int)player.x, (int)player.y);
		objectManager.render(deltaTime, batch);
		
		batch.setShader(null);
		hud.render(batch);
		
		
		batch.end();
		
		
		if(Globals.DEBUG) {
		    shaperRenderer.begin(ShapeType.Line);
		    int radius = 25;
		    Vector3 temp = camera.project(light.getPos());
		    Vector3 temp2 = camera.project(player.getPosition());
		    shaperRenderer.end();
		    
//		}
			map.debugRender(camera.combined, (int)player.x, (int)player.y);
			objectManager.debugRender(camera.combined);
		
//		if(Globals.DEBUG) {
            Utils.DrawDebugLine(new Vector2(0,-50), new Vector2(0,50), camera.combined);
            Utils.DrawDebugLine(new Vector2(-50,0), new Vector2(50,0), camera.combined);
        }
		
		
	}
	int floor = 0;
	public void input() {
		
	    player.input(deltaTime);
        camera.position.set(player.x, player.y, 0);
        // Zoom camera
        if(Gdx.input.isKeyPressed(Input.Keys.E)){
            zoom += 0.01f;
            System.out.println("Zoom=" +zoom);
        } else if(Gdx.input.isKeyPressed(Input.Keys.Q)){
            zoom -= 0.01f;
            System.out.println("Zoom=" +zoom);
        }else if(Gdx.input.isKeyPressed(Input.Keys.R)){
            zoom = Globals.DEFAULT_CAMERA_ZOOM;
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
        
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            Lighting.printLog();
        }
        
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
        	
            dispose();
        }
           
	}
	
	@Override
	public void dispose () {
		Archiver.dispose();
	    batch.dispose();
		objectManager.dispose();
		map.dispose();
		Lighting.dispose();
		AssetManager.dispose();
	}
}
