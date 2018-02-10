package com.gstrzal.insects.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gstrzal.insects.Insects;
import com.gstrzal.insects.config.Constants;
import com.gstrzal.insects.config.GameConfig;
import com.gstrzal.insects.utils.GdxUtils;

/**
 * Created by lelo on 15/01/18.
 * based on "LibGDX Game Development By Example"
 */

public class LoadingScreen extends ScreenAdapter{

    private static final float PROGRESS_BAR_WIDTH = 100;
    private static final float PROGRESS_BAR_HEIGHT = 25;
    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private Camera camera;
    private float progress = 0;
    private final Insects insects;
    protected final AssetManager assetManager;

    public LoadingScreen(Insects insects) {
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
        camera.position.set(GameConfig.SCREEN_WIDTH_PX /2, GameConfig.SCREEN_HEIGHT_PX , 0);
        camera.update();
        viewport = new FitViewport(GameConfig.SCREEN_WIDTH_PX, GameConfig.SCREEN_HEIGHT_PX, camera);
        shapeRenderer = new ShapeRenderer();
        assetManager.load(Constants.MENU_BACKGROUND, Texture.class);
        assetManager.load(Constants.MENU_PLAYBUTTON, Texture.class);
        assetManager.load(Constants.MENU_PLAYBUTTON_PRESSED, Texture.class);
        assetManager.load(Constants.MENU_OPTIONS, Texture.class);
        assetManager.load(Constants.MENU_OPTIONS_PRESSED, Texture.class);
        assetManager.load(Constants.MENU_SELECT_LEVEL, Texture.class);
        assetManager.load(Constants.MENU_SELECT_LEVEL_PRESSED, Texture.class);
        assetManager.load(Constants.LEVEL_1, TiledMap.class);
        assetManager.load(Constants.LEVEL_2, TiledMap.class);
        assetManager.load(Constants.LEVEL_3, TiledMap.class);
        assetManager.load(Constants.JOANINHA, Texture.class);
        assetManager.load(Constants.FLOWER, Texture.class);
    }
    @Override
    public void render(float delta) {
        update();
        GdxUtils.clearScreen();
        draw();
    }
    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
    private void update() {
        if (assetManager.update()) {
            insects.setScreen(new MenuScreen(insects));
        } else {
            progress = assetManager.getProgress();
        }
    }
    private void draw() {
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(
                (GameConfig.SCREEN_WIDTH_PX - PROGRESS_BAR_WIDTH) / 2,
                (GameConfig.SCREEN_HEIGHT_PX - PROGRESS_BAR_HEIGHT / 2),
                progress * PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT);
        shapeRenderer.end();

    }
}
