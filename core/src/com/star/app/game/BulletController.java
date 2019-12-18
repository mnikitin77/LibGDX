package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.star.app.game.helpers.ObjectPool;
import com.star.app.screen.utils.Assets;

public class BulletController extends ObjectPool<Bullet> {
    private TextureRegion textureLaser;
    private TextureRegion textureGun;
    private TextureRegion tmpTexture;
    private GameController gc;

    @Override
    protected Bullet newObject() {
        return new Bullet();
    }

    public BulletController(GameController gc) {
        textureLaser = Assets.getInstance().
                getAtlas().findRegion("bullet32");
        textureGun = Assets.getInstance().
                getAtlas().findRegion("bullet");
        this.gc = gc;
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            Bullet b = activeList.get(i);
            batch.draw(b.getTextureRegion(), b.getPosition().x - 32,
                    b.getPosition().y - 16, 32, 16, 64,
                    32, 1, 1, b.getAngle());
        }
    }

    public void setup(float x, float y, float vx, float vy, int damage,
                      float angle, float lifetimeDistance) {
        switch (gc.getHero().getCurrentWeapon().getType()) {
            case LASER:
                tmpTexture = textureLaser;
                break;
            case GUN:
                tmpTexture = textureGun;
                break;
            default:
                break;
        }
        getActiveElement().activate(x, y, vx, vy, damage, angle,
                lifetimeDistance, tmpTexture);
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }
}
