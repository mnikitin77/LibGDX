package com.star.app.game.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.star.app.game.Consumable;

public class Medkit extends Item {
    private static final int HP_MULTIPLIER = 100;

    public Medkit(Texture texture) {
        super(texture);
    }

    @Override
    public void interact(Consumable consumer) {
        consumer.heal(getAmount());
    }

    @Override
    public void setup() {
        super.setAmount(MathUtils.random(1, 5) * HP_MULTIPLIER);
    }

    @Override
    public void activate(float x, float y, float vx, float vy, float scale) {
        setup();
        super.activate(x, y, vx, vy, scale);
    }
}
