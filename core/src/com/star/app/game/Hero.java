package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.StringBuilder;
import com.star.app.game.helpers.Counter;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;
import com.star.app.screen.utils.OptionsUtils;

public class Hero implements Consumable{
    private static final int HERO_HP_MAX = 1000;
    //private static final int HERO_AMMO_MAX = 300;
    private static final int AMMO_LASER_MAX = 200;
    private static final int AMMO_GUN_MAX = 15;

    private GameController gc;
    private TextureRegion texture;
    private KeysControl keysControl;
    private Vector2 position;
    private Vector2 velocity;
    private float angle;
    private float enginePower;
    private float fireTimer;
    private int score;
    private int scoreView;
    private Counter hp;
    private int hpView;
    private int money;
    private int moneyView;
    private Circle hitArea;
    private StringBuilder strBuilder;
    private Weapon currentWeapon;
    private Weapon[] weapons;
    private Sound collisionSound;


    public int getMoney() {
        return money;
    }

    public void addScore(int amount) {
        score += amount;
    }

    public int getScore() {
        return score;
    }

    public int getHp() {
        return hp.getCurrent();
    }

    public Vector2 getPosition() {
        return position;
    }

    public Circle getHitArea() {
        return hitArea;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public float getAngle() {
        return angle;
    }

    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    public Hero(GameController gc, String keysControlPrefix) {
        this.gc = gc;
        texture = Assets.getInstance().getAtlas().findRegion("ship");
        position = new Vector2(ScreenManager.SCREEN_WIDTH / 2,
                ScreenManager.SCREEN_HEIGHT / 2);
        velocity = new Vector2(0f, 0f);
        angle = 0.0f;
        enginePower = 750.0f;
        hp = new Counter(HERO_HP_MAX, true);
        hpView = hp.getCurrent();
        hitArea = new Circle(0f, 0f, texture.getRegionWidth() / 2 * 0.9f);
        strBuilder = new StringBuilder();
        money = 0;

        keysControl = new KeysControl(OptionsUtils.loadProperties(), keysControlPrefix);

        weapons = new Weapon[2];

        weapons[WeaponType.LASER.getIndex()] = new Weapon(
                gc, this, "Laser", 0.2f,
                10, 800f,500.0f, AMMO_LASER_MAX, WeaponType.LASER,
                Assets.getInstance().getAssetManager().get("audio/Shoot.mp3"),
                Assets.getInstance().getAtlas().findRegion("laser-turret"),
                new Vector3[]{
                        new Vector3(24, 90, 0),
                        new Vector3(24, -90, 0)});

        weapons[WeaponType.GUN.getIndex()] = new Weapon(
                gc, this, "Gun", 0.2f,
                50, 1200f,400.0f, AMMO_GUN_MAX, WeaponType.GUN,
                Assets.getInstance().getAssetManager().get("audio/Cannon.mp3"),
                Assets.getInstance().getAtlas().findRegion("turret"),
                new Vector3[]{
                        new Vector3(24, 0, 0)});

        currentWeapon = weapons[0];

        collisionSound = Assets.getInstance().
                getAssetManager().get("audio/Collision.mp3");
    }

    public void initialize() {
        hp.fill();
        hpView = hp.getCurrent();
        weapons[WeaponType.LASER.getIndex()].recharge(AMMO_LASER_MAX);
        weapons[WeaponType.GUN.getIndex()].recharge(AMMO_GUN_MAX);
        currentWeapon = weapons[0];
        position.set(ScreenManager.SCREEN_WIDTH / 2,
                ScreenManager.SCREEN_HEIGHT / 2);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32,
                64, 64, 1, 1, angle);
    }

    public void renderGUI(SpriteBatch batch, BitmapFont... font) {
        strBuilder.clear();
        strBuilder.append("LEVEL: ").append(gc.getLevel()).append("\n");
        strBuilder.append("SCORE: ").append(scoreView).append("\n");
        strBuilder.append("COINS: ").append(moneyView).append("\n");
        strBuilder.append("HP: ").append(hpView).append("\n");
        strBuilder.append("BULLETS: ").
                append(currentWeapon.getCurBullets()).append(" / ").
                append(currentWeapon.getMaxBullets()).append("\n");
        font[0].draw(batch, strBuilder, 20, ScreenManager.SCREEN_HEIGHT - 20);
        batch.draw(currentWeapon.getTextureRegion(),
                ScreenManager.HALF_SCREEN_WIDTH - 48,
                ScreenManager.SCREEN_HEIGHT - 116);
    }

    public void update(float dt) {
        updateHP(dt);
        // Проверяем, что живой
        if (!hp.isAboveZero()) {
            hp.setCurrent(0);
            return;
        }

        currentWeapon.update(dt);

        updateScore(dt);
        updateMoney(dt);

        fireTimer += dt;
        if (Gdx.input.isKeyPressed(keysControl.fire)) {
            currentWeapon.fire();
        }

        if (Gdx.input.isKeyPressed(keysControl.left)) {
            angle += 180.0f * dt;
        }
        if (Gdx.input.isKeyPressed(keysControl.right)) {
            angle -= 180.0f * dt;
        }
        if (Gdx.input.isKeyPressed(keysControl.forward)) {
            velocity.x += (float) Math.cos(Math.toRadians(angle)) *
                    enginePower * dt;
            velocity.y += (float) Math.sin(Math.toRadians(angle)) *
                    enginePower * dt;
        }
        if (Gdx.input.isKeyPressed(keysControl.backward)) {
            velocity.x -= (float) Math.cos(Math.toRadians(angle)) *
                    enginePower * dt;
            velocity.y -= (float) Math.sin(Math.toRadians(angle)) *
                    enginePower * dt;
        }

        if (Gdx.input.isKeyPressed(keysControl.laser)) {
            currentWeapon = weapons[WeaponType.LASER.getIndex()];
        }
        if (Gdx.input.isKeyPressed(keysControl.gun)) {
            currentWeapon = weapons[WeaponType.GUN.getIndex()];
        }
        if (Gdx.input.isKeyPressed(keysControl.prevWeapon)) {
            int index = currentWeapon.getType().getIndex() == 0 ?
                    weapons.length - 1 :
                    currentWeapon.getType().getIndex() - 1;
            currentWeapon = weapons[index];
        }
        if (Gdx.input.isKeyPressed(keysControl.nextWeapon)) {
            int index =
                    (currentWeapon.getType().getIndex() + 1) % weapons.length;
            currentWeapon = weapons[index];
        }

        position.mulAdd(velocity, dt);

        float stopKoef = 1.0f - 2.0f * dt;
        if (stopKoef < 0.0f) {
            stopKoef = 0.0f;
        }
        velocity.scl(stopKoef);

        exhaust();
        checkSpaceBorders();

        hitArea.setPosition(position);
    }

    public boolean takeDamage(int amount) {
        collisionSound.play();
        hp.decrease(amount);
        if(!hp.isAboveZero()) {
            hp.setCurrent(0);
            return true;
        }
        return false;
    }

    @Override
    public void rechargeLaser(int amount) {
        strBuilder.clear();
        strBuilder.append("am +" + amount);
        gc.getInfoController().setup(position.x,
                position.y + 20, strBuilder.toString(), Color.WHITE);
        weapons[WeaponType.LASER.getIndex()].recharge(amount);
    }

    @Override
    public void rechargeGun(int amount) {
        strBuilder.clear();
        strBuilder.append("am +" + amount);
        gc.getInfoController().setup(position.x,
                position.y + 20, strBuilder.toString(), Color.WHITE);
        weapons[WeaponType.GUN.getIndex()].recharge(amount);
    }

    @Override
    public void takeMoney(int amount) {
        strBuilder.clear();
        strBuilder.append("mo +" + amount);
        gc.getInfoController().setup(position.x,
                position.y + 20, strBuilder.toString(), Color.GOLD);
        money += amount;
    }

    @Override
    public void heal(int amount) {
        strBuilder.clear();
        strBuilder.append("hp +" + amount);
        gc.getInfoController().setup(position.x,
                position.y + 20, strBuilder.toString(), Color.GREEN);
        hp.increase(amount);
    }

    private void checkSpaceBorders() {
        if (position.x < hitArea.radius) {
            position.x = hitArea.radius;
            velocity.x *= -1;
        }
        if (position.x > ScreenManager.SCREEN_WIDTH - hitArea.radius) {
            position.x = ScreenManager.SCREEN_WIDTH - hitArea.radius;
            velocity.x *= -1;
        }
        if (position.y < hitArea.radius) {
            position.y = hitArea.radius;
            velocity.y *= -1;
        }
        if (position.y > ScreenManager.SCREEN_HEIGHT - hitArea.radius) {
            position.y = ScreenManager.SCREEN_HEIGHT - hitArea.radius;
            velocity.y *= -1;
        }
    }

    private void updateScore(float dt) {
        if (scoreView < score) {
            float scoreSpeed = (score - scoreView) / 2.0f;
            if (scoreSpeed < 2000.0f) {
                scoreSpeed = 2000.0f;
            }
            scoreView += scoreSpeed * dt;
            if (scoreView > score) {
                scoreView = score;
            }
        }
    }

    private void updateHP(float dt) {
        if (hpView != hp.getCurrent()) {
            float hpSpeed = (hpView - hp.getCurrent()) / 2.0f;
            if (Math.abs(hpSpeed) < 500.0f) {
                hpSpeed = Math.signum(hpSpeed) * 500.0f;
            }
            hpView -= hpSpeed * dt;
        }
        if (hpView > HERO_HP_MAX || hpView < 0) {
            hpView = hp.getCurrent();
        }
    }

    private void updateMoney(float dt) {
        if (moneyView < money) {
            float moneySpeed = (money - moneyView) / 2.0f;
            if (moneySpeed < 100.0f) {
                moneySpeed = 100.0f;
            }
            moneyView += moneySpeed * dt;
            if (moneyView > money) {
                moneyView = money;
            }
        }
    }

    private void exhaust() {
        if (velocity.len() > 50.0f) {
            float bx, by;
            bx = position.x - 28.0f * (float) Math.cos(Math.toRadians(angle));
            by = position.y - 28.0f * (float) Math.sin(Math.toRadians(angle));
            for (int i = 0; i < 2; i++) {
                gc.getParticleController().setup(
                        bx + MathUtils.random(-4, 4),
                        by + MathUtils.random(-4, 4),
                        velocity.x * -0.3f + MathUtils.random(-20, 20),
                        velocity.y * -0.3f + MathUtils.random(-20, 20),
                        0.5f,
                        1.2f, 0.2f,
                        1.0f, 0.5f, 0.0f, 1.0f,
                        1.0f, 1.0f, 1.0f, 0.0f
                );
            }
        }
    }
}
