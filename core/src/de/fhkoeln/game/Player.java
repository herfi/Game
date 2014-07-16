package de.fhkoeln.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

public class Player {
	
	public static enum State {

		Standing, Walking, Jumping, Dying, Dead
	}
	public static enum Direction {
		LEFT, RIGHT;
	}
	
	private float max_velocity;
	private float jump_velocity = 40f;
	private Vector2 velocity;
    private Vector2 pos;
	


	private float PlayerPosX = Gdx.graphics.getWidth()/2;
	private float PlayerPosY = Gdx.graphics.getHeight()/2;
	private float PlayerPosZ;
	private State state;
	private Direction dir;
	private TextureAtlas textureAtlas;
    private TextureAtlas.AtlasSprite atlasSprite;
    private Sprite sprite;
	private Animation animation;
	

    Texture koalaTexture;
    TextureRegion[] regions;
    Animation stand;
    Animation jump;
    Animation walk;


    public Sprite getSprite() {
        return sprite;
    }

    public Vector2 getPos() {
        return pos;
    }

    public Player(){
/*		this.textureAtlas = new TextureAtlas(Gdx.files.internal("spineboy.atlas"));
		//this.animation = new Animation(1/15f, textureAtlas.getRegions());
//        this.stand = new Animation(1/15f, textureAtlas.findRegion("spineboy"));
        sprite = textureAtlas.createSprite("spineboy");
        sprite.rotate90(true);*/

        setState(State.Standing);
		setMax_velocity(10f);
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
	
	public void move(){

		if(state == State.Walking){
			if (dir == Direction.RIGHT)
				velocity.x = max_velocity;

			if (dir == Direction.LEFT)
				velocity.x = -max_velocity;
		}
		
		else if(state == State.Jumping){
			velocity.y = jump_velocity;
		}
		
		else if(state == State.Standing){
			velocity.x = 0;
		}
	}
	
	public void update(){
		
		PlayerPosX += velocity.x;			
		PlayerPosY += velocity.y;
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
		this.velocity = velocity;
	}


	public Direction getDir() {
		return dir;
	}


	public void setDir(Direction dir) {
		this.dir = dir;
	}
	
	
	
	//GestureListener start
	
	
	
	//InputProcessor end
}
