package com.star.app.game.items;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.star.app.game.Consumable;
import com.star.app.screen.utils.Assets;

public class GunBulletBox extends Item {
    private static final int BULLET_MULTIPLIER = 100;
    private Sound rechargeSound;

    public GunBulletBox(TextureRegion texture) {
        super(texture);
        rechargeSound = Assets.getInstance().
                getAssetManager().get("audio/RechargeWeapon.mp3");
    }

    @Override
    public void interact(Consumable consumer) {
        rechargeSound.play();
        consumer.rechargeLaser(getAmount());
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
