package com.gstrzal.insects.entity.GlidingAnt;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;

/**
 * Created by lelo on 18/03/18.
 */

public class GlidingAnt {
    private static final float DIVE_ACCEL = 0.30F;
    private float ySpeed = 0;
    private static final float FLY_ACCEL = 8F;
    private static final float COLLISION_RADIUS = 24f;
    private static final int TILE_WIDTH = 118;
    private static final int TILE_HEIGHT = 118;
    private final Animation animation;


    public Circle getCollisionCircle() {
        return collisionCircle;
    }

    private final Circle collisionCircle;
    private float x = 0;
    private float y = 0;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }




    public GlidingAnt(Texture glidingantTexture) {
        TextureRegion[][] glidingAntTextures = new
                TextureRegion(glidingantTexture).split(TILE_WIDTH, TILE_HEIGHT);
        animation = new Animation(0,glidingAntTextures[0][0], glidingAntTextures[0][1]);
        collisionCircle = new Circle(x, y, COLLISION_RADIUS);
    }

    public void draw(SpriteBatch batch) {
        int anim = 0;
        if(ySpeed > 0) anim = 1;
        TextureRegion glidingAntTexture = (TextureRegion) animation.getKeyFrame(anim);
        float textureX = collisionCircle.x - glidingAntTexture.getRegionWidth() / 2 - 5;
        float textureY = collisionCircle.y - glidingAntTexture.getRegionHeight() / 2 + 5;
        batch.draw(glidingAntTexture, textureX, textureY);
    }

    public void drawDebug( ShapeRenderer shapeRenderer) {
        shapeRenderer.circle(collisionCircle.x, collisionCircle.y,
                collisionCircle.radius);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        updateCollisionCircle();
    }
    private void updateCollisionCircle() {
        collisionCircle.setX(x);
        collisionCircle.setY(y);
    }

    public void update(float delta) {
        ySpeed -= DIVE_ACCEL;
        setPosition(x, y + ySpeed);
    }

    public void flyUp() {
        ySpeed = FLY_ACCEL;
        setPosition(x, y + ySpeed);
    }
    public void glidingAntReset(){
        ySpeed = 0;
    }
}
