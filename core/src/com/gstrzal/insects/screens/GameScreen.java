package com.gstrzal.insects.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gstrzal.insects.Insects;
import com.gstrzal.insects.assets.AssetPaths;
import com.gstrzal.insects.config.GameConfig;
import com.gstrzal.insects.entity.Joaninha;
import com.gstrzal.insects.utils.GdxUtils;

public class GameScreen extends ScreenAdapter {
    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private Camera camera;
    private SpriteBatch batch;
    private final Insects insects;
    private Joaninha joaninha;
    private AssetManager assetManager;

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;

public GameScreen(Insects insects) {
        this.insects = insects;
        this.assetManager = insects.getAssetManager();
    }
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.SCREEN_WIDTH_PX, GameConfig.SCREEN_HEIGHT_PX, camera);
        viewport.apply(true);
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        tiledMap = assetManager.get(AssetPaths.LEVEL_06);
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, batch);
        orthogonalTiledMapRenderer.setView((OrthographicCamera) camera);
        joaninha = new Joaninha((Texture) assetManager.get(AssetPaths.JOANINHA));
    }
    @Override
    public void render(float delta) {
        update(delta);
        GdxUtils.clearScreen();
        draw();
        drawDebug();
    }
    private void update(float delta) {
        joaninha.update(delta);
        stopJoaninhaLeavingTheScreen();
    }

    private void draw() {
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        orthogonalTiledMapRenderer.render();
        batch.begin();
        joaninha.draw(batch);
        batch.end();
    }
    private void drawDebug() {
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        joaninha.drawDebug(shapeRenderer);
        shapeRenderer.end();
    }
    private void stopJoaninhaLeavingTheScreen() {
        if (joaninha.getY() < 32) {
            joaninha.setPosition(joaninha.getX(), 32);
            joaninha.landed();
        }
        if (joaninha.getX() < 32) {
            joaninha.setPosition(32, joaninha.getY());
        }
        if (joaninha.getX() + joaninha.WIDTH > (GameConfig.SCREEN_WIDTH_PX-32)) {
            joaninha.setPosition(GameConfig.SCREEN_WIDTH_PX - joaninha.WIDTH, joaninha.getY());
        }
    }
}
