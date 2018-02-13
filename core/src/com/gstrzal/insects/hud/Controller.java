package com.gstrzal.insects.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
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
    Stage stageGameControls;
    private boolean rightPressed;
    private boolean leftPressed;
    private boolean actionPressed;


    OrthographicCamera camera;

    private Texture backButtonTexture;
    private Texture backButtonPressedTexture;
    private Texture soundTexture;
    private Texture soundPressedTexture;
    private Texture soundCheckedTexture;
    private Texture rightButtonTexture;
    private Texture rightPressedButtonTexture;
    private Texture leftButtonTexture;
    private Texture leftPressedButtonTexture;
    private Texture actionButtonTexture;
    private Texture actionPressedButtonTexture;
    private Texture controlsButtonTexture;
    private Texture controlsButtonPressedTexture;
    private Texture controlsButtonCheckedTexture;
    private AssetManager assetManager;


    private static final float BACK_BUTTON_X = GameConfig.SCREEN_WIDTH_PX - 250;
    private static final float BACK_BUTTON_Y = GameConfig.SCREEN_HEIGHT_PX - 250;
    private static final float BUTTON_SIZE = 180;
    private static final float BUTTON_PADDING = 220;
    private static final float ACTION_BUTTON_WIDTH = 1500;

    private Insects insects;


    public Controller(final Insects insects){
        this.insects = insects;
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.SCREEN_WIDTH_PX, GameConfig.SCREEN_HEIGHT_PX);
        stage = new Stage(viewport);
        stageGameControls = new Stage(viewport);
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(stageGameControls);
        Gdx.input.setInputProcessor(inputMultiplexer);
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
                insects.setAudioOn(!insects.isAudioOn());

            }
        });
        if(!insects.isAudioOn()) {
            soundBtn.setChecked(true);
        }

        stage.addActor(soundBtn);

        //Controls Button
        controlsButtonTexture = assetManager.get(Constants.CONTROLLER_CONTROLS);
        controlsButtonPressedTexture = assetManager.get(Constants.CONTROLLER_CONTROLS_PRESSED);
        controlsButtonCheckedTexture = assetManager.get(Constants.CONTROLLER_CONTROLS_CHECKED);
        ImageButton controlsBtn = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(controlsButtonTexture)),
                new TextureRegionDrawable(new TextureRegion(controlsButtonPressedTexture)),
                new TextureRegionDrawable(new TextureRegion(controlsButtonCheckedTexture)));
        controlsBtn.setPosition(BACK_BUTTON_X - (BUTTON_PADDING * 2), BACK_BUTTON_Y);
        controlsBtn.setSize(BUTTON_SIZE,BUTTON_SIZE);

        controlsBtn.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                insects.setDisplayControllers(!insects.isDisplayControllers());
            }
        });
        if(!insects.isDisplayControllers()) {
            controlsBtn.setChecked(true);
        }
        stage.addActor(controlsBtn);



        //Right Button
        rightButtonTexture = assetManager.get(Constants.CONTROLLER_RIGHT);
        rightPressedButtonTexture = assetManager.get(Constants.CONTROLLER_RIGHT_PRESSED);
        ImageButton rightBtn = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(rightButtonTexture)),
                new TextureRegionDrawable(new TextureRegion(rightPressedButtonTexture)));
        rightBtn.setPosition(GameConfig.SCREEN_WIDTH_PX-400, GameConfig.SCREEN_HEIGHT_PX/2 - 176);
        rightBtn.setSize(BUTTON_SIZE*2,BUTTON_SIZE*2);

        rightBtn.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                rightPressed = true;
                leftPressed = false;
            }
        });
        stageGameControls.addActor(rightBtn);


        //Left Button
        leftButtonTexture = assetManager.get(Constants.CONTROLLER_LEFT);
        leftPressedButtonTexture = assetManager.get(Constants.CONTROLLER_LEFT_PRESSED);
        ImageButton leftBtn = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(leftButtonTexture)),
                new TextureRegionDrawable(new TextureRegion(leftPressedButtonTexture)));
        leftBtn.setPosition(50, GameConfig.SCREEN_HEIGHT_PX/2 - 176);
        leftBtn.setSize(BUTTON_SIZE*2,BUTTON_SIZE*2);

        leftBtn.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                leftPressed = true;
                rightPressed = false;
            }
        });
        stageGameControls.addActor(leftBtn);


        //Action Button
        actionButtonTexture = assetManager.get(Constants.CONTROLLER_ACTIONS);
        actionPressedButtonTexture = assetManager.get(Constants.CONTROLLER_ACTIONS_PRESSED);
        final ImageButton actionBtn = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(actionButtonTexture)),
                new TextureRegionDrawable(new TextureRegion(actionPressedButtonTexture)));
        actionBtn.setPosition(GameConfig.SCREEN_WIDTH_PX/2 - ACTION_BUTTON_WIDTH/2, 10);
        actionBtn.setWidth(ACTION_BUTTON_WIDTH);

        actionBtn.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                actionPressed = true;
            }
        });
        stageGameControls.addActor(actionBtn);


    }

    public void resize(int width, int height){
        stage.getViewport().update(width, height);

    }

    public void draw(){
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        if(insects.isDisplayControllers()){
            stageGameControls.draw();
        }

    }


    public boolean isRightPressed() {
        return rightPressed;
    }

    public void setRightPressed(boolean rightPressed) {
        this.rightPressed = rightPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public void setLeftPressed(boolean leftPressed) {
        this.leftPressed = leftPressed;
    }

    public boolean isActionPressed() {
        return actionPressed;
    }

    public void setActionPressed(boolean actionPressed) {
        this.actionPressed = actionPressed;
    }
}
