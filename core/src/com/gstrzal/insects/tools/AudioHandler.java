package com.gstrzal.insects.tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.gstrzal.insects.Insects;
import com.gstrzal.insects.config.Constants;

/**
 * Created by lelo on 25/02/18.
 */

public class AudioHandler {

    private Insects game;
    private Music backgroundMusic;
    private Sound jumpSound;
    private Sound hitHurtSound;
    private Sound endLevelSound;
    private Sound changeInsectSound;
    private Sound pickupFlowerlSound;
    private AssetManager assetManager;
    long gameSoundID;
    float volume = 0.3f;

    public AudioHandler(Insects insects){
        game = insects;
        assetManager = game.getAssetManager();
        gameSoundID = game.backgroundAudioID;
    }


    public long playBackGroundMusic(){
        if(backgroundMusic!= null) {
            if (!backgroundMusic.isPlaying()) {
                return gameSoundID;
            }
        }else{
            backgroundMusic = assetManager.get(Constants.AUDIO_BACKGROUND_MUSIC);
            backgroundMusic.setLooping(true);
            backgroundMusic.setVolume(volume);
            backgroundMusic.play();

            if(!game.getGameStatsHandler().isAudioOn()) {
                backgroundMusic.pause();
            }
        }
        return gameSoundID;
    }

    public void stopBackGroundMusic(long gameSoundID){
        backgroundMusic.stop();
    }

    public void updateMusic(){

        updateBackgroundMusic();

    }

    private void updateBackgroundMusic(){
        //backgroundMusic
        if(game.getGameStatsHandler().isAudioOn()) {
            if(backgroundMusic!= null && !backgroundMusic.isPlaying())
                backgroundMusic.play();
        }else{
            backgroundMusic.pause();
        }
    }

    public void playJumpSound(){
        jumpSound = assetManager.get(Constants.AUDIO_JUMP);
        if(game.getGameStatsHandler().isAudioOn()) jumpSound.play(volume);
    }

    public void playHitHurtSound(){
        hitHurtSound = assetManager.get(Constants.AUDIO_HIT_HURT);
        if(game.getGameStatsHandler().isAudioOn()) hitHurtSound.play(volume);
    }

    public void playEndLevelSound(){
        endLevelSound = assetManager.get(Constants.AUDIO_END_LEVEL);
        if(game.getGameStatsHandler().isAudioOn()) endLevelSound.play(volume);
    }

    public void playChangeInsectSound(){
        changeInsectSound = assetManager.get(Constants.AUDIO_CHANGE_INSECT);
        if(game.getGameStatsHandler().isAudioOn()) changeInsectSound.play(volume);
    }

    public void playPickupFlowerSound(){
        pickupFlowerlSound = assetManager.get(Constants.AUDIO_PICKUP_FLOWER);
        if(game.getGameStatsHandler().isAudioOn()) pickupFlowerlSound.play(volume);
    }
}
