package com.star.app.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.bodies.Asteroid;
import com.star.app.game.bodies.AsteroidController;
import com.star.app.game.items.Item;
import com.star.app.game.items.ItemsController;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

import static java.lang.Math.*;

public class GameController {
    public static final float NEW_LEVEL_TIMER = 3.0f;
    private static final float ITEM_ATTRACT_DISTANCE = 50f;

    private Background background;
    private BulletController bulletController;
    private AsteroidController asteroidController;
    private ParticleController particleController;
    private ItemsController itemsController;
    private InfoController infoController;
    private Hero hero;
    private Vector2 tmpVec;
    private boolean isActive;
    private boolean isPaused;
    private Circle itemAttractArea;
    private int level;
    private boolean isLevelFinished;
    private float newLevelMsgTimer;
    private Sound newLevelSound;

    public GameController() {
        isPaused = false;
        isActive = false;
        isLevelFinished = false;
        level = 1;
        newLevelMsgTimer = NEW_LEVEL_TIMER;
        newLevelSound = Assets.getInstance().
                getAssetManager().get("audio/NewLevel.mp3");
        newLevelSound.play();

        background = new Background(this);
        hero = new Hero(this, "PLAYER1");
        bulletController = new BulletController(this);
        asteroidController = new AsteroidController(this);
        particleController = new ParticleController();
        itemsController = new ItemsController(this);
        infoController = new InfoController();
        tmpVec = new Vector2(0.0f, 0.0f);
        itemAttractArea = new Circle();

        ScreenManager.getInstance().setGc(this);
    }

    public boolean isActive() {
        return isActive;
    }

    public void activate() {
        isActive = true;
    }

    public void deactivate() {
        isActive = false;
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

    public BulletController getBulletController() {
        return bulletController;
    }

    public AsteroidController getAsteroidController() {
        return asteroidController;
    }

    public ParticleController getParticleController() {
        return particleController;
    }

    public ItemsController getItemsController() {
        return itemsController;
    }

    public Background getBackground() {
        return background;
    }

    public Hero getHero() {
        return hero;
    }

    public int getLevel() {
        return level;
    }

    public float getNewLevelMsgTimer() {
        return newLevelMsgTimer;
    }

    public InfoController getInfoController() {
        return infoController;
    }

    public void levelUp() {
        level++;
        newLevelMsgTimer = NEW_LEVEL_TIMER;
        newLevelSound.play();

        // Обновляем игрока
        hero.initialize();
        // Обновляем астероиды
        asteroidController.clear();
        asteroidController.initialize();
        // Убираем несобранные предметы
        itemsController.clear();

        isLevelFinished = false;
    }

    public void update(float dt) {
        if (newLevelMsgTimer > 0f) {
            newLevelMsgTimer -= dt;
        }

        background.update(dt);
        hero.update(dt);
        bulletController.update(dt);
        asteroidController.update(dt);
        itemsController.update(dt);
        particleController.update(dt);
        infoController.update(dt);

        if (hero.getHp() > 0) {
            // Проверяем столкновение героя с объектами.
            checkCollisions();
            // проверяем на завершение уровня.
            if (checkWin()) {
                isLevelFinished = true; // Уровень завершён!
            }
        } else {
            deactivate(); // Game Over
            ScreenManager.getInstance().changeScreen(
                    ScreenManager.ScreenType.GAMEOVER);
        }

        if (isLevelFinished) {
            levelUp();
        }
    }

    public void hit(Hero h, Asteroid a) {
        // h - 1
        // a - 2
        float v1 = h.getVelocity().len();
        float v2 = a.getVelocity().len();

        float m1 = 0.1f;
        float m2 = a.getScale();

        float th1 = h.getVelocity().angleRad();
        float th2 = a.getVelocity().angleRad();

        float phi1 = tmpVec.set(a.getPosition()).
                sub(h.getPosition()).angleRad();
        float phi2 = tmpVec.set(h.getPosition()).
                sub(a.getPosition()).angleRad();

        float v1xN = (float) (((v1 * cos(th1 - phi1) * (m1 - m2) +
                2 * m2 * v2 * cos(th2 - phi1)) / (m1 + m2)) * cos(phi1) +
                v1 * sin(th1 - phi1) * cos(phi1 + PI / 2.0f));
        float v1yN = (float) (((v1 * cos(th1 - phi1) * (m1 - m2) +
                2 * m2 * v2 * cos(th2 - phi1)) / (m1 + m2)) * sin(phi1) +
                v1 * sin(th1 - phi1) * sin(phi1 + PI / 2.0f));

        float v2xN = (float) (((v2 * cos(th2 - phi2) * (m2 - m1) +
                2 * m1 * v1 * cos(th1 - phi2)) / (m2 + m1)) * cos(phi2) +
                v2 * sin(th2 - phi2) * cos(phi2 + PI / 2.0f));
        float v2yN = (float) (((v2 * cos(th2 - phi2) * (m2 - m1) +
                2 * m1 * v1 * cos(th1 - phi2)) / (m2 + m1)) * sin(phi2) +
                v2 * sin(th2 - phi2) * sin(phi2 + PI / 2.0f));

        h.getVelocity().set(v1xN, v1yN);
        a.getVelocity().set(v2xN, v2yN);
    }

    public void checkCollisions() {
        // Столкновение корабля с астероидом
        checkActeroidCollisions();

        // Попадание боеприпасом в астероид
        checkBulletCollisions();

        // Собираем предметы
        pickUpItems();
    }

    private boolean checkWin() {
        return asteroidController.getActiveList().size() <= 0;
    }

    private void checkActeroidCollisions() {
        for (int i = 0; i < asteroidController.getActiveList().size(); i++) {
            Asteroid a = asteroidController.getActiveList().get(i);
            if (hero.getHitArea().overlaps(a.getHitArea())) {
                float dst = a.getPosition().dst(hero.getPosition());
                float halfOverLen = (a.getHitArea().radius +
                        hero.getHitArea().radius - dst) / 2.0f;
                tmpVec.set(hero.getPosition()).sub(a.getPosition()).nor();
                hero.getPosition().mulAdd(tmpVec, halfOverLen);
                a.getPosition().mulAdd(tmpVec, -halfOverLen);

                hit(hero, a);
                if(a.takeDamage(2)) {
                    hero.addScore(a.getHpMax() * 10);
                }
                //hero.takeDamage(a.getWeight() * 20);
                hero.takeDamage(a.getHitPoints());
            }
        }
    }

    private void checkBulletCollisions() {
        for (int i = 0; i < bulletController.getActiveList().size(); i++) {
            Bullet b = bulletController.getActiveList().get(i);
            for (int j = 0; j < asteroidController.getActiveList().size(); j++) {
                Asteroid a = asteroidController.getActiveList().get(j);
                if (a.getHitArea().contains(b.getPosition())) {

                    particleController.setup(
                            b.getPosition().x + MathUtils.random(-4, 4),
                            b.getPosition().y + MathUtils.random(-4, 4),
                            b.getVelocity().x * -0.3f + MathUtils.random(-30, 30),
                            b.getVelocity().y * -0.3f + MathUtils.random(-30, 30),
                            0.2f,
                            2.2f, 1.7f,
                            1.0f, 1.0f, 1.0f, 1.0f,
                            0.0f, 0.0f, 1.0f, 0.0f
                    );

                    b.deactivate(); // Считаем, что одним зарядом можем
                    // убить только один астероид.
//                    if (a.takeDamage(10)) {
                    if (a.takeDamage(b.getDamage())) {
                        hero.addScore(a.getHpMax() * 100);
                    }
                    break;
                }
            }
        }
    }

    private void pickUpItems() {
        for (int i = 0; i < itemsController.getActiveList().size(); i++) {
            Item item = itemsController.getActiveList().get(i);

        // Приближаем предмет к герою, если он в зоне притяжения.
            itemAttractArea.set(item.getPosition(), ITEM_ATTRACT_DISTANCE);
            if (hero.getHitArea().overlaps(itemAttractArea)) {
                float dst = item.getPosition().dst(hero.getPosition());
                tmpVec.set(hero.getPosition()).sub(item.getPosition()).nor();
                item.getPosition().mulAdd(tmpVec, dst / 2);
            }

        // Потребляем предмет
            if (hero.getHitArea().overlaps(item.getHitArea())) {
                item.interact(hero); // Кормим героя предметом.
                item.deactivate(); // Возвращаем предмет в пул.
            }
        }
    }

    public void dispose() {
        background.dispose();
    }
}