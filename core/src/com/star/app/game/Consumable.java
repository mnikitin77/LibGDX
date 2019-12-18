package com.star.app.game;

public interface Consumable {
    void rechargeLaser(int amount);
    void rechargeGun(int amount);
    void takeMoney(int amount);
    void heal(int amount);
}
