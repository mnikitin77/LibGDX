package com.star.app.game.bodies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class SpaceBody {
    protected Vector2 position;
    protected Vector2 velocity;
    protected float rotAngle;
    protected int height;
    protected int width;
    protected float weight;
    protected int hitPoints;

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public float getWeight() {
        return weight;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public abstract void render(SpriteBatch batch);

    public abstract void update(float dt);
}
