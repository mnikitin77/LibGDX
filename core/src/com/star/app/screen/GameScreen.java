package com.star.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.star.app.game.GameController;
import com.star.app.game.WorldRenderer;
import com.star.app.screen.utils.Assets;

public class GameScreen extends AbstractScreen {
    private GameController gameController;
    private WorldRenderer worldRenderer;
    private BitmapFont font24;
    private Stage stage;

    public GameScreen(SpriteBatch batch) {
        super(batch);
    }

    @Override
    public void show() {
        // Если игра была начата ранее, не проводим инициализацию заново.
//        if (ScreenManager.getInstance().getGc() != null &&
//                ScreenManager.getInstance().getGc().isActive()) {
//            Assets.getInstance().loadAssets(ScreenManager.ScreenType.GAME);
//            Gdx.input.setInputProcessor(stage);
//            return;
//        }

        Assets.getInstance().loadAssets(ScreenManager.ScreenType.GAME);
//        if (ScreenManager.getInstance().getGc() == null ||
//                !ScreenManager.getInstance().getGc().isActive()) {
        if (ScreenManager.getInstance().getGc() == null) {
            gameController = new GameController();
        }
            worldRenderer = new WorldRenderer(gameController, batch);
//        }

        stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        font24 = Assets.getInstance().getAssetManager().get("fonts/font24.ttf");

        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());

        TextButton.TextButtonStyle textButtonStyle =
                new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("smButton");
        textButtonStyle.font = font24;
        skin.add("simpleSkin", textButtonStyle);

        final Button btnPause = new TextButton("Pause", textButtonStyle);
        Button btnMenu = new TextButton("Menu", textButtonStyle);
        btnMenu.setPosition(ScreenManager.SCREEN_WIDTH - btnMenu.getWidth() - 5,
                ScreenManager.SCREEN_HEIGHT - btnMenu.getHeight() - 20);
        btnPause.setPosition(btnMenu.getX() - btnPause.getWidth() - 5 ,
                btnMenu.getY());

        btnPause.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (ScreenManager.getInstance().getGc().isPaused()) {
                    ((TextButton)btnPause).setText("Pause");
                    ScreenManager.getInstance().getGc().resume();
                } else {
                    ((TextButton)btnPause).setText("Resume");
                    ScreenManager.getInstance().getGc().pause();
                }
            }
        });

        btnMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().getGc().pause();
                ScreenManager.getInstance().changeScreen(
                        ScreenManager.ScreenType.MENU);
            }
        });

        stage.addActor(btnPause);
        stage.addActor(btnMenu);
        skin.dispose();

    // Активируем игру
        ScreenManager.getInstance().getGc().activate();
    }

    public void update(float dt) {
        stage.act(dt);
        if (!ScreenManager.getInstance().getGc().isPaused()) {
            gameController.update(dt);
        }
    }

    @Override
    public void render(float delta) {
        update(delta);
        worldRenderer.render();
        stage.draw();
    }

    @Override
    public void dispose() {
        gameController.dispose();
    }
}
