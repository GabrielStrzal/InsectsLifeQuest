package com.gstrzal.insects.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gstrzal.insects.Insects;
import com.gstrzal.insects.config.GameConfig;
import com.gstrzal.insects.utils.GdxUtils;
import com.gstrzal.insects.utils.ViewportUtils;

/**
 * Created by Gabriel on 09/10/2017.
 */

public class MenuScreen implements Screen {

    private static final float PLAY_BUTTON_WIDTH = 6f;
    private static final float PLAY_BUTTON_HEIGHT = 2f;
    private static final int PLAY_BUTTON_Y = 10;
    private static final int PLAY_BUTTON_X = 17;



    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer renderer;

    private Insects game;

    Texture backgroundImage;
    Texture playButton;
    //Texture playButtonActive;

    public MenuScreen(Insects game){
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.WORLD_WIDTH_UNITS, GameConfig.WORLD_HEIGHT_UNITS, camera);
        renderer = new ShapeRenderer();

        backgroundImage = new Texture("menu/insects_menu_800x480.png");
        playButton = new Texture("menu/start_button.png");
        //playButtonActive = new Texture("menu/start_button_active.png");
    }

    @Override
    public void show() {

    }
    private void handleInput(float dt) {
        if (Gdx.input.isTouched()){
            game.setScreen(new PlayScreen(game));
        }
    }
    public void update(float dt){
        handleInput(dt);

    }

    @Override
    public void render(float delta) {
        update(delta);
        GdxUtils.clearScreen(new Color(0.15f, 0.15f, 0.3f, 1));
        viewport.apply();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(backgroundImage, 0,0,GameConfig.WORLD_WIDTH_UNITS, GameConfig.WORLD_HEIGHT_UNITS);
        game.batch.draw(playButton, PLAY_BUTTON_X, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
        game.batch.end();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        ViewportUtils.debugPixelPerUnit(viewport);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        playButton.dispose();
        backgroundImage.dispose();
        renderer.dispose();
    }
}
