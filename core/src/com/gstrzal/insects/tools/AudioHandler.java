package com.gstrzal.insects.tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.gstrzal.insects.Insects;
import com.gstrzal.insects.config.Constants;

/**
 * Created by lelo on 25/02/18.
 */

public class AudioHandler {

    private Insects game;
    private Sound gameSound;
    private AssetManager assetManager;
    long gameSoundID;







    public AudioHandler(Insects insects){
        game = insects;
        assetManager = game.getAssetManager();
    }


    public long playBackGroundMusic(){
        gameSound = assetManager.get(Constants.AUDIO_001);
        gameSoundID = gameSound.loop();

        if(!game.isAudioOn()) {
            gameSound.pause();
        }
        return gameSoundID;
    }

    public void stopBackGroundMusic(long gameSoundID){
        gameSound.stop(gameSoundID);
    }

    public void updateMusic(){

        //backgroundMusic
        if(game.isAudioOn()) {
            gameSound.resume();
        }else{
            gameSound.pause();
        }
    }
}
