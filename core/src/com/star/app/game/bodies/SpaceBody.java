package com.star.app.game.bodies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class SpaceBody {
    protected Texture texture;
    protected Vector2 position;
    protected Vector2 velocity;
    protected float rotAngle;
    protected float rotSpeed;
    protected int height;
    protected int width;
    protected int weight;

    public abstract void render(SpriteBatch batch);

    public abstract void update(float dt);
}
