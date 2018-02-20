package com.gstrzal.insects.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.gstrzal.insects.Insects;
import com.gstrzal.insects.config.Constants;

/**
 * Created by Gabriel on 15/10/2017.
 */

public class Besouro extends Insect {

    public static final int WIDTH = 128;
    public static final int HEIGHT = 208;

    private float animationTimer = 0;
    private final Animation walking;
    private final TextureRegion standing;
    private final TextureRegion jumpUp;
    private final TextureRegion jumpDown;

    private float circleDifference = 0f;


    public Besouro(World world, Texture texture){
        this(world, texture, 200/ Insects.PPM, 380/ Insects.PPM);
    }


    public Besouro(World world, Texture texture, float x, float y){
        super(texture);
        this.world = world;

        TextureRegion[] regions = TextureRegion.split(texture, WIDTH, HEIGHT)[0];
        walking = new Animation(0.25F, regions[0], regions[1]);
        walking.setPlayMode(Animation.PlayMode.LOOP);
        standing = regions[0];
        jumpUp = regions[2];
        jumpDown = regions[3];

        defineBesouro(x, y);
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
        if (ySpeed > 0) {
            regionToDraw = jumpUp;
        } else if (ySpeed < 0) {
            regionToDraw = jumpDown;
        }
        if (xSpeed < 0) {
            if (!regionToDraw.isFlipX()) regionToDraw.flip(true,false);
        } else if (xSpeed > 0) {
            if (regionToDraw.isFlipX()) regionToDraw.flip(true,false);
        }
        setRegion(regionToDraw);
    }

    public void defineBesouro(float x, float y){

        BodyDef bdef = new BodyDef();
        bdef.position.set(x, y); //initial position
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body  = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(55/ Insects.PPM ,(100 - circleDifference)/ Insects.PPM , new Vector2(0, circleDifference/ Insects.PPM),0);
        fdef.filter.categoryBits = Insects.INSECT_BIT;
        fdef.filter.maskBits = Insects.BRICK_BIT | Insects.FLOWER_BIT | Insects.LEVEL_END_BIT
                | Insects.DAMAGE_BIT | Insects.PASS_BLOCK_BIT | Insects.SLOPE_BIT | Insects.SWITCH_BIT;
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(Constants.BESOURO_BODY);


        //Base Circle
        FixtureDef fdefCircle = new FixtureDef();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(55/ Insects.PPM);
        circleShape.setPosition(new Vector2(0,-50/ Insects.PPM));
        fdefCircle.filter.categoryBits = Insects.INSECT_BIT;
        fdefCircle.filter.maskBits = Insects.BRICK_BIT | Insects.SLOPE_BIT;
        fdefCircle.shape = circleShape;
        fdefCircle.friction = 0;
        b2body.createFixture(fdefCircle).setUserData(Constants.INSECT_BODY);


        //Base Sensor
        PolygonShape shape2 = new PolygonShape();
        shape2.setAsBox(35/ Insects.PPM ,16/ Insects.PPM , new Vector2(0,-100/ Insects.PPM),0);
        fdef.shape = shape2;
        fdef.isSensor = true;
        fdef.filter.categoryBits = Insects.BASE_BIT;
        fdef.filter.maskBits = Insects.BRICK_BIT | Insects.PASS_BLOCK_BIT | Insects.SLOPE_BIT;
        b2body.createFixture(fdef).setUserData(Constants.INSECT_BASE);
    }
}
