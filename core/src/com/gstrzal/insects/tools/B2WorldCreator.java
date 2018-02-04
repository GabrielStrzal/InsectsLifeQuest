package com.gstrzal.insects.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.gstrzal.insects.Insects;
import com.gstrzal.insects.assets.AssetPaths;

/**
 * Created by Gabriel on 03/09/2017.
 */

public class B2WorldCreator {
    public B2WorldCreator(World world, TiledMap map){
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //Blocks
        for (MapObject object : map.getLayers().get(AssetPaths.MAP_BLOCKS).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Insects.PPM, (rect.getY() + rect.getHeight() / 2) / Insects.PPM);
            body = world.createBody(bdef);
            shape.setAsBox((rect.getWidth() / 2) / Insects.PPM, (rect.getHeight() / 2) / Insects.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = Insects.BRICK_BIT;
            fdef.filter.maskBits = Insects.INSECT_BIT | Insects.BASE_BIT;
            fdef.friction = 0;

            body.createFixture(fdef).setUserData(AssetPaths.MAP_BLOCKS);
        }

        //Coins
        for (MapObject object : map.getLayers().get(AssetPaths.MAP_COINS).getObjects()) {
            Ellipse ellipse = ((EllipseMapObject) object).getEllipse();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(ellipse.x / Insects.PPM, ellipse.y/ Insects.PPM);
            CircleShape circleShape =new CircleShape();
            circleShape.setRadius(8/Insects.PPM);
            fdef.shape = circleShape;
            fdef.isSensor = true;
            fdef.filter.categoryBits = Insects.COIN_BIT;
            fdef.filter.maskBits = Insects.INSECT_BIT;
            Body coinBody = world.createBody(bdef);
            coinBody.createFixture(fdef).setUserData(AssetPaths.MAP_COINS);
        }

        //Level End
        for (MapObject object : map.getLayers().get(AssetPaths.MAP_END).getObjects()) {
            Ellipse ellipse = ((EllipseMapObject) object).getEllipse();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(ellipse.x / Insects.PPM, ellipse.y/ Insects.PPM);
            CircleShape circleShape =new CircleShape();
            circleShape.setRadius(8/Insects.PPM);
            fdef.shape = circleShape;
            fdef.isSensor = true;
            fdef.filter.categoryBits = Insects.LEVEL_END;
            fdef.filter.maskBits = Insects.INSECT_BIT;
            Body coinBody = world.createBody(bdef);
            coinBody.createFixture(fdef).setUserData(AssetPaths.MAP_END);
        }
    }
}
