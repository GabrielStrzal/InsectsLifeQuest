package com.gstrzal.insects.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.gstrzal.insects.Insects;

/**
 * Created by lelo on 11/03/18.
 */

public class GameStatsHandler {

    private Insects insects;
    Preferences prefs = Gdx.app.getPreferences("insectsGameStats");


    public GameStatsHandler(Insects insects) {
        this.insects = insects;
    }

    public void saveLevelData(LevelStats levelStats){
        String levelNum = "level_" + levelStats.levelNumber;

        prefs.putInteger(levelNum, levelStats.levelNumber);
        prefs.putFloat(levelNum + "_successFactor", levelStats.successFactor);

        //total Deaths
        int totalDeaths = prefs.getInteger("totalDeaths", 0);
        prefs.putInteger("totalDeaths", totalDeaths + levelStats.deaths);

        //level Deaths
        int levelDeaths = prefs.getInteger(levelNum + "_deaths", 0);
        prefs.putInteger(levelNum + "_deaths", levelDeaths + levelStats.deaths);

        //Best Time
        int bestT = prefs.getInteger(levelNum+ "_bestTime", 0);
        if(bestT >levelStats.bestTime) {
            prefs.putInteger(levelNum + "_bestTime", levelStats.bestTime);
        }
    }


    public void saveLevelSuccess(LevelStats levelStats) {
        String levelNum = "level_" + levelStats.levelNumber;
        float currentSuccessFact = prefs.getFloat("level_" + levelStats.levelNumber + "_successFactor", 0);
        if(currentSuccessFact < levelStats.successFactor) {
            prefs.putFloat(levelNum + "_successFactor", levelStats.successFactor);
            prefs.flush();
        }
    }

    public float loadLevelSuccess(int levelnumber){
        LevelStats levelstats = new LevelStats(levelnumber);
        levelstats.levelNumber = prefs.getInteger("level_" + levelnumber, 0);
        levelstats.successFactor = prefs.getFloat("level_" + levelnumber + "_successFactor", 0);
        return levelstats.successFactor;
    }
    public int getLevelSuccess(int levelnumber) {
        if(loadLevelSuccess(levelnumber) <= 0)
            return 0;
        else if (loadLevelSuccess(levelnumber) <= 0.6)
            return 1;
        else if (loadLevelSuccess(levelnumber) < 1 && loadLevelSuccess(levelnumber) > 0.6)
            return 2;
        else
            return 3;
    }
}