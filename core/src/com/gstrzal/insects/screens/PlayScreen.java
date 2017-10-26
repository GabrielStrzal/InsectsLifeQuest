package com.gstrzal.insects.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gstrzal.insects.Insects;
import com.gstrzal.insects.sprites.Ant;
import com.gstrzal.insects.tools.B2WorldCreator;
import com.gstrzal.insects.utils.GdxUtils;


/**
 * Created by Gabriel on 03/09/2017.
 */

public class PlayScreen implements Screen{
    private Insects game;
    private TextureAtlas atlas;


    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private Ant ant;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private World world;
    private Box2DDebugRenderer b2dr;

    public PlayScreen(Insects game){
        atlas = new TextureAtlas("sprites_32x32.txt");
        this.game = game;
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(Insects.V_WIDTH / Insects.PPM, Insects.V_HEIGHT/ Insects.PPM, gamecam);

        world = new World(new Vector2(0,-32), true);
        b2dr = new Box2DDebugRenderer();
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("ins_level_06.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / Insects.PPM);
        gamecam.position.set(gamePort.getWorldWidth()/2,gamePort.getWorldHeight()/2, 0);
        ant = new Ant(world, this);

        new B2WorldCreator(world, map);

    }
    public TextureAtlas getAtlas(){
        return atlas;
    }
    @Override
    public void show() {

    }

    public void update(float dt){
        handleInput(dt);

        world.step(1/60f, 6, 2);
        ant.update(dt);
        //gamecam.position.x = ant.b2body.getPosition().x;
        gamecam.update();
        renderer.setView(gamecam);

    }
    private void handleInput(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)&& ant.b2body.getLinearVelocity().y == 0){
            ant.b2body.applyLinearImpulse(new Vector2(0,32f),ant.b2body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && ant.b2body.getLinearVelocity().x <= 32){
            ant.b2body.applyLinearImpulse(new Vector2(32f, 0), ant.b2body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && ant.b2body.getLinearVelocity().x >= -32){
            ant.b2body.applyLinearImpulse(new Vector2(-32f, 0), ant.b2body.getWorldCenter(), true);
        }


        if (Gdx.input.isKeyJustPressed(Input.Keys.M)){
            game.setScreen(new MenuScreen(game));
        }


    }
    @Override
    public void render(float delta) {
        update(delta);
        GdxUtils.clearScreen();
        renderer.render();

        b2dr.render(world, gamecam.combined);
        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        ant.draw(game.batch);
        game.batch.end();

    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
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
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
    }
}
