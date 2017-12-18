package com.gstrzal.insects;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gstrzal.insects.config.GameConfig;
import com.gstrzal.insects.screens.MenuScreen;
import com.gstrzal.insects.screens.StartScreen;


public class Insects extends Game {
	public static final int V_WIDTH = (int)GameConfig.SCREEN_WIDTH_PX;
	public static final int V_HEIGHT = (int)GameConfig.SCREEN_HEIGHT_PX;
	public static final float PPM = 1;


	public static final short DEFAULT_BIT = 1;
	public static final short ANT_BIT = 2;
	public static final short BRICK_BIT = 4;
	public static final short COIN_BIT = 8;
	public static final short DESTROYED_BIT = 16;

	public SpriteBatch batch;

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		batch = new SpriteBatch();
		setScreen(new StartScreen(this));

	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
