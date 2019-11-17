/******************************************************************************
* 3. Сделайте астероид, которые просто летает в случайную сторону, и          *
* 	 пролетает сквозь экран                                                   *
******************************************************************************/

package com.star.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Asteroid {
    private StarGame game;
    private Texture texture;
    private Vector2 position;
    private float dirAngle;
    private float rotAngle;
    private int height;
    private int width;

    public Asteroid(StarGame game) {
        this.game = game;
        texture = new Texture("asteroid.png");
        height = texture.getHeight();
        width = texture.getWidth();

        position = new Vector2(0, 0);
        rotAngle = 0f;

        // setting the asteroid's initial values
        initialize();
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - width / 2, position.y - height / 2,
                width / 2, height / 2, width, height, 1, 1, rotAngle,
                0, 0, width, height, false, false);
    }

    public void update(float dt) {
        position.x += (float) Math.cos(Math.toRadians(dirAngle)) * 120 * dt;
        position.y += (float) Math.sin(Math.toRadians(dirAngle)) * 120 * dt;

         //if (position.x < -width / 2) {
        if (position.x < -width) {
            // gone left
            position.x = ScreenManager.SCREEN_WIDTH + width / 2;
            dirAngle = (float)MathUtils.random(110, 250);
        //} else if (position.y > ScreenManager.SCREEN_HEIGHT + height / 2) {
        } else if (position.y > ScreenManager.SCREEN_HEIGHT + height) {
            // gone top
            position.y = -height / 2;
            dirAngle = (float)MathUtils.random(20, 160);
        //} else if (position.x > ScreenManager.SCREEN_WIDTH + width / 2) {
        } else if (position.x > ScreenManager.SCREEN_WIDTH + width) {
            // gone right
            position.x = -width / 2;
            dirAngle = (float)MathUtils.random(-70, 70);
        } else if (position.y < -height) {
            // gone down
            position.y = ScreenManager.SCREEN_HEIGHT + height / 2;
            dirAngle = (float)MathUtils.random(200, 340);
        }

        rotAngle = (rotAngle + 0.5f) % 360;
    }

    private void initialize() {
        switch (MathUtils.random(0, 3)) {
            case 0: // LEFT
                position.x = -width / 2;
                position.y = MathUtils.random(0, ScreenManager.SCREEN_HEIGHT);
                dirAngle = (float)MathUtils.random(-70, 70);
                break;
            case 1: // TOP
                position.x = MathUtils.random(0, ScreenManager.SCREEN_WIDTH);
                position.y = ScreenManager.SCREEN_HEIGHT + height / 2;
                dirAngle = (float)MathUtils.random(200, 340);
                break;
            case 2: // RIGHT
                position.x = ScreenManager.SCREEN_WIDTH + width / 2;
                position.y = MathUtils.random(0, ScreenManager.SCREEN_HEIGHT);
                dirAngle = (float)MathUtils.random(110, 250);
                break;
            case 3: // BOTTOM
                position.x = MathUtils.random(0, ScreenManager.SCREEN_WIDTH);
                position.y = -height / 2;
                dirAngle = (float)MathUtils.random(20, 160);
                break;
            default:
                break;
        }
    }
}
