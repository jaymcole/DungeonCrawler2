package ecu.se.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ecu.se.GameObject;


public class TestObject extends GameObject {
    Texture texture;
    float radius = 1000;
    float speed = 0.1f;
    float degree = 0;
    float rads;

    public TestObject(float x, float y, float z, String filePath) {
        super(x, y, z);
        texture = loadTexture(filePath);
    }

    @Override
    public void update(double deltaTime) {
        degree = (float) (degree + (speed * deltaTime));
        while(degree > 360f) {degree-=360.0f;}
        rads = (float) Math.toRadians(degree);
        x = (float) Math.cos(rads) * radius;
        y = (float) Math.sin(rads) * radius;
    }

    @Override
    public void render(double deltaTime, SpriteBatch batch) {       
        batch.draw(texture, x, y);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }   
    
}
