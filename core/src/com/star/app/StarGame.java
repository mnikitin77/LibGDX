package com.star.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.star.app.game.GameController;
import com.star.app.screen.ScreenManager;

public class StarGame extends Game {
    private SpriteBatch batch;
    private boolean isActive;
    private boolean isPaused;
    private GameController gc;

    public boolean isActive() {
        return isActive;
    }

    public void activate(GameController gc) {
        if (gc != null) {
            isActive = true;
            this.gc = gc;
        }
    }

    public void deactivate() {
        isActive = false;
    }

    public GameController getGc() {
        return gc;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void pause() {
        isPaused = true;
    }

    public void resume() {
        isPaused = false;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        isActive = false;
        ScreenManager.getInstance().init(this, batch);
        ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
	}

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        float dt = Gdx.graphics.getDeltaTime();
        getScreen().render(dt);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}