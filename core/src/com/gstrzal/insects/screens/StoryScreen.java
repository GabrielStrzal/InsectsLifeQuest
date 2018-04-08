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
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.gstrzal.insects.Insects;
import com.gstrzal.insects.config.Constants;
import com.gstrzal.insects.config.GameConfig;
import com.gstrzal.insects.tools.ScreenEnum;
import com.gstrzal.insects.tools.ScreenManager;
import com.gstrzal.insects.utils.GdxUtils;

/**
 * Created by lelo on 08/04/18.
 */

public class StoryScreen extends ScreenAdapter {
    private static final float BACK_BUTTON_Y = ((int)GameConfig.DISPLAY_SCREEN_HEIGHT_PX)*.7f;
    private static final float BACK_BUTTON_X = ((int)GameConfig.DISPLAY_SCREEN_WIDTH_PX)*.8f;

    private Texture skipButtonTexture;


    private Stage stage;
    private final Insects game;
    private final AssetManager assetManager;

    private long levelStopTime;
    private int levelTime;
    private final int WAIT_TIME = 20;

    private int screenNumber = 0;

    public StoryScreen(Insects game) {
        this.game = game;
        this.assetManager = game.getAssetManager();
        game.actionResolver.setTrackerScreenName("com.gstrzal.insects.screens.StoryScreen");
        levelStopTime = TimeUtils.millis();
        levelTime = WAIT_TIME;
    }

    public void show() {
        stage = new Stage(new FitViewport(GameConfig.DISPLAY_SCREEN_WIDTH_PX, GameConfig.DISPLAY_SCREEN_HEIGHT_PX));
        Gdx.input.setInputProcessor(stage);

    }


    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    public void render(float delta) {
        GdxUtils.clearScreen();

        checkTimePassed();
        checkAndShowScreen();
        stage.act(delta);
        stage.draw();
    }

    private void checkAndShowScreen() {
        switch(screenNumber) {
            case 0:{
                showStoryScreen(Constants.STORY_01);
                break;
            }
            case 1: {
                showStoryScreen(Constants.STORY_02);
                break;
            }
            case 2: {
                showStoryScreen(Constants.STORY_03);
                break;
            }
            case 3: {
                showStoryScreen(Constants.STORY_04);
                break;
            }
            case 4: {
                showStoryScreen(Constants.STORY_05);
                break;
            }
            case 5: {
                ScreenManager.getInstance().showScreen(ScreenEnum.GAME_SCREEN, game, 1);
                break;
            }
        }
    }


    @Override
    public void dispose() {
        stage.dispose();
    }

    private void checkTimePassed() {
        if(((TimeUtils.millis() - levelStopTime) / 100) > levelTime){
            levelTime += WAIT_TIME;
            screenNumber++;
        }
    }

    private void showStoryScreen(String assetName){
        Texture texture = assetManager.get(assetName);
        Image textureImg = new Image(texture);
        stage.addActor(textureImg);
        showSkipButton();
    }

    private void showSkipButton() {
        skipButtonTexture = assetManager.get(Constants.MENU_STORY_SKIP_BUTTON);
        ImageButton skipBtn = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(skipButtonTexture)));
        skipBtn.setHeight(skipButtonTexture.getHeight()*.15f);
        skipBtn.setPosition(560, 400);
        skipBtn.addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int count,
                                  int button) {
                super.touchDown(event, x, y, count, button);
                ScreenManager.getInstance().showScreen(ScreenEnum.GAME_SCREEN, game, 1);
            }
        });
        stage.addActor(skipBtn);
    }


}
