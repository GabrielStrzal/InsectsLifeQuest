package com.gstrzal.insects.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.gstrzal.insects.Insects;
import com.gstrzal.insects.config.GameConfig;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration((int) GameConfig.DISPLAY_SCREEN_WIDTH_PX_HTML, (int)GameConfig.DISPLAY_SCREEN_HEIGHT_PX_HTML);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new Insects();
        }
}