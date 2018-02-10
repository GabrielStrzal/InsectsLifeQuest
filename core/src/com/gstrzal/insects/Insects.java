package com.gstrzal.insects;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Logger;
import com.gstrzal.insects.config.GameConfig;
import com.gstrzal.insects.screens.LoadingScreen;


public class Insects extends Game {
	public static final int V_WIDTH = (int)GameConfig.SCREEN_WIDTH_PX;
	public static final int V_HEIGHT = (int)GameConfig.SCREEN_HEIGHT_PX;
	public static final float PPM = 100;


	public static final short DEFAULT_BIT = 1;
	public static final short INSECT_BIT = 2;
	public static final short BASE_BIT = 4;
	public static final short BRICK_BIT = 8;
	public static final short FLOWER_BIT = 16;
	public static final short LEVEL_END_BIT = 32;
	public static final short DAMAGE_BIT = 64;

	public SpriteBatch batch;
	private final AssetManager assetManager = new AssetManager();

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		assetManager.getLogger().setLevel(Logger.DEBUG);

		batch = new SpriteBatch();
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		setScreen(new LoadingScreen(this));

	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		assetManager.dispose();
	}

    public AssetManager getAssetManager() {
        return assetManager;
    }
}
