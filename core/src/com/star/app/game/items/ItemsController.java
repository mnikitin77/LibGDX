package com.star.app.game.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.star.app.game.GameController;
import com.star.app.game.helpers.ObjectPool;

public class ItemsController  extends ObjectPool<Item> {
    private GameController gc;
//    private static final int ITEMS_COUNT = 10;
    protected Texture medkitTexture;
    protected Texture coinsTexture;
    protected Texture bulletBoxTexture;

    @Override
    protected Item newObject() {
        return getItem();
    }

    public ItemsController(GameController gc) {
        this.gc = gc;
        medkitTexture =  new Texture("images/medical-pack-alt-white.png");
        coinsTexture =  new Texture("images/two-coins.png");
        bulletBoxTexture =  new Texture("images/heavy-bullets.png");
    }

    public void setup(float x, float y, float vx, float vy, float scale) {
        getActiveElement().activate(x, y, vx, vy, scale);
    }

    public void render(SpriteBatch batch, BitmapFont font) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).render(batch, font);
        }
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }

    private Item getItem() {
        switch(MathUtils.random(0, 2)) {
            case 0:
                return new Medkit(medkitTexture);
            case 1:
                return new Coins(coinsTexture);
            case 2:
                return new BulletBox(bulletBoxTexture);
            default:
                return null;
        }
    }
}
