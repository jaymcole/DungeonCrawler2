package ecu.se.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ecu.se.GameObject;
import ecu.se.assetManager.Animation;
import ecu.se.assetManager.SpriteAsset;

public class Effect extends GameObject{

	private Animation animation;
	
	public Effect(float x, float y, SpriteAsset spriteAsset, float cycles) {
		super(x, y);
		animation = new Animation(x, y, 10, spriteAsset);
		animation.setXY((int)x, (int)y);
		animation.setIdle(false);
		animation.setUseAllRows(true);
		animation.setLoopCycles(cycles);

	}
	
	public Effect(float x, float y, SpriteAsset spriteAsset, int row, boolean useAllRows, float cycles) {
		super(x, y);
		animation = new Animation(x, y, 10, spriteAsset);
		animation.setXY((int)x, (int)y);
		animation.setIdle(false);
		animation.setUseAllRows(useAllRows);
		animation.setRow(row);
		animation.setLoopCycles(cycles);
	}

	@Override
	public void update(float deltaTime) {
		if (!animation.isAlive())
			this.kill();
		animation.setXY((int)x, (int)y);
		animation.update(deltaTime);
	}

	@Override
	public void render(SpriteBatch batch) {
		animation.render(batch);
	}

	@Override
	public void dispose() {
		animation.dispose();
	}

}
