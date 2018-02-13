package com.gstrzal.insects.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.gstrzal.insects.Insects;


public class Insect extends Sprite {


    public World world;
    public Body b2body;
    public float sizeDiff = (88/2)/ Insects.PPM;

    public Insect(Texture texture){
        super(texture);
    }

    public void update(float dt){
    }

    public void dispose(){
        world.destroyBody(b2body);
    }

}
