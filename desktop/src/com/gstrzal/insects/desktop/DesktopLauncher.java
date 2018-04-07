package com.gstrzal.insects.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gstrzal.insects.Insects;
import com.gstrzal.insects.config.GameConfig;
import com.gstrzal.insects.resolvers.ActionResolverDummy;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = (int) GameConfig.DISPLAY_SCREEN_WIDTH_PX;
		config.height = (int)GameConfig.DISPLAY_SCREEN_HEIGHT_PX;
		new LwjglApplication(new Insects(new ActionResolverDummy()), config);
	}
}
