/**
 * 
 */
package com.snowgoon.game;

import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.snowgoon.game.LockDisk.LaserSrc;

/**
 * Automated testing and solving of LockScreen
 * 
 * @author snowgoon88@gmail.com
 */
public class Solver {
	//final float ANG_INCREMENT = MathUtils.PI / 18f; // 10 degres
	final float ANG_INCREMENT = 2f * MathUtils.PI / 3f; // 180 degres
	
	boolean _verb = true;
	
	/**
	 * Enumerate all Solutions to the problem described in a LockScreen
	 * 
	 * @param problem with _disks and _sensors
	 * @return ArrayList<[AngleRadForEachDisk]>
	 */
	public ArrayList<float[]> getSolutions( LockScreen problem ) {
		
		ArrayList<float[]> solutions = new ArrayList<float[]>();
		int pbSize = problem._disks.size();
		
		// init all to -PI
		for (LockDisk disk : problem._disks) {
			disk.reset();
			disk._rotAngle = - MathUtils.PI;	
		}
		// Solution
		System.out.println( "****** Cherche Solution pour");
		for (LockDisk d : problem._disks) {
			System.out.println( "Disk "+d._id+ " ang="+d._rotAngle*MathUtils.radDeg);
		}
		boolean fgSol = evaluateSolution(problem);
		if (fgSol) {
			float[] oneSolution = new float[pbSize];
			for (int i = 0; i < oneSolution.length; i++) {
				oneSolution[i] = problem._disks.get(i)._rotAngle;
			}
			solutions.add( oneSolution );
		}
		
		// increment while not finished
		boolean fgFinished = false;
		while ( !fgFinished ) {
			// CHECK if SOLUTION
			// INCREMENT
			boolean fgNeedIncrement = true;
			LockDisk disk = problem._disks.get(problem._disks.size()-1);
			while (fgNeedIncrement == true && disk != null) {
				disk._rotAngle += ANG_INCREMENT;
			
				// If over PI -> will try to increment next disk
				if (disk._rotAngle >= MathUtils.PI) {
					disk._rotAngle = - MathUtils.PI;
					disk = disk._nextDisk;
				}
				// else ok, increment is finished
				else {
					fgNeedIncrement = false;
				}
			}
			
			// Finished if disk=null and still need increment
			if (fgNeedIncrement == true && disk == null ) {
				fgFinished = true;
				System.out.println(">>>>>>> FINISHED <<<<<<");
			}
			// else, do we have a solution
			else {
				// Solution
				System.out.println( "****** Cherche Solution pour");
				for (LockDisk d : problem._disks) {
					System.out.println( "Disk "+d._id+ " ang="+d._rotAngle*MathUtils.radDeg);
				}
				fgSol = evaluateSolution(problem);
				if (fgSol) {
					float[] oneSolution = new float[pbSize];
					for (int i = 0; i < oneSolution.length; i++) {
						oneSolution[i] = problem._disks.get(i)._rotAngle;
					}
					solutions.add( oneSolution );
				}
			}
			
		}
		
		System.out.println( "**** SOLUTIONS ****");
		String solStr = "";
		for (float[] fs : solutions) {
			solStr += " (";
			for (int i = 0; i < fs.length; i++) {
				solStr += (fs[i]*MathUtils.radDeg)+", ";
			}
			solStr += "),";
		}
		System.out.println(solStr);
		
		
		return solutions;
	}
	boolean evaluateSolution( LockScreen problem ) {
		// Need to evaluate the distance for each laser
		for (LockDisk disk : problem._disks) {
			for (LaserSrc laser : disk._lasers) {
				laser._distBeam = -1.0f;
				if( disk._nextDisk != null ) {
					laser._distBeam = disk._nextDisk.isBlocking(
							disk.normalizeAngleRad( laser._angLaser + disk._rotAngle - disk._nextDisk._rotAngle));
				}
				if (laser._distBeam < 0.0f ) {
					laser._distBeam = LockDisk.MAXDISTLASER;
				}
				if (_verb) {
					System.out.println( "Update Laser "+laser._id+ " / " + disk._id 
							+ " with d="+ laser._distBeam 
							+ " at angle=" + (MathUtils.radDeg * (disk._rotAngle+laser._angLaser)));
				}
			}
		}
		// Then the status of every Sensor
		boolean _fgOpen = true;
		boolean _fgClosed = false;

		for (Sensor sensor : problem._sensors) {
			sensor._fgActivated = false;
			System.out.println( "Sensor ["+sensor._id+"]");
			for (LockDisk disk : problem._disks) {
				for (LockDisk.LaserSrc laser : disk._lasers) {
					if (laser._distBeam > 180f ) {
						System.out.println("  laser_"+disk._id+"/"+laser._id +" at angle " + (MathUtils.radDeg * (laser._angLaser + disk._rotAngle)));
						sensor.updateWithBeam(laser._angLaser + disk._rotAngle);

						//Adapt _fgOpen according to fgActivated and type of Sensor
						// If any "open" is not activated => NOT open
						if (sensor._type.compareTo("open") == 0) {
							if (sensor._fgActivated == false)
								_fgOpen = false; 	
						}
						// if any other activated => IS closed
						else if (sensor._fgActivated) {
							_fgClosed = true;
						}
					}
				}
			}
		}
		if (_fgOpen && !_fgClosed) {
			System.out.println(">>>>>> Sol VALID");
		}
		else {
			System.out.println(">>>>>> Sol NOT valid");
		}
		return (_fgOpen && !_fgClosed);
		
	}
}
