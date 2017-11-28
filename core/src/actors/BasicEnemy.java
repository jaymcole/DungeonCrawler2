package actors;

import java.util.LinkedList;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import ecu.se.Game;
import ecu.se.Globals;
import ecu.se.map.Direction;
import ecu.se.map.Map;
import ecu.se.map.PathFinder;
import ecu.se.map.PathNode;
import pathfinding.Pathfinder2;
import stats.Stats;

public class BasicEnemy extends Actor{
	
	private LinkedList<Vector2> path;
	
	public BasicEnemy(float x, float y, float z, String[] spriteSheet, int[] row) {
		super(x, y, z, spriteSheet, row);
		 currentSpeed = new Vector2(0, 0);
	     team = Team.MOB;
	     
	     Random random = new Random();
	     this.baseStats[Stats.SIZE.ordinal()] += (random.nextFloat() - 0.5f) / 2;
	     this.baseStats[Stats.SPEED.ordinal()] = 1;
	     
	     calculateStats();	     	     
	}
	
	public void act(float deltaTime) {
		counter++;
		if (counter > 30) {
			path = Pathfinder2.findPath(getPositionV2(), Game.player.getPositionV2());
		    counter = 0;
		}
		
		if (path != null && path.size() > 0) {
			lookAt = path.get(0);
			move(deltaTime, Direction.angleRad(this.getPositionV2(), lookAt), true);
		}
		
		
    }
	
	private int counter = 0;
	
	@Override
	public void debugRender(ShapeRenderer render) {
		render.setColor(Color.WHITE);			
		render.polygon(bounds.getTransformedVertices());
		render.ellipse(bounds.getOriginX(), bounds.getOriginY(), 10, 10);
		Vector2 center = new Vector2();
		bounds.getBoundingRectangle().getCenter(center);
		float hHeight = bounds.getBoundingRectangle().height * 0.5f;
		float hWidth = bounds.getBoundingRectangle().width * 0.5f;
		render.line(center.x - hWidth, center.y - hHeight, center.x + hWidth, center.y + hHeight);
		render.line(center.x - hWidth, center.y + hHeight, center.x + hWidth, center.y - hHeight);
		
		
		for (int i = 0; i < Map.getCurrentFloor().getMapWidth(); i++) {
			for (int j = 0; j < Map.getCurrentFloor().getMapHeight(); j++) {
				PathNode n1 = Map.getTileByIndex(i, j).pathNode;
				PathNode n2 = n1.pathParent;
				
				
				if (n1 != null && n2 != null) {
					
					render.line(
							n1.x * Globals.TILE_PIXEL_WIDTH, 
							n1.y * Globals.TILE_PIXEL_HEIGHT, 
							n2.x * Globals.TILE_PIXEL_WIDTH, 
							n2.y * Globals.TILE_PIXEL_HEIGHT
							);
					
				}
			}
		}
		
		
		
		if (path != null && path.size() > 0) {
			render.setColor(Color.MAGENTA);
			
			
			
			render.line(
					path.get(0).x, 
					path.get(0).y, 
					x , 
					y
			);
			for (int i = 0 ; i < path.size() - 1; i++) {
				render.line(
						path.get(i).x, 
						path.get(i).y, 
						path.get(i+1).x, 
						path.get(i+1).y
				);
				
				render.circle(path.get(i).x, path.get(i).y, 15.0f);
			}
		}
	}
}
