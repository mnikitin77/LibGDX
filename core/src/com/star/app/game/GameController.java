package com.star.app.game;

import com.star.app.game.bodies.Asteroid;
import com.star.app.game.bodies.AsteroidController;

public class GameController {
    private Background background;
    private BulletController bulletController;
    private AsteroidController asteroidController;
    private Hero hero;

    public BulletController getBulletController() {
        return bulletController;
    }

    public AsteroidController getAsteroidController() {
        return asteroidController;
    }

    public Background getBackground() {
        return background;
    }

    public Hero getHero() {
        return hero;
    }

    public GameController() {
        this.background = new Background(this);
        this.hero = new Hero(this);
        this.bulletController = new BulletController();
        this.asteroidController = new AsteroidController();
    }

    public void update(float dt) {
        background.update(dt);
        hero.update(dt);
        bulletController.update(dt);
        asteroidController.update(dt);
        checkCollisions();
    }

    // Заготовка под столкновение с астероидами (для ДЗ)
    public void checkCollisions() {
        for (int i = 0; i < bulletController.getActiveList().size(); i++) {
            Bullet b = bulletController.getActiveList().get(i);
            if (hero.getPosition().dst(b.getPosition()) < 32.0f) { // 32.0f - примерно радиус корабля
                // b.deactivate();
                // считаем что столкнулись
            }
        }

        for (int i = 0; i < bulletController.getActiveList().size(); i++) {
            Bullet b = bulletController.getActiveList().get(i);
            for (int j = 0; j < asteroidController.getActiveList().size(); j++) {
                Asteroid a = asteroidController.getActiveList().get(j);
                if (a.isHit(b.getPosition())) {
                    a.deactivate();
                    b.deactivate(); // Считаем, что одним зарядом можем
                                    // убить только один астероид.
                    break;
                }
            }
        }
    }
}