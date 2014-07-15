package de.fhkoeln.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

public class Player implements GestureListener, InputProcessor{
	
	public static enum State {

		Standing, Walking, Jumping, Dying, Dead
	}
	public static enum Direction {
		LEFT, RIGHT;
	}
	
	private float max_velocity;
	private float jump_velocity = 40f;
	private Vector2 velocity;
	


	private float PlayerPosX = Gdx.graphics.getWidth()/2;
	private float PlayerPosY = Gdx.graphics.getHeight()/2;
	private float PlayerPosZ;
	private State state;
	private Direction dir;
	private TextureAtlas textureAtlas;
	private Animation animation;
	private GestureDetector gd;

    Texture koalaTexture;
    TextureRegion[] regions;
    Animation stand;
    Animation jump;
    Animation walk;
	
	


	


	public Player(){
//		this.textureAtlas = new TextureAtlas(Gdx.files.internal("spineboy.atlas"));
//		this.animation = new Animation(1/15f, textureAtlas.getRegions());
		setState(State.Standing);
		setMax_velocity(10f);
		//this.gd = new GestureDetector(this);
		this.velocity = new Vector2(0, 0);

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
		
		textureAtlas.dispose();
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
	
	public void draw(){
		
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
	
	public GestureDetector getGd() {
		return gd;
	}


	public void setGd(GestureDetector gd) {
		this.gd = gd;
	}
	
	//GestureListener start
	
	
	public boolean touchDragged (int x, int y, int pointer) {
		
		
		//Gdx.input.getDeltaX(pointer);
		
		
		if (Gdx.input.getX(pointer) < Gdx.graphics.getWidth()/2 ){
			
			if (Gdx.input.getDeltaX(pointer) > 0){
				setState(State.Walking);
				setDir(Direction.RIGHT);
				System.out.println("Pointer Nr:"+pointer);
			}
			if (Gdx.input.getDeltaX(pointer) < 0) {
				setState(State.Walking);
				setDir(Direction.LEFT);
			}
		}
		if (Gdx.input.getX(pointer) > Gdx.graphics.getWidth()/2 ){
			if (Gdx.input.getDeltaY(pointer) < 0){
				setState(State.Jumping);
				setDir(Direction.RIGHT);
				System.out.println("Pointer Nr:"+pointer);
			}
		}
		
		move();
				
	    return true;
	    }
	
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		
		
//		if (deltaX > 0){
//			setState(State.Walking);
//			setDir(Direction.RIGHT);
//		}
//		if (deltaX < 0) {
//			setState(State.Walking);
//			setDir(Direction.LEFT);
//		}
//		move();
	
		
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {

		
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}

	
	
	//GestureListener end
	
	
	//InputProcessor start

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		if (Gdx.input.getX(pointer) < Gdx.graphics.getWidth()/2 ){
			setState(State.Standing);
			move();
		}
		return false;
	}


	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
	//InputProcessor end
}
