package com.star.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.star.app.StarGame;
import com.star.app.game.Background;
import com.star.app.screen.utils.Assets;
import com.star.app.screen.utils.OptionsUtils;

public class MenuScreen extends AbstractScreen {
    private Background background;
    private BitmapFont font72;
    private BitmapFont font24;
    private Stage stage;
    private StarGame game;

    public MenuScreen(SpriteBatch batch, StarGame game) {
        super(batch);
        this.game = game;
    }

    @Override
    public void show() {
        background = new Background(null);
        stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        font72 = Assets.getInstance().getAssetManager().get("fonts/font72.ttf");
        font24 = Assets.getInstance().getAssetManager().get("fonts/font24.ttf");

        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = font24;
        skin.add("simpleSkin", textButtonStyle);

        Button btnNewGame = new TextButton("New Game", textButtonStyle);
        Button btnExitGame = new TextButton("Exit Game", textButtonStyle);
        btnNewGame.setPosition(
                ScreenManager.HALF_SCREEN_WIDTH - btnExitGame.getWidth() / 2,
                ScreenManager.HALF_SCREEN_HEIGHT - 200);
        btnExitGame.setPosition(
                ScreenManager.HALF_SCREEN_WIDTH - btnNewGame.getWidth() / 2,
                ScreenManager.HALF_SCREEN_HEIGHT - 300);

        btnNewGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
            }
        });

        btnExitGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        stage.addActor(btnNewGame);
        stage.addActor(btnExitGame);

        if (game.isActive()) {
            Button btnResumeGame = new TextButton("Resume Game", textButtonStyle);
            btnResumeGame.setPosition(
                    ScreenManager.HALF_SCREEN_WIDTH - btnExitGame.getWidth() / 2,
                    ScreenManager.HALF_SCREEN_HEIGHT - 400);

            btnResumeGame.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (game.isPaused()) {
                        game.resume();
                    }
                    ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
                }
            });

            stage.addActor(btnResumeGame);
        }

        skin.dispose();

        if (!OptionsUtils.isOptionsExists()) {
            OptionsUtils.createDefaultProperties();
        }
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
        font72.draw(batch, "Star Game 2019", 0, ScreenManager.HALF_SCREEN_HEIGHT,
                ScreenManager.SCREEN_WIDTH, Align.center, false);
        batch.end();
        stage.draw();
    }

    @Override
    public void dispose() {
        background.dispose();
    }
}
