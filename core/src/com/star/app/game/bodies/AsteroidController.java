package com.star.app.game.bodies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.star.app.game.helpers.ObjectPool;

public class AsteroidController extends ObjectPool<Asteroid> {
    private static final int ASTEROIDS_COUNT = 3;
    protected Texture asteroidTexture;

    @Override
    protected Asteroid newObject() {
        return new Asteroid(asteroidTexture, MathUtils.random(5, 10));
    }

    public AsteroidController() {
        asteroidTexture = new Texture("asteroid.png");

        for (int i = 0; i < ASTEROIDS_COUNT; i++) {
            getActiveElement().activate(0f,0f,0f,0f);
        }
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            ((Asteroid)activeList.get(i)).render(batch);
        }
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }
}
