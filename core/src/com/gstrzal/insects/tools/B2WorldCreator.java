package com.gstrzal.insects.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.gstrzal.insects.Insects;
import com.gstrzal.insects.config.Constants;
import com.gstrzal.insects.entity.Flower;
import com.gstrzal.insects.entity.LevelEndBlock;
import com.gstrzal.insects.entity.LevelSwitch;
import com.gstrzal.insects.entity.PushBox;

/**
 * Created by Gabriel on 03/09/2017.
 */

public class B2WorldCreator {

    public Array<Flower> flowers;
    public Array<PushBox> pushBoxes;
    public LevelSwitch levelSwitch;
    public LevelEndBlock levelEndBlock;
    private final static float circleRadius = 32f;

    public B2WorldCreator(World world, TiledMap map, Insects insects){
        flowers = new Array<Flower>();
        pushBoxes = new Array<PushBox>();

        //Blocks
        FixtureDef blockFdef = new FixtureDef();
        PolygonShape blockShape = new PolygonShape();
        BodyDef blockBdef = new BodyDef();
        Body blockBody;
        if(map.getLayers().get(Constants.MAP_BLOCKS) != null)
        for (MapObject object : map.getLayers().get(Constants.MAP_BLOCKS).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            blockBdef.type = BodyDef.BodyType.StaticBody;
            blockBdef.position.set((rect.getX() + rect.getWidth() / 2) / Insects.PPM, (rect.getY() + rect.getHeight() / 2) / Insects.PPM);
            blockBody = world.createBody(blockBdef);
            blockShape.setAsBox((rect.getWidth() / 2) / Insects.PPM, (rect.getHeight() / 2) / Insects.PPM);
            blockFdef.shape = blockShape;
            blockFdef.filter.categoryBits = Insects.BRICK_BIT;
            blockFdef.filter.maskBits = Insects.INSECT_BIT | Insects.BASE_BIT | Insects.CHANGE_INSECT_BIT | Insects.PUSH_BLOCK_BIT;
            blockFdef.friction = 0;
            blockBody.createFixture(blockFdef).setUserData(Constants.MAP_BLOCKS);
        }

        //Pass Blocks
        FixtureDef passBlockFdef = new FixtureDef();
        PolygonShape passBlockShape = new PolygonShape();
        BodyDef passBlockBdef = new BodyDef();
        Body passBlockBody;
        if(map.getLayers().get(Constants.MAP_PASS_BLOCKS) != null)
        for (MapObject object : map.getLayers().get(Constants.MAP_PASS_BLOCKS).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            passBlockBdef.type = BodyDef.BodyType.StaticBody;
            passBlockBdef.position.set((rect.getX() + rect.getWidth() / 2) / Insects.PPM, (rect.getY() + rect.getHeight() / 2) / Insects.PPM);
            passBlockBody = world.createBody(passBlockBdef);
            passBlockShape.setAsBox((rect.getWidth() / 2) / Insects.PPM, (rect.getHeight() / 2) / Insects.PPM);
            passBlockFdef.shape = passBlockShape;
            passBlockFdef.filter.categoryBits = Insects.PASS_BLOCK_BIT;
            passBlockFdef.filter.maskBits = Insects.INSECT_BIT | Insects.BASE_BIT;
            passBlockFdef.friction = 0;
            passBlockBody.createFixture(passBlockFdef).setUserData(Constants.MAP_PASS_BLOCKS);
        }


        //Flowers
        FixtureDef flowerFdef = new FixtureDef();
        CircleShape flowerCircleShape =new CircleShape();
        BodyDef flowerBdef = new BodyDef();
        if(map.getLayers().get(Constants.MAP_FLOWERS) != null)
        for (MapObject object : map.getLayers().get(Constants.MAP_FLOWERS).getObjects()) {
            Ellipse ellipse = ((EllipseMapObject) object).getEllipse();
            flowerBdef.type = BodyDef.BodyType.StaticBody;
            flowerBdef.position.set(ellipse.x / Insects.PPM + (ellipse.width / 2) / Insects.PPM,
                    ellipse.y/ Insects.PPM + (ellipse.height / 2) / Insects.PPM);
            flowerCircleShape.setRadius(circleRadius/Insects.PPM);
            flowerFdef.shape = flowerCircleShape;
            flowerFdef.isSensor = true;
            flowerFdef.filter.categoryBits = Insects.FLOWER_BIT;
            flowerFdef.filter.maskBits = Insects.INSECT_BIT ;
            Body flowerBody = world.createBody(flowerBdef);
            flowerBody.createFixture(flowerFdef).setUserData(Constants.MAP_FLOWERS);
            Flower f = new Flower(flowerBody, insects);
            flowerBody.setUserData(f);
            flowers.add(f);

        }

        //Level End
        FixtureDef levelEndFdef = new FixtureDef();
        CircleShape levelEndCircularShape =new CircleShape();
        BodyDef levelEndBdef = new BodyDef();
        if(map.getLayers().get(Constants.MAP_END) != null)
        for (MapObject object : map.getLayers().get(Constants.MAP_END).getObjects()) {
            Ellipse ellipse = ((EllipseMapObject) object).getEllipse();
            levelEndBdef.type = BodyDef.BodyType.StaticBody;
            levelEndBdef.position.set(ellipse.x / Insects.PPM + (ellipse.width / 2) / Insects.PPM,
                    ellipse.y/ Insects.PPM + (ellipse.height / 2) / Insects.PPM);
            levelEndCircularShape.setRadius(circleRadius/Insects.PPM);
            levelEndFdef.shape = levelEndCircularShape;
            levelEndFdef.isSensor = true;
            levelEndFdef.filter.categoryBits = Insects.LEVEL_END_BIT;
            levelEndFdef.filter.maskBits = Insects.INSECT_BIT;
            Body endBody = world.createBody(levelEndBdef);
            endBody.createFixture(levelEndFdef).setUserData(Constants.MAP_END);
        }

        //Damage
        FixtureDef damageFdef = new FixtureDef();
        PolygonShape damageShape = new PolygonShape();
        BodyDef damageBdef = new BodyDef();
        Body damageBody;
        if(map.getLayers().get(Constants.MAP_DAMAGE) != null)
        for (MapObject object : map.getLayers().get(Constants.MAP_DAMAGE).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            damageBdef.type = BodyDef.BodyType.StaticBody;
            damageBdef.position.set((rect.getX() + rect.getWidth() / 2) / Insects.PPM, (rect.getY() + rect.getHeight() / 2) / Insects.PPM);
            damageBody = world.createBody(damageBdef);
            damageShape.setAsBox((rect.getWidth() / 2) / Insects.PPM, (rect.getHeight() / 2) / Insects.PPM);
            damageFdef.shape = damageShape;
            damageFdef.isSensor = true;
            damageFdef.filter.categoryBits = Insects.DAMAGE_BIT;
            damageFdef.filter.maskBits = Insects.INSECT_BIT | Insects.BASE_BIT;
            damageFdef.friction = 0;
            damageBody.createFixture(damageFdef).setUserData(Constants.MAP_DAMAGE);
        }


        //Slope
        BodyDef slopeBdef = new BodyDef();
        FixtureDef slopeFdef = new FixtureDef();
        Body slopeBody;
        if(map.getLayers().get(Constants.MAP_SLOPE) != null)
            for (MapObject object : map.getLayers().get(Constants.MAP_SLOPE).getObjects().getByType(PolygonMapObject.class)) {
                Polygon polygonObject = ((PolygonMapObject) object).getPolygon();
                slopeBdef.type = BodyDef.BodyType.StaticBody;
                slopeBdef.position.set(polygonObject.getOriginX()/Insects.PPM, polygonObject.getOriginY()/Insects.PPM);
                PolygonShape polygon = new PolygonShape();
                float[] vertices = polygonObject.getTransformedVertices();
                float[] worldVertices = new float[vertices.length];

                for (int i = 0; i < vertices.length; ++i) {
                    worldVertices[i] = vertices[i] / Insects.PPM;
                }
                polygon.set(worldVertices);
                slopeFdef.shape = polygon;
                slopeFdef.filter.categoryBits = Insects.SLOPE_BIT;
                slopeFdef.filter.maskBits = Insects.INSECT_BIT | Insects.BASE_BIT | Insects.CHANGE_INSECT_BIT;
                slopeBody = world.createBody(slopeBdef);
                slopeBody.createFixture(slopeFdef).setUserData(Constants.MAP_SLOPE);
            }


        //Push Block
        BodyDef pushBlockBdef = new BodyDef();
        FixtureDef pushBlockFdef = new FixtureDef();
        PolygonShape pushBlockShape = new PolygonShape();
        Body pushBlockBody;
        if(map.getLayers().get(Constants.MAP_PUSH_BLOCKS) != null)
            for (MapObject object : map.getLayers().get(Constants.MAP_PUSH_BLOCKS).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                pushBlockBdef.type = BodyDef.BodyType.DynamicBody;
                pushBlockBdef.position.set((rect.getX() + rect.getWidth() / 2) / Insects.PPM, (rect.getY() + rect.getHeight() / 2) / Insects.PPM);
                pushBlockBody = world.createBody(pushBlockBdef);
                pushBlockShape.setAsBox((rect.getWidth() / 2) / Insects.PPM, (rect.getHeight() / 2) / Insects.PPM);
                pushBlockFdef.shape = pushBlockShape;
                pushBlockFdef.filter.categoryBits = Insects.PUSH_BLOCK_BIT;
                pushBlockFdef.filter.maskBits = Insects.INSECT_BIT | Insects.BASE_BIT | Insects.BRICK_BIT | Insects.CHANGE_INSECT_BIT;
                MassData massData = new MassData();
                massData.mass = 1000000;
                pushBlockBody.setMassData(massData);
                pushBlockBody.createFixture(pushBlockFdef).setUserData(Constants.MAP_PUSH_BLOCKS);
                PushBox pushBox = new PushBox(pushBlockBody, insects);
                pushBlockBody.setUserData(pushBox);
                pushBoxes.add(pushBox);
            }

        //Switch
        BodyDef switchBdef = new BodyDef();
        FixtureDef switchFdef = new FixtureDef();
        PolygonShape switchShape = new PolygonShape();
        Body switchBody;
        if(map.getLayers().get(Constants.MAP_SWITCH) != null)
            for (MapObject object : map.getLayers().get(Constants.MAP_SWITCH).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                switchBdef.type = BodyDef.BodyType.StaticBody;
                switchBdef.position.set((rect.getX() + rect.getWidth() / 2) / Insects.PPM, (rect.getY() + rect.getHeight() / 2) / Insects.PPM);
                switchBody = world.createBody(switchBdef);
                switchShape.setAsBox((rect.getWidth() / 2) / Insects.PPM, (rect.getHeight() / 2) / Insects.PPM);
                switchFdef.shape = switchShape;
                switchFdef.isSensor = true;
                switchFdef.filter.categoryBits = Insects.SWITCH_BIT;
                switchFdef.filter.maskBits = Insects.INSECT_BIT | Insects.BASE_BIT;

                switchBody.createFixture(switchFdef).setUserData(Constants.MAP_SWITCH);
                levelSwitch = new LevelSwitch(switchBody, insects);
                switchBody.setUserData(levelSwitch);

            }



        //Level End Block
        BodyDef levelEndBlockBdef = new BodyDef();
        FixtureDef levelEndBlockFdef = new FixtureDef();
        PolygonShape levelEndShape = new PolygonShape();
        Body levelEndBlockBody;
        if(map.getLayers().get(Constants.MAP_LEVEL_END_BLOCK) != null)
            for (MapObject object : map.getLayers().get(Constants.MAP_LEVEL_END_BLOCK).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                levelEndBlockBdef.type = BodyDef.BodyType.StaticBody;
                levelEndBlockBdef.position.set((rect.getX() + rect.getWidth() / 2) / Insects.PPM, (rect.getY() + rect.getHeight() / 2) / Insects.PPM);
                levelEndBlockBody = world.createBody(levelEndBlockBdef);
                levelEndShape.setAsBox((rect.getWidth() / 2) / Insects.PPM, (rect.getHeight() / 2) / Insects.PPM);
                levelEndBlockFdef.shape = levelEndShape;
                levelEndBlockFdef.filter.categoryBits = Insects.BRICK_BIT;
                levelEndBlockFdef.filter.maskBits = Insects.INSECT_BIT | Insects.BASE_BIT;

                levelEndBlockBody.createFixture(levelEndBlockFdef).setUserData(Constants.MAP_LEVEL_END_BLOCK);
                levelEndBlock = new LevelEndBlock(levelEndBlockBody, insects);
                levelEndBlockBody.setUserData(levelEndBlock);

            }
    }
}
