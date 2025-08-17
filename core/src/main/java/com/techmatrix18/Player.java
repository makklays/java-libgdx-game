package com.techmatrix18;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Разработано для techmatrix18.com
 *
 * @autor: Alexander Kuziv
 * @date: 17-08-2025
 * @version: 0.0.1
 */

public class Player {
    private Texture texture;
    private Rectangle bounds;
    private float speed = 200; // пикселей в секунду

    public Player(float x, float y) {
        texture = new Texture("player.png"); // картинка игрока
        bounds = new Rectangle(x, y, texture.getWidth(), texture.getHeight());
    }

    public void update(float delta) {
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.LEFT)) {
            bounds.x -= speed * delta;
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.RIGHT)) {
            bounds.x += speed * delta;
        }
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, bounds.x, bounds.y);
    }

    public void dispose() {
        texture.dispose();
    }
}

