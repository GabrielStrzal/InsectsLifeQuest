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
import com.gstrzal.insects.screens.enums.SelectLevelsType;
import com.gstrzal.insects.tools.ScreenEnum;
import com.gstrzal.insects.tools.ScreenManager;
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
    private Texture instructionsTexture;
    private Texture instructionsPressTexture;
    private Texture minigamesTexture;
    private Texture minigamesPressTexture;
    private BitmapFont font;

    private Stage stage;
    private final Insects game;
    private final AssetManager assetManager;



    public MenuScreen(Insects game) {
        this.game = game;
        this.assetManager = game.getAssetManager();
        game.actionResolver.setTrackerScreenName("com.gstrzal.insects.screens.MenuScreen");
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

        game.backgroundAudioID = game.getAudioHandler().playBackGroundMusic();



        //Play Button
        playTexture = assetManager.get(Constants.MENU_PLAYBUTTON);
        playPressTexture = assetManager.get(Constants.MENU_PLAYBUTTON_PRESSED);
        ImageButton play = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(playTexture)),
                new TextureRegionDrawable(new TextureRegion(playPressTexture)));
        play.setPosition(GameConfig.SCREEN_WIDTH_PX/2 - playTexture.getWidth()/2, PLAY_BUTTON_Y);

        play.addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int count,
                            int button) {
                super.touchDown(event, x, y, count, button);
                ScreenManager.getInstance().showScreen(ScreenEnum.STORY_SCREEN, game);
            }
        });
        stage.addActor(play);

        //Options Button
        optionsTexture = assetManager.get(Constants.MENU_OPTIONS);
        optionsPressTexture = assetManager.get(Constants.MENU_OPTIONS_PRESSED);
        ImageButton options = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(optionsTexture)),
                new TextureRegionDrawable(new TextureRegion(optionsPressTexture)));
        options.setPosition(OPTIONS_BUTTON_X, PLAY_BUTTON_Y);

        options.addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int count,
                            int button) {
                super.touchDown(event, x, y, count, button);
                ScreenManager.getInstance().showScreen(ScreenEnum.OPTIONS_SCREEN, game);
            }
        });
        stage.addActor(options);


        //Intructions Button
        instructionsTexture = assetManager.get(Constants.MENU_INSTRUCTIONS);
        instructionsPressTexture = assetManager.get(Constants.MENU_INSTRUCTIONS_PRESSED);
        ImageButton instructions = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(instructionsTexture)),
                new TextureRegionDrawable(new TextureRegion(instructionsPressTexture)));
        instructions.setPosition(OPTIONS_BUTTON_X, PLAY_BUTTON_Y + 210);

        instructions.addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int count,
                            int button) {
                super.touchDown(event, x, y, count, button);
                ScreenManager.getInstance().showScreen(ScreenEnum.INSTRUCTIONS_SCREEN, game);
            }
        });
        stage.addActor(instructions);

        //Select Levels Button
        selectLevelsTexture = assetManager.get(Constants.MENU_SELECT_LEVEL);
        selectLevelsPressTexture = assetManager.get(Constants.MENU_SELECT_LEVEL_PRESSED);
        ImageButton selectLevels = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(selectLevelsTexture)),
                new TextureRegionDrawable(new TextureRegion(selectLevelsPressTexture)));
        selectLevels.setPosition(SELECT_LEVELS_BUTTON_X, PLAY_BUTTON_Y);

        selectLevels.addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int count,
                            int button) {
                super.touchDown(event, x, y, count, button);
                ScreenManager.getInstance().showScreen(ScreenEnum.SELECT_LEVELS_SCREEM, game, SelectLevelsType.ONE);
            }
        });
        stage.addActor(selectLevels);

        //Minigames Button
        minigamesTexture = assetManager.get(Constants.MENU_MINIGAMES);
        minigamesPressTexture = assetManager.get(Constants.MENU_MINIGAMES_PRESSED);
        ImageButton minigames = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(minigamesTexture)),
                new TextureRegionDrawable(new TextureRegion(minigamesPressTexture)));
        minigames.setPosition(SELECT_LEVELS_BUTTON_X, PLAY_BUTTON_Y + 210);

        minigames.addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int count,
                            int button) {
                super.touchDown(event, x, y, count, button);
                ScreenManager.getInstance().showScreen(ScreenEnum.MINIGAMES_SCREEN, game);
            }
        });
        stage.addActor(minigames);



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
                (GameConfig.SCREEN_WIDTH_PX * .87f), (GameConfig.SCREEN_HEIGHT_PX * .95f));
        stage.getBatch().end();
        game.getAudioHandler().updateMusic();

    }

    @Override
    public void dispose() {
        stage.dispose();
        font.dispose();
    }

}
