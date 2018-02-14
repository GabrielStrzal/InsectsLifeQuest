package com.gstrzal.insects.entity;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.gstrzal.insects.Insects;
import com.gstrzal.insects.config.Constants;

/**
 * Created by lelo on 06/02/18.
 */

public class PushBox {
    private Body body;
    private AssetManager assetManager;
    private float width;
    private float height;
    private Texture texture;

    public PushBox(Body body, Insects insects){
        this.body = body;
        this.assetManager = insects.getAssetManager();
        texture = assetManager.get(Constants.PUSH_BLOCK);
        height = texture.getHeight()/Insects.PPM;
        width = texture.getWidth()/Insects.PPM;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, (body.getPosition().x - width/2), (body.getPosition().y - height/2), height, width);
    }

    public float getWidth() { return width; }
    public float getHeight() { return height; }
}
