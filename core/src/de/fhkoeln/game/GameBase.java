package de.fhkoeln.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class GameBase extends ApplicationAdapter   {
	SpriteBatch batch;
	Texture img;
	//private BitmapFont font;
	//private String message = "Touch something already!";
	Player player;
	World world = new World(new Vector2(0, -10), true);	
	
	private float elapsedTime = 0;
	
	float w;
	float h;
	private float rotationSpeed =0;
	float dx;
	float dy;
	
	
	
	TiledMap tiledMap;
    OrthographicCamera camera;
    TiledMapRenderer tiledMapRenderer;
	
	@Override
	public void create () {
		rotationSpeed += dy;
		batch = new SpriteBatch();
		//img = new Texture("badlogic.jpg");
		player = new Player();
		
		//font = new BitmapFont(Gdx.files.internal("data/arial-15.fnt"),false);
        //font.setColor(Color.RED);			
		w = Gdx.graphics.getWidth();
        h = Gdx.graphics.getHeight();
        
        
        camera = new OrthographicCamera();
        camera.setToOrtho(false,w,h);
        //camera.zoom += 0.5;
		camera.position.set(w/2, h/2, 100);

        //camera.rotate(rotationSpeed, 1, 0, 0);
        //camera.update();
        tiledMap = new TmxMapLoader().load("test.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        //Gdx.input.setInputProcessor(this);
        
        //Gdx.input.setInputProcessor(player.getGd());
        Gdx.input.setInputProcessor(player);

        
	}

	@Override
	public void render () {
//		Gdx.gl.glClearColor(1, 0, 0, 1);
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		camera.rotate(rotationSpeed, 0, 0, 1);
		camera.position.set(player.getPlayerPosX(), player.getPlayerPosY(), 100);
        camera.update();
        
		Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        
        batch.setProjectionMatrix(camera.combined);
        player.draw();
        batch.begin();
		elapsedTime += Gdx.graphics.getDeltaTime();
		batch.draw(player.getAnimation().getKeyFrame(elapsedTime, true), player.getPlayerPosX(), player.getPlayerPosY());
		//player.getAnimation().getKeyFrame(elapsedTime, true);
		//font.drawMultiLine(batch, message, x, y);
		
		batch.end();
		
		world.step(1/45f, 6, 2);
	}
	
	
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		player.disposeRes();
		
	}

	
}
