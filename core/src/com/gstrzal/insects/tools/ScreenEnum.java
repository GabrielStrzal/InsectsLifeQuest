package com.gstrzal.insects.tools;

import com.badlogic.gdx.Screen;
import com.gstrzal.insects.Insects;
import com.gstrzal.insects.screens.GameScreen;
import com.gstrzal.insects.screens.InstructionsScreen;
import com.gstrzal.insects.screens.LoadingScreen;
import com.gstrzal.insects.screens.MenuScreen;
import com.gstrzal.insects.screens.OptionsScreen;
import com.gstrzal.insects.screens.SelectLevelsScreen;
import com.gstrzal.insects.screens.YouWonScreen;

/**
 * Based on http://www.pixnbgames.com/blog/libgdx/how-to-manage-screens-in-libgdx/
 */

public enum ScreenEnum {
    GAME_SCREEN {
        public Screen getScreen(Object... params) {
            return new GameScreen((Insects)params[0], (Integer) params[1]);
        }
    },
    LOADING_SCREEN {
        public Screen getScreen(Object... params) {
            return new LoadingScreen((Insects)params[0]);
        }
    },
    MENU_SCREEN {
        public Screen getScreen(Object... params) {
            return new MenuScreen((Insects)params[0]);
        }
    },
    OPTIONS_SCREEN {
        public Screen getScreen(Object... params) {
            return new OptionsScreen((Insects)params[0]);
        }
    },
    INSTRUCTIONS_SCREEN {
        public Screen getScreen(Object... params) {
            return new InstructionsScreen((Insects)params[0]);
        }
    },
    SELECT_LEVELS_SCREEM {
        public Screen getScreen(Object... params) {
            return new SelectLevelsScreen((Insects)params[0]);
        }
    },
    YOU_WON_SCREEM {
        public Screen getScreen(Object... params) {
            return new YouWonScreen((Insects)params[0]);
        }
    };

    public abstract Screen getScreen(Object... params);
}
