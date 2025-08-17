package com.techmatrix18;

import com.badlogic.gdx.Screen;

/**
 * Разработано для techmatrix18.com
 *
 * @autor: Alexander Kuziv
 * @date: 17-08-2025
 * @version: 0.0.1
 */

public class MultiUnitMapScreen implements Screen {
    private MyGame game;
    private MultiUnitMap mapScreen; // твой существующий класс MultiUnitMap

    public MultiUnitMapScreen(MyGame game) {
        this.game = game;
        mapScreen = new MultiUnitMap();
        mapScreen.create(); // инициализация карты и юнитов
    }

    @Override
    public void render(float delta) {
        mapScreen.render();
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override
    public void dispose() {
        mapScreen.dispose();
    }
}

