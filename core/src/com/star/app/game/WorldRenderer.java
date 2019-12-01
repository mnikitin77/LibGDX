package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.StringBuilder;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class WorldRenderer {
    private GameController gc;
    private SpriteBatch batch;
    private BitmapFont font32;
    private BitmapFont font24;
    private StringBuilder strBuilder;

    public WorldRenderer(GameController gc, SpriteBatch batch) {
        this.gc = gc;
        this.batch = batch;
        font32 = Assets.getInstance().getAssetManager().get(
                "fonts/font32.ttf", BitmapFont.class);
        font24 = Assets.getInstance().getAssetManager().get(
                "fonts/font24.ttf", BitmapFont.class);
        strBuilder = new StringBuilder();
    }

    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        gc.getBackground().render(batch);
        gc.getHero().render(batch);
        gc.getBulletController().render(batch);
        gc.getAsteroidController().render(batch);
        gc.getItemsController().render(batch, font24);
        gc.getParticleController().render(batch);
        gc.getHero().renderGUI(batch, font32);
        if (gc.getHero().getHp() <= 0) {
            batch.draw(
                    Assets.getInstance().getAtlas().findRegion("gameover"),
                    ScreenManager.SCREEN_WIDTH / 2 - 170,
                    ScreenManager.SCREEN_HEIGHT / 2 - 74);
        }
        batch.end();
    }
}
