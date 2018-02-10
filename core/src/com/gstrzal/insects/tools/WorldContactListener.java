package com.gstrzal.insects.tools;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
import com.gstrzal.insects.config.Constants;

/**
 * Created by lelo on 31/01/18.
 */

public class WorldContactListener implements ContactListener {

    private int onGrounds = 0;
    private Array<Body> bodiesToRemove;
    private boolean levelFinished = false;


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

        if(isContact(contact, Constants.INSECT_BODY, Constants.MAP_FLOWERS)){
            Fixture flower = fixA.getUserData() == Constants.MAP_FLOWERS ? fixA : fixB;
            bodiesToRemove.add(flower.getBody());
        }

        if(isContact(contact, Constants.INSECT_BODY, Constants.MAP_END)){
            setLevelFinished(true);
        }

        if(isContact(contact, Constants.INSECT_BODY, Constants.MAP_DAMAGE)){
            System.out.println("Started contact with Damage");
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if(isContact(contact, Constants.INSECT_BASE, Constants.MAP_BLOCKS)){
            onGrounds--;
        }
        if(isContact(contact, Constants.INSECT_BODY, Constants.MAP_FLOWERS)){
//            System.out.println("Ended contact with Flower");
        }
        if(isContact(contact, Constants.INSECT_BODY, Constants.MAP_END)){
//            System.out.println("Ended contact with Level End");
        }

        if(isContact(contact, Constants.INSECT_BODY, Constants.MAP_DAMAGE)){
            System.out.println("Ended contact with Damage");
        }

//        if(fixA.getUserData() == "base" || fixB.getUserData() == "base"){
//            Fixture head = fixA.getUserData() == "head" ? fixA : fixB;
//            Fixture object = head == fixA ? fixB : fixA;
//            System.out.println("Ended contact with Base");
//            onGrounds--;
//
//        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

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
    public Array<Body> getBodiesToRemove() {
        return bodiesToRemove;
    }


    public boolean isLevelFinished() {
        return levelFinished;
    }

    public void setLevelFinished(boolean levelFinished) {
        this.levelFinished = levelFinished;
    }
}
