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
        this.asteroidController = new AsteroidController(this);
    }

    public void update(float dt) {
        background.update(dt);
        hero.update(dt);
        bulletController.update(dt);
        asteroidController.update(dt);
        checkCollisions();
    }

    public void checkCollisions() {
        // Столкновение корабля с астероидом
        for (int i = 0; i < asteroidController.getActiveList().size(); i++) {
            Asteroid a = asteroidController.getActiveList().get(i);
            if (hero.getHitArea().overlaps(a.getHitArea())) {
                a.rebound(hero.getPosition(), hero.getVelocity(),
                        hero.getHitArea().radius);
                hero.rebound(a.getPosition(), a.getVelocity(),
                        a.getHitArea().radius, a.getScale());
                hero.takeDamage(a.getWeight() * 20);
                a.deactivate();
            }
        }

        // Попадание боеприпасом в астероидом
        for (int i = 0; i < bulletController.getActiveList().size(); i++) {
            Bullet b = bulletController.getActiveList().get(i);
            for (int j = 0; j < asteroidController.getActiveList().size(); j++) {
                Asteroid a = asteroidController.getActiveList().get(j);
                if (a.getHitArea().contains(b.getPosition())) {
                    b.deactivate(); // Считаем, что одним зарядом можем
                                    // убить только один астероид.
                    if (a.takeDamage(1)) {
                        hero.addScore(a.getHpMax() * 100);
                    }
                    break;
                }
            }
        }
    }
}