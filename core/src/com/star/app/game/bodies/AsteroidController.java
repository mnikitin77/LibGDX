package com.star.app.game.bodies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.star.app.game.GameController;
import com.star.app.game.helpers.ObjectPool;
import com.star.app.screen.utils.Assets;

public class AsteroidController extends ObjectPool<Asteroid> {
    private GameController gc;
    //private static final int ASTEROIDS_COUNT = 3;
    private static final int ASTEROIDS_COUNT = 2;
    protected TextureRegion asteroidTexture;
    private Sound crashSound;

    @Override
    protected Asteroid newObject() {
        return new Asteroid(gc, asteroidTexture, crashSound, MathUtils.random(5f, 10f));
    }

    public AsteroidController(GameController gc) {
        this.gc = gc;
        asteroidTexture = Assets.getInstance().getAtlas().findRegion("asteroid");
        crashSound = Assets.getInstance().getAssetManager().get("audio/Stone.mp3");
        initialize();
    }

    public void initialize() {
        for (int i = 0; i < ASTEROIDS_COUNT; i++) {
            getActiveElement().activate(0f,0f,0f,0f);
        }
    }

    public void setup(float x, float y, float vx, float vy, float scale) {
        getActiveElement().activate(x, y, vx, vy, scale);
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch);
        }
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        //checkPool();
    }
}
