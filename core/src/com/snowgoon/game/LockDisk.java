/**
 * 
 */
package com.snowgoon.game;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

/**
 * One of the Disk in a Lock System.
 * 
 * With the following members.
 * <ul>
 * <li> Vector3 _center : center of the Disk.</li>
 * <li> float _radInf, _radSup : radius of the Disk.</li>
 * <li> float _rotAngle : rotation angle of the Disk. 0 = up.</li>
 * <li> float _oldRotAngle : memory of previous rotation angle.</li>
 * <li> boolean _fgTapped : is the Disk selected (by the user).</li>
 * <li> float _angTapped : current angle of the user click/tap.</li>
 * </ul>
 * @author snowgoon88@gmail.com
 *
 */
public class LockDisk {
	/** Ref to the game */
	GDXInertia _game;
	
	// Disk Fields
	int _id;
	Vector3 _center;
	float _radInf;
	float _radSup;
	float _rotAngle;
	float _oldRotAngle;
	boolean _fgTapped;
	float _angTapped;
	
	static final float MAXDISTLASER = 1000.0f;
	
	/** Laser Position */
	class LaserSrc {
		int _id;
		float _angLaser; // relative to disc	
		float _radLaser;
		float _distBeam; // how far travels the Beam
		
		public LaserSrc( int id, float angle ) {
			_angLaser = angle;
			_id = id;
			_radLaser = (_radInf + _radSup ) / 2.0f;
		}
		
		public void render( float distMax ) {
			_game._renderer.rotate(0, 0, 1, _angLaser * MathUtils.radDeg);
			
			_game._renderer.begin();
			// Source
			_game._renderer.setColor(1.0f, 1.0f, 0.0f, 1.0f);
			_game._renderer.line(-4f, _radLaser, 4f, _radLaser);
			_game._renderer.line(4f, _radLaser, 0f, _radLaser-4f);
			_game._renderer.line(0f, _radLaser-4f, -4f, _radLaser);
			// Beam
			_game._renderer.setColor(0.0f, 1.0f, 0.0f, 1.0f);
			_game._renderer.line( 0f,  _radLaser, 0f, distMax );
			_game._renderer.end();
			
			_game._renderer.rotate(0, 0, 1, - _angLaser * MathUtils.radDeg);
		}
	}
	ArrayList<LaserSrc> _lasers;
	
	class Obstacle {
		int _id;
		float _angStart, _angEnd;
		float _radObstacle;
		public Obstacle( int id, float angStart, float angEnd ) {
			_id = id;
			_angStart = angStart;
			_angEnd = angEnd;
			_radObstacle = (_radInf + _radSup ) / 2.0f;
		}
		
		public void render() {
			_game._renderer.begin();
			_game._renderer.setColor(0f, 0f, 1f, 1f);
			
			float ang = _angStart;
			while (ang < _angEnd) {
				float nextAng = Math.min(ang + MathUtils.PI/30f, _angEnd);
				// Don't forget, angle=0 is up !!!
				_game._renderer.line( _radObstacle * MathUtils.cos(ang+MathUtils.PI/2.0f), 
						_radObstacle * MathUtils.sin(ang+MathUtils.PI/2.0f),
						_radObstacle * MathUtils.cos( nextAng+MathUtils.PI/2.0f ),
						_radObstacle * MathUtils.sin( nextAng+MathUtils.PI/2.0f ) );
				ang = nextAng;
			}
//			_game._renderer.arc(0f, 0f, _radObstacle, 
//					_angStart * MathUtils.radDeg, _angEnd * MathUtils.radDeg );
			_game._renderer.end();
		}
	}
	ArrayList<Obstacle> _obstacles;
	
	LockDisk _nextDisk = null;
	
	
	/** Verbose ? */
	public boolean _verb = false;
	
	/** Transformation Matrix for rendering */
	Matrix4 _diskTransform = new Matrix4();
	/** Decimal formating */
	static DecimalFormat df1_1 = new DecimalFormat( "0.0" );
	
	public LockDisk( final GDXInertia game, int id, Vector3 center,
			float radInf, float radSup, LockDisk next ) {
		_game = game;
		_id = id;
		_nextDisk = next;
	
		_center = center;
		_radInf = radInf;
		_radSup = radSup;
		
		_rotAngle = 0.0f;
		_oldRotAngle = _rotAngle;
		_fgTapped = false;
		_angTapped = _rotAngle;
		
		_lasers = new ArrayList<LaserSrc>();
		_obstacles = new ArrayList<Obstacle>();
	}
	public void reset() {
		_rotAngle = 0.0f;
		_oldRotAngle = _rotAngle;
		_fgTapped = false;
		_angTapped = _rotAngle;
	}
	
	public void addLaser( float angle ) {
		_lasers.add( new LaserSrc( _lasers.size(), angle));
	}
	public void addObstacle( float angleStart, float angleEnd ) {
		_obstacles.add( new Obstacle( _obstacles.size(), angleStart, angleEnd) );
	}
	
	/** Render on a Game with proper renderer */
	public void render() {
		// Draw Circle
		// Transformation : translation(_center) then rotation(_rotAngle);
		_diskTransform.setToTranslation(_center);
		_diskTransform.rotateRad(0f, 0f, 1f, _rotAngle); 
	
		_game._renderer.setTransformMatrix(_diskTransform);
		_game._renderer.begin();
		if (_fgTapped ) {
			_game._renderer.setColor(1.0f, 0.0f, 0.0f, 1.0f); // red
		} else {
			_game._renderer.setColor(1.0f, 1.0f, 0.0f, 1.0f); // yellow
		}
		_game._renderer.circle(0f, 0f, _radInf, 64);
		_game._renderer.circle(0f, 0f, _radSup, 64);
		_game._renderer.line(0f, _radSup, 0f, _radSup-10f);
		_game._renderer.end();
		
		// With information
		String info = df1_1.format( normalizeAngleDeg(MathUtils.radDeg * _rotAngle));
		TextBounds tb = _game._font.getBounds(info);

		_game._spriteBatch.setTransformMatrix( _diskTransform );
		_game._spriteBatch.begin();
		_game._font.draw(_game._spriteBatch, info,
				- tb.width/2.0f, _radInf+tb.height+5);
		_game._spriteBatch.end();
		
		for (LaserSrc laser : _lasers) {
			laser._distBeam = -1.0f;
			if( _nextDisk != null ) {
				laser._distBeam = _nextDisk.isBlocking(
						normalizeAngleRad( laser._angLaser + _rotAngle - _nextDisk._rotAngle));
			}
			if (laser._distBeam < 0.0f ) {
				laser._distBeam = MAXDISTLASER;
			}
			if (_verb) {
				System.out.println( "Render Laser "+laser._id+ "with d="+ laser._distBeam 
						+ " at angle=" + (MathUtils.radDeg * (_rotAngle+laser._angLaser)));
			}
			laser.render( laser._distBeam );
		}
		
		for (Obstacle obst : _obstacles) {
			obst.render();
		}
	}

	public boolean isTouched( Vector3 touchPos ) {
		// Within the Disk => distance to center
		Vector3 ptFromCenter = touchPos.cpy().sub( _center );
		float distFromCenter = ptFromCenter.len();
		if (_verb) {
			System.out.println( "ptDist = " + ptFromCenter.toString() +
					" => dist = " + distFromCenter );
		}
		if( (distFromCenter > _radInf) && (distFromCenter < _radSup)) {
			return true;
		}
		else {
			return false;
		}
	}
	/**
	 * Update Disk when the screen has been touched AND disk chosen.
	 * 
	 * @param touchPos : position where screen touched
	 */
	public void updateTouched( Vector3 touchPos ) {
		// Within the Disk => distance to center
		Vector3 ptFromCenter = touchPos.cpy().sub( _center );

		if (_fgTapped) { // Already tapped
			float curAng = MathUtils.atan2( ptFromCenter.y, ptFromCenter.x);
			_rotAngle = normalizeAngleRad(_oldRotAngle + curAng - _angTapped);
		}
		else { // New Tapped
			_angTapped = MathUtils.atan2( ptFromCenter.y, ptFromCenter.x);
			_oldRotAngle = _rotAngle;
		}
		_fgTapped = true;
		if (_verb) {
			System.out.println( "angTapped = " + _angTapped );
		}
		
	}
	/**
	 * Update Disk when the screen is not touched.
	 */
	public void updateNotTouched() {
		_fgTapped = false;
	}
	
	/**
	 * Does this Disk block the "view" for that (relative) angle. 
	 * If not, asks the other 'outsider" Disks.
	 * 
	 * @param angle
	 * @return distance blocking or -1.0f if not.
	 */
	public float isBlocking( float angle ) {
		float dist = -1.0f;
		
		if (_verb) {
			System.out.println( "is Disk_" + _id + " blocking at " + MathUtils.radDeg * angle);
		}
		
		for (Obstacle obst : _obstacles) {
			if( (angle >= obst._angStart) && (angle <= obst._angEnd)) {
				dist = obst._radObstacle;
				if (_verb) {
					System.out.println( "YES : Obst " + obst._id +
							"("+obst._angStart*MathUtils.radDeg +
							", "+ obst._angEnd*MathUtils.radDeg +
							") => Dist = "+dist);
				}
			}
			if( _verb ) {
				System.out.println( "NO : Obst " + obst._id +
							"("+obst._angStart*MathUtils.radDeg +
							", "+ obst._angEnd*MathUtils.radDeg +")");
			}
		}
		
		if( (dist < 0.0f) && (_nextDisk != null) ) {
			float relAngle = normalizeAngleRad( angle + _rotAngle - _nextDisk._rotAngle );
			return _nextDisk.isBlocking( relAngle );
		}
		return dist;
	}
	
	/** Returns angle in ]-180.0, 180.0[ */
	float normalizeAngleDeg(float angle)
	{
	    float newAngle = angle;
	    while (newAngle <= -180f) newAngle += 360f;
	    while (newAngle > 180f) newAngle -= 360f;
	    return newAngle;
	}
	/** Returns angle in ]-PI, PI[ */
	float normalizeAngleRad(float angle)
	{
	    float newAngle = angle;
	    while (newAngle <= - MathUtils.PI) newAngle += 2.0f * MathUtils.PI;
	    while (newAngle > MathUtils.PI) newAngle -= 2.0f * MathUtils.PI;
	    return newAngle;
	}
}
