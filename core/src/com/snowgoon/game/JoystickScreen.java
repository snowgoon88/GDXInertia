/**
 * 
 */
package com.snowgoon.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

/**
 * Trial to get a Joystick
 * 
 * @author snowgoon88@gmail.com
 */
public class JoystickScreen implements Screen {
	// Ref to Main App
	final GDXInertia _game;
	
	private OrthographicCamera _camera;
	
	/** Position and Radius of virtual joystick */
	final int _centerX = 100;
	final int _centerY = 100;
	final int _radius = 25;
	
	/** Touch position */
	Vector3 _touchPos = new Vector3();
	boolean _fgTouched = false;
	
	public JoystickScreen( final GDXInertia game ) {
		_game = game;

		// Set a camera view
		_camera = new OrthographicCamera();
		_camera.setToOrtho(false, 800, 480);
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#show()
	 */
	@Override
	public void show() {
		// TODO Auto-generated method stub
		_game._renderer.setAutoShapeType(true);
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render(float delta) {
		// Clear bakckground using black
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Not needed in every frame but good practice
		_camera.update();
		_game._renderer.setProjectionMatrix(_camera.combined);
		
		// Screen touched or mouse clicked
		_fgTouched = false;
		if(Gdx.input.isTouched()) {
			_touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			_game._spriteBatch.setProjectionMatrix(_camera.combined); // Use Camera Coordinate
			_game._spriteBatch.begin();
			// DEBUG
			_game._font.draw( _game._spriteBatch,  "Touched at "+_touchPos, 10, 470);
			// Project in camera coordinate
			_camera.unproject( _touchPos );
			_game._font.draw( _game._spriteBatch,  "        => "+_touchPos, 10, 400);
			_game._spriteBatch.end();
			_fgTouched = true;
		}
		
		// Draw joystick
		_game._renderer.begin();
		_game._renderer.setColor( 1.0f, 1.0f, 0.0f, 1.0f );
		_game._renderer.circle(_centerX, _centerY, _radius, 32);
		if( _fgTouched ) {
			// Light Yellow circle
			_game._renderer.setColor(1.0f, 1.0f, 0.7f, 1.0f);
			_game._renderer.circle( _centerX, _centerY, _radius * 1.35f );
			_game._renderer.setColor( 0.0f, 0.749f, 1.0f, 1.0f );
			_game._renderer.line(_centerX, _centerY, _touchPos.x, _touchPos.y);
		}
		_game._renderer.end();
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
		// TODO Auto-generated method stub

	}

}
