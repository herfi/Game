package de.fhkoeln.game;

import com.badlogic.gdx.Gdx;
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

    static final float WORLD_TO_BOX=1/100f;
    static final float Box_TO_WORLD=100f;



    public static enum State {

		Standing, Walking, Jumping, Dying, Dead
	}
	public static enum Direction {
		LEFT, RIGHT;
	}
	
	private float max_velocity;
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
	private Direction dir;
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
    	StartPlayerPos=new Vector3(Gdx.graphics.getWidth()/2*WORLD_TO_BOX, Gdx.graphics.getHeight()/2*WORLD_TO_BOX,0);
    	PlayerPosX=StartPlayerPos.x;
    	PlayerPosY=StartPlayerPos.y;
    	
        setState(State.Standing);
		setMax_velocity(130*WORLD_TO_BOX);
		//this.gd = new GestureDetector(this);
		this.velocity = new Vector2(0, 0);
        this.pos = new Vector2(PlayerPosX, PlayerPosY);

        this.koalaTexture = new Texture("koala.png");
        this.regions = TextureRegion.split(koalaTexture, 18, 26)[0];
        this.stand = new Animation(0, regions[0]);
        this.jump = new Animation(0, regions[1]);
        this.walk = new Animation(0.15f, regions[2], regions[3], regions[4]);
        this.walk.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        
        
        
       

        //body.setLinearVelocity(15,10);
        //playerBody.setLinearVelocity(5,0);

       

        // Remember to dispose of any shapes after you're done with them!
        // BodyDef and FixtureDef don't need disposing, but shapes do.
        //groundBox.dispose();
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
        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        playerBodyDef.type = BodyDef.BodyType.DynamicBody;
        playerBodyDef.fixedRotation=true;
//        playerBodyDef.linearDamping = 2f;
        // Set our body's starting position in the world
        groundBodyDef.position.set(getPlayerPosX(), getPlayerPosY());
        playerBodyDef.position.set(getPlayerPosX(), getPlayerPosY()+50);

        //        bodyDef.position.set(1, 500);

        //System.out.println((Float)tiledMap.getLayers().get("Grund").getObjects().get("grund").getProperties().get("y"));

        // Create our body in the world using our body definition
        this.groundBody = world.getWorld().createBody(groundBodyDef);

        this.playerBody = world.getWorld().createBody(playerBodyDef);

        // Create a circle shape and set its radius to 6
        //      CircleShape circle = new CircleShape();
        //      circle.setRadius(6f);
        // Create a polygon shape
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(camera.viewportWidth,1);
        // Create a fixture definition to apply our shape to
        FixtureDef groundFixtureDef = new FixtureDef();
        groundFixtureDef.shape = groundBox;
        groundFixtureDef.density = 1f;
        groundFixtureDef.friction = 1f;
        groundFixtureDef.restitution = 0f; // Make it bounce a little bit


        Fixture fixture = groundBody.createFixture(groundFixtureDef);

        PolygonShape playerBox = new PolygonShape();
        playerBox.setAsBox(100*WORLD_TO_BOX,100*WORLD_TO_BOX);

        FixtureDef playerFixtureDef = new FixtureDef();
        playerFixtureDef.shape = playerBox;
        playerFixtureDef.density = 1f;
        playerFixtureDef.friction = 0.1f;
        playerFixtureDef.restitution = 0f; // Make it bounce a little bit
        Fixture playerFixture = playerBody.createFixture(playerFixtureDef);



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
			if (dir == Direction.RIGHT)
				setVelocity(max_velocity,playerBody.getLinearVelocity().y);
				

			if (dir == Direction.LEFT)
				setVelocity(-max_velocity,playerBody.getLinearVelocity().y);
				//velocity.x = -max_velocity;
		}
		
		if(jumpState){
			//if(playerBody.getPosition().y == body.getPosition().y)
            if (canApplyForce){
                playerBody.applyLinearImpulse(0, playerBody.getMass()*10, 0, playerBody.getWorldCenter().y, true);
                canApplyForce = false;
                jumpState = false;
            }

		}
		
		else if(state == State.Standing){
			setVelocity(0f,playerBody.getLinearVelocity().y);
			//velocity.x = 0;


			
		}

        System.out.println("State: "+state+" jumpState: "+jumpState);
	}
	
	public void update(){
		
		//PlayerPosX += velocity.x;			
		//PlayerPosY += velocity.y;
		
		PlayerPosX=playerBody.getPosition().x;
		PlayerPosY=playerBody.getPosition().y;

		// groundbody sollte player folgen:
		//groundBody.setTransform(playerBody.getPosition().x, groundBody.getPosition().y, 0);
		
	}


	public float getMax_velocity() {
		return max_velocity;
	}


	public void setMax_velocity(float max_velocity) {
		this.max_velocity = max_velocity;
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


	public Direction getDir() {
		return dir;
	}


	public void setDir(Direction dir) {
		this.dir = dir;
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
