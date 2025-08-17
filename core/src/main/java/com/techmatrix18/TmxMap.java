package com.techmatrix18;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;

/**
 * Разработано для techmatrix18.com
 *
 * @autor: Alexander Kuziv
 * @date: 17-08-2025
 * @version: 0.0.1
 */

public class TmxMap extends ApplicationAdapter {

    private OrthographicCamera camera;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private float worldWidth, worldHeight;
    private int lastX, lastY;
    private boolean dragging = false;

    // Юнит
    private float unitX = 100, unitY = 100;
    private float unitSpeed = 100; // пикселей в секунду
    private SpriteBatch batch;
    private TextureRegion unitUp, unitDown, unitLeft, unitRight, currentUnit;

    // Цель клика мышью
    private Float targetX = null, targetY = null;
    private boolean unitSelected = false;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        map = new TmxMapLoader().load("maps/my-space-map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);

        int mapWidth = map.getProperties().get("width", Integer.class);
        int mapHeight = map.getProperties().get("height", Integer.class);
        int tileWidth = map.getProperties().get("tilewidth", Integer.class);
        int tileHeight = map.getProperties().get("tileheight", Integer.class);

        worldWidth = mapWidth * tileWidth;
        worldHeight = mapHeight * tileHeight;

        batch = new SpriteBatch();
        Texture unitTextureSheet = new Texture("maps/unit-sprites.png");

        unitUp    = new TextureRegion(unitTextureSheet, 0, 0, 32, 32);
        unitDown  = new TextureRegion(unitTextureSheet, 32, 0, 32, 32);
        unitLeft  = new TextureRegion(unitTextureSheet, 64, 0, 32, 32);
        unitRight = new TextureRegion(unitTextureSheet, 96, 0, 32, 32);

        currentUnit = unitDown;

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 worldCoords = camera.unproject(new Vector3(screenX, screenY, 0));
                float mouseX = worldCoords.x;
                float mouseY = worldCoords.y;

                if (button == Input.Buttons.LEFT) {
                    // Клик на юнит
                    if (mouseX >= unitX && mouseX <= unitX + currentUnit.getRegionWidth() &&
                        mouseY >= unitY && mouseY <= unitY + currentUnit.getRegionHeight()) {
                        unitSelected = true; // выделяем
                    } else {
                        // Клик на пустое место → если юнит выделен, ставим цель
                        if (unitSelected) {
                            targetX = mouseX;
                            targetY = mouseY;
                        }
                    }

                    lastX = screenX;
                    lastY = screenY;
                    dragging = true;
                    return true;
                }

                if (button == Input.Buttons.RIGHT) {
                    // Снимаем выделение
                    unitSelected = false;
                    // но цель остаётся, юнит продолжает движение
                    return true;
                }

                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                if (dragging) {
                    float deltaX = screenX - lastX;
                    float deltaY = screenY - lastY;
                    camera.position.add(-deltaX * camera.zoom, deltaY * camera.zoom, 0);
                    clampCamera();

                    lastX = screenX;
                    lastY = screenY;
                    return true;
                }
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if (button == Input.Buttons.LEFT) {
                    dragging = false;
                    return true;
                }
                return false;
            }

            @Override
            public boolean scrolled(float amountX, float amountY) {
                camera.zoom += amountY * 0.1f;
                camera.zoom = Math.max(0.5f, Math.min(2.5f, camera.zoom));
                clampCamera();
                return true;
            }
        });
    }

    private void clampCamera() {
        float halfWidth = camera.viewportWidth * camera.zoom / 2f;
        float halfHeight = camera.viewportHeight * camera.zoom / 2f;

        camera.position.x = Math.max(halfWidth, Math.min(worldWidth - halfWidth, camera.position.x));
        camera.position.y = Math.max(halfHeight, Math.min(worldHeight - halfHeight, camera.position.y));
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        handleKeyboardInput(delta);
        handleMouseTarget(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        clampCamera();

        renderer.setView(camera);
        renderer.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(currentUnit, unitX, unitY);
        batch.end();
    }

    private void handleKeyboardInput(float delta) {
        float speed = unitSpeed * delta;

        boolean moved = false;

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            unitY += speed;
            currentUnit = unitUp;
            moved = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            unitY -= speed;
            currentUnit = unitDown;
            moved = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            unitX -= speed;
            currentUnit = unitLeft;
            moved = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            unitX += speed;
            currentUnit = unitRight;
            moved = true;
        }

        if (moved) {
            targetX = null;
            targetY = null;
        }

        unitX = Math.max(0, Math.min(worldWidth - currentUnit.getRegionWidth(), unitX));
        unitY = Math.max(0, Math.min(worldHeight - currentUnit.getRegionHeight(), unitY));
    }

    private void handleMouseTarget(float delta) {
        if (targetX != null && targetY != null) {
            float dx = targetX - unitX;
            float dy = targetY - unitY;
            float distance = (float) Math.sqrt(dx * dx + dy * dy);

            if (distance < 1f) {
                targetX = null;
                targetY = null;
                return;
            }

            float move = unitSpeed * delta;
            unitX += dx / distance * move;
            unitY += dy / distance * move;

            if (Math.abs(dx) > Math.abs(dy)) {
                currentUnit = dx > 0 ? unitRight : unitLeft;
            } else {
                currentUnit = dy > 0 ? unitUp : unitDown;
            }
        }
    }

    @Override
    public void dispose() {
        renderer.dispose();
        map.dispose();
        batch.dispose();
    }
}

