package ecu.se;

import java.util.Random;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

import actors.Player;
import actors.RangedBadGuy;
import archive.Archiver;
import archive.TimeRecords;
import archive.TotalRecords;
import assetManager.Animation;
import assetManager.AssetManager;
import ecu.se.gui.GUI;
import ecu.se.map.Map;
import ecu.se.objects.Decal;
import ecu.se.objects.ItemObject;
import ecu.se.objects.Light;

public class Game extends ApplicationAdapter {
	SpriteBatch batch;

	private float deltaTime;
	private ObjectManager objectManager;
	private int screenHeight, screenWidth;
	private Map map;
	private OrthographicCamera camera;
	public static Player player;
	private GUI hud;

	private FrameBuffer frameBuffer;
	private TextureRegion frameBufferRegion;

	private float zoom = Globals.DEFAULT_CAMERA_ZOOM;

	private String backgroundTextureName = "texture/misc/black.jpg";
	private Texture backgroundTexture;

	// DEBUG OBJECT(S)
	private ShapeRenderer shapeRenderer;
	private Light light;
	// END DEBUG OBJECT(S)
	
	
	
	public static final int GAME_STATE_RUNNING = 0;
	public static final int GAME_STATE_PAUSED = 1;
	public static final int GAME_STATE_EXITING = 2;
	public static int currentState = 0;
	
	public static final int MOUSE_UP		= -1;
	public static final int MOUSE_DOWN 	= 0;
	public static final int MOUSE_RELEASED = 1;
	public static final int MOUSE_PRESSED 	= 2;
	public static int leftMouseState = MOUSE_UP;
	public static int rightMouseState = MOUSE_UP;

	@Override
	public void create() {
		// RECORDS
		Archiver.startArchiver();
		Archiver.set(TimeRecords.TOTAL_TIME_PLAYED, false);
		Archiver.set(TotalRecords.TIMES_STARTING_GAME, 1);
		Archiver.set(TimeRecords.TIME_IN_MENU, false);
		Archiver.set(TimeRecords.TIME_IDLE, false);

		deltaTime = TimeUtils.millis();
		screenHeight = Gdx.graphics.getHeight();
		screenWidth = Gdx.graphics.getWidth();
		objectManager = new ObjectManager();
		map = new Map();
		Map.setScreenResolution(screenWidth, screenHeight);
		player = new Player(Map.getFloorUp(0, 0).x, Map.getFloorUp(0, 0).y, 0, map, camera, 
				new String[]{
						"texture/spritesheet/bleh.png",
						"texture/spritesheet/bleh.png",
						"texture/spritesheet/bleh.png"}, 
				new int[]{3,2,1});
		ObjectManager.setPlayer(player);
		
		Animation a = new Animation(player.x, player.y,0, AssetManager.getSpriteSheet("texture/spritesheet/fire_spritesheet.png"));
		
		ObjectManager.add(a);
		ObjectManager.add(new ItemObject(player.getX(), player.getY(), "Sprite LOL", "texture/test/spritePlaceholder.png"));
		
		Random random = new Random();
		for (int i = 0; i < 50; i++) {
//			ObjectManager.add(new RangedBadGuy(random.nextInt(Globals.MAP_TILE_WIDTH * 128), random.nextInt(Globals.MAP_TILE_HEIGHT * 128), 0, map, new String[]{"texture/spritesheet/zombie_spritesheet.png"}, new int[]{random.nextInt(5)}, player));
			ObjectManager.add(new RangedBadGuy(random.nextInt(Globals.MAP_TILE_WIDTH * 128), random.nextInt(Globals.MAP_TILE_HEIGHT * 128), 0, map, new String[]{"texture/spritesheet/grayguys.png"}, new int[]{0}, player));
		}
		
		Map.getTile((int)player.getX(), (int)player.getY()).addObject(new Decal(player.x, player.y, "ass", AssetManager.getTexture("texture/decals/decal_lava.png").getTextureRegion()));

		hud = new GUI(player, screenWidth, screenHeight, this);
		camera = new OrthographicCamera(screenWidth, screenHeight);
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();

		Lighting.init(camera, player);
		Lighting.setShader(batch);

		light = new Light(player);
		light.setColor(new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 1.0f));
		light.setIntensity(1800);
		Lighting.addLight(light);

		backgroundTexture = AssetManager.getTexture(backgroundTextureName).getTexture();

		halfWidth = screenWidth * 0.5f;
		halfHeight = screenHeight * 0.5f;
	}

	// Update all game objects
	public void update() {
		deltaTime = Gdx.graphics.getDeltaTime();
		hud.update(deltaTime);
		if (currentState == GAME_STATE_RUNNING) {
			ObjectManager.update(deltaTime);
			player.update(deltaTime);
			camera.update();
			Lighting.updateLights(deltaTime, player.getPositionV2());
			camera.zoom = zoom;
		}
		Map.update(deltaTime, (int) player.x, (int) player.y);
	}

	private static Random rand = new Random();
	private float halfWidth;
	private float halfHeight;
	@Override
	public void render() {
		if (currentState == GAME_STATE_EXITING) {
			close();
			return;
		}	
		
		
		
		input(); // JUST MOVED THIS FROM THE BOTTOM TO THE TOP

		update();

		// TEST
		frameBuffer = new FrameBuffer(Format.RGBA8888, screenWidth, screenHeight, false);
		frameBufferRegion = new TextureRegion(frameBuffer.getColorBufferTexture(), Gdx.graphics.getWidth(),
				(int) (Gdx.graphics.getHeight()));
		frameBufferRegion.flip(false, true);

		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		frameBuffer.begin();
		batch.begin();
		batch.setProjectionMatrix(camera.combined);
		batch.draw(backgroundTexture, camera.position.x - Gdx.graphics.getWidth() * 0.5f,
				camera.position.y - Gdx.graphics.getHeight() * 0.5f, Gdx.graphics.getWidth(),
				(int) (Gdx.graphics.getHeight()));
		Map.render(batch);
		ObjectManager.render(deltaTime, batch);
		batch.end();
		frameBuffer.end();

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		batch.setProjectionMatrix(camera.combined);
		Lighting.setShader(batch);
		batch.draw(frameBufferRegion, camera.position.x - halfWidth, camera.position.y - halfHeight,
				Gdx.graphics.getWidth(), (int) (Gdx.graphics.getHeight()));
		batch.setShader(null);

		batch.end();

		frameBuffer.dispose();

		if (Globals.DEBUG) {
			shapeRenderer.begin(ShapeType.Line);

			shapeRenderer.setProjectionMatrix(camera.combined);

			Map.debugRender(shapeRenderer, (int) player.x, (int) player.y);
			objectManager.debugRender(shapeRenderer);
			shapeRenderer.end();

			Utils.DrawDebugLine(new Vector2(0, -50), new Vector2(0, 50), camera.combined);
			Utils.DrawDebugLine(new Vector2(-50, 0), new Vector2(50, 0), camera.combined);
		}

		batch.begin();
		hud.render(batch);
		batch.end();
		
		if (Globals.DEBUG) {
			shapeRenderer.begin(ShapeType.Line);
			hud.debugRender(shapeRenderer);
			shapeRenderer.end();
		}

	}

	int floor = 0;
	
	//TODO: Consolidate input groups (things that should be allowed while the game is paused and things that should be blocked)
	
	public void input() {
		if (currentState != GAME_STATE_PAUSED) {
			player.input(deltaTime);
		}
		camera.position.set(player.x, player.y, 0);
		// Zoom camera
		if (Gdx.input.isKeyPressed(Input.Keys.E)) {
			zoom += 0.01f;
			System.out.println("Zoom=" + zoom);
		} else if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
			zoom -= 0.01f;
			System.out.println("Zoom=" + zoom);
		} else if (Gdx.input.isKeyPressed(Input.Keys.R)) {
			zoom = Globals.DEFAULT_CAMERA_ZOOM;
		}

		// Generate new floor
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
			floor++;
			Map.setFloor(floor);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
			floor--;
			if (floor < 0)
				floor = 0;
			Map.setFloor(floor);
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
			Globals.RENDER_ALL_TILES ^= true;
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			Lighting.toggleLights();
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.P))
			pauseGame();

		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			hud.closeWindow(GUI.WINDOW_HUD, GUI.WINDOW_MAIN_MENU);
		}
		
		if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
			Globals.DEBUG ^= true;
		}

		
		// MOUSE INPUT
		leftMouseState = checkMouseButton(leftMouseState, Gdx.input.isButtonPressed(Input.Buttons.LEFT));
		rightMouseState = checkMouseButton(rightMouseState, Gdx.input.isButtonPressed(Input.Buttons.RIGHT));
		
		
		Vector3 pos = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		if (!hud.mouseUsed() && currentState != GAME_STATE_PAUSED) {
			if (leftMouseState == MOUSE_PRESSED) 
				player.primaryAction((int) pos.x, (int) pos.y);
	
			if (rightMouseState == MOUSE_PRESSED) 
				player.secondaryAction((int) pos.x, (int) pos.y);
		}
		player.lookAt = new Vector2(pos.x, pos.y);

	}
	
	private int checkMouseButton(int oldState, boolean buttonDown) {
		int newState = 0;
		switch (oldState) {
		case MOUSE_DOWN:
			if (!buttonDown)
				newState = MOUSE_RELEASED;
			break;
		case MOUSE_PRESSED:
			if (buttonDown)
				newState = MOUSE_DOWN;
			else 
				newState = MOUSE_RELEASED;
			break;
		case MOUSE_RELEASED:
			if (buttonDown)
				newState = MOUSE_PRESSED;
			else 
				newState = MOUSE_UP;
			break;
		case MOUSE_UP:
			if (buttonDown)
				newState = MOUSE_PRESSED;
			else 
				newState = MOUSE_UP;
			break;
		}
		
		return newState;
	}
	
	public static void pauseGame() {
		if (currentState == GAME_STATE_PAUSED) {
			currentState = GAME_STATE_RUNNING;
		} else {
			currentState = GAME_STATE_PAUSED;
			Archiver.set(TimeRecords.TIME_PAUSED, false);
		}
	}
	
	public void close() {
//		System.out.println("Disposing assets");
//		AssetManager.dispose();
		System.out.println("Disposing Archiver");
		Archiver.dispose();		
		System.out.println("Disposing Objects");
		ObjectManager.dispose();
		System.out.println("Disposing Map");
		Map.dispose();
		System.out.println("Disposing Lighting");
		Lighting.dispose();
		System.out.println("Exiting");
		Gdx.app.exit();
	}

}
