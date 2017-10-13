package com.gstrzal.insects;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gstrzal.insects.screens.MenuScreen;


public class Insects extends Game {
	public static final int V_WIDTH = 1200;
	public static final int V_HEIGHT = 624;
	public static final float PPM = 100;

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
