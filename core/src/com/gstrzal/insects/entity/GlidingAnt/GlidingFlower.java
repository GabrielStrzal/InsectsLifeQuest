package com.gstrzal.insects.entity.GlidingAnt;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by lelo on 18/03/18.
 */

public class GlidingFlower {
    private static final float COLLISION_RECTANGLE_WIDTH = 13f;
    private static final float COLLISION_RECTANGLE_HEIGHT = 447f;
    private static final float COLLISION_CIRCLE_RADIUS = 33f;
    private static final float MAX_SPEED_PER_SECOND = 200F;
    public static final float WIDTH = COLLISION_CIRCLE_RADIUS * 2;
    private static final float HEIGHT_OFFSET = -150f;
    private static final float DISTANCE_BETWEEN_FLOWER_TOP_AND_BOTTOM = 225F;
    private final Circle floorCollisionCircle;
    private final Rectangle floorCollisionRectangle;
    private final Circle ceilingCollisionCircle;
    private final Rectangle ceilingCollisionRectangle;


    private final Texture floorTexture;
    private final Texture ceilingTexture;

    private boolean pointClaimed = false;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    private float x = 0;
    private float y = 0;

    public GlidingFlower(Texture floorTexture,Texture ceilingTexture) {
        this.floorTexture = floorTexture;
        this.ceilingTexture = ceilingTexture;
        this.y = MathUtils.random(-380, HEIGHT_OFFSET);
        this.floorCollisionRectangle = new Rectangle(x, y,
                COLLISION_RECTANGLE_WIDTH, COLLISION_RECTANGLE_HEIGHT);
        this.floorCollisionCircle = new Circle(x +
                floorCollisionRectangle.width / 2, y +
                floorCollisionRectangle.height, COLLISION_CIRCLE_RADIUS);
        this.ceilingCollisionRectangle = new Rectangle(x,
                floorCollisionCircle.y + DISTANCE_BETWEEN_FLOWER_TOP_AND_BOTTOM,
                COLLISION_RECTANGLE_WIDTH, COLLISION_RECTANGLE_HEIGHT);
        this.ceilingCollisionCircle = new Circle(x +
                ceilingCollisionRectangle.width / 2,
                ceilingCollisionRectangle.y, COLLISION_CIRCLE_RADIUS);

    }
    public void setPosition(float x) {
        this.x = x;
        updateCollisionCircle();
        updateCollisionRectangle();
    }
    private void updateCollisionCircle() {
        floorCollisionCircle.setX(x + floorCollisionRectangle.width / 2);
        ceilingCollisionCircle.setX(x + ceilingCollisionRectangle.width / 2);
    }
    private void updateCollisionRectangle() {
        floorCollisionRectangle.setX(x);
        ceilingCollisionRectangle.setX(x);
    }
    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.circle(floorCollisionCircle.x,
                floorCollisionCircle.y, floorCollisionCircle.radius);
        shapeRenderer.rect(floorCollisionRectangle.x,
                floorCollisionRectangle.y, floorCollisionRectangle.width,
                floorCollisionRectangle.height);
        shapeRenderer.circle(ceilingCollisionCircle.x,
                ceilingCollisionCircle.y, ceilingCollisionCircle.radius);
        shapeRenderer.rect(ceilingCollisionRectangle.x,
                ceilingCollisionRectangle.y, ceilingCollisionRectangle.width,
                ceilingCollisionRectangle.height);
    }
    public void update(float delta) {
        setPosition(x - (MAX_SPEED_PER_SECOND * delta));
    }

    public boolean isGlidingAntColliding(GlidingAnt glidingAnt) {
        Circle glidingAntCollisionCircle = glidingAnt.getCollisionCircle();
        return Intersector.overlaps(glidingAntCollisionCircle, ceilingCollisionCircle) ||
                Intersector.overlaps(glidingAntCollisionCircle, floorCollisionCircle) ||
                Intersector.overlaps(glidingAntCollisionCircle, ceilingCollisionRectangle) ||
                Intersector.overlaps(glidingAntCollisionCircle, floorCollisionRectangle);
    }
    public boolean isPointClaimed() {
        return pointClaimed;
    }
    public void markPointClaimed() {
        pointClaimed = true;
    }
    public void draw(SpriteBatch batch) {
        drawFloorFlower(batch);
        drawCeilingFlower(batch);
    }
    private void drawFloorFlower(SpriteBatch batch) {
        float textureX = floorCollisionCircle.x - floorTexture.getWidth() / 2;
        float textureY = floorCollisionRectangle.getY() + COLLISION_CIRCLE_RADIUS;
        batch.draw(floorTexture, textureX, textureY);
    }
    private void drawCeilingFlower(SpriteBatch batch) {
        float textureX = ceilingCollisionCircle.x - ceilingTexture.getWidth() / 2;
        float textureY = ceilingCollisionRectangle.getY() - COLLISION_CIRCLE_RADIUS;
        batch.draw(ceilingTexture, textureX, textureY);
    }
}
