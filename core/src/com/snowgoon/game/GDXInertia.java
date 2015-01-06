package com.snowgoon.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

public class GDXInertia extends Game {
//	/** Need orthographic Camera for visualization of 2D projection */
//	OrthographicCamera _cam;
	/** Something that can render Shapes. Link with OpenGL */
	ShapeRenderer _renderer;
	
	private OrthographicCamera _camera;
	SpriteBatch _spriteBatch;
	public BitmapFont _font;
	
	// Position where screen touched or clicked
	Vector3 _touchPos;
	
	@Override
	public void create () {
//		_cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		_renderer = new ShapeRenderer();
		
		// Set a camera view
		_camera = new OrthographicCamera();
		_camera.setToOrtho(false, 800, 480);
		
		// To display sprites
		_spriteBatch = new SpriteBatch();
		
		//Use LibGDX's default Arial font.
        _font = new BitmapFont();
        this.setScreen(new MainMenuScreen(this));

	}

	@Override
	public void render () {
//		GL20 gl = Gdx.gl;
//	
//		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
//		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		
//		float ship[] = { 1.0f, 0.0f,	
//				0.0f, -0.5f,
//				0.0f, 0.5f,
//				1.0f, 0.0f };
//		
//		
//		_renderer.setAutoShapeType(true);
//		_renderer.begin();
//		_renderer.setColor( 1.0f, 0.0f, 0.0f, 0.0f );
//		_renderer.line(0.0f, 0.0f, 100.0f, 100.0f);
//		_renderer.end();
//		
//		_renderer.begin();
//		_renderer.translate(100.0f, 100.0f, 0.0f);
//		_renderer.setColor( 1.0f, 1.0f, 1.0f, 0.0f );
//		_renderer.scale(10.0f, 10.0f, 1.0f);
//		_renderer.polyline(ship);
//		_renderer.scale(0.1f,  0.1f,  0.1f);
//		_renderer.translate(-100.0f, -100.0f, 0.0f);
//		_renderer.end();
		
		// When using screen
		super.render(); //important!
	}
	

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.ApplicationAdapter#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		_spriteBatch.dispose();
		_font.dispose();
	}
	
	
}
