package com.gstrzal.insects.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gstrzal.insects.Insects;
import com.gstrzal.insects.screens.GameScreen;

/**
 * Created by Gabriel on 15/10/2017.
 */

public class Ant extends Sprite {


    public World world;
    public Body b2body;
    private TextureRegion antStand;



    public Ant(World world, GameScreen screen){
//        super(screen.getAtlas().findRegion("ant_32x32"));
        this.world = world;

        antStand = new TextureRegion(getTexture(),0, 0, 32, 32);
        defineAnt();

    }

    public void update(float dt){
        setPosition(b2body.getPosition().x - getWidth() /2, b2body.getPosition().y - getHeight()/2);
        setRegion(antStand);
    }

    public void defineAnt(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(32/ Insects.PPM + 32,32/ Insects.PPM + 32); //initial position
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body  = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(16/ Insects.PPM);
        fdef.filter.categoryBits = Insects.INSECT_BIT;
        fdef.filter.maskBits = Insects.DEFAULT_BIT | Insects.FLOWER_BIT | Insects.BRICK_BIT;


        fdef.shape = shape;
        b2body.createFixture(fdef);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2/Insects.PPM, 6 /Insects.PPM), new Vector2(2/Insects.PPM, 6 /Insects.PPM));
        fdef.shape = head;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("head");
    }
}
