package com.star.app.game.bodies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.GameController;
import com.star.app.game.helpers.Poolable;
import com.star.app.screen.ScreenManager;

public class Asteroid extends SpaceBody implements Poolable {
    private final static int MAX_WEIGHT = 10;
    private final static int MIN_WEIGHT = 1;
    private final static int LOWEST_SPEED = 120;
    private final static float ITEM_THROW_PROBABILITY = 0.25f;

    private GameController gc;
    private Circle hitArea;
    private float scale;
    private int hpMax;
    private int hp;

    protected TextureRegion texture;
    private int imgWidth;
    private int imgHeight;

    private boolean active;

    public Asteroid(GameController gc, TextureRegion texture, int weight)
            throws IllegalArgumentException {
        if (weight < MIN_WEIGHT || weight > MAX_WEIGHT) {
            throw new IllegalArgumentException(
                    "Asteroid's weight must be in the range between " +
                    MIN_WEIGHT + " and " + MAX_WEIGHT);
        }

        this.gc = gc;
        this.texture = texture;
        this.weight = weight;

        scale = (float)weight / MAX_WEIGHT;
        imgHeight = texture.getRegionHeight();
        imgWidth = texture.getRegionWidth();
        height = MathUtils.round(imgHeight * scale);
        width = MathUtils.round(imgWidth * scale);

        position = new Vector2();
        velocity = new Vector2();
        rotAngle = 0;
        hitArea = new Circle();

        active = false;
    }

    public int getHpMax() {
        return hpMax;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Circle getHitArea() {
        return hitArea;
    }

    public float getScale() {
        return scale;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void activate(float x, float y, float vx, float vy) {
        Vector2[] initialParams = initialize();
        activate(initialParams[0].x, initialParams[0].y,
                initialParams[1].x, initialParams[1].y, scale);
    }

    public void activate(float x, float y, float vx, float vy, float scale) {
        position.set(x, y);
        velocity.set(vx, vy);
        hpMax = (int)(10 * this.scale);
        hp = this.hpMax;
        hitArea.setPosition(position);
        this.scale = scale;
        hitArea.setRadius(width / 2 * 0.9f);

        active = true;
    }

    public void deactivate() {
        active = false;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - imgWidth / 2,
                position.y - imgHeight / 2, imgWidth / 2, imgHeight / 2,
                imgWidth, imgHeight, scale, scale, rotAngle);
    }

    @Override
    public void update(float dt) {
        position.mulAdd(velocity, dt);

        if (position.x < -width) {
            // gone left
            position.x = ScreenManager.SCREEN_WIDTH + width / 2;
        } else if (position.y > ScreenManager.SCREEN_HEIGHT + height) {
            // gone top
            position.y = -height / 2;
        } else if (position.x > ScreenManager.SCREEN_WIDTH + width) {
            // gone right
            position.x = -width / 2;
        } else if (position.y < -height) {
            // gone down
            position.y = ScreenManager.SCREEN_HEIGHT + height / 2;
        }

        rotAngle = (rotAngle - 2.5f * (0.1f + scale -1)) % 360;
        hitArea.setPosition(position);
    }

    public boolean takeDamage(int amount) {
        hp -= amount;
        if (hp <= 0) {
            deactivate();
            if (scale >= 0.5f) {
                for (int i = 0; i < 3; i++) {
                    gc.getAsteroidController().setup(position.x, position.y,
                            MathUtils.random(-150.0f, 150.0f),
                            MathUtils.random(-150.0f, 150.0f),
                            scale - 0.2f);
                }
            } else { // выкидываем предмет с вероятностью 10%
                if (MathUtils.random() <= ITEM_THROW_PROBABILITY) {
                    gc.getItemsController().setup(
                            position.x, position.y, 0f, 0f, 1.0f);
                }
            }
            return true;
        }
        return false;
    }

    private Vector2[] initialize() {
        float dirAngle = 0f;
        Vector2[] res = new Vector2[2];
        for (int i = 0; i < res.length; i++) {
            res[i] = new Vector2();
        }

        switch (MathUtils.random(0, 3)) {
            case 0: // LEFT
                res[0].x = -width / 2;
                res[0].y = MathUtils.random(0, ScreenManager.SCREEN_HEIGHT);
                dirAngle = (float)MathUtils.random(-70, 70);
                break;
            case 1: // TOP
                res[0].x = MathUtils.random(0, ScreenManager.SCREEN_WIDTH);
                res[0].y = ScreenManager.SCREEN_HEIGHT + height / 2;
                dirAngle = (float)MathUtils.random(200, 340);
                break;
            case 2: // RIGHT
                res[0].x = ScreenManager.SCREEN_WIDTH + width / 2;
                res[0].y = MathUtils.random(0, ScreenManager.SCREEN_HEIGHT);
                dirAngle = (float)MathUtils.random(110, 250);
                break;
            case 3: // BOTTOM
                res[0].x = MathUtils.random(0, ScreenManager.SCREEN_WIDTH);
                res[0].y = -height / 2;
                dirAngle = (float)MathUtils.random(20, 160);
                break;
            default:
                break;
        }

        res[1].set((float) Math.cos(Math.toRadians(dirAngle)) *
                        LOWEST_SPEED / scale,
                (float) Math.sin(Math.toRadians(dirAngle)) *
                        LOWEST_SPEED / scale);

        return res;
    }
}
