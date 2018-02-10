package com.gstrzal.insects.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
    private LBug lBug;

    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    private ShapeRenderer renderer;

    private World world;
    private Box2DDebugRenderer b2dr;
    private AssetManager assetManager;

    private boolean debugGrid = false;

    private boolean isDirectionRight = true;

    private float xVelocity = 1f;
    private float gravity = -10f;
    private float jumpSpeed = 250f;

    //camera debug
    private DebugCameraController debugCameraController;

    private WorldContactListener worldContactListener;
    private B2WorldCreator b2World;

    public GameScreen(Insects game, String level){

        this.assetManager = game.getAssetManager();


        this.game = game;
        gamecam = new OrthographicCamera();
        b2dcam = new OrthographicCamera();
        b2dcam.setToOrtho(false,Insects.V_WIDTH / Insects.PPM, Insects.V_HEIGHT/ Insects.PPM);
        gamePort = new FitViewport(Insects.V_WIDTH / Insects.PPM, Insects.V_HEIGHT/ Insects.PPM, gamecam);



        world = new World(new Vector2(0, gravity), true);
        b2dr = new Box2DDebugRenderer();

        map = assetManager.get(Constants.LEVEL+level+ Constants.TMX);
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / Insects.PPM);
        gamecam.position.set(gamePort.getWorldWidth()/2,gamePort.getWorldHeight()/2, 0);
        lBug = new LBug(world, (Texture) assetManager.get(Constants.JOANINHA));

        b2World = new B2WorldCreator(world, map, game);
        worldContactListener = new WorldContactListener();
        world.setContactListener(worldContactListener);


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
        lBug.update(dt);
        gamecam.update();
        b2dcam.update();
        mapRenderer.setView(b2dcam);

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
            lBug.b2body.setLinearVelocity(xVelocity,lBug.b2body.getLinearVelocity().y);
        }else{
            lBug.b2body.setLinearVelocity(-xVelocity,lBug.b2body.getLinearVelocity().y);
        }



        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            if(worldContactListener.isOnGrounds()){
                lBug.b2body.applyForceToCenter(0,jumpSpeed,true);
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            isDirectionRight = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            isDirectionRight = false;
        }


        //mobile
        if (Gdx.input.isTouched()) {
            if ((Gdx.input.getY() < Gdx.graphics.getHeight() / 4)) {
                if(worldContactListener.isOnGrounds()){
                    lBug.b2body.applyForceToCenter(0,jumpSpeed,true);
                }
            }else if (Gdx.input.getX() > Gdx.graphics.getWidth() / 2){
                isDirectionRight = true;
            } else {
                isDirectionRight = false;
            }
        }



        //Return to menu screen
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)){
            game.setScreen(new MenuScreen(game));
        }


        //Turn debugGrid on/of
        if (Gdx.input.isKeyJustPressed(Input.Keys.G)){
            debugGrid = debugGrid? false: true;
        }

    }

    @Override
    public void render(float delta) {
        //camera debug
        debugCameraController.handleDebugInput(delta);
        debugCameraController.applyTo(gamecam);


        update(delta);
        checkLevelCompleted();
        GdxUtils.clearScreen();
        mapRenderer.render();

        b2dr.render(world, b2dcam.combined);
        game.batch.setProjectionMatrix(b2dcam.combined);
        game.batch.begin();

        // draw flowers
        for(int i = 0; i < b2World.flowers.size; i++) {
            b2World.flowers.get(i).render(game.batch);
        }

        lBug.draw(game.batch);

        game.batch.end();

        renderDebug();

    }

    private void checkLevelCompleted() {
        if (worldContactListener.isLevelFinished()) {
            worldContactListener.setLevelFinished(false);
            game.setScreen(new GameScreen(game , "02"));
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
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        mapRenderer.dispose();
        world.dispose();
        b2dr.dispose();
    }
}
