package com.techmatrix18;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GridScreen implements Screen {

    private Texture backgroundTexture;  // фон
    private final MyGame game;
    private Stage stage;
    private Texture[] textures;

    public GridScreen(MyGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        //batch = new SpriteBatch();

        backgroundTexture = new Texture(Gdx.files.internal("images/fons/fon11.png"));  // загрузи фон

        textures = new Texture[]{
            new Texture(Gdx.files.internal("images/base1_2-1.png")),
            new Texture(Gdx.files.internal("images/base1_3-1.png")),
            new Texture(Gdx.files.internal("images/base1_4-1.png")),
            new Texture(Gdx.files.internal("images/banco_2-1.png")),
        };

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        for (int i = 0; i < textures.length; i++) {
            final int index = i;

            ImageButton button = new ImageButton(new TextureRegionDrawable(textures[i]));
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // При клике переключаемся на экран деталей, передаём индекс
                    game.setScreen(new DetailScreen(game, index));
                }
            });

            table.add(button).size(170, 140).pad(10);
        }

        stage.addActor(table);
    }

    @Override
    public void show() { }

    @Override
    public void render(float delta) {
        // Очистка экрана
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Рисуем фон через SpriteBatch напрямую
        game.getBatch().begin();
        game.getBatch().draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.getBatch().end();

        // Затем отрисовываем UI поверх
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
        for (Texture t : textures) {
            t.dispose();
        }
    }
}

