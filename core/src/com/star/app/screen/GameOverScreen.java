package com.star.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.StringBuilder;
import com.star.app.StarGame;
import com.star.app.game.Background;
import com.star.app.screen.utils.Assets;

public class GameOverScreen extends AbstractScreen {
    private Background background;
    private Stage stage;
    private StarGame game;
    private TextureRegion texture;

    public GameOverScreen(SpriteBatch batch, StarGame game) {
        super(batch);
        this.game = game;
    }

    @Override
    public void show() {
        background = new Background(null);
        texture = Assets.getInstance().getAtlas().findRegion("gameover");

        stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        Gdx.input.setInputProcessor(stage);

        stage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                game.deactivate();
                ScreenManager.getInstance().changeScreen(
                        ScreenManager.ScreenType.MENU);
            }
        });
    }

    public void update(float dt) {
        background.update(dt);
        stage.act(dt);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.render(batch);
        batch.draw(texture,
                ScreenManager.HALF_SCREEN_WIDTH - texture.getRegionWidth() / 2,
                ScreenManager.HALF_SCREEN_HEIGHT - texture.getRegionHeight() / 2);

        BitmapFont font = Assets.getInstance().getAssetManager().get(
                "fonts/font32.ttf", BitmapFont.class);
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("SCORE: ").
                append(game.getGc().getHero().getScore()).append("\n");
        strBuilder.append("COINS: ").
                append(game.getGc().getHero().getMoney()).append("\n");
        font.draw(batch, strBuilder,
                ScreenManager.HALF_SCREEN_WIDTH
                        - texture.getRegionWidth() / 2,
                ScreenManager.HALF_SCREEN_HEIGHT -
                        texture.getRegionHeight() / 2 - 50,
                texture.getRegionWidth(), Align.center, false);

        batch.end();
        stage.draw();
    }

    @Override
    public void dispose() {
        background.dispose();
    }
}
