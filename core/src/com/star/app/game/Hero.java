package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class Hero {
    private static final int HERO_HP = 600;
    private static final float REBOUND_COEFFICIENT = 3.0f;

    private GameController gc;
    private TextureRegion texture;
    private Vector2 position;
    private Vector2 velocity;
    private float angle;
    private float enginePower;
    private float fireTimer;
    private int score;
    private int scoreView;
    private int hp;
    private int hpView;
    private Circle hitArea;
    private boolean rightOrLeftSocket;

    public int getScoreView() {
        return scoreView;
    }

    public int getHPView() {
        return hpView;
    }

    public void addScore(int amount) {
        score += amount;
    }

    public int getScore() {
        return score;
    }

    public void takeHP(int amount) {
        hp -= amount;
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

    public Hero(GameController gc) {
        this.gc = gc;
        texture = Assets.getInstance().getAtlas().findRegion("ship");
        position = new Vector2(ScreenManager.SCREEN_WIDTH / 2,
                ScreenManager.SCREEN_HEIGHT / 2);
        velocity = new Vector2(0f, 0f);
        angle = 0.0f;
        enginePower = 750.0f;
        hp = HERO_HP;
        hpView = hp;
        hitArea = new Circle(0f, 0f, texture.getRegionWidth() / 2 * 0.9f);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32,
                64, 64, 1, 1, angle);
    }

    public void update(float dt) {
        if (hpView >= hp) {
            float hpSpeed = (hpView - hp) / 2.0f;
            if (hpSpeed < 200.0f) {
                hpSpeed = 200.0f;
            }
            hpView -= hpSpeed * dt;
            if (hp <= 0) {
                hpView = 0;
            } else if (hpView <= hp) {
                hpView = hp;
            }
        }

        if(hp <= 0) {
            return;
        }

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

        fireTimer += dt;
        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            if (fireTimer > 0.2f) {
                fireTimer = 0.0f;
                float wx = 0.0f, wy = 0.0f;

                rightOrLeftSocket = !rightOrLeftSocket;

                if (rightOrLeftSocket) {
                    wx = position.x + (float) Math.cos(
                            Math.toRadians(angle + 90)) * 25;
                    wy = position.y + (float) Math.sin(
                            Math.toRadians(angle + 90)) * 25;
                } else {
                    wx = position.x + (float) Math.cos(
                            Math.toRadians(angle - 90)) * 25;
                    wy = position.y + (float) Math.sin(
                            Math.toRadians(angle - 90)) * 25;
                }

                gc.getBulletController().setup(wx, wy,
                        (float) Math.cos(Math.toRadians(angle)) * 600 + velocity.x,
                        (float) Math.sin(Math.toRadians(angle)) * 600 + velocity.y,
                        angle);
            }
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

        if (position.x < 0.0f) {
            position.x = 0.0f;
            velocity.x *= -1;
        }
        if (position.x > ScreenManager.SCREEN_WIDTH) {
            position.x = ScreenManager.SCREEN_WIDTH;
            velocity.x *= -1;
        }
        if (position.y < 0.0f) {
            position.y = 0.0f;
            velocity.y *= -1;
        }
        if (position.y > ScreenManager.SCREEN_HEIGHT) {
            position.y = ScreenManager.SCREEN_HEIGHT;
            velocity.y *= -1;
        }

        hitArea.setPosition(position);
    }

    public boolean takeDamage(int amount) {
        hp -= amount;
        if (hp <= 0) {
            return true;
        }
        return false;
    }

    public void rebound(Vector2 objPosition, Vector2 objVelocity,
                        float objRadius, float sizeFactor) {
        float shift = sizeFactor * REBOUND_COEFFICIENT *
                (hitArea.radius + objRadius -
                        position.dst(objPosition)) / 2.0f;

        velocity.x += Math.abs(shift * Math.cos(Math.toRadians(angle)));
        velocity.y += Math.abs(shift * Math.sin(Math.toRadians(angle)));
    }
}
