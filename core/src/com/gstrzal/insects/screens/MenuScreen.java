package com.gstrzal.insects.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.gstrzal.insects.Insects;
import com.gstrzal.insects.config.GameConfig;
import com.gstrzal.insects.utils.GdxUtils;

/**
 * Created by Gabriel on 09/10/2017.
 */

public class MenuScreen implements Screen {

    private static final int MENU_SCREEN_WIDTH = (int) GameConfig.SCREEN_WIDTH_PX;
    private static final int MENU_SCREEN_HEIGHT = (int)GameConfig.SCREEN_HEIGHT_PX;
    private static final int PLAY_BUTTON_WIDTH = 300;
    private static final int PLAY_BUTTON_HEIGHT = 120;
    private static final int PLAY_BUTTON_Y = 520;
    private static final int PLAY_BUTTON_X = 340;





    private Insects game;

    Texture backgroundImage;
    Texture playButton;
    //Texture playButtonActive;

    public MenuScreen(Insects game){
        this.game = game;


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

        game.batch.begin();
        game.batch.draw(backgroundImage, (game.V_WIDTH/2) - MENU_SCREEN_WIDTH/2, (game.V_HEIGHT/2) - MENU_SCREEN_HEIGHT/2);
        game.batch.draw(playButton, PLAY_BUTTON_Y, PLAY_BUTTON_X);
        game.batch.end();

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
