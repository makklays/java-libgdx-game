package com.techmatrix18;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Разработано для techmatrix18.com
 *
 * @autor: Alexander Kuziv
 * @date: 17-08-2025
 * @version: 0.0.1
 */

public class MyGame extends Game {

    private Music bgMusic;
    private SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new MainMenuScreen(this));

        // Загружаем музыкальный файл из assets
        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/Земляне_Трава_У_Дома.mp3"));
        //bgMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/background-audio-arcade.wav"));
        //bgMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/fon-sound.wav"));
        //bgMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/Земляне_Поверь_в_мечту.mp"));

        // Включаем повтор (чтобы музыка играла циклично)
        bgMusic.setLooping(true);
        // Запускаем воспроизведение
        bgMusic.play();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    @Override
    public void dispose() {
        batch.dispose();
        super.dispose();

        // Освобождаем ресурсы при закрытии
        bgMusic.dispose();
    }
}

