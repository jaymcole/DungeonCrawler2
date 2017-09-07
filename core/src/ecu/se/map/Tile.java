package ecu.se.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Tile {
    public int x, y;
    private Texture texture;
    public boolean alive;
    
    public Tile() {
        texture = new Texture("texture/floor/grass.png");
    }
    
    public void render (SpriteBatch batch) {
        batch.draw(texture, x, y);
    }
}
