package com.star.app.game.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.Consumable;
import com.star.app.game.helpers.Poolable;
import com.badlogic.gdx.utils.StringBuilder;

public abstract class Item implements Poolable {
    protected int amount;
    protected Vector2 position;
    protected Vector2 velocity;
    protected Circle hitArea;
    protected Texture texture;
    protected int imgWidth;
    protected int imgHeight;
    protected float scale;
    protected StringBuilder strBuilder;
    protected boolean active;

    public Item(Texture texture) {
        this.texture = texture;
        this.amount = 0;
        position = new Vector2();
        velocity = new Vector2();
        scale = 0f;
        imgWidth = texture.getWidth();
        imgHeight = texture.getHeight();
        strBuilder = new StringBuilder();
        active = false;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Circle getHitArea() {
        return hitArea;
    }

    public Texture getTexture() {
        return texture;
    }

    public abstract void interact(Consumable consumer);

    public abstract void setup();

    @Override
    public boolean isActive() {
        return active;
    }

    public void activate(float x, float y, float vx, float vy, float scale) {
        position.set(x, y);
        velocity.set(vx, vy);
        hitArea.setPosition(position);
        this.scale = scale;
        hitArea.setRadius(imgWidth / 2 * 0.9f);
        active = true;
    }

    public void deactivate() {
        active = false;
    }

    public void render(SpriteBatch batch, BitmapFont font) {
        batch.draw(texture, position.x, position.y, imgWidth, imgHeight);
        strBuilder.clear();
        strBuilder.append("x").append(amount);
        font.draw(batch, strBuilder, position.x, position.y - 5);
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);

//  Пока предметы будут зависать на месте.
//        if (position.x < -imgWidth) {
//            // gone left
//            deactivate();
//        } else if (position.y > ScreenManager.SCREEN_HEIGHT + imgHeight) {
//            // gone top
//            deactivate();
//        } else if (position.x > ScreenManager.SCREEN_WIDTH + imgWidth) {
//            // gone right
//            deactivate();
//        } else if (position.y < -imgHeight) {
//            // gone down
//            deactivate();
//        }

        hitArea.setPosition(position);
    }
}
