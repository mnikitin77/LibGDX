package com.star.app.game;

public interface Consumable {
    void rechargeWeapon(int amount);
    void takeMoney(int amount);
    void heal(int amount);
}
