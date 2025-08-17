package com.techmatrix18;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * Разработано для techmatrix18.com
 *
 * @autor: Alexander Kuziv
 * @date: 17-08-2025
 * @version: 0.0.1
 */

import com.badlogic.gdx.Screen;

public class MainMenuScreen implements Screen {

    private final MyGame game; // твой Game класс
    private SpriteBatch batch;
    private BitmapFont font;
    private Rectangle[] buttons;
    private String[] buttonTexts = {"Play", "History", "Exit"};
    private boolean[] hoverFlags;
    private OrthographicCamera camera;
    private Sound hoverSound;
    private Texture pixelTexture;
    private Texture backgroundTexture;

    public MainMenuScreen(MyGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(2f);

        hoverSound = Gdx.audio.newSound(Gdx.files.internal("sounds/hover.wav"));
        backgroundTexture = new Texture("maps/background-menu.png");

        // Белый пиксель для отрисовки кнопок
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        pixelTexture = new Texture(pixmap);
        pixmap.dispose();

        // Создаем кнопки
        buttons = new Rectangle[buttonTexts.length];
        hoverFlags = new boolean[buttonTexts.length];

        int btnWidth = 300;
        int btnHeight = 60;
        int spacing = 20;
        int startY = Gdx.graphics.getHeight() / 2 + (btnHeight + spacing);

        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new Rectangle(
                Gdx.graphics.getWidth() / 2 - btnWidth / 2,
                startY - i * (btnHeight + spacing),
                btnWidth,
                btnHeight
            );
            hoverFlags[i] = false;
        }

        // Input processor для мыши
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                Vector3 worldCoords = camera.unproject(new Vector3(screenX, screenY, 0));
                float mouseX = worldCoords.x;
                float mouseY = worldCoords.y;

                for (int i = 0; i < buttons.length; i++) {
                    boolean hovering = buttons[i].contains(mouseX, mouseY);

                    // Если только что навели курсор на кнопку, воспроизводим звук
                    if (hovering && !hoverFlags[i]) {
                        hoverSound.play();
                    }

                    // Обновляем состояние кнопки
                    hoverFlags[i] = hovering;
                }

                return true;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 worldCoords = camera.unproject(new Vector3(screenX, screenY, 0));
                float mouseX = worldCoords.x;
                float mouseY = worldCoords.y;

                for (int i = 0; i < buttons.length; i++) {
                    if (buttons[i].contains(mouseX, mouseY)) {
                        handleButtonClick(i);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void handleButtonClick(int index) {
        switch (index) {
            case 0: // Играть
                game.setScreen(new MultiUnitMapScreen(game));
                break;
            case 1: // History
                System.out.println("History clicked");
                break;
            case 2: // Exit
                Gdx.app.exit();
                break;
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        //Gdx.gl.glClearColor(0, 0, 0, 1);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //batch.begin();
        // Рисуем фон на весь экран
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        for (int i = 0; i < buttons.length; i++) {
            // Цвет кнопки
            Color btnColor = hoverFlags[i] ? Color.RED : Color.DARK_GRAY;
            batch.setColor(btnColor);
            batch.draw(pixelTexture, buttons[i].x, buttons[i].y, buttons[i].width, buttons[i].height);

            // Текст
            batch.setColor(Color.WHITE);
            GlyphLayout layout = new GlyphLayout(font, buttonTexts[i]);
            float textX = buttons[i].x + buttons[i].width / 2 - layout.width / 2;
            float textY = buttons[i].y + buttons[i].height / 2 + layout.height / 2;
            font.draw(batch, layout, textX, textY);
        }
        batch.setColor(Color.WHITE);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}
    @Override
    public void dispose() {
        batch.dispose();
        pixelTexture.dispose();
        font.dispose();
    }
}

