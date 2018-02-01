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
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gstrzal.insects.Insects;
import com.gstrzal.insects.assets.AssetPaths;
import com.gstrzal.insects.config.GameConfig;
import com.gstrzal.insects.entity.*;
import com.gstrzal.insects.tools.B2WorldCreator;
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
    private boolean isDirectionUp = false;
    private boolean blockJump = false;

    private float xVelocity = 1f;
    private float yVelocity = 1f;
    private float gravity = -10f;

    //camera debug
    private DebugCameraController debugCameraController;

    public GameScreen(Insects game){

        this.assetManager = game.getAssetManager();


        this.game = game;
        gamecam = new OrthographicCamera();
        b2dcam = new OrthographicCamera();
        b2dcam.setToOrtho(false,Insects.V_WIDTH / Insects.PPM, Insects.V_HEIGHT/ Insects.PPM);
        gamePort = new FitViewport(Insects.V_WIDTH / Insects.PPM, Insects.V_HEIGHT/ Insects.PPM, gamecam);



        world = new World(new Vector2(0, gravity), true);
        b2dr = new Box2DDebugRenderer();

        map = assetManager.get(AssetPaths.LEVEL_07);
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / Insects.PPM);
        gamecam.position.set(gamePort.getWorldWidth()/2,gamePort.getWorldHeight()/2, 0);
        lBug = new LBug(world, (Texture) assetManager.get(AssetPaths.JOANINHA));

        new B2WorldCreator(world, map);

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

        world.step(1/60f, 6, 2);
        lBug.update(dt);
        gamecam.update();
        b2dcam.update();
        mapRenderer.setView(b2dcam);

    }
    private void handleInput(float dt) {

        if(isDirectionRight && !blockJump){
            lBug.b2body.setLinearVelocity(xVelocity,0);
        }
        else if (!isDirectionRight && !blockJump){
            lBug.b2body.setLinearVelocity(-xVelocity,0);
        }
        if (isDirectionUp){
            if(isDirectionRight){
                lBug.b2body.applyLinearImpulse(new Vector2(xVelocity,yVelocity),lBug.b2body.getWorldCenter(), true);
            }else{
                lBug.b2body.applyLinearImpulse(new Vector2(-xVelocity,yVelocity),lBug.b2body.getWorldCenter(), true);
            }
            isDirectionUp = false;
        }




        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && !blockJump){
            isDirectionUp = true;
            blockJump = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            isDirectionRight = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            isDirectionRight = false;
        }

        if(lBug.b2body.getLinearVelocity().x < 40 && lBug.b2body.getLinearVelocity().x > -40){
            blockJump = false;
            isDirectionUp = false;
        }

        //mobile
        if (Gdx.input.isTouched()) {
            if ((Gdx.input.getY() < Gdx.graphics.getHeight() / 4) && !blockJump) {
                isDirectionUp = true;
                blockJump = true;
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
        GdxUtils.clearScreen();
        mapRenderer.render();

        b2dr.render(world, b2dcam.combined);
        game.batch.setProjectionMatrix(b2dcam.combined);
        game.batch.begin();
        lBug.draw(game.batch);
        game.batch.end();

        renderDebug();

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
