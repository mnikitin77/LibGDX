package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.StringBuilder;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;
import com.star.app.screen.utils.OptionsUtils;

public class Hero implements Consumable{
    private static final int HERO_HP_MAX = 1000;
    private static final int HERO_AMMO_MAX = 300;

    private GameController gc;
    private TextureRegion texture;
    private KeysControl keysControl;
    private Vector2 position;
    private Vector2 velocity;
    private float angle;
    private float enginePower;
    private float fireTimer;
    private int score;
    private int scoreView;
    private int hp;
    private int hpView;
    private int money;
    private int moneyView;
    private Circle hitArea;
    private StringBuilder strBuilder;
    private Weapon currentWeapon;
    private int levelFinishedTimer;


    public int getMoney() {
        return money;
    }

    public void addScore(int amount) {
        score += amount;
    }

    public int getScore() {
        return score;
    }

    public int getHp() {
        return hp;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Circle getHitArea() {
        return hitArea;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public float getAngle() {
        return angle;
    }

    public Hero(GameController gc, String keysControlPrefix) {
        this.gc = gc;
        texture = Assets.getInstance().getAtlas().findRegion("ship");
        position = new Vector2(ScreenManager.SCREEN_WIDTH / 2,
                ScreenManager.SCREEN_HEIGHT / 2);
        velocity = new Vector2(0f, 0f);
        angle = 0.0f;
        enginePower = 750.0f;
        hp = HERO_HP_MAX;
        hpView = hp;
        hitArea = new Circle(0f, 0f, texture.getRegionWidth() / 2 * 0.9f);
        strBuilder = new StringBuilder();
        money = 0;
        levelFinishedTimer = 0;

        keysControl = new KeysControl(OptionsUtils.loadProperties(), keysControlPrefix);

        currentWeapon = new Weapon(
                gc, this, "Laser", 0.2f,
                1, 500.0f, HERO_AMMO_MAX,
                new Vector3[]{
                        new Vector3(24, 90, 0),
                        new Vector3(24, -90, 0)
                }
        );
    }

    public void initialize() {
        hp = HERO_HP_MAX;
        hpView = hp;
        currentWeapon.recharge(HERO_AMMO_MAX);
        position.set(ScreenManager.SCREEN_WIDTH / 2,
                ScreenManager.SCREEN_HEIGHT / 2);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32,
                64, 64, 1, 1, angle);
    }

    public void renderGUI(SpriteBatch batch, BitmapFont... font) {
        strBuilder.clear();
        strBuilder.append("LEVEL: ").append(gc.getLevel()).append("\n");
        strBuilder.append("SCORE: ").append(scoreView).append("\n");
        strBuilder.append("COINS: ").append(moneyView).append("\n");
        strBuilder.append("HP: ").append(hpView).append("\n");
        strBuilder.append("BULLETS: ").
                append(currentWeapon.getCurBullets()).append(" / ").
                append(currentWeapon.getMaxBullets()).append("\n");
        font[0].draw(batch, strBuilder, 20, ScreenManager.SCREEN_HEIGHT - 20);

        if (gc.isLevelFinished() &&
                levelFinishedTimer <= gc.LEVEL_FINISHED_DELAY) {
            strBuilder.clear();
            strBuilder.append("Level ").
                    append(gc.getLevel()).append(" finished.\n");
            font[1].draw(batch, strBuilder, 0,
                    ScreenManager.HALF_SCREEN_HEIGHT,
                    ScreenManager.SCREEN_WIDTH,
                    Align.center, false);
        }
    }

    public void update(float dt) {
        updateHP(dt);
        if(hp <= 0) {
            hp = 0;
            return;
        }
        updateScore(dt);
        updateMoney(dt);

        if (gc.isLevelFinished()) {
            // !!! НЕ СМОГ СДЕЛАТЬ ЧЕРЕЗ dt !!!
            levelFinishedTimer += 1;
            if (levelFinishedTimer > gc.LEVEL_FINISHED_DELAY) {
                System.out.println(levelFinishedTimer);
                // Даём команду на переход на следующий уровень.
                gc.levelUp();
                levelFinishedTimer = 0;
            }
        } else {
            fireTimer += dt;
            if (Gdx.input.isKeyPressed(Input.Keys.P)) {
                tryToFire();
            }

            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                angle += 180.0f * dt;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                angle -= 180.0f * dt;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                velocity.x += (float) Math.cos(Math.toRadians(angle)) *
                        enginePower * dt;
                velocity.y += (float) Math.sin(Math.toRadians(angle)) *
                        enginePower * dt;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                velocity.x -= (float) Math.cos(Math.toRadians(angle)) *
                        enginePower * dt;
                velocity.y -= (float) Math.sin(Math.toRadians(angle)) *
                        enginePower * dt;
            }

            position.mulAdd(velocity, dt);

            float stopKoef = 1.0f - 2.0f * dt;
            if (stopKoef < 0.0f) {
                stopKoef = 0.0f;
            }
            velocity.scl(stopKoef);
        }

        exhaust();
        checkSpaceBorders();

        hitArea.setPosition(position);
    }

    public void tryToFire() {
        if (fireTimer > currentWeapon.getFirePeriod()) {
            fireTimer = 0.0f;
            currentWeapon.fire();
        }
    }

    public boolean takeDamage(int amount) {
        hp -= amount;
        if (hp <= 0) {
            hp = 0;
            return true;
        }
        return false;
    }

    @Override
    public void rechargeWeapon(int amount) {
        currentWeapon.recharge(amount);
    }

    @Override
    public void takeMoney(int amount) {
        money += amount;
    }

    @Override
    public void heal(int amount) {
        hp += amount;
        if (hp > HERO_HP_MAX) {
            hp = HERO_HP_MAX;
        }
    }

    private void checkSpaceBorders() {
        if (position.x < hitArea.radius) {
            position.x = hitArea.radius;
            velocity.x *= -1;
        }
        if (position.x > ScreenManager.SCREEN_WIDTH - hitArea.radius) {
            position.x = ScreenManager.SCREEN_WIDTH - hitArea.radius;
            velocity.x *= -1;
        }
        if (position.y < hitArea.radius) {
            position.y = hitArea.radius;
            velocity.y *= -1;
        }
        if (position.y > ScreenManager.SCREEN_HEIGHT - hitArea.radius) {
            position.y = ScreenManager.SCREEN_HEIGHT - hitArea.radius;
            velocity.y *= -1;
        }
    }

    private void updateScore(float dt) {
        if (scoreView < score) {
            float scoreSpeed = (score - scoreView) / 2.0f;
            if (scoreSpeed < 2000.0f) {
                scoreSpeed = 2000.0f;
            }
            scoreView += scoreSpeed * dt;
            if (scoreView > score) {
                scoreView = score;
            }
        }
    }

    private void updateHP(float dt) {
        // ПОХВАЛИТЬСЯ
        if (hpView != hp) {
            float hpSpeed = (hpView - hp) / 2.0f;
            if (Math.abs(hpSpeed) < 500.0f) {
                hpSpeed = hpSpeed / Math.abs(hpSpeed) * 500.0f;
            }
            hpView -= hpSpeed * dt;
        }
        if (hpView > HERO_HP_MAX || hpView < 0) {
            hpView = hp;
        }
    }

    private void updateMoney(float dt) {
        if (moneyView < money) {
            float moneySpeed = (money - moneyView) / 2.0f;
            if (moneySpeed < 100.0f) {
                moneySpeed = 100.0f;
            }
            moneyView += moneySpeed * dt;
            if (moneyView > money) {
                moneyView = money;
            }
        }
    }

    private void exhaust() {
        if (velocity.len() > 50.0f) {
            float bx, by;
            bx = position.x - 28.0f * (float) Math.cos(Math.toRadians(angle));
            by = position.y - 28.0f * (float) Math.sin(Math.toRadians(angle));
            for (int i = 0; i < 2; i++) {
                gc.getParticleController().setup(
                        bx + MathUtils.random(-4, 4),
                        by + MathUtils.random(-4, 4),
                        velocity.x * -0.3f + MathUtils.random(-20, 20),
                        velocity.y * -0.3f + MathUtils.random(-20, 20),
                        0.5f,
                        1.2f, 0.2f,
                        1.0f, 0.5f, 0.0f, 1.0f,
                        1.0f, 1.0f, 1.0f, 0.0f
                );
            }
        }
    }
}
