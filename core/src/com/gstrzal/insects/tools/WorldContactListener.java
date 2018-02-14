package com.gstrzal.insects.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
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


    public WorldContactListener() {
        super();
        bodiesToRemove = new Array<Body>();
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if(isContact(contact, Constants.INSECT_BASE, Constants.MAP_BLOCKS)){
            onGrounds++;
        }
        if(isContact(contact, Constants.INSECT_BASE, Constants.MAP_PASS_BLOCKS)){
            onGrounds++;
        }

        if(isContact(contact, Constants.INSECT_CHANGE, Constants.MAP_BLOCKS)){
            changeCollision++;
        }

        if(isContact(contact, Constants.INSECT_BODY, Constants.MAP_FLOWERS)){
            Fixture flower = fixA.getUserData() == Constants.MAP_FLOWERS ? fixA : fixB;
            bodiesToRemove.add(flower.getBody());
        }

        if(isContact(contact, Constants.INSECT_BODY, Constants.MAP_END)){
            setLevelFinished(true);
        }

        if(isContact(contact, Constants.INSECT_BODY, Constants.MAP_DAMAGE)){
            setGameOver(true);
        }


        if(isContact(contact, Constants.ANT_BODY, Constants.MAP_FLOWERS)){
            Fixture flower = fixA.getUserData() == Constants.MAP_FLOWERS ? fixA : fixB;
            bodiesToRemove.add(flower.getBody());
        }

        if(isContact(contact, Constants.ANT_BODY, Constants.MAP_END)){
            setLevelFinished(true);
        }

        if(isContact(contact, Constants.ANT_BODY, Constants.MAP_DAMAGE)) {
            setGameOver(true);
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if(isContact(contact, Constants.INSECT_BASE, Constants.MAP_BLOCKS)){
            onGrounds--;
        }
        if(isContact(contact, Constants.INSECT_BASE, Constants.MAP_PASS_BLOCKS)){
            onGrounds--;
        }
        if(isContact(contact, Constants.INSECT_CHANGE, Constants.MAP_BLOCKS)){
            changeCollision--;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        //Pass Platform
        if(isContact(contact, Constants.INSECT_BODY, Constants.MAP_PASS_BLOCKS)){

            float passPlatform_y;
            float insect_y;

            if (fixtureA.getBody().getUserData() == Constants.MAP_PASS_BLOCKS) {
                passPlatform_y = fixtureA.getBody().getPosition().y;
                insect_y = fixtureB.getBody().getPosition().y;
            } else {
                insect_y = fixtureA.getBody().getPosition().y;
                passPlatform_y = fixtureB.getBody().getPosition().y;
            }
            if(insect_y < passPlatform_y + .1185f ){
                contact.setEnabled(false);
            } else {
                contact.setEnabled(true);
            }
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

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
        if(changeCollision == 1) {
            return false;
        }else{
            return true;
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
}
