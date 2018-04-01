package com.gstrzal.insects.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.utils.Array;
import com.gstrzal.insects.config.Constants;

/**
 * Created by lelo on 31/01/18.
 */

public class WorldContactListener implements ContactListener {

    private int onGrounds = 0;
    private int changeCollision = 0;
    private Array<Body> bodiesToRemove;
    private boolean levelFinished = false;
    private boolean gameOver = false;
    private boolean switchOn = false;
    private boolean warpA = false;
    private boolean warpB = false;
    private boolean justWarpedA = false;
    private boolean justWarpedB = false;
    private boolean showInfo = false;


    public WorldContactListener() {
        super();
        bodiesToRemove = new Array<Body>();
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        checkCharacterOnGrounds(contact);

        checkCharacterFlowerContact(contact);

        checkCharacterMapEndContact(contact);

        checkCharacterDamageContact(contact);

        checkCharacterSwitchContact(contact);

        checkCharacterWarpAContact(contact);

        checkCharacterWarpBContact(contact);

        checkCharacterEnterInfoContact(contact);


        //For ant character - size check
        if(isContact(contact, Constants.INSECT_CHANGE, Constants.MAP_BLOCKS)){
            changeCollision++;
        }

        //Besouro Push Blocks
        if(isContact(contact, Constants.BESOURO_BODY, Constants.MAP_PUSH_BLOCKS)) {
            final Fixture pushBlock = fixA.getUserData() == Constants.MAP_PUSH_BLOCKS ? fixA : fixB;
            final MassData massData = new MassData();
            massData.mass = 250;
            Gdx.app.postRunnable(new Runnable() {

                @Override
                public void run () {
                    pushBlock.getBody().setMassData(massData);
                }
            });
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        uncheckCharacterOnGrounds(contact);

        //For ant character - size check
        if(isContact(contact, Constants.INSECT_CHANGE, Constants.MAP_BLOCKS)){
            changeCollision--;
        }

        //Besouro Push Blocks
        if(isContact(contact, Constants.BESOURO_BODY, Constants.MAP_PUSH_BLOCKS)) {
            final Fixture pushBlock = fixA.getUserData() == Constants.MAP_PUSH_BLOCKS ? fixA : fixB;
            final MassData massData = new MassData();
            massData.mass = 1000000;
            Gdx.app.postRunnable(new Runnable() {

                @Override
                public void run () {
                    pushBlock.getBody().setMassData(massData);
                    pushBlock.getBody().setLinearVelocity(0,0);
                }
            });
        }

        checkCharacterWarpAEndContact(contact);

        checkCharacterWarpBEndContact(contact);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

        checkPassPlatformContact(contact);
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    private void checkCharacterSwitchContact(Contact contact) {

        //Joaninha
        if(isContact(contact, Constants.INSECT_BODY, Constants.MAP_SWITCH)){
            switchOn = true;
        }
        //Ant
        if(isContact(contact, Constants.ANT_BODY, Constants.MAP_SWITCH)){
            switchOn = true;
        }
        //Besouro
        if(isContact(contact, Constants.BESOURO_BODY, Constants.MAP_SWITCH)){
            switchOn = true;
        }

    }

    private void checkPassPlatformContact(Contact contact) {

        //Joaninha
        if(isContact(contact, Constants.INSECT_BODY, Constants.MAP_PASS_BLOCKS)){
            changePassPlatformContactStatus(contact);
        }
        //Besouro
        if(isContact(contact, Constants.BESOURO_BODY, Constants.MAP_PASS_BLOCKS)){
            changePassPlatformContactStatus(contact);
        }

    }

    private void changePassPlatformContactStatus(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        float passPlatform_y;
        float insect_y;

        if (fixtureA.getBody().getUserData() == Constants.MAP_PASS_BLOCKS) {
            passPlatform_y = fixtureA.getBody().getPosition().y;
            insect_y = fixtureB.getBody().getPosition().y;
        } else {
            insect_y = fixtureA.getBody().getPosition().y;
            passPlatform_y = fixtureB.getBody().getPosition().y;
        }
        if(insect_y < passPlatform_y + .31f ){ //.1185F
            contact.setEnabled(false);
        } else {
            contact.setEnabled(true);
        }
    }



    private void checkCharacterDamageContact(Contact contact) {

        //Joaninha
        if(isContact(contact, Constants.INSECT_BODY, Constants.MAP_DAMAGE)){
            setGameOver(true);
        }

        //Ant
        if(isContact(contact, Constants.ANT_BODY, Constants.MAP_DAMAGE)) {
            setGameOver(true);
        }

        //Besouro
        if(isContact(contact, Constants.BESOURO_BODY, Constants.MAP_DAMAGE)) {
            setGameOver(true);
        }
    }

    private void checkCharacterMapEndContact(Contact contact) {

        //Joaninha
        if(isContact(contact, Constants.INSECT_BODY, Constants.MAP_END)){
            setLevelFinished(true);
        }

        //Ant
        if(isContact(contact, Constants.ANT_BODY, Constants.MAP_END)){
            setLevelFinished(true);
        }

        //Besouro
        if(isContact(contact, Constants.BESOURO_BODY, Constants.MAP_END)){
            setLevelFinished(true);
        }
    }

    private void checkCharacterEnterInfoContact(Contact contact){
        if (isContact(contact, Constants.INSECT_BODY, Constants.MAP_INFO)
                || isContact(contact, Constants.ANT_BODY, Constants.MAP_INFO)
                || isContact(contact, Constants.BESOURO_BODY, Constants.MAP_INFO)) {
            setShowInfo(true);
        }
    }

    private void checkCharacterWarpAContact(Contact contact) {

        if(!justWarpedB) {
            if (isContact(contact, Constants.INSECT_BODY, Constants.MAP_WARP_A)
                || isContact(contact, Constants.ANT_BODY, Constants.MAP_WARP_A)
                || isContact(contact, Constants.BESOURO_BODY, Constants.MAP_WARP_A)) {
                setWarpA(true);
                setJustWarpedA(true);
            }
        }
    }
    private void checkCharacterWarpAEndContact(Contact contact) {
        if (isContact(contact, Constants.INSECT_BODY, Constants.MAP_WARP_A)
                || isContact(contact, Constants.ANT_BODY, Constants.MAP_WARP_A)
                || isContact(contact, Constants.BESOURO_BODY, Constants.MAP_WARP_A)) {
            setJustWarpedB(false);
        }
    }

    private void checkCharacterWarpBContact(Contact contact) {

        if(!justWarpedA) {
            if (isContact(contact, Constants.INSECT_BODY, Constants.MAP_WARP_B)
                    || isContact(contact, Constants.ANT_BODY, Constants.MAP_WARP_B)
                    || isContact(contact, Constants.BESOURO_BODY, Constants.MAP_WARP_B)) {
                setWarpB(true);
                setJustWarpedB(true);
            }
        }
    }

    private void checkCharacterWarpBEndContact(Contact contact) {
        if (isContact(contact, Constants.INSECT_BODY, Constants.MAP_WARP_B)
                || isContact(contact, Constants.ANT_BODY, Constants.MAP_WARP_B)
                || isContact(contact, Constants.BESOURO_BODY, Constants.MAP_WARP_B)) {
            setJustWarpedA(false);
        }
    }

    private void checkCharacterFlowerContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        //Joaninha
        if(isContact(contact, Constants.INSECT_BODY, Constants.MAP_FLOWERS)){
            Fixture flower = fixA.getUserData() == Constants.MAP_FLOWERS ? fixA : fixB;
            bodiesToRemove.add(flower.getBody());
        }

        //Ant
        if(isContact(contact, Constants.ANT_BODY, Constants.MAP_FLOWERS)){
            Fixture flower = fixA.getUserData() == Constants.MAP_FLOWERS ? fixA : fixB;
            bodiesToRemove.add(flower.getBody());
        }

        //Besouro
        if(isContact(contact, Constants.BESOURO_BODY, Constants.MAP_FLOWERS)){
            Fixture flower = fixA.getUserData() == Constants.MAP_FLOWERS ? fixA : fixB;
            bodiesToRemove.add(flower.getBody());
        }
    }

    private void checkCharacterOnGrounds(Contact contact){

        //Jump and Character change check
        if(isContact(contact, Constants.INSECT_BASE, Constants.MAP_BLOCKS)
            || isContact(contact, Constants.INSECT_BASE, Constants.MAP_PASS_BLOCKS)
            || isContact(contact, Constants.INSECT_BASE, Constants.MAP_SLOPE)
            || isContact(contact, Constants.INSECT_BASE, Constants.MAP_PUSH_BLOCKS)
            || isContact(contact, Constants.INSECT_BASE, Constants.MAP_LEVEL_END_BLOCK)){
            onGrounds++;
        }
    }

    private void uncheckCharacterOnGrounds(Contact contact){
        if(isContact(contact, Constants.INSECT_BASE, Constants.MAP_BLOCKS)
                || isContact(contact, Constants.INSECT_BASE, Constants.MAP_PASS_BLOCKS)
                || isContact(contact, Constants.INSECT_BASE, Constants.MAP_SLOPE)
                || isContact(contact, Constants.INSECT_BASE, Constants.MAP_PUSH_BLOCKS)
                || isContact(contact, Constants.INSECT_BASE, Constants.MAP_LEVEL_END_BLOCK)){
            onGrounds--;
        }
    }


    private boolean isContact(Contact contact, String a, String b){
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        if((fixA.getUserData() == a && fixB.getUserData() == b) || (fixA.getUserData() == b && fixB.getUserData() == a)){
            return true;
        }else{
            return false;
        }
    }
    public boolean isOnGrounds(){
        if(onGrounds == 1) {
            return true;
        }else{
            return false;
        }
    }

    public boolean isChangePossible(){
        if(changeCollision == 0) {
            return true;
        }else{
            return false;
        }
    }
    public Array<Body> getBodiesToRemove() {
        return bodiesToRemove;
    }


    public boolean isLevelFinished() {
        return levelFinished;
    }

    public void setLevelFinished(boolean levelFinished) {
        this.levelFinished = levelFinished;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean isSwitchOn() {
        return switchOn;
    }


    public void setWarpA(boolean warpA) {
        this.warpA = warpA;
    }

    public void setWarpB(boolean warpB) {
        this.warpB = warpB;
    }

    public boolean isWarpA() {
        return warpA;
    }

    public boolean isWarpB() {
        return warpB;
    }

    public boolean isJustWarpedA() {
        return justWarpedA;
    }

    public void setJustWarpedA(boolean justWarpedA) {
        this.justWarpedA = justWarpedA;
    }

    public boolean isJustWarpedB() {
        return justWarpedB;
    }

    public void setJustWarpedB(boolean justWarpedB) {
        this.justWarpedB = justWarpedB;
    }

    public boolean isShowInfo() {
        return showInfo;
    }

    public void setShowInfo(boolean showInfo) {
        this.showInfo = showInfo;
    }
}
