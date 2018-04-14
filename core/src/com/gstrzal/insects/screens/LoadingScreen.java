package com.gstrzal.insects.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gstrzal.insects.Insects;
import com.gstrzal.insects.config.Constants;
import com.gstrzal.insects.config.GameConfig;
import com.gstrzal.insects.utils.GdxUtils;

/**
 * Created by lelo on 15/01/18.
 * based on "LibGDX Game Development By Example"
 */

public class LoadingScreen extends ScreenAdapter{

    private static final float PROGRESS_BAR_WIDTH = 400;
    private static final float PROGRESS_BAR_HEIGHT = 100;
    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private Camera camera;
    private float progress = 0;
    private final Insects insects;
    protected final AssetManager assetManager;

    public LoadingScreen(Insects insects) {
        this.insects = insects;
        this.assetManager = insects.getAssetManager();
        insects.actionResolver.setTrackerScreenName("com.gstrzal.insects.screens.LoadingScreen");
    }
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.position.set(GameConfig.SCREEN_WIDTH_PX /2, GameConfig.SCREEN_HEIGHT_PX , 0);
        camera.update();
        viewport = new FitViewport(GameConfig.SCREEN_WIDTH_PX, GameConfig.SCREEN_HEIGHT_PX, camera);
        shapeRenderer = new ShapeRenderer();
        assetManager.load(Constants.MENU_BACKGROUND, Texture.class);
        assetManager.load(Constants.MENU_SELECT_LEVELS_BACKGROUND, Texture.class);
        assetManager.load(Constants.YOU_WON_BACKGROUND, Texture.class);
        assetManager.load(Constants.MENU_INSTRUCTIONS_BACKGROUND, Texture.class);
        assetManager.load(Constants.MENU_MADE_BY, Texture.class);
        assetManager.load(Constants.MENU_PLAYBUTTON, Texture.class);
        assetManager.load(Constants.MENU_PLAYBUTTON_PRESSED, Texture.class);
        assetManager.load(Constants.MENU_OPTIONS, Texture.class);
        assetManager.load(Constants.MENU_OPTIONS_PRESSED, Texture.class);
        assetManager.load(Constants.MENU_INSTRUCTIONS, Texture.class);
        assetManager.load(Constants.MENU_INSTRUCTIONS_PRESSED, Texture.class);
        assetManager.load(Constants.MENU_SELECT_LEVEL, Texture.class);
        assetManager.load(Constants.MENU_SELECT_LEVEL_PRESSED, Texture.class);
        assetManager.load(Constants.MENU_MINIGAMES, Texture.class);
        assetManager.load(Constants.MENU_MINIGAMES_PRESSED, Texture.class);
        assetManager.load(Constants.MENU_SELECT_LEVEL_BACK_BUTTON, Texture.class);
        assetManager.load(Constants.MENU_SELECT_LEVEL_BACK_BUTTON_PRESSED, Texture.class);
        assetManager.load(Constants.MENU_SELECT_LEVEL_RIGHT_BUTTON, Texture.class);
        assetManager.load(Constants.MENU_SELECT_LEVEL_LEFT_BUTTON, Texture.class);
        assetManager.load(Constants.MENU_SELECT_LEVEL_BUTTON_0, Texture.class);
        assetManager.load(Constants.MENU_SELECT_LEVEL_BUTTON_0_PRESSED, Texture.class);
        assetManager.load(Constants.MENU_SELECT_LEVEL_BUTTON_1, Texture.class);
        assetManager.load(Constants.MENU_SELECT_LEVEL_BUTTON_1_PRESSED, Texture.class);
        assetManager.load(Constants.MENU_SELECT_LEVEL_BUTTON_2, Texture.class);
        assetManager.load(Constants.MENU_SELECT_LEVEL_BUTTON_2_PRESSED, Texture.class);
        assetManager.load(Constants.MENU_SELECT_LEVEL_BUTTON_3, Texture.class);
        assetManager.load(Constants.MENU_SELECT_LEVEL_BUTTON_3_PRESSED, Texture.class);
        assetManager.load(Constants.MENU_SELECT_LEVEL_BUTTON_BLOCKED, Texture.class);
        assetManager.load(Constants.MENU_STORY_SKIP_BUTTON, Texture.class);
        assetManager.load(Constants.GAME_BACKGROUND, Texture.class);
        assetManager.load(Constants.GAME_OVER_POPUP, Texture.class);
        assetManager.load(Constants.LEVEL_CLEARED_POPUP_0, Texture.class);
        assetManager.load(Constants.LEVEL_CLEARED_POPUP_1, Texture.class);
        assetManager.load(Constants.LEVEL_CLEARED_POPUP_2, Texture.class);
        assetManager.load(Constants.LEVEL_CLEARED_POPUP_3, Texture.class);
        assetManager.load(Constants.CONTROLLER_AUDIO, Texture.class);
        assetManager.load(Constants.CONTROLLER_AUDIO_PRESSED, Texture.class);
        assetManager.load(Constants.CONTROLLER_RIGHT, Texture.class);
        assetManager.load(Constants.CONTROLLER_RIGHT_PRESSED, Texture.class);
        assetManager.load(Constants.CONTROLLER_LEFT, Texture.class);
        assetManager.load(Constants.CONTROLLER_LEFT_PRESSED, Texture.class);
        assetManager.load(Constants.CONTROLLER_ACTIONS, Texture.class);
        assetManager.load(Constants.CONTROLLER_ACTIONS_PRESSED, Texture.class);
        assetManager.load(Constants.CONTROLLER_ACTIONS_BLOCKED, Texture.class);
        assetManager.load(Constants.CONTROLLER_CONTROLS, Texture.class);
        assetManager.load(Constants.CONTROLLER_CONTROLS_PRESSED, Texture.class);
        assetManager.load(Constants.CONTROLLER_CONTROLS_CHECKED, Texture.class);
        assetManager.load(Constants.CONTROLLER_INSECT_SWITCH_TRANSPARENT, Texture.class);
        assetManager.load(Constants.CONTROLLER_LEVEL_RESTART, Texture.class);
        assetManager.load(Constants.CONTROLLER_LEVEL_RESTART_PRESSED, Texture.class);
        assetManager.load(Constants.MINIGAMES_GLIDING_ANT_LOGO, Texture.class);
        assetManager.load(Constants.MINIGAMES_GLIDING_ANT_LOGO_PRESSED, Texture.class);
        assetManager.load(Constants.MINIGAMES_GLIDING_LBUG_LOGO, Texture.class);
        assetManager.load(Constants.MINIGAMES_GLIDING_LBUG_LOGO_PRESSED, Texture.class);
        assetManager.load(Constants.MINIGAMES_BLOCKED_ANT, Texture.class);
        assetManager.load(Constants.MINIGAMES_BLOCKED_LBUG, Texture.class);
        assetManager.load(Constants.MINIGAMES_GLIDING_ANT_FLOWER_BOTTOM, Texture.class);
        assetManager.load(Constants.MINIGAMES_GLIDING_ANT_FLOWER_TOP, Texture.class);
        assetManager.load(Constants.MINIGAMES_GLIDING_ANT_SPRITE, Texture.class);
        assetManager.load(Constants.MINIGAMES_GLIDING_LBUG_SPRITE, Texture.class);
        assetManager.load(Constants.MINIGAMES_GLIDING_BACKGROUND, Texture.class);
        assetManager.load(Constants.MINIGAMES_GLIDING_GROUND, Texture.class);
        assetManager.load(Constants.GAME_FONT, BitmapFont.class);
        assetManager.load(Constants.LEVEL_1, TiledMap.class);
        assetManager.load(Constants.LEVEL_2, TiledMap.class);
        assetManager.load(Constants.LEVEL_3, TiledMap.class);
        assetManager.load(Constants.LEVEL_4, TiledMap.class);
        assetManager.load(Constants.LEVEL_5, TiledMap.class);
        assetManager.load(Constants.LEVEL_6, TiledMap.class);
        assetManager.load(Constants.LEVEL_7, TiledMap.class);
        assetManager.load(Constants.LEVEL_8, TiledMap.class);
        assetManager.load(Constants.LEVEL_9, TiledMap.class);
        assetManager.load(Constants.LEVEL_10, TiledMap.class);
        assetManager.load(Constants.LEVEL_11, TiledMap.class);
        assetManager.load(Constants.LEVEL_12, TiledMap.class);
        assetManager.load(Constants.LEVEL_13, TiledMap.class);
        assetManager.load(Constants.LEVEL_14, TiledMap.class);
        assetManager.load(Constants.LEVEL_15, TiledMap.class);
        assetManager.load(Constants.LEVEL_16, TiledMap.class);
        assetManager.load(Constants.LEVEL_17, TiledMap.class);
        assetManager.load(Constants.LEVEL_18, TiledMap.class);
        assetManager.load(Constants.LEVEL_19, TiledMap.class);
        assetManager.load(Constants.LEVEL_20, TiledMap.class);
        assetManager.load(Constants.LEVEL_21, TiledMap.class);
        assetManager.load(Constants.LEVEL_22, TiledMap.class);
        assetManager.load(Constants.LEVEL_23, TiledMap.class);
        assetManager.load(Constants.LEVEL_24, TiledMap.class);
        assetManager.load(Constants.LEVEL_25, TiledMap.class);
        assetManager.load(Constants.JOANINHA, Texture.class);
        assetManager.load(Constants.BESOURO, Texture.class);
        assetManager.load(Constants.ANT, Texture.class);
        assetManager.load(Constants.FLOWER, Texture.class);
        assetManager.load(Constants.PUSH_BLOCK, Texture.class);
        assetManager.load(Constants.SWITCH_ON, Texture.class);
        assetManager.load(Constants.SWITCH_OFF, Texture.class);
        assetManager.load(Constants.LEVEL_END_BLOCK, Texture.class);
        assetManager.load(Constants.NEXT_JOANINHA, Texture.class);
        assetManager.load(Constants.NEXT_ANT, Texture.class);
        assetManager.load(Constants.NEXT_BESOURO, Texture.class);

        //Story
        assetManager.load(Constants.STORY_01, Texture.class);
        assetManager.load(Constants.STORY_02, Texture.class);
        assetManager.load(Constants.STORY_03, Texture.class);
        assetManager.load(Constants.STORY_04, Texture.class);
        assetManager.load(Constants.STORY_05, Texture.class);



        //Info
        assetManager.load(Constants.INFO_1, Texture.class);
        assetManager.load(Constants.INFO_4, Texture.class);
        assetManager.load(Constants.INFO_6, Texture.class);



        assetManager.load(Constants.AUDIO_BACKGROUND_MUSIC, Music.class);
        assetManager.load(Constants.AUDIO_JUMP, Sound.class);
        assetManager.load(Constants.AUDIO_HIT_HURT, Sound.class);
        assetManager.load(Constants.AUDIO_END_LEVEL, Sound.class);
        assetManager.load(Constants.AUDIO_CHANGE_INSECT, Sound.class);
        assetManager.load(Constants.AUDIO_PICKUP_FLOWER, Sound.class);
        assetManager.load(Constants.AUDIO_WARP, Sound.class);
        assetManager.load(Constants.AUDIO_SWITCH, Sound.class);
        assetManager.load(Constants.AUDIO_PUSH_BLOCK, Sound.class);
    }
    @Override
    public void render(float delta) {
        update();
        GdxUtils.clearScreen();
        draw();
    }
    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
    private void update() {
        if (assetManager.update()) {
            insects.setScreen(new MenuScreen(insects));
        } else {
            progress = assetManager.getProgress();
        }
    }
    private void draw() {
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(
                (GameConfig.SCREEN_WIDTH_PX - PROGRESS_BAR_WIDTH) / 2,
                (GameConfig.SCREEN_HEIGHT_PX - PROGRESS_BAR_HEIGHT / 2),
                progress * PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT);
        shapeRenderer.end();

    }
}
