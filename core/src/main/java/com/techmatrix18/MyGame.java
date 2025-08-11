package com.techmatrix18;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGame extends Game {

    private Music bgMusic;
    private SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new GridScreen(this));

        // Загружаем музыкальный файл из assets
        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/Земляне_Трава_У_Дома.mp3"));
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

