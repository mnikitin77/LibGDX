package com.star.app.game.items;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.star.app.game.Consumable;
import com.star.app.screen.utils.Assets;

public class Coins extends Item{
    private static final int COINS_MULTIPLIER = 5;
    private Sound moneySound;

    public Coins(TextureRegion texture) {
        super(texture);
        moneySound = Assets.getInstance().
                getAssetManager().get("audio/Money.mp3");
    }

    @Override
    public void interact(Consumable consumer) {
        moneySound.play();
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
