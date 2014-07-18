package de.fhkoeln.game;

import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
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

/**
 * Created by goetsch on 15.07.14.
 */
public class WorldBuilder {
    World world;
    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;
    static final float WORLD_TO_BOX=1/100f;
    float accumulator;
    private Body body;
    private BodyDef.BodyType bodyType;
    BodyDef bodyDef;
    Player player;

    public Body getBody() {
        return body;
    }

    public WorldBuilder(Player player) {
        world = new World(new Vector2(0, -10), true);
        tiledMap = new TmxMapLoader().load("test.tmx");
       
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, WORLD_TO_BOX);
        
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
        bodyDef.position.set(player.getPlayerPosX(), (Float)tiledMap.getLayers().get("Grund").getObjects().get("grund").getProperties().get("y"));


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
   }
}
