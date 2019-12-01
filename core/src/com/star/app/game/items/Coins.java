package com.star.app.game.items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.star.app.game.Consumable;

public class Coins extends Item{
    private static final int COINS_MULTIPLIER = 5;

    public Coins(TextureRegion texture) {
        super(texture);
    }

    @Override
    public void interact(Consumable consumer) {
        consumer.takeMoney(getAmount());
    }

    @Override
    public void setup() {
        super.setAmount(MathUtils.random(1, 20) * COINS_MULTIPLIER);
    }

    @Override
    public void activate(float x, float y, float vx, float vy, float scale) {
        setup();
        super.activate(x, y, vx, vy, scale);
    }
}
