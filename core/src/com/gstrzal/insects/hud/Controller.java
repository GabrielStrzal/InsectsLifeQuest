package com.gstrzal.insects.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gstrzal.insects.Insects;
import com.gstrzal.insects.config.Constants;
import com.gstrzal.insects.config.GameConfig;
import com.gstrzal.insects.screens.MenuScreen;

/**
 * Created by lelo on 12/02/18.
 */

public class Controller {
    Viewport viewport;
    Stage stage;
    private boolean optionsPressed;
    private boolean audioPressed = true;
    OrthographicCamera camera;

    private Texture backButtonTexture;
    private Texture backButtonPressedTexture;
    private Texture soundTexture;
    private Texture soundPressedTexture;
    private Texture soundCheckedTexture;
    private Texture optionsButtonTexture;
    private Texture optionsButtonPressedTexture;
    private AssetManager assetManager;


    private static final float BACK_BUTTON_X = GameConfig.SCREEN_WIDTH_PX - 250;
    private static final float BACK_BUTTON_Y = GameConfig.SCREEN_HEIGHT_PX - 250;
    private static final float BUTTON_SIZE = 180;
    private static final float BUTTON_PADDING = 220;



    public Controller(final Insects insects){
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.SCREEN_WIDTH_PX, GameConfig.SCREEN_HEIGHT_PX);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
        assetManager = insects.getAssetManager();


        //Back Button
        backButtonTexture = assetManager.get(Constants.MENU_SELECT_LEVEL_BACK_BUTTON);
        backButtonPressedTexture = assetManager.get(Constants.MENU_SELECT_LEVEL_BACK_BUTTON_PRESSED);
        ImageButton backBtn = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(backButtonTexture)),
                new TextureRegionDrawable(new TextureRegion(backButtonPressedTexture)));
        backBtn.setPosition(BACK_BUTTON_X, BACK_BUTTON_Y);
        backBtn.setSize(BUTTON_SIZE,BUTTON_SIZE);

        backBtn.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count,
                            int button) {
                super.tap(event, x, y, count, button);
                insects.setScreen(new MenuScreen(insects));

            }
        });
        stage.addActor(backBtn);


        //Sound Button
        soundTexture = assetManager.get(Constants.CONTROLLER_AUDIO);
        soundPressedTexture = assetManager.get(Constants.CONTROLLER_AUDIO_PRESSED);
        soundCheckedTexture = assetManager.get(Constants.CONTROLLER_AUDIO_PRESSED);
        ImageButton soundBtn = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(soundTexture)),
                new TextureRegionDrawable(new TextureRegion(soundPressedTexture)),
                new TextureRegionDrawable(new TextureRegion(soundCheckedTexture)));
        soundBtn.setPosition(BACK_BUTTON_X - BUTTON_PADDING , BACK_BUTTON_Y);
        soundBtn.setSize(BUTTON_SIZE, BUTTON_SIZE);

        soundBtn.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count,
                            int button) {
                super.tap(event, x, y, count, button);
                audioPressed = !audioPressed;
                System.out.println(audioPressed);

            }
        });

        stage.addActor(soundBtn);

        //Options Button
        optionsButtonTexture = assetManager.get(Constants.CONTROLLER_OPTIONS);
        optionsButtonPressedTexture = assetManager.get(Constants.CONTROLLER_OPTIONS_PRESSED);
        ImageButton optionsBtn = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(optionsButtonTexture)),
                new TextureRegionDrawable(new TextureRegion(optionsButtonPressedTexture)));
        optionsBtn.setPosition(BACK_BUTTON_X - (BUTTON_PADDING * 2), BACK_BUTTON_Y);
        optionsBtn.setSize(BUTTON_SIZE,BUTTON_SIZE);

        optionsBtn.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                optionsPressed = true;
            }
        });
        stage.addActor(optionsBtn);




    }

    public void resize(int width, int height){
        stage.getViewport().update(width, height);

    }

    public void draw(){
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

    }

    public boolean isOptionsPressed() {
        return optionsPressed;
    }

    public void setOptionsPressed(boolean optionsPressed) {
        this.optionsPressed = optionsPressed;
    }

    public boolean isAudioPressed() {
        return audioPressed;
    }

    public void setAudioPressed(boolean audioPressed) {
        this.audioPressed = audioPressed;
    }

}
