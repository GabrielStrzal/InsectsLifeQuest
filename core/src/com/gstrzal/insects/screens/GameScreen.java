package com.gstrzal.insects.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gstrzal.insects.Insects;
import com.gstrzal.insects.config.Constants;
import com.gstrzal.insects.config.GameConfig;
import com.gstrzal.insects.entity.*;
import com.gstrzal.insects.hud.Controller;
import com.gstrzal.insects.tools.B2WorldCreator;
import com.gstrzal.insects.tools.WorldContactListener;
import com.gstrzal.insects.utils.GdxUtils;
import com.gstrzal.insects.utils.ViewportUtils;
import com.gstrzal.insects.utils.debug.DebugCameraController;


/**
 * Created by Gabriel on 03/09/2017.
 */

public class GameScreen implements Screen{

    private static final Logger log = new Logger(GameScreen.class.getName(), Logger.DEBUG);

    private Insects game;


    private OrthographicCamera gamecam;
    private OrthographicCamera b2dcam;
    private Viewport gamePort;
    private Insect insectPlayer;

    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    private ShapeRenderer renderer;

    private World world;
    private Box2DDebugRenderer b2dr;
    private AssetManager assetManager;

    private boolean debugGrid = false;


    private boolean isDirectionRight = true;
    private boolean characterChange;

    private float xVelocity = 1f;
    private float gravity = -10f;
    private float jumpSpeed = 250f;

    //camera debug
    private DebugCameraController debugCameraController;

    private WorldContactListener worldContactListener;
    private B2WorldCreator b2World;
    private int currentLevel;
    private boolean waitRestartComplete;
    private long levelStopTime;
    private boolean isDirectionUp;


    private enum STATE {
        PLAYING, GAME_OVER, LEVEL_CLEARED, PAUSED
    }
    private STATE state;

    private BitmapFont bitmapFont;
    private GlyphLayout layout = new GlyphLayout();
    private Controller controller;

    private Sound gameSound;



    public GameScreen(Insects game, int level){

        this.assetManager = game.getAssetManager();
        state = STATE.PLAYING;

        this.game = game;
        gamecam = new OrthographicCamera();
        b2dcam = new OrthographicCamera();
        b2dcam.setToOrtho(false,Insects.V_WIDTH / Insects.PPM, Insects.V_HEIGHT/ Insects.PPM);
        gamePort = new FitViewport(Insects.V_WIDTH / Insects.PPM, Insects.V_HEIGHT/ Insects.PPM, gamecam);


        this.currentLevel = level;
        world = new World(new Vector2(0, gravity), true);
        b2dr = new Box2DDebugRenderer();

        map = assetManager.get(Constants.LEVEL+ level + Constants.TMX);
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / Insects.PPM);
        gamecam.position.set(gamePort.getWorldWidth()/2,gamePort.getWorldHeight()/2, 0);
        insectPlayer = new LBug(world, (Texture) assetManager.get(Constants.JOANINHA));

        b2World = new B2WorldCreator(world, map, game);
        worldContactListener = new WorldContactListener();
        world.setContactListener(worldContactListener);

//        bitmapFont = assetManager.get(Constants.GAME_FONT);
        bitmapFont = new BitmapFont();

        controller = new Controller(game);

        gameSound = assetManager.get(Constants.AUDIO_001);

        gameSound.play();
        if(!game.isAudioOn()) {
            gameSound.pause();
        }


    }
    @Override
    public void show() {

        renderer = new ShapeRenderer();

        //camera debug
        debugCameraController = new DebugCameraController();
        debugCameraController.setStartPosition(GameConfig.SCREEN_WIDTH_PX/2, GameConfig.SCREEN_HEIGHT_PX/2);

    }

    public void update(float dt){
        handleInput(dt);

        collectFlowers();

        world.step(1/60f, 6, 2);
        insectPlayer.update(dt);
        gamecam.update();
        b2dcam.update();
        mapRenderer.setView(b2dcam);

        if(game.isAudioOn()){
            gameSound.resume();
        }else{
            gameSound.pause();
        }

    }

    private void collectFlowers() {
        Array<Body> bodies = worldContactListener.getBodiesToRemove();
        for(int i = 0; i < bodies.size; i++) {
            Body b = bodies.get(i);
            b2World.flowers.removeValue((Flower) b.getUserData(), true);
            world.destroyBody(bodies.get(i));
//            TODO: add Flower number
        }
        bodies.clear();
    }

    private void handleInput(float dt) {

        if(isDirectionRight){
            insectPlayer.b2body.setLinearVelocity(xVelocity, insectPlayer.b2body.getLinearVelocity().y);
        }else{
            insectPlayer.b2body.setLinearVelocity(-xVelocity, insectPlayer.b2body.getLinearVelocity().y);
        }

        if(isDirectionUp && worldContactListener.isOnGrounds() && insectPlayer instanceof LBug){
            insectPlayer.b2body.applyForceToCenter(0,jumpSpeed,true);
            isDirectionUp = false;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            if(worldContactListener.isOnGrounds() && insectPlayer instanceof LBug){
                isDirectionUp = true;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            isDirectionRight = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            isDirectionRight = false;
        }

        //Character Change
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
            if(worldContactListener.isOnGrounds()) {
                characterChange = true;
            }
        }


        //Character Change
        if (characterChange) {
            Insect tempInsc = insectPlayer;
            if (tempInsc instanceof LBug) {
                insectPlayer = new Ant(world, (Texture) assetManager.get(Constants.ANT),
                        tempInsc.b2body.getPosition().x, tempInsc.b2body.getPosition().y);
            } else {
                insectPlayer = new LBug(world, (Texture) assetManager.get(Constants.JOANINHA),
                        tempInsc.b2body.getPosition().x, tempInsc.b2body.getPosition().y);
            }
            tempInsc.dispose();
            characterChange = false;
        }


        //Touch
        if(controller.isRightPressed()){
            isDirectionRight = true;
            controller.setRightPressed(false);
        }else if(controller.isLeftPressed()) {
            isDirectionRight = false;
            controller.setLeftPressed(false);
        }
        if(controller.isActionPressed()){
            if(worldContactListener.isOnGrounds() && insectPlayer instanceof LBug){
                isDirectionUp = true;
            }
            controller.setActionPressed(false);
        }
        if(controller.isInsectSwitchPressed()){
            if(worldContactListener.isOnGrounds()) {
                characterChange = true;
            }
            controller.setInsectSwitchPressed(false);
        }

        //Turn debugGrid on/of
        if (Gdx.input.isKeyJustPressed(Input.Keys.G)){
            debugGrid = debugGrid? false: true;
        }

    }

    @Override
    public void render(float delta) {

        switch(state) {
            case PLAYING: {
                update(delta);
                checkLevelCompleted();
                checkGameOver();
            }
            break;
            case GAME_OVER: {
                waitForRestart();
            }
            break;
            case LEVEL_CLEARED: {
                waitForRestart();
            }
            break;
            case PAUSED: {

            }
            break;
        }

        //camera debug
        debugCameraController.handleDebugInput(delta);
        debugCameraController.applyTo(gamecam);


        GdxUtils.clearScreen();
        mapRenderer.render();

        if(GameConfig.debug) {
            b2dr.render(world, b2dcam.combined);
        }
        draw();

        renderDebug();
        doRestartIfGameOver();
        changeLevelIfLevelCompleted();
    }

    private void draw() {
        game.batch.setProjectionMatrix(b2dcam.combined);
        game.batch.begin();
        // draw flowers
        for(int i = 0; i < b2World.flowers.size; i++) {
            b2World.flowers.get(i).render(game.batch);
        }
        insectPlayer.draw(game.batch);
        drawGameOver();
        drawLevelCleared();
        game.batch.end();
        controller.draw();
    }

    private void checkLevelCompleted() {
        if (worldContactListener.isLevelFinished()) {
            worldContactListener.setLevelFinished(false);
            currentLevel++;
            state = STATE.LEVEL_CLEARED;
            levelStopTime = System.currentTimeMillis();
        }
    }
    private void checkGameOver(){
        if (worldContactListener.isGameOver()) {
            worldContactListener.setGameOver(false);
            state = STATE.GAME_OVER;
            levelStopTime = System.currentTimeMillis();
        }

    }
    private void waitForRestart() {
        if(((System.currentTimeMillis() - levelStopTime) / 100) > 5)
        waitRestartComplete = true;
    }

    private void doRestartIfGameOver() {
        if(waitRestartComplete && state == STATE.GAME_OVER){
            game.setScreen(new GameScreen(game, currentLevel));
        }
    }

    private void changeLevelIfLevelCompleted(){
        if(waitRestartComplete && state == STATE.LEVEL_CLEARED) {
            if (currentLevel <= GameConfig.GAME_MAX_LEVELS) {
                game.setScreen(new GameScreen(game, currentLevel));
            } else {
                game.setScreen(new YouWonScreen(game));
            }
        }
    }


    private void drawGameOver() {
        if (state == STATE.GAME_OVER) {
            Texture gameOvertexture = assetManager.get(Constants.GAME_OVER_POPUP);
            float height = gameOvertexture.getHeight() / Insects.PPM;
            float width = gameOvertexture.getWidth() / Insects.PPM;
            game.batch.draw(gameOvertexture,
                    ((GameConfig.SCREEN_WIDTH_PX / Insects.PPM) / 2 - width / 2), ((GameConfig.SCREEN_HEIGHT_PX / Insects.PPM) / 2 - height / 2),
                    width, height);
        }
    }


    private void drawLevelCleared() {
        if (state == STATE.LEVEL_CLEARED) {
            Texture levelClearedTexture = assetManager.get(Constants.LEVEL_CLEARED_POPUP);
            float height = levelClearedTexture.getHeight() / Insects.PPM;
            float width = levelClearedTexture.getWidth() / Insects.PPM;
            game.batch.draw(levelClearedTexture,
                    ((GameConfig.SCREEN_WIDTH_PX / Insects.PPM) / 2 - width / 2), ((GameConfig.SCREEN_HEIGHT_PX / Insects.PPM) / 2 - height / 2),
                    width, height);
        }
    }


    private void renderDebug() {
        if(debugGrid) {
            ViewportUtils.drawGrid(gamePort, renderer, 32);
        }
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
        ViewportUtils.debugPixelPerUnit(gamePort);
        controller.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();

    }

    @Override
    public void dispose() {
        map.dispose();
        mapRenderer.dispose();
        world.dispose();
        b2dr.dispose();
        gameSound.stop();
    }
}
