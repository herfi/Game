package de.fhkoeln.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Player implements ContactListener {

    static final float WORLD_TO_BOX=1/100f; //4.821428571f*
    static final float Box_TO_WORLD=100f;

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


    /*

    P -> E,CP
    CP -> S,P
    E -> P,CE
    S -> C

     */


    public static enum State {

		Standing, Walking, Jumping, Dying, Dead
	}
	public static enum DirectionX {
		LEFT, RIGHT, STAND
	}

    public static enum DirectionY {
        UP, DOWN, STAND
    }
	
	private float maxVelocity;
    private float maxVelocityY;
	private float jump_velocity;
	private Vector2 velocity;
    private Vector2 pos;

    boolean canApplyForce = false;
    boolean jumpState = false;

	private float PlayerPosX ;
	private float PlayerPosY ;
	private float PlayerPosZ;
	private Vector3 StartPlayerPos;
	
	private State state;
	private DirectionX dirx;
    private DirectionY diry;
    private TextureAtlas textureAtlas;
    private TextureAtlas.AtlasSprite atlasSprite;
    private Sprite sprite;
	private Animation animation;

    OrthographicCamera camera;
	

    Texture koalaTexture;
    TextureRegion[] regions;
    Animation stand;
    Animation jump;
    Animation walk;
	private Body groundBody;
	private Body playerBody;


    public Sprite getSprite() {
        return sprite;
    }

    public Vector2 getPos() {
        return pos;
    }

    public Player(OrthographicCamera camera){
/*		this.textureAtlas = new TextureAtlas(Gdx.files.internal("spineboy.atlas"));
		//this.animation = new Animation(1/15f, textureAtlas.getRegions());
//        this.stand = new Animation(1/15f, textureAtlas.findRegion("spineboy"));
        sprite = textureAtlas.createSprite("spineboy");
        sprite.rotate90(true);*/
    	this.camera = camera;
  //  	StartPlayerPos=new Vector3(Gdx.graphics.getWidth()/2*WORLD_TO_BOX, Gdx.graphics.getHeight()/2*WORLD_TO_BOX,0);
        StartPlayerPos=new Vector3(1f, 0.5f, 0);

        PlayerPosX=StartPlayerPos.x;
    	PlayerPosY=StartPlayerPos.y;
    	
        setState(State.Standing);
		setMaxVelocity(100 * WORLD_TO_BOX);
        setMaxVelocityY(50 * WORLD_TO_BOX);

        //this.gd = new GestureDetector(this);
		this.velocity = new Vector2(0, 0);
        this.pos = new Vector2(PlayerPosX, PlayerPosY);

        this.koalaTexture = new Texture("koala.png");
        this.regions = TextureRegion.split(koalaTexture, 18, 26)[0];
        this.stand = new Animation(0, regions[0]);
        this.jump = new Animation(0, regions[1]);
        this.walk = new Animation(0.15f, regions[2], regions[3], regions[4]);
        this.walk.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
	}

	
	public Vector3 getStartPlayerPos() {
		return StartPlayerPos;
	}

	public void setStartPlayerPos(Vector3 startPlayerPos) {
		StartPlayerPos = startPlayerPos;
	}

    public float getBody(WorldBuilder world) {

        BodyDef groundBodyDef = new BodyDef();
        BodyDef playerBodyDef = new BodyDef();
        // We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
        groundBodyDef.fixedRotation=true;
        groundBodyDef.type = BodyDef.BodyType.DynamicBody;
        playerBodyDef.fixedRotation=true;
        playerBodyDef.type = BodyDef.BodyType.DynamicBody;

//        playerBodyDef.linearDamping = 2f;
        // Set our body's starting position in the world
        groundBodyDef.position.set(world.getMapWidth()/2, getPlayerPosY());
        playerBodyDef.position.set(getPlayerPosX(), getPlayerPosY()+1);

        //        bodyDef.position.set(1, 500);

        //System.out.println((Float)tiledMap.getLayers().get("Grund").getObjects().get("grund").getProperties().get("y"));

        // Create our body in the world using our body definition
        this.groundBody = world.getWorld().createBody(groundBodyDef);

        this.playerBody = world.getWorld().createBody(playerBodyDef);
        this.groundBody.setGravityScale(0);
        // Create a circle shape and set its radius to 6
        //      CircleShape circle = new CircleShape();
        //      circle.setRadius(6f);
        // Create a polygon shape
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(world.getMapWidth()/2,0.001f);
        // Create a fixture definition to apply our shape to
        FixtureDef groundFixtureDef = new FixtureDef();
        groundFixtureDef.shape = groundBox;
        groundFixtureDef.density = 10000000f;
        groundFixtureDef.friction = 1f;
        groundFixtureDef.restitution = 0f; // Make it bounce a little bit
        groundFixtureDef.filter.categoryBits=CATEGORY_CONTROLP;
        groundFixtureDef.filter.maskBits=MASK_CONTROLLER;


        Fixture fixture = groundBody.createFixture(groundFixtureDef);

        PolygonShape playerBox = new PolygonShape();
        playerBox.setAsBox(10*WORLD_TO_BOX,20*WORLD_TO_BOX);

        FixtureDef playerFixtureDef = new FixtureDef();
        playerFixtureDef.shape = playerBox;
        playerFixtureDef.density = 1f;
        playerFixtureDef.friction = 0.1f;
        playerFixtureDef.restitution = 0f; // Make it bounce a little bit
        Fixture playerFixture = playerBody.createFixture(playerFixtureDef);
        playerFixtureDef.filter.categoryBits=CATEGORY_PLAYER;
        playerFixtureDef.filter.maskBits=MASK_PLAYER;


        playerBox.dispose();
        groundBox.dispose();

/*        //test
        BodyDef enemyBodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(10,10);


        // Create our body in the world using our body definition
        Body body = world.createBody(bodyDef);
        Body playerBody = world.createBody(enemyBodyDef);

        // Create a polygon shape
        PolygonShape enemyBox = new PolygonShape();
        enemyBox.setAsBox(camera.viewportWidth,1*WORLD_TO_BOX);
        // Create a fixture definition to apply our shape to
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = enemyBox;


        // Create our fixture and attach it to the body
        Fixture fixture = body.createFixture(fixtureDef);

        // Remember to dispose of any shapes after you're done with them!
        // BodyDef and FixtureDef don't need disposing, but shapes do.
        enemyBox.dispose();
        //test ende*/

        return 0;
    }

/*    private FixtureDef makeRectFixture(float width,float height, BodyDef.BodyType bodyType, float density, float restitution, Vector2 pos){

        PolygonShape bodyShape = new PolygonShape();

        float w=ConvertToBox(width/2f);
        float h=ConvertToBox(height/2f);
        bodyShape.setAsBox(w,h);

        FixtureDef fixtureDef=new FixtureDef();
        fixtureDef.density=density;
        fixtureDef.restitution=restitution;
        fixtureDef.shape=bodyShape;


        body.createFixture(fixtureDef);
        bodyShape.dispose();
    }*/
	
	public float getFixture() {
		
		
		
		return 0;
	}
	
	public float getPlayerPosX() {
		return PlayerPosX;
	}

	public void setPlayerPosX(float playerPosX) {
		PlayerPosX = playerPosX;
	}

	public float getPlayerPosY() {
		return PlayerPosY;
	}

	public void setPlayerPosY(float playerPosY) {
		PlayerPosY = playerPosY;
	}

	public float getPlayerPosZ() {
		return PlayerPosZ;
	}


	public void setPlayerPosZ(float playerPosZ) {
		PlayerPosZ = playerPosZ;
	}
	
	public Animation getAnimation(){
		
		return walk;
	}
	
	public void disposeRes() {
		
		//textureAtlas.dispose();
	}


	public State getState() {
		return state;
	}


	public void setState(State state) {
		this.state = state;
	}

    public void setJumpState(){ jumpState = true; }
	
	public void move(){

		if(state == State.Walking){
			if (dirx == DirectionX.RIGHT)
				setVelocity(maxVelocity,playerBody.getLinearVelocity().y);

			if (dirx == DirectionX.LEFT)
				setVelocity(-maxVelocity,playerBody.getLinearVelocity().y);

            if (diry == DirectionY.UP)
                groundBody.setLinearVelocity(0f, maxVelocityY);

            if (diry == DirectionY.DOWN)
                groundBody.setLinearVelocity(0f, -maxVelocityY);
        }
		
		if(jumpState){
			//if(playerBody.getPosition().y == body.getPosition().y)
            if (canApplyForce){
                playerBody.applyLinearImpulse(0, playerBody.getMass()*9, 0, playerBody.getWorldCenter().y, true);
                canApplyForce = false;
                jumpState = false;
            }

		}
		
		else if(state == State.Standing){
			setVelocity(0f,playerBody.getLinearVelocity().y);
            groundBody.setLinearVelocity(0,0);
            dirx = DirectionX.STAND;
            diry = DirectionY.STAND;
			//velocity.x = 0;


			
		}

        System.out.println("State: "+state+" Direction: "+diry+"--"+dirx);
	}
	
	public void update(){
		
		//PlayerPosX += velocity.x;			
		//PlayerPosY += velocity.y;
		
		PlayerPosX=playerBody.getPosition().x;
		PlayerPosY=playerBody.getPosition().y;

		// groundbody sollte player folgen:
		//groundBody.setTransform(playerBody.getPosition().x, groundBody.getPosition().y, 0);
		
	}


	public float getMaxVelocity() {
		return maxVelocity;
	}

	public void setMaxVelocity(float maxVelocity) {
		this.maxVelocity = maxVelocity;
	}

    public float getMaxVelocityY() {
        return maxVelocityY;
    }

    public void setMaxVelocityY(float maxVelocityY) {
        this.maxVelocityY = maxVelocityY;
    }
	
	public Vector2 getVelocity() {
		return velocity;
	}


	public void setVelocity(Vector2 velocity) {
		//body.setLinearVelocity(velocity.x,velocity.y);
        playerBody.setLinearVelocity(velocity);
        //velocity.x = max_velocity;
		this.velocity = velocity;
	}
	
	public void setVelocity(Float velocityX, Float velocityY) {
		//body.setLinearVelocity(velocityX,velocityY);
		//body.s
		Vector2 temp = new Vector2(velocity.x,velocityY);
        playerBody.setLinearVelocity(temp);
        velocity.x = velocityX;
        velocity.y = velocityY;
		
	}


	public DirectionX getDirX() {
		return dirx;
	}

    public DirectionY getDirY() {
        return diry;
    }

	public void setDirX(DirectionX dir) {
		this.dirx = dir;
	}

    public void setDirY(DirectionY dir) {
        this.diry = dir;
    }
	
	
	//GestureListener start
	
	
	
	//InputProcessor end

    @Override
    public void beginContact(Contact contact) {
        //state = State.Standing;
        canApplyForce = true;
        jumpState = false;
        System.out.println("CONTACT!:");
    }

    @Override
    public void endContact(Contact contact) {
       // state = State.Jumping;
        System.out.println("LOST CONTACT!:");
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
