package de.fhkoeln.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

import de.fhkoeln.game.Player.Direction;
import de.fhkoeln.game.Player.State;

public class Controller implements GestureListener, InputProcessor{
	
	private GestureDetector gd;
	private Player player;
    private float firstDownX;
    private float firstDownY;

	public Controller(Player player) {
		this.player = player;
	}
	
	public GestureDetector getGd() {
		return gd;
	}


	public void setGd(GestureDetector gd) {
		this.gd = gd;
	}
	
	public boolean touchDragged (int x, int y, int pointer) {
		
		
		//Gdx.input.getDeltaX(pointer);
		
		
		if (Gdx.input.getX(pointer) < Gdx.graphics.getWidth()/2 ){
			
			if (Gdx.input.getDeltaX(pointer) > 0){
				player.setState(State.Walking);
				player.setDir(Direction.RIGHT);
				//System.out.println("Pointer Nr:"+pointer+"velocity"+velocity.x);
			}
			if (Gdx.input.getDeltaX(pointer) < 0) {
				player.setState(State.Walking);
				player.setDir(Direction.LEFT);
			}
		}
		else if (Gdx.input.getX(pointer) > Gdx.graphics.getWidth()/2 ){
			if (Gdx.input.getDeltaY(pointer) < 0){
				player.setState(State.Jumping);
				player.setDir(Direction.RIGHT);
				System.out.println("Pointer Nr:"+pointer);
			}
		}
		
		player.move();
				
	    return true;
	    }
	
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		firstDownX = x;
        firstDownY = y;
        System.out.println("Pointer Nr:"+x);

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
		//if (Gdx.input.getX(pointer) < Gdx.graphics.getWidth()/2 ){
			player.setState(State.Standing);
			player.move();
		//}
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
	

}
