package com.star.app.game;

public enum WeaponType {
    LASER(0), GUN(1);

    private int index;
    private WeaponType(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
