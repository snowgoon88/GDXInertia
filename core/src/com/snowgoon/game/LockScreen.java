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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
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
	public boolean _verb = true;
	
	private OrthographicCamera _camera;

	/** Array of Disks */
	ArrayList<LockDisk> _disks;
	/** Array of Sensors */
	ArrayList<Sensor> _sensors;

	// Buttons Textures
	Texture _resetIcon;
	Rectangle _resetRect; // position and size of Icon
	boolean _resetClicked = false;
	Texture _nextIcon;
	Rectangle _nextRect;
	boolean _nextClicked = false;
	// Last Click Time
	long _lastClickTime = 0;
	long REFRACTORY_TIME = 100000000; // 0.1 in seconds
	
	// Which solution is displayed
	int _indSolDisplayed = -1;
	
	// Position screen touched
	Vector3 _touchPos = new Vector3();
	// Transformation Matrix
	Matrix4 _diskTransform = new Matrix4();
	
	
	/** Decimal formating */
	static DecimalFormat df1_1 = new DecimalFormat( "0.0" );
	
	
	public LockScreen( final GDXInertia game) {
		_game = game;
		
		// Buttons
		_resetIcon = new Texture(Gdx.files.internal("reset-icon.png"));
		_resetRect = new Rectangle(800f - 70f, 480f - 70f, 64f, 64f);
		_nextIcon = new Texture(Gdx.files.internal("next-icon.png"));
		_nextRect = new Rectangle(800f - 70f, 480f - 2*70f, 64f, 64f);
		
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
		
		// Init Sensor Info
		_sensors = new ArrayList<Sensor>();
		Sensor sensor;
		sensor = new Sensor( _sensors.size(), _center, MathUtils.degRad * 15f, 200f, "close");
		_sensors.add(sensor);
		
		sensor = new Sensor( _sensors.size(), _center, MathUtils.degRad * 180f, 200f, "open");
		_sensors.add(sensor);
		
		sensor = new Sensor( _sensors.size(), _center, MathUtils.degRad * (-100f), 200f, "alarm");
		_sensors.add(sensor);
		
		
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
		
		
		debugTouch();
		// Detect if Disk is tapped
		if(Gdx.input.isTouched()) {
			_touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			_camera.unproject( _touchPos );
			
			if (_verb) {
				System.out.println( "touchPos = " + _touchPos.toString());
			}
			// Check Buttons only after some refractory time
			if (System.nanoTime() - _lastClickTime > REFRACTORY_TIME) {
				if ( _resetRect.contains( _touchPos.x, _touchPos.y) ) {
					_resetClicked = true;
					_lastClickTime = System.nanoTime();
				}
				else if ( _nextRect.contains( _touchPos.x, _touchPos.y )) {
					_nextClicked = true;
					_lastClickTime = System.nanoTime();
				}
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
//			_game._renderer.setTransformMatrix( _diskTransform.idt() );
//			_game._renderer.begin();
//			_game._renderer.setColor(1f, 0, 0, 1f); // Red
//			_game._renderer.circle(_touchPos.x, _touchPos.y, 25, 16);
////			if (_fgTapped ) {
////				_game._renderer.line(_center, _touchPos );
////			}
//			_game._renderer.end();
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
		
		// Update and Draw Sensors
		for (Sensor sensor : _sensors) {
			sensor._fgActivated = false;
			//System.out.println( "Sensor ["+sensor._id+"]");
			for (LockDisk disk : _disks) {
				for (LockDisk.LaserSrc laser : disk._lasers) {
					if (laser._distBeam > 180f ) {
						//System.out.println("  laser_"+disk._id+"/"+laser._id +" at angle " + (MathUtils.radDeg * (laser._angLaser + disk._rotAngle)));
						sensor.updateWithBeam(laser._angLaser + disk._rotAngle);
					}
				}

			}
			sensor.render( _game );
		}
		
		// Draw Buttons and information message
		String info = "Sol n° " + _indSolDisplayed + " / " + _game._solutions.size();
		if (_indSolDisplayed >= 0) {
			info += "\n=> (";
			info += df1_1.format( _game._solutions.get(_indSolDisplayed)[0]*MathUtils.radDeg) + ", ";
			info += df1_1.format( _game._solutions.get(_indSolDisplayed)[1]*MathUtils.radDeg) + ", ";
			info += df1_1.format( _game._solutions.get(_indSolDisplayed)[2]*MathUtils.radDeg) + ", ";
			info += ")";
		}
		//TextBounds tb = _game._font.getBounds(info);
		_game._renderer.setTransformMatrix(_diskTransform.idt());
		_game._spriteBatch.setTransformMatrix(_diskTransform.idt());
		_game._spriteBatch.begin();
		// Buttons
		_game._spriteBatch.draw(_resetIcon, _resetRect.x, _resetRect.y, _resetRect.width, _resetRect.height);
		_game._spriteBatch.draw(_nextIcon, _nextRect.x, _nextRect.y, _nextRect.width, _nextRect.height);
		
		// Information message
		// With information
		_game._font.drawMultiLine(_game._spriteBatch, info, 550f, 470f );	
		_game._spriteBatch.end();
		
		// TODO Pas beau car melange au reste !!!!
		if (_resetClicked) {
			_game._renderer.begin();
			_game._renderer.setColor(1f, 0, 0, 1f); // Red
			_game._renderer.rect(_resetRect.x, _resetRect.y, _resetRect.width, _resetRect.height);
			_game._renderer.end();
			for (LockDisk disk : _disks) {
				disk.reset();
			}
			_indSolDisplayed = -1;
			
			// TODO pourrait revenir en position neutre a une certaine vitesse
			_resetClicked = false;
		}
		//
		if (_nextClicked) {
			_game._renderer.begin();
			_game._renderer.setColor(1f, 0, 0, 1f); // Red
			_game._renderer.rect(_nextRect.x, _nextRect.y, _nextRect.width, _nextRect.height);
			
			_indSolDisplayed += 1;
			if (_indSolDisplayed == _game._solutions.size()) {
				_indSolDisplayed = 0;
			}
			// set up the solution
			float [] oneSolution = _game._solutions.get(_indSolDisplayed);
			for (int i = 0; i < oneSolution.length; i++) {
				_disks.get(i).reset();
				_disks.get(i)._rotAngle = oneSolution[i];
			}
			
			_game._renderer.end();
			_nextClicked = false;
		}
	}
	/**
	 * draw disk + number for every touch position.
	 */
	void debugTouch() {

		float[] x = new float[20];
		float[] y = new float[20];
		String[] msg = new String[20];

		// Set up renderer
		_game._renderer.setTransformMatrix( _diskTransform.idt() );
		_game._renderer.begin();
		_game._renderer.setColor(1f, 0, 0, 1f); // Red
		
		// should follow 20 touch max
		for (int i = 0; i < 20; i++) { // 20 is max number of touch points
			if (Gdx.input.isTouched(i)) {
				_touchPos.set(Gdx.input.getX(i), Gdx.input.getY(i), 0);
				_camera.unproject( _touchPos );
				_game._renderer.circle(_touchPos.x, _touchPos.y, 25, 16);
				x[i] = _touchPos.x;
				y[i] = _touchPos.y;
				msg[i] = Integer.toString(i);
				// System.out.println( "msg["+i+"]="+msg[i]);
			}
			else {
				msg[i] = "";
			}
		}
		_game._renderer.end();
		
		// And then number
		_game._spriteBatch.setTransformMatrix( _diskTransform.idt() );
		_game._spriteBatch.begin();
		for (int i = 0; i < msg.length; i++) {
			if (msg[i].compareTo("") != 0) {
				_game._font.draw(_game._spriteBatch, msg[i], x[i], y[i]+30);
			}
		}
		_game._spriteBatch.end();
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
		// Free Textures
		_resetIcon.dispose();

	}
	
	

}
