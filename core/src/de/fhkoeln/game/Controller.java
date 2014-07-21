package de.fhkoeln.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

import de.fhkoeln.game.Player.DirectionX;
import de.fhkoeln.game.Player.DirectionY;
import de.fhkoeln.game.Player.State;
import de.fhkoeln.game.utils.libgdx.Multiplexer;

public class Controller implements InputProcessor{
	
	private GestureDetector gd;



    private InputMultiplexer im;
	private Player player;
    private int leftDownX;
    private int leftDownY;
    private int leftPointer;
    private int rightDownX;
    private int rightDownY;
    private int rightPointer;
    private float tapSquareSize = 40;
    private State tmp1;
    private DirectionX tmp2;
    private DirectionY tmp3;
    private Boolean tmp4;

    public Controller(Player player) {
        //InputMultiplexer im = new InputMultiplexer(this);
        //this.gd = new GestureDetector(this);


        Gdx.input.setInputProcessor(this);


        this.player = player;
	}
	
	public GestureDetector getGd() {
		return gd;
	}


	public void setGd(GestureDetector gd) {
		this.gd = gd;
	}


	private void moveFilter(){
		
		
		if((tmp1 != player.getState()) || tmp2 != player.getDirX() || tmp3 != player.getDirY() || tmp4 != player.jumpState){
			
			player.move();
			tmp1 = player.getState();
			tmp2 = player.getDirX();
			tmp3 = player.getDirY();
			tmp4 = player.jumpState;
		}
			
	}
	public boolean touchDragged (int x, int y, int pointer) {


		//Gdx.input.getDeltaX(pointer);


		if (Gdx.input.getX(pointer) < Gdx.graphics.getWidth()/2 ){

            if (!isWithinTapSquare(x,y,leftDownX,leftDownY)) {

                if ((leftPointer == pointer) && (x > leftDownX+20)) {
                    player.setState(State.Walking);
                    player.setDirX(DirectionX.RIGHT);
                }
                if ((leftPointer == pointer) && (x < leftDownX-20)) {
                    player.setState(State.Walking);
                    player.setDirX(DirectionX.LEFT);
                }
                if ((leftPointer == pointer) && (y < leftDownY-20)) {
                    player.setState(State.Walking);
                    player.setDirY(DirectionY.UP);
                }
                if ((leftPointer == pointer) && (y > leftDownY+20)) {
                    player.setState(State.Walking);
                    player.setDirY(DirectionY.DOWN);
                }
            }
            else player.setState(State.Standing);
		}
		else if (Gdx.input.getX(pointer) > Gdx.graphics.getWidth()/2 ){
			if (Gdx.input.getDeltaY(pointer) < 0){
				player.setJumpState();
				//System.out.println("Pointer Nr:"+pointer);
			}
		}

		player.move();

	    return false;
	    }

    private boolean isWithinTapSquare (float x, float y, float centerX, float centerY) {
        return Math.abs(x - centerX) < tapSquareSize && Math.abs(y - centerY) < tapSquareSize;
    }
	

    private boolean isTapped(){



        return false;
    }
	
	
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
        if (Gdx.input.getX(pointer) < Gdx.graphics.getWidth()/2 ) {
            leftDownX = screenX;
            leftDownY = screenY;
            leftPointer = pointer;
        }else{
            rightDownX = screenX;
            rightDownY = screenY;
            rightPointer = pointer;


        }
        System.out.println("POS: "+leftDownX+"-"+leftDownY);

		return false;
	}


	@Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // TODO Auto-generated method stub
        //if (Gdx.input.getX(pointer) < Gdx.graphics.getWidth()/2 ){
        if (leftPointer == pointer) {
            player.setState(State.Standing);
        }

            if(isWithinTapSquare(screenX,screenY,rightDownX,rightDownY)){
                player.setHit(8);

        }
        //moveFilter();
        player.move();
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
