/**
 * 
 */
package com.snowgoon.game;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.MathUtils;



/**
 * Implements rotating disk with :
 * <ul>
 * <li> Laser sources </li>.
 * </ul>
 * @author snowgoon88@gmail.com
 *
 */
public class LockScreen implements Screen {
	/** Ref to the game */
	GDXInertia _game;
	
	Vector3 _center;
	public boolean _verb = false;
	
	private OrthographicCamera _camera;

	/** Array of Disks */
	ArrayList<LockDisk> _disks;
	
	// Position screen touched
	Vector3 _touchPos = new Vector3();
	// Transformation Matrix
	Matrix4 _diskTransform = new Matrix4();
	
	
	/** Decimal formating */
	static DecimalFormat df4_1 = new DecimalFormat( "0.0" );
	
	
	public LockScreen( final GDXInertia game) {
		_game = game;
		
		// Init disk info : IN REVERSE ORDER
		_center = new Vector3( 400, 240, 0);
		_disks = new ArrayList<LockDisk>();
		LockDisk disk;
		disk = new LockDisk(_game, _disks.size(), _center, 141, 180, null);
		disk.addLaser( - 2.0f * MathUtils.PI / 3.0f );
		disk.addObstacle( MathUtils.PI / 2.0f, MathUtils.PI * 3.0f / 4.0f);
		disk.addObstacle( MathUtils.PI - 0.2f, MathUtils.PI);
		disk.addObstacle( - MathUtils.PI, - MathUtils.PI + 0.2f);
		//disk._verb = true;
		_disks.add( disk );
		
		disk = new LockDisk(_game, _disks.size(), _center, 101, 140, disk);
		disk.addLaser( MathUtils.PI / 3.0f );
		disk.addLaser( MathUtils.PI);
		disk.addObstacle( - MathUtils.PI * 3.0f/ 4.0f, - MathUtils.PI * 5.0f / 8.0f);
		//disk._verb = true;
		_disks.add( disk );
		
		disk = new LockDisk(_game, _disks.size(), _center, 60, 100, disk);
		disk.addLaser( MathUtils.PI / 12.0f );
		disk.addLaser( - MathUtils.PI/2.0f);
		//disk._verb = true;
		_disks.add( disk );
		
		
		// Set a camera view
		_camera = new OrthographicCamera();
		_camera.setToOrtho(false, 800, 480);
	}
	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#show()
	 */
	@Override
	public void show() {
		_game._renderer.setProjectionMatrix(_camera.combined);
		_game._spriteBatch.setProjectionMatrix(_camera.combined);
		_game._renderer.setAutoShapeType(true);

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
		
		// Detect if Disk is tapped
		if(Gdx.input.isTouched()) {
			_touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			_camera.unproject( _touchPos );
			
			if (_verb) {
				System.out.println( "touchPos = " + _touchPos.toString());
			}
//			// Update Disk Independantly
//			for (LockDisk disk : _disks) {
//				disk.updateTouched( _touchPos );
//			}
			// Update All interior disks
			boolean forceUpdate = false;
			for (LockDisk disk : _disks) {
				if (forceUpdate || disk.isTouched( _touchPos)) {
					forceUpdate = true;
					disk.updateTouched( _touchPos );	
				}
			}
			
			// DEBUG
			_game._renderer.setTransformMatrix( _diskTransform.idt() );
			_game._renderer.begin();
			_game._renderer.setColor(1f, 0, 0, 1f); // Red
			_game._renderer.circle(_touchPos.x, _touchPos.y, 25, 16);
//			if (_fgTapped ) {
//				_game._renderer.line(_center, _touchPos );
//			}
			_game._renderer.end();
		}
		else {
			// Update Disk
			for (LockDisk disk : _disks) {
				disk.updateNotTouched();
			}
		}
		
		// Draw Circles
		//System.out.println("\n*******************************");
		for (LockDisk disk : _disks) {
			disk.render();
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
