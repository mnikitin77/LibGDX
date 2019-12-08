package com.star.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.StringBuilder;
import com.star.app.game.Background;
import com.star.app.screen.utils.Assets;

public class GameOverScreen extends AbstractScreen {
    private Background background;
    private TextureRegion texture;
    private StringBuilder strBuilder;
    private BitmapFont font32;
    private Sound gameOverSound;

    public GameOverScreen(SpriteBatch batch) {
        super(batch);
        strBuilder = new StringBuilder();
    }

    @Override
    public void show() {
        background = new Background(null);
        texture = Assets.getInstance().getAtlas().findRegion("gameover");
        font32 = Assets.getInstance().getAssetManager().get(
                "fonts/font32.ttf", BitmapFont.class);
        gameOverSound = Assets.getInstance().getAssetManager().get("audio/GameOver.mp3");
        gameOverSound.play();
    }

    public void update(float dt) {
        background.update(dt);
        if (Gdx.input.justTouched() ||
                Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            ScreenManager.getInstance().changeScreen(
                    ScreenManager.ScreenType.MENU);
            ScreenManager.getInstance().getGc().deactivate();
        }
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.render(batch);
        batch.draw(texture,
                ScreenManager.HALF_SCREEN_WIDTH -
                        texture.getRegionWidth() / 2,
                ScreenManager.HALF_SCREEN_HEIGHT -
                        texture.getRegionHeight() / 2);

        strBuilder.clear();
        strBuilder.append("SCORE: ").append(ScreenManager.getInstance().
                getGc().getHero().getScore()).append("\n");
        strBuilder.append("COINS: ").append(ScreenManager.getInstance().
                getGc().getHero().getMoney()).append("\n");
        font32.draw(batch, strBuilder,
                ScreenManager.HALF_SCREEN_WIDTH
                        - texture.getRegionWidth() / 2,
                ScreenManager.HALF_SCREEN_HEIGHT -
                        texture.getRegionHeight() / 2 - 50,
                texture.getRegionWidth(), Align.center, false);

        batch.end();
    }

    @Override
    public void dispose() {
        background.dispose();
    }
}
