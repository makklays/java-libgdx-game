package com.techmatrix18;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Player player;

    private Music bgMusic;

    @Override
    public void create() {
        batch = new SpriteBatch();
        player = new Player(100, 100);

        // Загружаем музыкальный файл из assets
        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/Земляне_Трава_У_Дома.mp3"));
        // Включаем повтор (чтобы музыка играла циклично)
        bgMusic.setLooping(true);
        // Запускаем воспроизведение
        bgMusic.play();
    }

    @Override
    public void render() {
        float delta = com.badlogic.gdx.Gdx.graphics.getDeltaTime();

        // обновляем игрока
        player.update(delta);

        // чистим экран
        com.badlogic.gdx.Gdx.gl.glClearColor(0, 0, 0, 1);
        com.badlogic.gdx.Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // рисуем
        batch.begin();
        player.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        player.dispose();

        // Освобождаем ресурсы при закрытии
        bgMusic.dispose();
    }
}

