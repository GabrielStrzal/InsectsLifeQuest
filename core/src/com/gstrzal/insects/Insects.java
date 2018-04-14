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
import com.gstrzal.insects.resolvers.ActionResolver;
import com.gstrzal.insects.tools.AudioHandler;
import com.gstrzal.insects.tools.GameStatsHandler;
import com.gstrzal.insects.tools.ScreenEnum;
import com.gstrzal.insects.tools.ScreenManager;


public class Insects extends Game {
	public static final int V_WIDTH = (int)GameConfig.SCREEN_WIDTH_PX;
	public static final int V_HEIGHT = (int)GameConfig.SCREEN_HEIGHT_PX;
	public static final float PPM = 400;


	public static final short DEFAULT_BIT = 0x0001;
	public static final short INSECT_BIT = 0x0002;
	public static final short BASE_BIT = 0x0004;
	public static final short BRICK_BIT = 0x0008;
	public static final short FLOWER_BIT = 0x0016;
	public static final short LEVEL_END_BIT = 0x0032;
	public static final short DAMAGE_BIT = 0x0064;
	public static final short PASS_BLOCK_BIT = 0x0128;
	public static final short CHANGE_INSECT_BIT = 0x0256;
	public static final short SLOPE_BIT = 0x0512;
	public static final short PUSH_BLOCK_BIT = 0x1024;
	public static final short LADYBUG_SLOP_BIT = 0x2048;
	public static final short SWITCH_BIT = 0x4096;


	public SpriteBatch batch;
	public int currentLevel;
	private final AssetManager assetManager = new AssetManager();
	private AudioHandler audioHandler;
	public long backgroundAudioID;
	private GameStatsHandler gameStatsHandler;

	public ActionResolver actionResolver;
	public boolean directionRight = true;

	public Insects(ActionResolver actionResolver){
		this.actionResolver = actionResolver;
	}

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_NONE);
		assetManager.getLogger().setLevel(Logger.DEBUG);

		batch = new SpriteBatch();
		gameStatsHandler = new GameStatsHandler(this);
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		audioHandler = new AudioHandler(this);
		ScreenManager.getInstance().initialize(this);
		ScreenManager.getInstance().showScreen(ScreenEnum.LOADING_SCREEN, this);
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

	public AudioHandler getAudioHandler() {
		return audioHandler;
	}

	public GameStatsHandler getGameStatsHandler() {
		return gameStatsHandler;
	}
}
