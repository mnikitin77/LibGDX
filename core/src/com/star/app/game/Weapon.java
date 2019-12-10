package com.star.app.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.star.app.screen.utils.Assets;

public class Weapon {

    private GameController gc;
    private Hero hero;
    private String title;
    private float firePeriod;
    private int damage;
    private float bulletSpeed;
    private int maxBullets;
    private int curBullets;
    float distance;
    private float fireTimer;
    private Sound shootSound;

    // Когда мы описываем слот Vector3[] slots:
    //   x - это то на сколько пикселей он смещен относительно центра
    //   y - угол смещения относильно центра корабля
    //   z - угол смещения вылета пуль относительно направления корабля
    private Vector3[] slots;


    public float getFirePeriod() {
        return firePeriod;
    }

    public int getMaxBullets() {
        return maxBullets;
    }

    public int getCurBullets() {
        return curBullets;
    }

    public Weapon(GameController gc, Hero hero, String title, float firePeriod, int damage, float distance, float bulletSpeed, int maxBullets, Vector3[] slots) {
        this.gc = gc;
        this.hero = hero;
        this.title = title;
        this.firePeriod = firePeriod;
        this.damage = damage;
        this.bulletSpeed = bulletSpeed;
        this.maxBullets = maxBullets;
        curBullets = this.maxBullets;
        this.slots = slots;
        this.distance = distance;
        shootSound = Assets.getInstance().getAssetManager().get("audio/Shoot.mp3");
    }

    public void update(float dt) {
        fireTimer += dt;
    }

    public void fire() {
        if (fireTimer > firePeriod && (curBullets > 0 || maxBullets == -1)) {
            fireTimer = 0;
            curBullets--;
            shootSound.play();

            for (int i = 0; i < slots.length; i++) {
                float x, y, vx, vy;
                x = hero.getPosition().x + slots[i].x * MathUtils.cosDeg(hero.getAngle() + slots[i].y);
                y = hero.getPosition().y + slots[i].x * MathUtils.sinDeg(hero.getAngle() + slots[i].y);
                vx = hero.getVelocity().x + bulletSpeed * MathUtils.cosDeg(hero.getAngle() + slots[i].z);
                vy = hero.getVelocity().y + bulletSpeed * MathUtils.sinDeg(hero.getAngle() + slots[i].z);
                gc.getBulletController().setup(x, y, vx, vy, damage, hero.getAngle() + slots[i].z, distance);
            }
        }
    }

    public void recharge (int amount) {
        curBullets += amount;
        if (curBullets > maxBullets) {
            curBullets = maxBullets;
        }
    }
}
