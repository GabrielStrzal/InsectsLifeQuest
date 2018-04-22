package com.gstrzal.insects.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.gstrzal.insects.Insects;
import com.gstrzal.insects.config.Constants;
import com.gstrzal.insects.config.GameConfig;
import com.gstrzal.insects.screens.enums.SelectLevelsType;
import com.gstrzal.insects.tools.GameStatsHandler;
import com.gstrzal.insects.tools.ScreenEnum;
import com.gstrzal.insects.tools.ScreenManager;
import com.gstrzal.insects.utils.GdxUtils;

/**
 * Created by lelo on 18/12/17.
 */

public class SelectLevelsScreen extends ScreenAdapter {

    private static final int BACK_BUTTON_Y = ((int)GameConfig.SCREEN_HEIGHT_PX/7)*5;
    private static final int BACK_BUTTON_X = ((int)GameConfig.SCREEN_WIDTH_PX/5)*4;

    private static final int TABLE_Y = ((int)GameConfig.SCREEN_HEIGHT_PX/3)*2;


    private Texture backgroundTexture;
    private Texture backButtonTexture;
    private Texture backButtonPressedTexture;
    private Texture rightScreenButtonTexture;
    private Texture rightScreenButtonPressedTexture;
    private Texture leftScreenButtonTexture;
    private Texture leftScreenButtonPressedTexture;
    private Texture levelButtonTexture_0;
    private Texture levelButtonPressedTexture_0;
    private Texture levelButtonTexture_1;
    private Texture levelButtonPressedTexture_1;
    private Texture levelButtonTexture_2;
    private Texture levelButtonPressedTexture_2;
    private Texture levelButtonTexture_3;
    private Texture levelButtonPressedTexture_3;
    private Texture levelButtonBlockedTexture;


    private Stage stage;
    private Table table;
    private final Insects game;
    private final AssetManager assetManager;

    public Array<Actor> buttonList;

    public int levelNumber = 0;

    private GameStatsHandler gameStatsHandler;
    private SelectLevelsType type;
    private int numberOfLevelsPerScreen = 21;



    public SelectLevelsScreen(Insects game, SelectLevelsType type) {
        this.game = game;
        assetManager = game.getAssetManager();
        buttonList = new Array<Actor>();
        gameStatsHandler = game.getGameStatsHandler();
        game.actionResolver.setTrackerScreenName("com.gstrzal.insects.screens.SelectLevelsScreen");
        this.type = type;
        if(type == SelectLevelsType.TWO) {
            levelNumber = numberOfLevelsPerScreen;
        }else if(type == SelectLevelsType.THREE) {
            levelNumber = numberOfLevelsPerScreen * 2;
        }
    }

    public void show() {
        stage = new Stage(new FitViewport(GameConfig.SCREEN_WIDTH_PX, GameConfig.SCREEN_HEIGHT_PX));
        Gdx.input.setInputProcessor(stage);
        backgroundTexture = assetManager.get(Constants.MENU_SELECT_LEVELS_BACKGROUND);
        Image background = new Image(backgroundTexture);
        stage.addActor(background);

        createBackButton();

        if(type == SelectLevelsType.ONE) {
            createRightScreenButton(SelectLevelsType.TWO);
        }else if(type == SelectLevelsType.TWO) {
            createLeftScreenButton(SelectLevelsType.ONE);
            createRightScreenButton(SelectLevelsType.THREE);
        }else if(type == SelectLevelsType.THREE) {
            createLeftScreenButton(SelectLevelsType.TWO);
        }

        //Table
        table = new Table();
        table.setWidth(stage.getWidth());
        table.align(Align.center | Align.top);

        table.setPosition(0,TABLE_Y);
        BitmapFont font = new BitmapFont(Gdx.files.internal(Constants.GAME_FONT),false);
        font.getData().setScale(3.0f);

        //button style 0
        levelButtonTexture_0 = assetManager.get(Constants.MENU_SELECT_LEVEL_BUTTON_0);
        levelButtonPressedTexture_0 = assetManager.get(Constants.MENU_SELECT_LEVEL_BUTTON_0_PRESSED);
        ImageTextButton.ImageTextButtonStyle levelImgStyle_0 = new ImageTextButton.ImageTextButtonStyle(
                new TextureRegionDrawable(new TextureRegion(levelButtonTexture_0)),
                new TextureRegionDrawable(new TextureRegion(levelButtonPressedTexture_0)),
                new TextureRegionDrawable(new TextureRegion(levelButtonTexture_0)),
                font
        );
        //button style 1
        levelButtonTexture_1 = assetManager.get(Constants.MENU_SELECT_LEVEL_BUTTON_1);
        levelButtonPressedTexture_1 = assetManager.get(Constants.MENU_SELECT_LEVEL_BUTTON_1_PRESSED);
        ImageTextButton.ImageTextButtonStyle levelImgStyle_1 = new ImageTextButton.ImageTextButtonStyle(
                new TextureRegionDrawable(new TextureRegion(levelButtonTexture_1)),
                new TextureRegionDrawable(new TextureRegion(levelButtonPressedTexture_1)),
                new TextureRegionDrawable(new TextureRegion(levelButtonTexture_1)),
                font
        );
        //button style 2
        levelButtonTexture_2 = assetManager.get(Constants.MENU_SELECT_LEVEL_BUTTON_2);
        levelButtonPressedTexture_2 = assetManager.get(Constants.MENU_SELECT_LEVEL_BUTTON_2_PRESSED);
        ImageTextButton.ImageTextButtonStyle levelImgStyle_2 = new ImageTextButton.ImageTextButtonStyle(
                new TextureRegionDrawable(new TextureRegion(levelButtonTexture_2)),
                new TextureRegionDrawable(new TextureRegion(levelButtonPressedTexture_2)),
                new TextureRegionDrawable(new TextureRegion(levelButtonTexture_2)),
                font
        );
        //button style 3
        levelButtonTexture_3 = assetManager.get(Constants.MENU_SELECT_LEVEL_BUTTON_3);
        levelButtonPressedTexture_3 = assetManager.get(Constants.MENU_SELECT_LEVEL_BUTTON_3_PRESSED);
        ImageTextButton.ImageTextButtonStyle levelImgStyle_3 = new ImageTextButton.ImageTextButtonStyle(
                new TextureRegionDrawable(new TextureRegion(levelButtonTexture_3)),
                new TextureRegionDrawable(new TextureRegion(levelButtonPressedTexture_3)),
                new TextureRegionDrawable(new TextureRegion(levelButtonTexture_3)),
                font
        );
        levelButtonBlockedTexture = assetManager.get(Constants.MENU_SELECT_LEVEL_BUTTON_BLOCKED);
        ImageTextButton.ImageTextButtonStyle levelImgBlockedStyle = new ImageTextButton.ImageTextButtonStyle(
                new TextureRegionDrawable(new TextureRegion(levelButtonBlockedTexture)),
                new TextureRegionDrawable(new TextureRegion(levelButtonBlockedTexture)),
                new TextureRegionDrawable(new TextureRegion(levelButtonBlockedTexture)),
                font
        );

        //add buttons to Table
        for(int row = 0; row< 3; row++) {
            for (int col = 0; col < 7; col++) {
                ++levelNumber;
                if(levelNumber<=GameConfig.GAME_MAX_LEVELS) {
                    ImageTextButton levelImg;
                    ImageTextButton.ImageTextButtonStyle tempimgTxtBStyle = new ImageTextButton.ImageTextButtonStyle();
                    if (levelNumber <= gameStatsHandler.getTopCLearedLevel() && levelNumber <= GameConfig.GAME_MAX_LEVELS) {

                        if (gameStatsHandler.getLevelSuccess(levelNumber) == 0)
                            tempimgTxtBStyle = levelImgStyle_0;
                        if (gameStatsHandler.getLevelSuccess(levelNumber) == 1)
                            tempimgTxtBStyle = levelImgStyle_1;
                        if (gameStatsHandler.getLevelSuccess(levelNumber) == 2)
                            tempimgTxtBStyle = levelImgStyle_2;
                        if (gameStatsHandler.getLevelSuccess(levelNumber) == 3)
                            tempimgTxtBStyle = levelImgStyle_3;

                        levelImg = new ImageTextButton(Integer.toString(levelNumber), tempimgTxtBStyle);
                    } else {
                        levelImg = new ImageTextButton("", levelImgBlockedStyle);
                    }
                    levelImg.setName(Integer.toString(levelNumber));
                    buttonList.add(levelImg);

                    levelImg.addListener(new ActorGestureListener() {
                        @Override
                        public void tap(InputEvent event, float x, float y, int count,
                                              int button) {
                            super.tap(event, x, y, count, button);
                            if (Integer.parseInt(event.getListenerActor().getName()) <= gameStatsHandler.getTopCLearedLevel()
                                    && Integer.parseInt(event.getListenerActor().getName()) <= GameConfig.GAME_MAX_LEVELS) {
                                if(Integer.parseInt(event.getListenerActor().getName()) == 1){
                                    ScreenManager.getInstance().showScreen(ScreenEnum.STORY_SCREEN, game);
                                }else {
                                    ScreenManager.getInstance().showScreen(ScreenEnum.GAME_SCREEN,
                                            game, Integer.parseInt(event.getListenerActor().getName()));
                                }
                            }
                        }
                    });
                    table.add(levelImg).pad(25);
                }
            }
            table.row();
        }


        stage.addActor(table);

    }

    private void createBackButton() {
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
                ScreenManager.getInstance().showScreen(ScreenEnum.MENU_SCREEN, game);
            }
        });
        stage.addActor(backBtn);
    }

    private void createRightScreenButton(final SelectLevelsType levelsType) {
        rightScreenButtonTexture = assetManager.get(Constants.MENU_SELECT_LEVEL_RIGHT_BUTTON);
        rightScreenButtonPressedTexture = assetManager.get(Constants.MENU_SELECT_LEVEL_RIGHT_PRESSED_BUTTON);
        ImageButton rightScreenBtn = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(rightScreenButtonTexture)),
                new TextureRegionDrawable(new TextureRegion(rightScreenButtonPressedTexture)));
        rightScreenBtn.setPosition(GameConfig.SCREEN_WIDTH_PX - 300, GameConfig.SCREEN_HEIGHT_PX/2 - 100);

        rightScreenBtn.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count,
                                  int button) {
                super.tap(event, x, y, count, button);
                ScreenManager.getInstance().showScreen(ScreenEnum.SELECT_LEVELS_SCREEM, game, levelsType);
            }
        });
        stage.addActor(rightScreenBtn);
    }

    private void createLeftScreenButton(final SelectLevelsType levelsType) {
        leftScreenButtonTexture = assetManager.get(Constants.MENU_SELECT_LEVEL_LEFT_BUTTON);
        leftScreenButtonPressedTexture = assetManager.get(Constants.MENU_SELECT_LEVEL_LEFT_PRESSED_BUTTON);
        ImageButton leftScreenBtn = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(leftScreenButtonTexture)),
                new TextureRegionDrawable(new TextureRegion(leftScreenButtonPressedTexture)));
        leftScreenBtn.setPosition(100, GameConfig.SCREEN_HEIGHT_PX/2 - 100);

        leftScreenBtn.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count,
                                  int button) {
                super.tap(event, x, y, count, button);
                ScreenManager.getInstance().showScreen(ScreenEnum.SELECT_LEVELS_SCREEM, game, levelsType);
            }
        });
        stage.addActor(leftScreenBtn);
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
