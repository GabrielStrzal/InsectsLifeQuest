package com.gstrzal.insects.desktop;

import com.badlogic.gdx.Files;
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
        config.title = "Insect's Life Quest";
        config.addIcon("desktop_icon_ant_32x32.png", Files.FileType.Internal);
		new LwjglApplication(new Insects(new ActionResolverDummy()), config);
	}
}
