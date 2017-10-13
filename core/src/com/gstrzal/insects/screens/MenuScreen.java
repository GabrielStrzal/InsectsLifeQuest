package com.gstrzal.insects.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.gstrzal.insects.Insects;

/**
 * Created by Gabriel on 09/10/2017.
 */

public class MenuScreen implements Screen {

    private static final int MENU_SCREEN_WIDTH = 1220;
    private static final int MENU_SCREEN_HEIGHT = 688;
    private static final int PLAY_BUTTON_WIDTH = 300;
    private static final int PLAY_BUTTON_HEIGHT = 120;
    private static final int PLAY_BUTTON_Y = 800;
    private static final int PLAY_BUTTON_X = 450;


    private Insects game;

    Texture backgroundImage;
    Texture playButton;
    //Texture playButtonActive;

    public MenuScreen(Insects game){
        this.game = game;
        backgroundImage = new Texture("menu/insects_menu.png");
        playButton = new Texture("menu/start_button.png");
        //playButtonActive = new Texture("menu/start_button_active.png");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(backgroundImage, (game.V_WIDTH/2) - MENU_SCREEN_WIDTH/2, (game.V_HEIGHT/2) - MENU_SCREEN_HEIGHT/2);
        game.batch.draw(playButton, PLAY_BUTTON_Y, PLAY_BUTTON_X);
        game.batch.end();

        if (Gdx.input.justTouched()){
            game.setScreen(new PlayScreen(game));
        }

    }

    @Override
    public void resize(int width, int height) {

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
    }
}
