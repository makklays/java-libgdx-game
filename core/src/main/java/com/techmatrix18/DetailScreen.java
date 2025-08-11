package com.techmatrix18;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class DetailScreen implements Screen {

    private final MyGame game;
    private Stage stage;
    private Texture detailTexture;
    private int index;

    public DetailScreen(MyGame game, int index) {
        this.game = game;
        this.index = index;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        String imageFile = "images/detail" + (index + 1) + ".png";
        detailTexture = new Texture(Gdx.files.internal(imageFile));

        Skin skin = new Skin(Gdx.files.internal("uiskin.json")); // стандартный скин

        Table table = new Table();
        table.setFillParent(true);
        table.center();

        /*TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = skin.newDrawable("button", Color.DARK_GRAY);  // фон кнопки
        style.down = skin.newDrawable("button-down", Color.DARK_GRAY);  // при нажатии
        style.font = skin.getFont("default-font");  // шрифт из скина
        style.fontColor = Color.WHITE;  // цвет текста*/

        // Кнопка "Назад"
        TextButton backButton = new TextButton("Back", skin);
        backButton.getLabel().setColor(Color.WHITE);
        //backButton.setColor(Color.DARK_GRAY);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GridScreen(game));  // возвращаемся на главный экран
            }
        });

        // Добавляем элементы в правильном порядке
        table.add(backButton).pad(10).size(100, 40).left().top();
        table.row();
        Label label = new Label("Detail object #" + (index + 1), skin);
        table.add(label).pad(10).center();
        table.row();
        table.add(new com.badlogic.gdx.scenes.scene2d.ui.Image(detailTexture)).size(170, 140).center();

        stage.addActor(table);
    }

    @Override
    public void show() { }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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
        detailTexture.dispose();
    }
}

