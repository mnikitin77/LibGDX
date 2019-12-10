package com.star.app.game.items;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.star.app.game.Consumable;
import com.star.app.screen.utils.Assets;

public class Medkit extends Item {
    private static final int HP_MULTIPLIER = 100;
    private Sound medSound;

    public Medkit(TextureRegion texture) {
        super(texture);
        medSound = Assets.getInstance().
                getAssetManager().get("audio/Consume.mp3");
    }

    @Override
    public void interact(Consumable consumer) {
        medSound.play();
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
