package com.star.app.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.bodies.Asteroid;
import com.star.app.game.bodies.AsteroidController;
import com.star.app.game.items.Item;
import com.star.app.game.items.ItemsController;
import com.star.app.screen.ScreenManager;

public class GameController {
    private Background background;
    private BulletController bulletController;
    private AsteroidController asteroidController;
    private ParticleController particleController;
    private ItemsController itemsController;
    private Hero hero;
    private Vector2 tmpVec;
    private boolean isGameOver;

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

    public void gameOver() {
        isGameOver = true;
    }

    public GameController() {
        background = new Background(this);
        hero = new Hero(this);
        bulletController = new BulletController();
        asteroidController = new AsteroidController(this);
        particleController = new ParticleController();
        itemsController = new ItemsController(this);
        tmpVec = new Vector2(0.0f, 0.0f);
        isGameOver = false;
    }

    public void update(float dt) {
        background.update(dt);
        hero.update(dt);
        bulletController.update(dt);
        asteroidController.update(dt);
        itemsController.update(dt);
        particleController.update(dt);
        if (hero.getHp() > 0 && !isGameOver) {
            checkCollisions();
        }
    }

    public void checkCollisions() {
        // Столкновение корабля с астероидом
        for (int i = 0; i < asteroidController.getActiveList().size(); i++) {
            Asteroid a = asteroidController.getActiveList().get(i);
            if (hero.getHitArea().overlaps(a.getHitArea())) {
                float dst = a.getPosition().dst(hero.getPosition());
                float halfOverLen = (a.getHitArea().radius + hero.getHitArea().radius - dst) / 2.0f;
                tmpVec.set(hero.getPosition()).sub(a.getPosition()).nor();
                hero.getPosition().mulAdd(tmpVec, halfOverLen);
                a.getPosition().mulAdd(tmpVec, -halfOverLen);

                float sumScl = hero.getHitArea().radius * 2 + a.getHitArea().radius;

                hero.getVelocity().mulAdd(tmpVec, 400.0f * halfOverLen * a.getHitArea().radius / sumScl);
                a.getVelocity().mulAdd(tmpVec, 400.0f * -halfOverLen  * hero.getHitArea().radius / sumScl);

                if(a.takeDamage(2)) {
                    hero.addScore(a.getHpMax() * 10);
                }
                hero.takeDamage(a.getWeight() * 20);
            }
        }

        // Попадание боеприпасом в астероид
        for (int i = 0; i < bulletController.getActiveList().size(); i++) {
            Bullet b = bulletController.getActiveList().get(i);
            for (int j = 0; j < asteroidController.getActiveList().size(); j++) {
                Asteroid a = asteroidController.getActiveList().get(j);
                if (a.getHitArea().contains(b.getPosition())) {

                    particleController.setup(
                            b.getPosition().x + MathUtils.random(-4, 4), b.getPosition().y + MathUtils.random(-4, 4),
                            b.getVelocity().x * -0.3f + MathUtils.random(-30, 30), b.getVelocity().y * -0.3f + MathUtils.random(-30, 30),
                            0.2f,
                            2.2f, 1.7f,
                            1.0f, 1.0f, 1.0f, 1.0f,
                            0.0f, 0.0f, 1.0f, 0.0f
                    );

                    b.deactivate(); // Считаем, что одним зарядом можем
                                    // убить только один астероид.
                    if (a.takeDamage(1)) {
                        hero.addScore(a.getHpMax() * 100);
                    }
                    break;
                }
            }
        }

        // Собираем предметы
        for (int i = 0; i < itemsController.getActiveList().size(); i++) {
            Item item = itemsController.getActiveList().get(i);
            if (hero.getHitArea().overlaps(item.getHitArea())) {
                item.interact(hero); // Кормим героя преметом.
                item.deactivate(); // Возвращаем предмет в пул.
            }
        }
    }
}