package com.gstrzal.insects.entity;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.gstrzal.insects.Insects;
import com.gstrzal.insects.config.Constants;

/**
 * Created by lelo on 06/02/18.
 */

public class Flower {
    private Body body;
    private AssetManager assetManager;
    private Sprite sprite;
    private float width;
    private float height;

    public Flower(Body body, Insects insects){
        this.body = body;
        this.assetManager = insects.getAssetManager();
        sprite = new Sprite((Texture)assetManager.get(Constants.FLOWER));
        height = sprite.getHeight()/Insects.PPM;
        width = sprite.getWidth()/Insects.PPM;
    }

    public void render(SpriteBatch batch, float delta) {
        float rotation = 0;
        rotation += sprite.getRotation() + (45 * delta);
        rotation %= 360;
        sprite.setOrigin(sprite.getWidth()/2/Insects.PPM, sprite.getHeight()/2/Insects.PPM);
        sprite.setRotation(rotation);
        batch.draw(sprite, (body.getPosition().x - width/2), (body.getPosition().y - height/2), height/2, width/2,height, width,1,1, rotation);
    }

    public float getWidth() { return width; }
    public float getHeight() { return height; }
}
