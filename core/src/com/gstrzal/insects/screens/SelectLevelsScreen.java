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
    private Texture levelButtonTexture;
    private Texture levelButtonPressedTexture;
    private Texture levelButtonBlockedTexture;


    private Stage stage;
    private Table table;
    private final Insects game;
    private final AssetManager assetManager;

    public Array<Actor> buttonList;

    public int levelNumber = 0;



    public SelectLevelsScreen(Insects game) {
        this.game = game;
        assetManager = game.getAssetManager();
        buttonList = new Array<Actor>();
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

        //Table
        table = new Table();
        table.setWidth(stage.getWidth());
        table.align(Align.center | Align.top);

        table.setPosition(0,TABLE_Y);

        //button style
        levelButtonTexture = assetManager.get(Constants.MENU_SELECT_LEVEL_BUTTON);
        levelButtonPressedTexture = assetManager.get(Constants.MENU_SELECT_LEVEL_BUTTON_PRESSED);
        ImageTextButton.ImageTextButtonStyle levelImgStyle = new ImageTextButton.ImageTextButtonStyle(
                new TextureRegionDrawable(new TextureRegion(levelButtonTexture)),
                new TextureRegionDrawable(new TextureRegion(levelButtonPressedTexture)),
                new TextureRegionDrawable(new TextureRegion(levelButtonTexture)),
                new BitmapFont(Gdx.files.internal(Constants.GAME_FONT),false)
        );
        levelButtonBlockedTexture = assetManager.get(Constants.MENU_SELECT_LEVEL_BUTTON_BLOCKED);
        ImageTextButton.ImageTextButtonStyle levelImgBlockedStyle = new ImageTextButton.ImageTextButtonStyle(
                new TextureRegionDrawable(new TextureRegion(levelButtonBlockedTexture)),
                new TextureRegionDrawable(new TextureRegion(levelButtonBlockedTexture)),
                new TextureRegionDrawable(new TextureRegion(levelButtonBlockedTexture)),
                new BitmapFont(Gdx.files.internal(Constants.GAME_FONT),false)
        );

        //add buttons to Table
        for(int row = 0; row< 2; row++) {
            for (int col = 0; col < 5; col++) {
                ++levelNumber;
                ImageTextButton levelImg;
                if(levelNumber <= GameConfig.GAME_MAX_LEVELS) {
                    levelImg = new ImageTextButton(Integer.toString(levelNumber), levelImgStyle);
                }else{
                    levelImg = new ImageTextButton("", levelImgBlockedStyle);
                }
                levelImg.setName(Integer.toString(levelNumber));
                buttonList.add(levelImg);

                levelImg.addListener(new ActorGestureListener() {
                    @Override
                    public void tap(InputEvent event, float x, float y, int count,
                                    int button) {
                        super.tap(event, x, y, count, button);
                        if(Integer.parseInt(event.getListenerActor().getName()) <= GameConfig.GAME_MAX_LEVELS) {
                            game.setScreen(new GameScreen(game, Integer.parseInt(event.getListenerActor().getName())));
                            dispose();
                        }
                    }
                });
                table.add(levelImg).pad(25);
            }
            table.row();
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
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
