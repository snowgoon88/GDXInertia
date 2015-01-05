/**
 * 
 */
package com.game.snowgoon;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * The Game by itself
 * 
 * @author snowgoon88@gmail.com
 */
public class GameScreen implements Screen {
	// Ref to Main App
	final GDXInertia _game;
	
	// Assets
	private Texture _dropImage;
	private Texture _bucketImage;
	private Sound _dropSound;
	private Music _rainMusic;
	
	private OrthographicCamera _camera;
	
	// Bucket has position and size
	private Rectangle _bucket;
	// Positions of the drops
	private Array<Rectangle> _raindrops;
	// Last time a drop appeared
	private long _lastDropTime;
	// Nb of Drops collected
	int _dropsGathered;
	
	// Position where screen touched or clicked
	Vector3 _touchPos;

	
	public GameScreen( final GDXInertia game) {
		_game = game;
		
		// Load Assets
		// load the images for the droplet and the bucket, 64x64 pixels each
		_dropImage = new Texture(Gdx.files.internal("droplet.png"));
		_bucketImage = new Texture(Gdx.files.internal("bucket.png"));

		// load the drop sound effect and the rain background "music"
		_dropSound = Gdx.audio.newSound(Gdx.files.internal("junggle_waterdrop.wav"));
		_rainMusic = Gdx.audio.newMusic(Gdx.files.internal("acclivity__undertreeinrain.mp3"));
		_rainMusic.setLooping(true);
		
		// Set a camera view
		_camera = new OrthographicCamera();
		_camera.setToOrtho(false, 800, 480);
		
		// Rectangle as virtual Bucket
		// Position the bucket at center bottom
		_bucket = new Rectangle();
		_bucket.x = 800 / 2 - 64 / 2;  // Its size is 64
		_bucket.y = 20;
		_bucket.width = 64;
		_bucket.height = 64;
		
		// Create array for droplet and spawn one
		_raindrops = new Array<Rectangle>();
		spawnRaindrop();
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#show()
	 */
	@Override
	public void show() {
		_rainMusic.play();

	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render(float delta) {
		// Clear bakckground using dark blue
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// Not needed in every frame but good practice
		_camera.update();
		
		// Render Bucket
		_game._spriteBatch.setProjectionMatrix(_camera.combined); // Use Camera Coordinate
		_game._spriteBatch.begin(); // Begin batch task
		_game._font.draw( _game._spriteBatch, "Drops Collected: " + _dropsGathered, 0, 480);
		_game._spriteBatch.draw(_bucketImage, _bucket.x, _bucket.y);
		for(Rectangle raindrop: _raindrops) {
			_game._spriteBatch.draw( _dropImage, raindrop.x, raindrop.y);
		}
		_game._spriteBatch.end();
		
		// Screen touched or mouse clicked
		if(Gdx.input.isTouched()) {
			_touchPos = new Vector3();
			_touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			// Project in camera coordinate
			_camera.unproject( _touchPos );
			_bucket.x = _touchPos.x - 64 / 2;
		}
		
		// Key pressed, move at 200 pixel/second
		if(Gdx.input.isKeyPressed(Keys.LEFT)) _bucket.x -= 200 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Keys.RIGHT)) _bucket.x += 200 * Gdx.graphics.getDeltaTime();
		
		// Stay in the limits
		if( _bucket.x < 0) _bucket.x = 0;
		if( _bucket.x > 800 - 64) _bucket.x = 800 - 64;
		
		// Long enough to create a new drop (1 second)
		if(TimeUtils.nanoTime() - _lastDropTime > 1000000000) spawnRaindrop();
		// Make drop fall down at 200 pixel/seconde
		Iterator<Rectangle> iter = _raindrops.iterator();
		while(iter.hasNext()) {
			Rectangle raindrop = iter.next();
			raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
			if(raindrop.y + 64 < 0) 
				iter.remove();
			// If Drop overlap bucket
			if( raindrop.overlaps( _bucket)) {
				_dropsGathered ++;
				_dropSound.play();
				iter.remove();
			}
		}

	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resize(int, int)
	 */
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#pause()
	 */
	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resume()
	 */
	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#hide()
	 */
	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#dispose()
	 */
	@Override
	public void dispose() {
		_dropImage.dispose();
		_bucketImage.dispose();
		_dropSound.dispose();
		_rainMusic.dispose();

	}
	
	/** Create a nex Raindrop */
	private void spawnRaindrop() {
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0, 800-64);
		raindrop.y = 480;
		raindrop.width = 64;
		raindrop.height = 64;
		_raindrops.add(raindrop);
		_lastDropTime = TimeUtils.nanoTime();
	}

}
