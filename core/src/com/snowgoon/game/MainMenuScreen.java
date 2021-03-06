/**
 * 
 */
package com.snowgoon.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Main Menu for the game
 * 
 * @author snowgoon88@gmail.com
 */
public class MainMenuScreen implements Screen {
	
	final GDXInertia _game;
	OrthographicCamera _camera;

	/**
	 * 
	 */
	public MainMenuScreen( final GDXInertia game ) {
		_game = game;
		
		_camera = new OrthographicCamera();
        _camera.setToOrtho(false, 800, 480);
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#show()
	 */
	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        _camera.update();
        _game._spriteBatch.setProjectionMatrix(_camera.combined);

        _game._spriteBatch.begin();
        _game._font.draw( _game._spriteBatch, "Welcome to Drop!!! ", 100, 150);
        _game._font.draw( _game._spriteBatch, "Tap anywhere to begin!", 100, 100);
        _game._spriteBatch.end();

        if (Gdx.input.isTouched()) {
            //_game.setScreen( new GameScreen( _game));
        	_game.setScreen(new JoystickScreen( _game ));
            dispose();
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
		// TODO Auto-generated method stub

	}

}
