package com.gstrzal.insects.screens.GlidingAnt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gstrzal.insects.Insects;
import com.gstrzal.insects.config.Constants;
import com.gstrzal.insects.config.GameConfig;
import com.gstrzal.insects.entity.GlidingAnt.GlidingAnt;
import com.gstrzal.insects.entity.GlidingAnt.GlidingEntityType;
import com.gstrzal.insects.entity.GlidingAnt.GlidingFlower;
import com.gstrzal.insects.hud.GlidingAntController;
import com.gstrzal.insects.utils.GdxUtils;

/**
 * Created by lelo on 18/03/18.
 */

public class GlindingAntGameScreen extends ScreenAdapter {
    private static final float GAP_BETWEEN_FLOWERS = 200F;

    public static final String SCORE_TEXT = "Score: ";
    public static final String HIGH_SCORE_TEXT = "High Score: ";
    private static final String GAME_OVER_TEXT = "Game Over... Tap to restart!";

    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private Camera camera;
    private SpriteBatch batch;

    private BitmapFont bitmapFont;
    private GlyphLayout glyphLayout;

    private Texture background;
    private Texture ground;
    private Texture flowerBottom;
    private Texture flowerTop;
    private Texture glidingAntTexture;


    private final Insects game;
    private final AssetManager assetManager;
    private GlidingAntController controller;

    private GlidingEntityType entityType;

    private enum STATE {
        PLAYING, GAME_OVER
    }
    private STATE state = STATE.PLAYING;


    private int score = 0;
    private int highScore = 0;

    private GlidingAnt glidingAnt;
    private Array<GlidingFlower> flowers = new Array<GlidingFlower>();

    public GlindingAntGameScreen(Insects game, GlidingEntityType entityType) {
        this.game = game;
        this.assetManager = game.getAssetManager();
        controller = new GlidingAntController(game);
        this.entityType = entityType;
        game.actionResolver.setTrackerScreenName("com.gstrzal.insects.screens.GlidingAnt.GlindingAntGameScreen");
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        controller.resize(width, height);
    }
    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.position.set(GameConfig.DISPLAY_SCREEN_WIDTH_PX / 2, GameConfig.DISPLAY_SCREEN_HEIGHT_PX / 2, 0);
        camera.update();
        viewport = new FitViewport(GameConfig.DISPLAY_SCREEN_WIDTH_PX, GameConfig.DISPLAY_SCREEN_HEIGHT_PX, camera);
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        bitmapFont = new BitmapFont(Gdx.files.internal(Constants.GAME_FONT),false);
        glyphLayout = new GlyphLayout();

        background = assetManager.get(Constants.MINIGAMES_GLIDING_BACKGROUND);
        ground = assetManager.get(Constants.MINIGAMES_GLIDING_GROUND);
        flowerBottom = assetManager.get(Constants.MINIGAMES_GLIDING_ANT_FLOWER_BOTTOM);
        flowerTop = assetManager.get(Constants.MINIGAMES_GLIDING_ANT_FLOWER_TOP);

        if (entityType == GlidingEntityType.LBUG){
            glidingAntTexture = assetManager.get(Constants.MINIGAMES_GLIDING_LBUG_SPRITE);
        }else{
            glidingAntTexture = assetManager.get(Constants.MINIGAMES_GLIDING_ANT_SPRITE);
        }

        glidingAnt = new GlidingAnt(glidingAntTexture);
        glidingAnt.setPosition(GameConfig.DISPLAY_SCREEN_WIDTH_PX / 4, GameConfig.DISPLAY_SCREEN_HEIGHT_PX / 2);
        highScore = game.getGameStatsHandler().getGlidingAntHighScore();
    }
    @Override
    public void render(float delta) {
        switch(state) {
            case PLAYING: {
                update(delta);
            }
            break;
            case GAME_OVER: {
                checkForRestart();
            }
            break;
        }
        GdxUtils.clearScreen();
        draw();
        controller.draw();
        drawDebug();



    }

    private void drawDebug() {
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        if(GameConfig.debug){
            glidingAnt.drawDebug(shapeRenderer);
            for (GlidingFlower flower : flowers) {
                flower.drawDebug(shapeRenderer);
            }
        }
        shapeRenderer.end();
    }

    private void draw() {
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();
        batch.draw(background, 0, 0);
        drawFlowers();
        glidingAnt.draw(batch);
        drawScore();
        drawHighScore();
        drawGameOver();
        drawGround();
        batch.end();
    }

    private void blockGlidingAntLeavingTheWorld() {
        glidingAnt.setPosition(glidingAnt.getX(),
                MathUtils.clamp(glidingAnt.getY(), 0, GameConfig.DISPLAY_SCREEN_HEIGHT_PX - 30));
    }
    private void update(float delta) {
        updateGlidingAnt(delta);
        updateFlowers(delta);
        updateScore();
        game.getAudioHandler().updateMusic();
        if (checkForCollision()) {
            state = STATE.GAME_OVER;
        }
    }
    private void updateGlidingAnt(float delta){
        glidingAnt.update(delta);
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.UP)
                || controller.isActionPressed() || Gdx.input.justTouched()){
            glidingAnt.flyUp();
            game.getAudioHandler().playJumpSound();
            controller.setActionPressed(false);
        }
        blockGlidingAntLeavingTheWorld();

    }
    private void updateFlowers(float delta) {
        for (GlidingFlower flower : flowers) {
            flower.update(delta);
        }
        checkIfNewFlowerIsNeeded();
        removeFlowersIfPassed();
    }
    private void updateScore() {
        GlidingFlower flower = flowers.first();
        if (flower.getX() < glidingAnt.getX() && !flower.isPointClaimed())
        {
            flower.markPointClaimed();
            game.getAudioHandler().playPickupFlowerSound();
            score++;
        }
    }
    private void createNewFlower() {
        GlidingFlower newFlower = new GlidingFlower(flowerBottom, flowerTop);
        newFlower.setPosition(GameConfig.DISPLAY_SCREEN_WIDTH_PX + GlidingFlower.WIDTH);
        flowers.add(newFlower);
    }
    private void checkIfNewFlowerIsNeeded() {
        if (flowers.size == 0) {
            createNewFlower();
        } else {
            GlidingFlower flower = flowers.peek();
            if (flower.getX() < GameConfig.DISPLAY_SCREEN_WIDTH_PX - GAP_BETWEEN_FLOWERS) {
                createNewFlower();
            }
        }
    }
    private void removeFlowersIfPassed() {
        if (flowers.size > 0) {
            GlidingFlower firstFlower = flowers.first();
            if (firstFlower.getX() < - GlidingFlower.WIDTH) {
                flowers.removeValue(firstFlower, true);
            }
        }
    }
    private void drawFlowers() {
        for (GlidingFlower flower : flowers) {
            flower.draw(batch);
        }
    }

    private boolean checkForCollision() {
        for (GlidingFlower flower : flowers) {
            if (flower.isGlidingAntColliding(glidingAnt)) {
                game.getAudioHandler().playHitHurtSound();
                return true;
            }
        }
        if(glidingAnt.getY() < 90) {
            game.getAudioHandler().playHitHurtSound();
            return true;
        }
        return false;
    }
    private void restart() {
        glidingAnt.setPosition(GameConfig.DISPLAY_SCREEN_WIDTH_PX / 4, GameConfig.DISPLAY_SCREEN_HEIGHT_PX / 2);
        glidingAnt.glidingAntReset();
        flowers.clear();
        score = 0;
        highScore = game.getGameStatsHandler().getGlidingAntHighScore();
    }
    private void drawScore() {
        String scoreAsString = Integer.toString(score);
        glyphLayout.setText(bitmapFont, scoreAsString);
        bitmapFont.draw(batch, scoreAsString,
                (viewport.getWorldWidth()/2 - glyphLayout.width / 2),
                (viewport.getWorldHeight() - 10) - glyphLayout.height / 2);
    }
    private void drawHighScore() {
        String scoreAsString = Integer.toString(highScore);
        String message = HIGH_SCORE_TEXT + scoreAsString;
        glyphLayout.setText(bitmapFont, message);
        bitmapFont.draw(batch, message,
                10,
                (viewport.getWorldHeight() - 10) - glyphLayout.height / 2);
    }

    private void drawGameOver() {
        if (state == STATE.GAME_OVER) {

            game.getGameStatsHandler().saveGlidingAntHighScore(score);


            String scoreAsString = Integer.toString(score);
            String highScoreAsString = Integer.toString(game.getGameStatsHandler().getGlidingAntHighScore());

            drawTextOnScreenCenter(HIGH_SCORE_TEXT + highScoreAsString, 0, 70);
            drawTextOnScreenCenter(SCORE_TEXT + scoreAsString, 0 , 35);
            drawTextOnScreenCenter(GAME_OVER_TEXT, 0, 0);
        }
    }

    private void drawTextOnScreenCenter(String text, float differenceX, float differenceY){
        bitmapFont.setColor(Color.BLACK);
        glyphLayout.setText(bitmapFont, text);
        bitmapFont.draw(batch, text,
                ((viewport.getWorldWidth() - glyphLayout.width) / 2) + differenceX,
                ((viewport.getWorldHeight() - glyphLayout.height) / 2) + differenceY);
        bitmapFont.setColor(Color.WHITE);
    }
    private void drawGround(){
        batch.draw(ground,0,0);
    }

    private void checkForRestart() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)
                || Gdx.input.isTouched()){
            controller.setActionPressed(false);
            doRestart();
        }
    }

    private void doRestart() {
        state = STATE.PLAYING;
        restart();
    }
}
