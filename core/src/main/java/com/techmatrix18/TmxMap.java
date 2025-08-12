package com.techmatrix18;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class TmxMap extends ApplicationAdapter {
    private OrthographicCamera camera;
    private TiledMap map;
    private IsometricTiledMapRenderer renderer;

    private float lastX, lastY;
    private boolean dragging = false;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);

        map = new TmxMapLoader().load("maps/map-export.tmx");
        renderer = new IsometricTiledMapRenderer(map);

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (button == 0) {
                    lastX = screenX;
                    lastY = screenY;
                    dragging = true;
                    return true;
                }
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                if (dragging) {
                    float deltaX = lastX - screenX;
                    float deltaY = screenY - lastY;

                    camera.position.add(deltaX * camera.zoom, -deltaY * camera.zoom, 0);

                    lastX = screenX;
                    lastY = screenY;
                    return true;
                }
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if (button == 0) {
                    dragging = false;
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);  // Черный фон (можно любой другой)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        renderer.setView(camera);
        renderer.render();
    }

    @Override
    public void dispose() {
        renderer.dispose();
        map.dispose();
    }
}

