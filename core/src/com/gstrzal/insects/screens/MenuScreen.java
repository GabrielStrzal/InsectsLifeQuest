package com.gstrzal.insects.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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

public class MenuScreen extends ScreenAdapter {

    private static final int PLAY_BUTTON_Y = (int)GameConfig.SCREEN_HEIGHT_PX/5;
    private static final int BUTTONS_Y = PLAY_BUTTON_Y + 24;
    private static final int OPTIONS_BUTTON_X = 400;
    private static final int SELECT_LEVELS_BUTTON_X = 2100;

    private Texture backgroundTexture;
    private Texture playTexture;
    private Texture playPressTexture;
    private Texture optionsTexture;
    private Texture optionsPressTexture;
    private Texture selectLevelsTexture;
    private Texture selectLevelsPressTexture;
    private BitmapFont font;

    private Stage stage;
    private final Insects game;
    private final AssetManager assetManager;



    public MenuScreen(Insects game) {
        this.game = game;
        this.assetManager = game.getAssetManager();
    }

    public void show() {
        stage = new Stage(new FitViewport(GameConfig.SCREEN_WIDTH_PX, GameConfig.SCREEN_HEIGHT_PX));
        Gdx.input.setInputProcessor(stage);
        backgroundTexture = assetManager.get(Constants.MENU_BACKGROUND);
        Image background = new Image(backgroundTexture);
        stage.addActor(background);
        font = new BitmapFont(Gdx.files.internal(Constants.GAME_FONT),false);
        font.setColor(Color.DARK_GRAY);
        font.getData().setScale(2f);



        //Play Button
        playTexture = assetManager.get(Constants.MENU_PLAYBUTTON);
        playPressTexture = assetManager.get(Constants.MENU_PLAYBUTTON_PRESSED);
        ImageButton play = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(playTexture)),
                new TextureRegionDrawable(new TextureRegion(playPressTexture)));
        play.setPosition(GameConfig.SCREEN_WIDTH_PX/2 - playTexture.getWidth()/2, PLAY_BUTTON_Y);

        play.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count,
                            int button) {
                super.tap(event, x, y, count, button);
                game.setScreen(new GameScreen(game, 1));
                dispose();
            }
        });
        stage.addActor(play);

        //Options Button
        optionsTexture = assetManager.get(Constants.MENU_OPTIONS);
        optionsPressTexture = assetManager.get(Constants.MENU_OPTIONS_PRESSED);
        ImageButton options = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(optionsTexture)),
                new TextureRegionDrawable(new TextureRegion(optionsPressTexture)));
        options.setPosition(OPTIONS_BUTTON_X, BUTTONS_Y);

        options.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count,
                            int button) {
                super.tap(event, x, y, count, button);
                game.setScreen(new OptionsScreen(game));
                dispose();
            }
        });
        stage.addActor(options);

        //Select Levels Button
        selectLevelsTexture = assetManager.get(Constants.MENU_SELECT_LEVEL);
        selectLevelsPressTexture = assetManager.get(Constants.MENU_SELECT_LEVEL_PRESSED);
        ImageButton selectLevels = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(selectLevelsTexture)),
                new TextureRegionDrawable(new TextureRegion(selectLevelsPressTexture)));
        selectLevels.setPosition(SELECT_LEVELS_BUTTON_X, BUTTONS_Y);

        selectLevels.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count,
                            int button) {
                super.tap(event, x, y, count, button);
                game.setScreen(new SelectLevelsScreen(game));
                dispose();
            }
        });
        stage.addActor(selectLevels);



    }
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    public void render(float delta) {
        GdxUtils.clearScreen();
        stage.act(delta);
        stage.draw();
        stage.getBatch().begin();
        font.draw(stage.getBatch(), GameConfig.GAME_VERSION,
                (GameConfig.SCREEN_WIDTH_PX/10)*9, (GameConfig.SCREEN_HEIGHT_PX/20)*19);
        stage.getBatch().end();

    }

    @Override
    public void dispose() {
        stage.dispose();
        font.dispose();
    }

}
