package com.gstrzal.insects.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.gstrzal.insects.Insects;
import com.gstrzal.insects.config.Constants;
import com.gstrzal.insects.config.GameConfig;
import com.gstrzal.insects.utils.GdxUtils;

/**
 * Created by lelo on 18/12/17.
 */

public class OptionsScreen extends ScreenAdapter {

    private static final int BACK_BUTTON_Y = ((int)GameConfig.SCREEN_HEIGHT_PX/7)*5;
    private static final int BACK_BUTTON_X = ((int)GameConfig.SCREEN_WIDTH_PX/5)*4;


    private Texture backgroundTexture;
    private Texture backButtonTexture;
    private Texture backButtonPressedTexture;


    private Stage stage;
    private final Insects game;
    private final AssetManager assetManager;



    public OptionsScreen(Insects game) {
        this.game = game;
        this.assetManager = game.getAssetManager();
    }

    public void show() {
        stage = new Stage(new FitViewport(GameConfig.SCREEN_WIDTH_PX, GameConfig.SCREEN_HEIGHT_PX));
        Gdx.input.setInputProcessor(stage);
        backgroundTexture = assetManager.get(Constants.MENU_SELECT_LEVELS_BACKGROUND);
        Image background = new Image(backgroundTexture);
        stage.addActor(background);

        //Back Button
        backButtonTexture = assetManager.get(Constants.MENU_SELECT_LEVEL_BACK_BUTTON);
        backButtonPressedTexture = assetManager.get(Constants.MENU_SELECT_LEVEL_BACK_BUTTON_PRESSED);
        ImageButton backBtn = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(backButtonTexture)),
                new TextureRegionDrawable(new TextureRegion(backButtonPressedTexture)));
        backBtn.setPosition(BACK_BUTTON_X, BACK_BUTTON_Y);

        backBtn.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count,
                            int button) {
                super.tap(event, x, y, count, button);
                game.setScreen(new MenuScreen(game));
                dispose();
            }
        });
        stage.addActor(backBtn);





    }
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    public void render(float delta) {
        GdxUtils.clearScreen();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
