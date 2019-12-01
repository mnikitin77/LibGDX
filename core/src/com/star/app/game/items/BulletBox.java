package com.star.app.game.items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.star.app.game.Consumable;

public class BulletBox extends Item {
    private static final int BULLET_MULTIPLIER = 100;

    public BulletBox(TextureRegion texture) {
        super(texture);
    }

    @Override
    public void interact(Consumable consumer) {
        consumer.rechargeWeapon(getAmount());
    }

    @Override
    public void setup() {
        super.setAmount(MathUtils.random(1, 3) * BULLET_MULTIPLIER);
    }

    @Override
    public void activate(float x, float y, float vx, float vy, float scale) {
        setup();
        super.activate(x, y, vx, vy, scale);
    }
}
