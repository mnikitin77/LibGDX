package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.Poolable;
import com.star.app.screen.ScreenManager;

public class Bullet implements Poolable {
    private Vector2 position;
    private Vector2 velocity;
    private boolean active;
    private float angle;
    private int damage;
    private float lifetimeDistance;
    private TextureRegion textureRegion;


    public Vector2 getPosition() {
        return position;
    }

    public float getAngle() {
        return angle;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public int getDamage() {
        return damage;
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
    }

    public Bullet() {
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.active = false;
    }

    public void activate(float x, float y, float vx, float vy, int damage,
                         float angle, float lifetimeDistance,
                         TextureRegion textureRegion) {
        this.position.set(x, y);
        this.velocity.set(vx, vy);
        this.active = true;
        this.angle = angle;
        this.damage = damage;
        this.lifetimeDistance = lifetimeDistance;
        this.textureRegion = textureRegion;
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        lifetimeDistance -= velocity.len() * dt;
        if (lifetimeDistance < 0.0f || position.x < 0.0f || position.x > ScreenManager.SCREEN_WIDTH ||
                position.y < 0.0f || position.y > ScreenManager.SCREEN_HEIGHT) {
            deactivate();
        }
    }
}