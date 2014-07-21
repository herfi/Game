package de.fhkoeln.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import de.fhkoeln.game.utils.Box2DMapObjectParser;

/**
 * Created by goetsch on 15.07.14.
 */
public class WorldBuilder {
    World world;
    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;
    static final float WORLD_TO_BOX=1/100f;
    float accumulator;


    private Float mapWidth;
    private Float mapHeight;
    private Body body;
    private BodyDef.BodyType bodyType;
    BodyDef bodyDef;
    Player player;
    OrthographicCamera camera;

    final short CATEGORY_PLAYER = 0x0001;  // 0000000000000001 in binary
    final short CATEGORY_ENEMY1 = 0x0002; // 0000000000000010 in binary
    final short CATEGORY_SCENERY = 0x0004; // 0000000000000100 in binary
    final short CATEGORY_CONTROLP = 0x0008; // 0000000000001000 in binary
    final short CATEGORY_CONTROLE1 = 0x0010; // 0000000000010000 in binary
    final short CATEGORY_CONTROLE2 = 0x0020;
    final short CATEGORY_CONTROLE3 = 0x0040;
    final short CATEGORY_CONTROLE4 = 0x0080; // 0000000000010000 in binary
    final short CATEGORY_CONTROLE5 = 0x0100;
    final short CATEGORY_CONTROLE6 = 0x0200;
    final short CATEGORY_ENEMY2 = 0x0400; // 0000000000000010 in binary
    final short CATEGORY_ENEMY3 = 0x0800; // 0000000000000010 in binary
    final short CATEGORY_ENEMY4 = 0x0002; // 0000000000000010 in binary
    final short CATEGORY_ENEMY5 = 0x0002; // 0000000000000010 in binary
    final short CATEGORY_ENEMY6 = 0x0002; // 0000000000000010 in binary

    final short MASK_PLAYER = CATEGORY_ENEMY1 | CATEGORY_CONTROLP;
    final short MASK_CONTROLLER = CATEGORY_SCENERY | CATEGORY_PLAYER;
    final short MASK_SCENERY = CATEGORY_CONTROLP;
    final short MASK_ENEMY1 = CATEGORY_PLAYER | CATEGORY_CONTROLE1;
    final short MASK_CONTROLE1 = CATEGORY_SCENERY | CATEGORY_ENEMY1;


    public Body getBody() {
        return body;
    }

    public WorldBuilder(Player player,OrthographicCamera camera) {
        world = new World(new Vector2(0, -9.81f), true);
        this.camera = camera;
        tiledMap = new TmxMapLoader().load("tmnt.tmx");
        Box2DMapObjectParser parser = new Box2DMapObjectParser(WORLD_TO_BOX);
        parser.load(world, tiledMap);

        //parser.getBodies().;
        mapWidth = tiledMap.getProperties().get("width", Integer.class).floatValue()*tiledMap.getProperties().get("tilewidth", Integer.class).floatValue();
        mapHeight = tiledMap.getProperties().get("height", Integer.class).floatValue()*tiledMap.getProperties().get("tileheight", Integer.class).floatValue();
        System.out.println("mapsize:"+ mapWidth);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, WORLD_TO_BOX);

        //camera.setToOrtho(false, mapHeight*(16/9f)*WORLD_TO_BOX, mapHeight*WORLD_TO_BOX);
        camera.setToOrtho(false, mapHeight*(16/9f)*WORLD_TO_BOX, mapHeight*WORLD_TO_BOX);

        camera.position.set(mapHeight*(16/9f)/2*WORLD_TO_BOX, mapHeight/2*WORLD_TO_BOX, 100);
        camera.update();


        //createWall(mapWidth / 2, 0, mapWidth / 2, 10f); //Bottom wall



        //Rectangle groundSize = ((RectangleMapObject)tiledMap.getLayers().get("Grund").getObjects().get("grund")).getRectangle();



        //System.out.println("groundSizeX:"+groundSize.getX());

        //createWall(groundSize.getWidth()/2,0,groundSize.getX()+groundSize.getWidth()/2,groundSize.getY()+groundSize.getHeight()); // top wall

        this.player = player;
        player.getBody(this);

        world.setContactListener(player);

        /*// First we create a body definition
        BodyDef bodyDef = new BodyDef();
        BodyDef playerBodyDef = new BodyDef();
        // We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        playerBodyDef.type = BodyDef.BodyType.DynamicBody;
        // Set our body's starting position in the world
        bodyDef.position.set(player.getPlayerPosX(), (Float)tiledMap.getLayers().get("Grund").getObjects().get("grund").getProperties().get("y"));
        playerBodyDef.position.set(player.getPlayerPosX(), player.getPlayerPosY());
        //        bodyDef.position.set(1, 500);

        System.out.println((Float)tiledMap.getLayers().get("Grund").getObjects().get("grund").getProperties().get("y"));

        // Create our body in the world using our body definition
        Body body = world.createBody(bodyDef);
        Body playerBody = world.createBody(playerBodyDef);

        //body.setLinearVelocity(15,10);
        //playerBody.setLinearVelocity(5,0);

        // Create a circle shape and set its radius to 6
        //      CircleShape circle = new CircleShape();
        //      circle.setRadius(6f);
        // Create a polygon shape
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(30,30);
        // Create a fixture definition to apply our shape to
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = groundBox;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0f; // Make it bounce a little bit

        // Create our fixture and attach it to the body
        Fixture fixture = body.createFixture(fixtureDef);
        Fixture playerFixture = playerBody.createFixture(fixtureDef);

        // Remember to dispose of any shapes after you're done with them!
        // BodyDef and FixtureDef don't need disposing, but shapes do.
        groundBox.dispose();
        //tiledMapRenderer.setView(camera);
        //tiledMapRenderer.render();
*/  
    }

    public void createEnemy(){
        BodyDef enemyBodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(80,80);


        // Create our body in the world using our body definition
        Body body = world.createBody(bodyDef);
        Body playerBody = world.createBody(enemyBodyDef);

        //body.setLinearVelocity(15,10);
        //playerBody.setLinearVelocity(5,0);
;
        // Create a polygon shape
        PolygonShape enemyBox = new PolygonShape();
        enemyBox.setAsBox(15,30);
        // Create a fixture definition to apply our shape to
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = enemyBox;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0f; // Make it bounce a little bit
        fixtureDef.filter.categoryBits = CATEGORY_ENEMY1;


        // Create our fixture and attach it to the body
        Fixture fixture = body.createFixture(fixtureDef);

        // Remember to dispose of any shapes after you're done with them!
        // BodyDef and FixtureDef don't need disposing, but shapes do.
        enemyBox.dispose();
    }

    public void createWall(float sizex, float sizey, float posx, float posy){
        BodyDef enemyBodyDef = new BodyDef();

        enemyBodyDef.type = BodyDef.BodyType.StaticBody;
        //enemyBodyDef.position.set(10,0.1f);
        enemyBodyDef.position.set(posx*WORLD_TO_BOX,posy*WORLD_TO_BOX);

        // Create our body in the world using our body definition
        Body body = world.createBody(enemyBodyDef);
        Body playerBody = world.createBody(enemyBodyDef);

         // Create a polygon shape
        PolygonShape enemyBox = new PolygonShape();
        //enemyBox.setAsBox(camera.viewportWidth,0.01f*WORLD_TO_BOX);
        enemyBox.setAsBox(sizex*WORLD_TO_BOX, sizey*WORLD_TO_BOX);

        // Create a fixture definition to apply our shape to
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = enemyBox;
        fixtureDef.filter.categoryBits = CATEGORY_SCENERY;
        fixtureDef.filter.maskBits = MASK_SCENERY;


        // Create our fixture and attach it to the body
        Fixture fixture = body.createFixture(fixtureDef);

        // Remember to dispose of any shapes after you're done with them!
        // BodyDef and FixtureDef don't need disposing, but shapes do.
        enemyBox.dispose();
    }

    public TiledMapRenderer getTiledMapRenderer() {
        return tiledMapRenderer;
    }

    public World getWorld() {
        return world;
    }


    float ConvertToBox(float x){
        return x*WORLD_TO_BOX;
    }

   void update(){
       //bodyDef.position.set(player.getPlayerPosX(), (Float)tiledMap.getLayers().get("Grund").getObjects().get("grund").getProperties().get("y"));
       //body.setLinearVelocity(player.getMax_velocity(),0);
       if ((player.getPlayerPosX() > camera.viewportWidth/2) && (camera.position.x+camera.viewportWidth/2 < getMapWidth()-0.1f))
           camera.position.set(player.getPlayerPosX(), mapHeight/2*WORLD_TO_BOX, 0);
       camera.update();
   }


    public float getMapWidth() {
        return mapWidth *WORLD_TO_BOX;
    }
}
