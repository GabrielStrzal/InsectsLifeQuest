package com.gstrzal.insects.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
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
import com.gstrzal.insects.tools.AudioHandler;
import com.gstrzal.insects.tools.B2WorldCreator;
import com.gstrzal.insects.tools.GameStatsHandler;
import com.gstrzal.insects.tools.LevelStats;
import com.gstrzal.insects.tools.ScreenEnum;
import com.gstrzal.insects.tools.ScreenManager;
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
    private float numberOfFlowerInLevel;
    private boolean waitRestartComplete;
    private long levelStopTime;
    private boolean isDirectionUp;

    //Next Insect
    private Texture nextJoaninha;
    private Texture nextAnt;
    private Texture nextBesouro;


    private enum STATE {
        PLAYING, GAME_OVER, LEVEL_CLEARED, PAUSED
    }
    private STATE state;

    private BitmapFont bitmapFont;
    private GlyphLayout layout = new GlyphLayout();
    private Controller controller;

    private AudioHandler audioHandler;
    private long gameSoundID;

    private LevelStats levelStats;
    private GameStatsHandler gameStatsHandler;
    private float numberOfFlowersCollected = 0;



    public GameScreen(Insects game, int level){

        this.game = game;
        this.assetManager = game.getAssetManager();
        this.audioHandler = game.getAudioHandler();
        this.gameStatsHandler = game.getGameStatsHandler();
        state = STATE.PLAYING;
        gamecam = new OrthographicCamera();
        b2dcam = new OrthographicCamera();
        b2dcam.setToOrtho(false,Insects.V_WIDTH / Insects.PPM, Insects.V_HEIGHT/ Insects.PPM);
        gamePort = new FitViewport(Insects.V_WIDTH / Insects.PPM, Insects.V_HEIGHT/ Insects.PPM, gamecam);

        levelStats = new LevelStats(level);
        game.currentLevel = level;

        world = new World(new Vector2(0, gravity), true);
        b2dr = new Box2DDebugRenderer();

        map = assetManager.get(Constants.LEVEL+ level + Constants.TMX);
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / Insects.PPM);
        gamecam.position.set(gamePort.getWorldWidth()/2,gamePort.getWorldHeight()/2, 0);
        insectPlayer = new LBug(world, (Texture) assetManager.get(Constants.JOANINHA));

        b2World = new B2WorldCreator(world, map, game);
        numberOfFlowerInLevel = b2World.flowers.size;
        worldContactListener = new WorldContactListener();
        world.setContactListener(worldContactListener);

//        bitmapFont = assetManager.get(Constants.GAME_FONT);
        bitmapFont = new BitmapFont();

        controller = new Controller(game);

        //Audio
        gameSoundID = audioHandler.playBackGroundMusic();

    }
    @Override
    public void show() {

        renderer = new ShapeRenderer();

        //camera debug
        debugCameraController = new DebugCameraController();
        debugCameraController.setStartPosition(GameConfig.SCREEN_WIDTH_PX/2, GameConfig.SCREEN_HEIGHT_PX/2);

    }

    public void update(float dt){
        world.step(1/60f, 6, 2);

        collectFlowers();
        handleInput(dt);

        insectPlayer.update(dt);
        gamecam.update();
        b2dcam.update();
        mapRenderer.setView(b2dcam);

        audioHandler.updateMusic();

    }

    private void collectFlowers() {
        Array<Body> bodies = worldContactListener.getBodiesToRemove();
        for(int i = 0; i < bodies.size; i++) {
            Body b = bodies.get(i);
            b2World.flowers.removeValue((Flower) b.getUserData(), true);
            world.destroyBody(bodies.get(i));
//            TODO: add Flower number
            audioHandler.playPickupFlowerSound();
            numberOfFlowersCollected++;
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
            audioHandler.playJumpSound();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)|| Gdx.input.isKeyJustPressed(Input.Keys.W)){
            if(worldContactListener.isOnGrounds() && insectPlayer instanceof LBug){
                isDirectionUp = true;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)){
            isDirectionRight = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)|| Gdx.input.isKeyPressed(Input.Keys.A)){
            isDirectionRight = false;
        }

        //Character Change
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            if(worldContactListener.isOnGrounds() && worldContactListener.isChangePossible()) {
                characterChange = true;
            }
        }


        //Character Change
        if (characterChange) {

            Insect tempInsc = insectPlayer;
            float tempInscX = tempInsc.b2body.getPosition().x;
            float tempInscY = tempInsc.b2body.getPosition().y;
            tempInsc.dispose();

            if (tempInsc instanceof Besouro) {
                insectPlayer = new Ant(world, (Texture) assetManager.get(Constants.ANT),
                        tempInscX , tempInscY - insectPlayer.sizeDiff);
            } else if (tempInsc instanceof Ant){
                insectPlayer = new LBug(world, (Texture) assetManager.get(Constants.JOANINHA),
                        tempInscX , tempInscY + insectPlayer.sizeDiff);
            } else{
                insectPlayer = new Besouro(world, (Texture) assetManager.get(Constants.BESOURO),
                        tempInscX , tempInscY);
            }
            characterChange = false;

            audioHandler.playChangeInsectSound();
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
            if(worldContactListener.isOnGrounds() && worldContactListener.isChangePossible()) {
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
                checkSwitchOn();
                checkWarp();
            }
            break;
            case GAME_OVER: {
                waitForRestart(10);
            }
            break;
            case LEVEL_CLEARED: {
                waitForRestart(15);
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
        // draw boxes
        for(int i = 0; i < b2World.pushBoxes.size; i++) {
            b2World.pushBoxes.get(i).render(game.batch);
            if(!b2World.pushBoxes.get(i).getBody().getLinearVelocity().isZero()
                    && insectPlayer instanceof Besouro) audioHandler.playPushBlockSound();

        }
        // draw switches
        if( b2World.levelSwitch != null) {
            b2World.levelSwitch.render(game.batch);
        }

        // draw levelEndBlock
        if( b2World.levelEndBlock != null) {
            b2World.levelEndBlock.render(game.batch);
        }

        insectPlayer.draw(game.batch);
        drawGameOver();
        drawLevelCleared();

        drawNextInsect();


        game.batch.end();
        controller.draw();
    }


    private void checkSwitchOn(){
        if(worldContactListener.isSwitchOn()){
            b2World.levelSwitch.turnSwitchOn();
            if( b2World.levelEndBlock != null) {
                world.destroyBody(b2World.levelEndBlock.getBody());
                b2World.levelEndBlock = null;
                audioHandler.playSwitch();
            }
        }
    }

    private void checkLevelCompleted() {
        if (worldContactListener.isLevelFinished()) {
            worldContactListener.setLevelFinished(false);
            game.currentLevel++;
            state = STATE.LEVEL_CLEARED;
            audioHandler.playEndLevelSound();
            levelStopTime = System.currentTimeMillis();
        }
    }
    private void checkGameOver(){
        if (worldContactListener.isGameOver()) {
            worldContactListener.setGameOver(false);
            state = STATE.GAME_OVER;
            audioHandler.playHitHurtSound();
            levelStopTime = System.currentTimeMillis();
        }

    }

    private void checkWarp() {
        if (worldContactListener.isWarpA()) {
            worldContactListener.setWarpA(false);
            insectPlayer.b2body.setTransform(b2World.warpBposition, insectPlayer.b2body.getAngle());
            audioHandler.playWarp();
        }
        if (worldContactListener.isWarpB()) {
            worldContactListener.setWarpB(false);
            insectPlayer.b2body.setTransform(b2World.warpAposition, insectPlayer.b2body.getAngle());
            audioHandler.playWarp();
        }
    }
    private void waitForRestart(int wait) {
        if(((System.currentTimeMillis() - levelStopTime) / 100) > wait)//5
        waitRestartComplete = true;
    }

    private void doRestartIfGameOver() {
        if(waitRestartComplete && state == STATE.GAME_OVER){
            ScreenManager.getInstance().showScreen(ScreenEnum.GAME_SCREEN, game, game.currentLevel);
        }
    }

    private void changeLevelIfLevelCompleted(){
        if(waitRestartComplete && state == STATE.LEVEL_CLEARED) {
            levelStats.successFactor = numberOfFlowersCollected/numberOfFlowerInLevel;
            gameStatsHandler.saveLevelSuccess(levelStats);
            if (game.currentLevel <= GameConfig.GAME_MAX_LEVELS) {
                ScreenManager.getInstance().showScreen(ScreenEnum.GAME_SCREEN, game, game.currentLevel);
            } else {
                ScreenManager.getInstance().showScreen(ScreenEnum.YOU_WON_SCREEM, game);
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

    private void drawNextInsect() {
            nextJoaninha = assetManager.get(Constants.NEXT_JOANINHA);
            nextAnt = assetManager.get(Constants.NEXT_ANT);
            nextBesouro = assetManager.get(Constants.NEXT_BESOURO);
            Texture nextInsect;

            float height = nextJoaninha.getHeight() / Insects.PPM;
            float width = nextJoaninha.getWidth() / Insects.PPM;

            if(insectPlayer instanceof  LBug) nextInsect = nextBesouro;
            else if(insectPlayer instanceof  Besouro) nextInsect = nextAnt;
            else nextInsect = nextJoaninha;

            game.batch.draw(nextInsect,
                    //50 / Insects.PPM, ((GameConfig.SCREEN_HEIGHT_PX - 250 )/ Insects.PPM) - height/2, //450
                    (GameConfig.SCREEN_WIDTH_PX-1036)/ Insects.PPM, 10/ Insects.PPM,
                    width*.7f, height*.7f);
    }


    private void drawLevelCleared() {
        if (state == STATE.LEVEL_CLEARED) {
            Texture levelClearedTexture;
            float successFactor = numberOfFlowersCollected/numberOfFlowerInLevel;
            if(successFactor <= 0)
                levelClearedTexture = assetManager.get(Constants.LEVEL_CLEARED_POPUP_0);
            else if (successFactor <= 0.6)
                levelClearedTexture = assetManager.get(Constants.LEVEL_CLEARED_POPUP_1);
            else if (successFactor < 1 && successFactor > 0.6)
                levelClearedTexture = assetManager.get(Constants.LEVEL_CLEARED_POPUP_2);
            else
                levelClearedTexture = assetManager.get(Constants.LEVEL_CLEARED_POPUP_3);

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
//        dispose();

    }

    @Override
    public void dispose() {
        map.dispose();
        mapRenderer.dispose();
        world.dispose();
        b2dr.dispose();
//        audioHandler.stopBackGroundMusic(gameSoundID);
    }
}
