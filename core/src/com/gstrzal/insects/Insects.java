package com.gstrzal.insects;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gstrzal.insects.config.GameConfig;
import com.gstrzal.insects.screens.MenuScreen;


public class Insects extends Game {
	public static final int V_WIDTH = (int)GameConfig.SCREEN_WIDTH;
	public static final int V_HEIGHT = (int)GameConfig.SCREEN_HEIGHT;
	public static final float PPM = 1;


	public static final short DEFAULT_BIT = 1;
	public static final short ANT_BIT = 2;
	public static final short BRICK_BIT = 4;
	public static final short COIN_BIT = 8;
	public static final short DESTROYED_BIT = 16;

	public SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new MenuScreen(this));

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
