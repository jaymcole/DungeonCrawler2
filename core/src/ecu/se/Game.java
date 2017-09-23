package ecu.se;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import actors.Player;
import assetManager.AssetManager;
import ecu.se.gui.HUD;
import ecu.se.map.Map;

public class Game extends ApplicationAdapter {
	SpriteBatch batch;
	
	private float deltaTime;
	private ObjectManager objectManager;
	private int screenHeight, screenWidth;
	private Map map;
	private OrthographicCamera camera;
	private Player player;
	private HUD hud;
	
	private int zoom = Globals.DEFAULT_CAMERA_ZOOM;
	
	// DEBUG THINGS - Needs to be deleted later
    private ShapeRenderer shaperRenderer;
	
	@Override
	public void create () {
	    deltaTime = TimeUtils.millis();
	    screenHeight = Gdx.graphics.getHeight();
	    screenWidth = Gdx.graphics.getWidth();
	    objectManager = new ObjectManager();
	    map = new Map();
	    map.setScreenResolution(screenWidth, screenHeight);
	    player = new Player(map.floorHelper(0,0).x, map.floorHelper(0,0).y, 0, map, camera);
	    objectManager.setPlayer(player);
	    objectManager.add(new Player(player.x + 15, player.y + 15, 0 , map, camera));
	    hud = new HUD(player, screenWidth, screenHeight);
	    camera = new OrthographicCamera(screenWidth, screenHeight);
		batch = new SpriteBatch();
		shaperRenderer = new ShapeRenderer();
		AssetManager.getSpriteSheet("texture/spritesheet/adventuretime_sprites.png");
	}
	
	// Update all game objects
	public void update() {
	    deltaTime = Gdx.graphics.getDeltaTime();
        objectManager.update(deltaTime);
        player.update(deltaTime);
        camera.update();
        camera.zoom = zoom;
	}
	
	@Override
	public void render () {
	    update();
		
	    Gdx.gl.glClearColor(0f, 0f, 0f, 1);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		batch.setProjectionMatrix(camera.combined);
		
		map.render(batch, (int)player.x, (int)player.y);
		objectManager.render(deltaTime, batch);
		player.render(deltaTime, batch);
		hud.render(batch);
		
		
		batch.end();
		
		
		
		if(Globals.DEBUG) {
		    shaperRenderer.begin(ShapeType.Line);
		    int radius = 25;
		    shaperRenderer.ellipse((int)(camera.viewportWidth*0.5-radius*0.5), (int)(camera.viewportHeight*0.5-radius*0.5), 25, 25);
		    shaperRenderer.end();
		}
		
		if(Globals.DEBUG) {
            Utils.DrawDebugLine(new Vector2(0,-50), new Vector2(0,50), camera.combined);
            Utils.DrawDebugLine(new Vector2(-50,0), new Vector2(50,0), camera.combined);
        }
		/*
        */
		
		input();
		
	}
	int floor = 0;
	public void input() {
	    player.input(deltaTime);
        camera.position.set(player.x, player.y, 0);
        // Zoom camera
        if(Gdx.input.isKeyPressed(Input.Keys.E)){
            zoom += 1;
        } else if(Gdx.input.isKeyPressed(Input.Keys.Q)){
            zoom -= 1;
        }else if(Gdx.input.isKeyPressed(Input.Keys.R)){
            zoom = 1;
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
