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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.gstrzal.insects.Insects;
import com.gstrzal.insects.assets.AssetPaths;
import com.gstrzal.insects.config.GameConfig;

/**
 * Created by lelo on 18/12/17.
 */

public class MenuScreen extends ScreenAdapter {

    private static final int PLAY_BUTTON_X = 510;
    private static final int PLAY_BUTTON_Y = 330;

    private Texture backgroundTexture;
    private Texture playTexture;
    private Texture playPressTexture;

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
        backgroundTexture = assetManager.get(AssetPaths.MENU_BACKGROUND);
        Image background = new Image(backgroundTexture);
        stage.addActor(background);

        playTexture = assetManager.get(AssetPaths.MENU_PLAYBUTTON);
        playPressTexture = assetManager.get(AssetPaths.MENU_PLAYBUTTON_ACTIVE);
        ImageButton play = new ImageButton(
                new TextureRegionDrawable(new TextureRegion(playTexture)),
                new TextureRegionDrawable(new TextureRegion(playPressTexture)));
        play.setPosition(PLAY_BUTTON_X, PLAY_BUTTON_Y);

        play.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count,
                            int button) {
                super.tap(event, x, y, count, button);
                game.setScreen(new GameScreen(game));
                dispose();
            }
        });
        stage.addActor(play);

    }
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
        playTexture.dispose();
        playPressTexture.dispose();
    }
}
