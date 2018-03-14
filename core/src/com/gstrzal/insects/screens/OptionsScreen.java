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
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.gstrzal.insects.Insects;
import com.gstrzal.insects.config.Constants;
import com.gstrzal.insects.config.GameConfig;
import com.gstrzal.insects.tools.ScreenEnum;
import com.gstrzal.insects.tools.ScreenManager;
import com.gstrzal.insects.utils.GdxUtils;

/**
 * Created by lelo on 18/12/17.
 */

public class OptionsScreen extends ScreenAdapter {

    private Texture backgroundTexture;
    private Texture madeByTexture;
    private Texture backButtonTexture;
    private Texture backButtonPressedTexture;


    private Texture soundTexture;
    private Texture soundPressedTexture;
    private Texture soundCheckedTexture;
    private Texture controlsButtonTexture;
    private Texture controlsButtonPressedTexture;
    private Texture controlsButtonCheckedTexture;


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
        //Backgound
        backgroundTexture = assetManager.get(Constants.MENU_SELECT_LEVELS_BACKGROUND);
        Image background = new Image(backgroundTexture);
        stage.addActor(background);
        madeByTexture = assetManager.get(Constants.MENU_MADE_BY);
        Image madeBy = new Image(madeByTexture);
        madeBy.setPosition(GameConfig.SCREEN_WIDTH_PX/2 - madeBy.getWidth()/2, 400);
        stage.addActor(madeBy);

        //Back Button
        backButtonTexture = assetManager.get(Constants.MENU_SELECT_LEVEL_BACK_BUTTON);
        backButtonPressedTexture = assetManager.get(Constants.MENU_SELECT_LEVEL_BACK_BUTTON_PRESSED);
        ImageButton backBtn = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(backButtonTexture)),
                new TextureRegionDrawable(new TextureRegion(backButtonPressedTexture)));
        backBtn.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count,
                            int button) {
                super.tap(event, x, y, count, button);
                ScreenManager.getInstance().showScreen(ScreenEnum.MENU_SCREEN, game);
            }
        });

        //Sound Button
        soundTexture = assetManager.get(Constants.CONTROLLER_AUDIO);
        soundPressedTexture = assetManager.get(Constants.CONTROLLER_AUDIO_PRESSED);
        soundCheckedTexture = assetManager.get(Constants.CONTROLLER_AUDIO_PRESSED);
        ImageButton soundBtn = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(soundTexture)),
                new TextureRegionDrawable(new TextureRegion(soundPressedTexture)),
                new TextureRegionDrawable(new TextureRegion(soundCheckedTexture)));
        soundBtn.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count,
                            int button) {
                super.tap(event, x, y, count, button);
                game.setAudioOn(!game.isAudioOn());

            }
        });
        if(!game.isAudioOn()) {
            soundBtn.setChecked(true);
        }


        //Controls Button
        controlsButtonTexture = assetManager.get(Constants.CONTROLLER_CONTROLS);
        controlsButtonPressedTexture = assetManager.get(Constants.CONTROLLER_CONTROLS_PRESSED);
        controlsButtonCheckedTexture = assetManager.get(Constants.CONTROLLER_CONTROLS_CHECKED);
        ImageButton controlsBtn = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(controlsButtonTexture)),
                new TextureRegionDrawable(new TextureRegion(controlsButtonPressedTexture)),
                new TextureRegionDrawable(new TextureRegion(controlsButtonCheckedTexture)));
        controlsBtn.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                game.setDisplayControllers(!game.isDisplayControllers());
            }
        });
        if(!game.isDisplayControllers()) {
            controlsBtn.setChecked(true);
        }


        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.add(controlsBtn).pad(50);
        table.add(soundBtn).pad(50);
        table.add(backBtn).pad(50);

        stage.addActor(table);


    }
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    public void render(float delta) {
        GdxUtils.clearScreen();
        stage.act(delta);
        stage.draw();
        game.getAudioHandler().updateMusic();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
