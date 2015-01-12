/**
 * 
 */
package com.snowgoon.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

/**
 * A sensor activates when touched by a Laser.
 * <ul>
 * <li> _center : pt where the sensor look at.</li>
 * <li> _angle : absolute angle from center.</li>
 * <li> _dist : distance from sensor </li>
 * <li> _type : sensor type.</li>
 * 
 * @author snowgoon88@gmail.com
 */
public class Sensor {
	int _id;
	Vector3 _center;
	float _angle;
	float _dist;
	String _type;
	boolean _fgActivated;
	
	final float ANGLEBOUND = MathUtils.PI / 72f; // 2.5 degree
	final float ANGCOS = MathUtils.cos(ANGLEBOUND);
	final float ANGSIN = MathUtils.sin(ANGLEBOUND);
	final float SYMBOLSIZE = 20f;
	
	/** Transformation Matrix for rendering */
	Matrix4 _sensorTransform = new Matrix4();
	
	public Sensor( int id, Vector3 center, float angle, float dist, String type) {
		_id = id;
		_center = center;
		_angle = angle; 
		_dist = dist;
		_type = type;
	}
	
	public void render( GDXInertia game) {
		// Dans la bonne direction
		// Transformation : translation(_center) then rotation(_rotAngle);
		_sensorTransform.setToTranslation(_center);
		_sensorTransform.rotateRad(0f, 0f, 1f, _angle); 

		game._renderer.setTransformMatrix(_sensorTransform);
		
		game._renderer.begin();
		if ( (_fgActivated && _type.compareTo("open")==0)
				|| (!_fgActivated && _type.compareTo("alarm")==0)) {
			game._renderer.setColor(0.0f, 1.0f, 0.0f, 1.0f); // green
		}
		else if ( (_fgActivated && _type.compareTo("close")==0)
				|| (_fgActivated && _type.compareTo("alarm")==0)) {
			game._renderer.setColor(1.0f, 0.0f, 0.0f, 1.0f); // red
		}
		else {
			game._renderer.setColor(1.0f, 1.0f, 0.0f, 1.0f); // yellow
		}
		// Receptive field
		game._renderer.line( - ANGSIN * (_dist-5f), ANGCOS * (_dist-5f),
				- ANGSIN * (_dist), ANGCOS * (_dist) );
		game._renderer.line( - ANGSIN * (_dist), ANGCOS * (_dist),
				 ANGSIN * (_dist), ANGCOS * (_dist));
		game._renderer.line( ANGSIN * (_dist), ANGCOS * (_dist),
				 ANGSIN * (_dist-5f), ANGCOS * (_dist-5f));
		game._renderer.line(0, _dist, 0, _dist+5f);
		// Type
		if ( _type.compareTo("close") == 0 ) {
			game._renderer.line(0, _dist+5f, 0, _dist+5f+SYMBOLSIZE);
			game._renderer.line(-SYMBOLSIZE/2f, _dist+SYMBOLSIZE/2f, SYMBOLSIZE/2f, _dist+SYMBOLSIZE/2f);
		}
		else if ( _type.compareTo("open") == 0 ) {
			game._renderer.circle(0, _dist+5f+SYMBOLSIZE/2f, SYMBOLSIZE/2f);
		}
		else if ( _type.compareTo("alarm") == 0 ) {
			game._renderer.line(-SYMBOLSIZE/2f, _dist+5f, SYMBOLSIZE/2f, _dist+5f);
			game._renderer.line(SYMBOLSIZE/2f, _dist+5f, 0, _dist+5f+SYMBOLSIZE);
			game._renderer.line(0, _dist+5f+SYMBOLSIZE, -SYMBOLSIZE/2f, _dist+5f);
		}
		//
		game._renderer.end();
	}
	public void updateWithBeam( float beamAngle ) {
		// TODO normalize for pb around PI
		float normedAngle = normalizeAngleRad(beamAngle);
		System.out.println("  beam="+MathUtils.degRad*normedAngle 
				+ " in (" + (MathUtils.degRad * (_angle-ANGLEBOUND)) 
				+ ", " + (MathUtils.degRad * (_angle+ANGLEBOUND)) + ")");
		if ( ((_angle-ANGLEBOUND <= normedAngle) && (normedAngle <= _angle+ANGLEBOUND)) 
				|| ((_angle-ANGLEBOUND <= normedAngle+2f*MathUtils.PI) 
						&& (normedAngle+2f*MathUtils.PI <= _angle+ANGLEBOUND))
				|| ((_angle-ANGLEBOUND <= normedAngle-2f*MathUtils.PI)
						&& (normedAngle-2f*MathUtils.PI <= _angle+ANGLEBOUND)) ){
			_fgActivated = true;
			System.out.println("  TRUE");
		}
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
