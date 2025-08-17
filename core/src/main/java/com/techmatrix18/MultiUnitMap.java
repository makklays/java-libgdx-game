package com.techmatrix18;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import java.util.ArrayList;
import java.util.List;

/**
 * Разработано для techmatrix18.com
 *
 * @autor: Alexander Kuziv
 * @date: 17-08-2025
 * @version: 0.0.1
 */

public class MultiUnitMap extends ApplicationAdapter {

    private OrthographicCamera camera;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private float worldWidth, worldHeight;
    private int lastX, lastY;
    private boolean dragging = false;
    private SpriteBatch batch;

    private List<Unit> units = new ArrayList<>();
    private float unitSpeed = 100;

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
        Texture unitSheet = new Texture("maps/unit-sprites.png");
        TextureRegion up = new TextureRegion(unitSheet, 0, 0, 32, 32);
        TextureRegion down = new TextureRegion(unitSheet, 32, 0, 32, 32);
        TextureRegion left = new TextureRegion(unitSheet, 64, 0, 32, 32);
        TextureRegion right = new TextureRegion(unitSheet, 96, 0, 32, 32);

        Texture unitOutlineSheet = new Texture("maps/unit-outline.png");
        TextureRegion upOutline    = new TextureRegion(unitOutlineSheet, 0, 0, 32, 32);
        TextureRegion downOutline  = new TextureRegion(unitOutlineSheet, 32, 0, 32, 32);
        TextureRegion leftOutline  = new TextureRegion(unitOutlineSheet, 64, 0, 32, 32);
        TextureRegion rightOutline = new TextureRegion(unitOutlineSheet, 96, 0, 32, 32);

        // Создаём 5 юнитов
        for (int i = 0; i < 5; i++) {
            units.add(new Unit(100 + i * 50, 100,
                up, down, left, right,
                upOutline, downOutline, leftOutline, rightOutline));
        }

        Gdx.input.setInputProcessor(new com.badlogic.gdx.InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 worldCoords = camera.unproject(new Vector3(screenX, screenY, 0));
                float mouseX = worldCoords.x;
                float mouseY = worldCoords.y;

                if (button == Input.Buttons.LEFT) {
                    boolean clickedUnit = false;
                    for (Unit u : units) {
                        if (mouseX >= u.x && mouseX <= u.x + u.current.getRegionWidth() &&
                            mouseY >= u.y && mouseY <= u.y + u.current.getRegionHeight()) {
                            // Ctrl для множественного выбора
                            if (!Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                                for (Unit uu : units) uu.selected = false;
                            }
                            u.selected = true;
                            clickedUnit = true;
                        }
                    }

                    if (!clickedUnit) {
                        for (Unit u : units) {
                            if (u.selected) {
                                u.targetX = mouseX;
                                u.targetY = mouseY;
                            }
                        }
                    }

                    lastX = screenX;
                    lastY = screenY;
                    dragging = true;
                    return true;
                }

                if (button == Input.Buttons.RIGHT) {
                    for (Unit u : units) {
                        u.selected = false;
                    }
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
        handleUnitsMovement(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        clampCamera();

        renderer.setView(camera);
        renderer.render();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (Unit u : units) {
            u.updateCurrent();
            batch.draw(u.current, u.x, u.y);
        }
        batch.end();
    }

    private void handleKeyboardInput(float delta) {
        float speed = unitSpeed * delta;

        for (Unit u : units) {
            if (!u.selected) continue;
            float dx = 0, dy = 0;
            boolean moved = false;

            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                dy += speed;
                u.current = u.up;
                moved = true;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                dy -= speed;
                u.current = u.down;
                moved = true;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                dx -= speed;
                u.current = u.left;
                moved = true;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                dx += speed;
                u.current = u.right;
                moved = true;
            }

            if (moved) {
                u.targetX = null;
                u.targetY = null;
                moveUnit(u, dx, dy);
            }
        }
    }

    private void handleUnitsMovement(float delta) {
        float moveStep = unitSpeed * delta;
        for (Unit u : units) {
            if (u.targetX != null && u.targetY != null) {
                float dx = u.targetX - u.x;
                float dy = u.targetY - u.y;
                float distance = (float) Math.sqrt(dx * dx + dy * dy);

                if (distance < 1f) {
                    u.targetX = null;
                    u.targetY = null;
                    continue;
                }

                float nx = u.x + dx / distance * moveStep;
                float ny = u.y + dy / distance * moveStep;

                moveUnit(u, nx - u.x, ny - u.y);

                if (Math.abs(dx) > Math.abs(dy)) u.current = dx > 0 ? u.right : u.left;
                else u.current = dy > 0 ? u.up : u.down;
            }
        }
    }

    private void moveUnit(Unit u, float dx, float dy) {
        float newX = u.x + dx;
        float newY = u.y + dy;

        if (canMove(u, newX, newY)) {
            u.x = Math.max(0, Math.min(worldWidth - u.current.getRegionWidth(), newX));
            u.y = Math.max(0, Math.min(worldHeight - u.current.getRegionHeight(), newY));
        }
    }

    private boolean canMove(Unit movingUnit, float newX, float newY) {
        for (Unit u : units) {
            if (u == movingUnit) continue;
            if (newX < u.x + u.current.getRegionWidth() &&
                newX + movingUnit.current.getRegionWidth() > u.x &&
                newY < u.y + u.current.getRegionHeight() &&
                newY + movingUnit.current.getRegionHeight() > u.y) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void dispose() {
        renderer.dispose();
        map.dispose();
        batch.dispose();
    }
}

