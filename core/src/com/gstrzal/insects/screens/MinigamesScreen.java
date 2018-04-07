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
import com.gstrzal.insects.entity.GlidingAnt.GlidingEntityType;
import com.gstrzal.insects.tools.ScreenEnum;
import com.gstrzal.insects.tools.ScreenManager;
import com.gstrzal.insects.utils.GdxUtils;

/**
 * Created by lelo on 18/12/17.
 */

public class MinigamesScreen extends ScreenAdapter {

    private Texture backgroundTexture;
    private Texture backButtonTexture;
    private Texture backButtonPressedTexture;
    private Texture glidingAntTexture;
    private Texture glidingAntPressedTexture;
    private Texture glidingLBugTexture;
    private Texture glidingLBugPressedTexture;
    private Texture blockedMiniGameTexture;



    private Stage stage;
    private final Insects game;
    private final AssetManager assetManager;
    private static final int BACK_BUTTON_Y = ((int)GameConfig.SCREEN_HEIGHT_PX/7)*5;
    private static final int BACK_BUTTON_X = ((int)GameConfig.SCREEN_WIDTH_PX/5)*4;



    public MinigamesScreen(Insects game) {
        this.game = game;
        this.assetManager = game.getAssetManager();
        game.actionResolver.setTrackerScreenName("com.gstrzal.insects.screens.MinigamesScreen");
    }

    public void show() {
        stage = new Stage(new FitViewport(GameConfig.SCREEN_WIDTH_PX, GameConfig.SCREEN_HEIGHT_PX));
        Gdx.input.setInputProcessor(stage);
        //Backgound
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
            public void touchDown(InputEvent event, float x, float y, int count,
                            int button) {
                super.touchDown(event, x, y, count, button);
                ScreenManager.getInstance().showScreen(ScreenEnum.MENU_SCREEN, game);
            }
        });
        stage.addActor(backBtn);



        //Gliding Ant Button
        glidingAntTexture = assetManager.get(Constants.MINIGAMES_GLIDING_ANT_LOGO);
        glidingAntPressedTexture = assetManager.get(Constants.MINIGAMES_GLIDING_ANT_LOGO_PRESSED);
        ImageButton glidingAnt = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(glidingAntTexture)),
                new TextureRegionDrawable(new TextureRegion(glidingAntPressedTexture)));
        glidingAnt.addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int count,
                            int button) {
                super.touchDown(event, x, y, count, button);
                ScreenManager.getInstance().showScreen(ScreenEnum.GLIDING_ANT_SCREEN, game, GlidingEntityType.ANT);
            }
        });

        //Gliding LBug Button
        glidingLBugTexture = assetManager.get(Constants.MINIGAMES_GLIDING_LBUG_LOGO);
        glidingLBugPressedTexture = assetManager.get(Constants.MINIGAMES_GLIDING_LBUG_LOGO_PRESSED);
        ImageButton glidingLBug = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(glidingLBugTexture)),
                new TextureRegionDrawable(new TextureRegion(glidingLBugPressedTexture)));
        glidingLBug.addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int count,
                                  int button) {
                super.touchDown(event, x, y, count, button);
                ScreenManager.getInstance().showScreen(ScreenEnum.GLIDING_ANT_SCREEN, game, GlidingEntityType.LBUG);
            }
        });

        //Blocked Button
        blockedMiniGameTexture = assetManager.get(Constants.MINIGAMES_BLOCKED);

        ImageButton blockedMinigame = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(blockedMiniGameTexture)));
        ImageButton blockedMinigameDouble = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(blockedMiniGameTexture)));

        Table table = new Table();
        table.setFillParent(true);
        table.center().padTop(400);
        if(game.getGameStatsHandler().getTopCLearedLevel()>=10){
            table.add(glidingAnt).pad(50);
        }else{
            table.add(blockedMinigame).pad(50);
        }

        if(game.getGameStatsHandler().getTopCLearedLevel()>=18) {
            table.add(glidingLBug).pad(50);
        }else {
            table.add(blockedMinigameDouble).pad(50);
        }
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
