package com.star.app.game;

import java.util.Properties;

public class KeysControl {
    int forward;
    int backward;
    int left;
    int right;
    int fire;
    int laser;
    int gun;
    int nextWeapon;
    int prevWeapon;

    public KeysControl(Properties properties, String prefix) {
        forward = Integer.parseInt(properties.getProperty(prefix + "_FORWARD"));
        backward = Integer.parseInt(properties.getProperty(prefix + "_BACKWARD"));
        left = Integer.parseInt(properties.getProperty(prefix + "_LEFT"));
        right = Integer.parseInt(properties.getProperty(prefix + "_RIGHT"));
        fire = Integer.parseInt(properties.getProperty(prefix + "_FIRE"));
        laser = Integer.parseInt(properties.getProperty(prefix + "_LASER"));
        gun = Integer.parseInt(properties.getProperty(prefix + "_GUN"));
        prevWeapon = Integer.parseInt(properties.getProperty(prefix + "_PREV"));
        nextWeapon = Integer.parseInt(properties.getProperty(prefix + "_NEXT"));
    }
}
