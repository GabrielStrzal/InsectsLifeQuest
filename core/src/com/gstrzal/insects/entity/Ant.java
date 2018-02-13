package com.gstrzal.insects.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gstrzal.insects.Insects;
import com.gstrzal.insects.config.Constants;
import com.gstrzal.insects.screens.GameScreen;

/**
 * Created by Gabriel on 15/10/2017.
 */

public class Ant extends Insect {


    public static final int WIDTH = 128;
    public static final int HEIGHT = 120;

    private float animationTimer = 0;
    private final Animation walking;
    private final TextureRegion standing;





    public Ant(World world, Texture texture){
        this(world, texture, 200/ Insects.PPM, 380/ Insects.PPM);
    }


    public Ant(World world, Texture texture, float x, float y){
        super(texture);
        this.world = world;

        TextureRegion[] regions = TextureRegion.split(texture, WIDTH, HEIGHT)[0];
        walking = new Animation(0.25F, regions[0], regions[1]);
        walking.setPlayMode(Animation.PlayMode.LOOP);
        standing = regions[0];



        defineAnt(x,y);
        setBounds(0,0, WIDTH/Insects.PPM, HEIGHT/Insects.PPM);

    }

    public void update(float dt){
        animationTimer += dt;

        float xSpeed = b2body.getLinearVelocity().x;
        float ySpeed = b2body.getLinearVelocity().y;

        setPosition(b2body.getPosition().x - getWidth() /2, b2body.getPosition().y - getHeight()/2);

        //set Texture
        TextureRegion regionToDraw = standing;
        if (xSpeed != 0) {
            regionToDraw = (TextureRegion) walking.getKeyFrame(animationTimer);
        }
        if (xSpeed < 0) {
            if (!regionToDraw.isFlipX()) regionToDraw.flip(true,false);
        } else if (xSpeed > 0) {
            if (regionToDraw.isFlipX()) regionToDraw.flip(true,false);
        }
        setRegion(regionToDraw);
    }

    public void defineAnt(float x, float y){

        BodyDef bdef = new BodyDef();
        bdef.position.set(x , y); //initial position
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body  = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(60/ Insects.PPM ,60/ Insects.PPM );
        fdef.filter.categoryBits = Insects.INSECT_BIT;
        fdef.filter.maskBits = Insects.FLOWER_BIT | Insects.BRICK_BIT | Insects.LEVEL_END_BIT | Insects.DAMAGE_BIT | Insects.PASS_BLOCK_BIT;
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(Constants.INSECT_BODY);

    }

}
