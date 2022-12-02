package edu.wm.cs301.amazebynoahschulman.gui;

import java.util.logging.Logger;

import edu.wm.cs301.amazebynoahschulman.generation.Maze;

/**
 * Class: WallFollower
 * 
 * Responsibilities: 
 * * Assigns a robot platform to the driver.
 * WallFollower, the driver, will drive the robot in such a way that it follows the WallFollower algorithm.
 * If the WallFollower is able to make the Robot's forward direction point towards the exit, return True.
 * If the WallFollower makes the Robot crash or run out of battery energy, throw an exception.
 * If the WallFollower can not find the exit due to a cycle or some other reason, return False.
 * Identify the total battery energy consumption of the Robot's journey.
 * Identify total number of cells traveled over in Robot's journey. 
 * 
 * Collaborators:
 * Implements RobotDriver.java class.
 * Robot class.
 * Control.java class (NOTE: to access Maze object)
 * 
 * @author Noah Schulman
 *
 */
public class WallFollower implements RobotDriver {
	//private static final Logger LOGGER = Logger.getLogger(Control.class.getName());

	/**
	 * Field variable for the Wizard's corresponding Robot object.
	 */
	Robot theRobot;
	/**
	 * Field variable for the Wizard's corresponding Maze object.
	 */
	Maze theMaze;
	/**
	 * Field variable for the Wizard's corresponding Control object.
	 */
	//Control theController;
	/**
	 * Field value to keep track of the Wizard's path length.
	 */
	int pathLength;
	
	/**
	 * Field variable that stores the amount of time (4 seconds) between failure for the robot's unreliable sensors
	 * multiplied by infinity because the thread.sleep() method takes in milliseconds as a parameter.
	 */
	int meanTimeBetweenFailures = 4 * 1000;
	
	/**
	 * Field variable that stores the amount of time (2 seconds) to repair one of the robot's unreliable sensors
	 * multiplied by infinity because the thread.sleep() method takes in milliseconds as a parameter.
	 */
	int meanTimeToRepair  = 2 * 1000;
	
	/**
	 * empty constructor
	 */
	public WallFollower() {
		// TODO Auto-generated constructor stub
		
	}

	@Override
	public void setRobot(Robot r) {
		// TODO Auto-generated method stub
		theRobot = r;
		pathLength = 0;
	}

	@Override
	public void setMaze(Maze maze) {
		// TODO Auto-generated method stub
		theMaze = maze;
	}

	@Override
	public boolean drive2Exit() throws Exception {
		// start staggered activation of sensor threads if applicable
		startUnreliableSensors();
		// while the robot has not reached the exit
		boolean working = true;
		while (working) {
			try {
				working = drive1Step2Exit();
				// once exit is reached stop sensor threads
				if (working == false) {
					theRobot.stopFailureAndRepairProcess(Robot.Direction.FORWARD);
					theRobot.stopFailureAndRepairProcess(Robot.Direction.LEFT);
					theRobot.stopFailureAndRepairProcess(Robot.Direction.RIGHT);
					theRobot.stopFailureAndRepairProcess(Robot.Direction.BACKWARD);
				}
			}
			// stop sensor threads if an exception is caught
			catch (Exception e){
				theRobot.stopFailureAndRepairProcess(Robot.Direction.FORWARD);
				theRobot.stopFailureAndRepairProcess(Robot.Direction.LEFT);
				theRobot.stopFailureAndRepairProcess(Robot.Direction.RIGHT);
				theRobot.stopFailureAndRepairProcess(Robot.Direction.BACKWARD);
				throw new Exception("robot stopped due to some problem, e.g. lack of energy");
			}
		}
		return true;
	}

	/**
	 * pseudocode for WallFollower algorithm
	 * 
	 *  if (left sensor > 0): (IF LEFT WALL DOESN'T EXIT)
	 *	rotate left, move forward once
	 *
	 *	else if (forward sensor) > 0: (IF LEFT WALL EXISTS AND FORWARD WALL DOESN'T EXIST)
	 *		move forward once
	 *
	 *	else: (IF LEFT WALL EXISTS AND FORWARD WALL EXISTS)
	 *		rotate right
	 */
	@Override
	public boolean drive1Step2Exit() throws Exception {
		// checks if the Robot has stopped for some reason, if so, throw and exception and break out of the method.
		if (theRobot.hasStopped()) {
			throw new Exception("robot stopped due to some problem, e.g. lack of energy");
		} 
		int left = theRobot.distanceToObstacle(Robot.Direction.LEFT);
		int forward = theRobot.distanceToObstacle(Robot.Direction.FORWARD);
		//LOGGER.severe("The left side distance is" + String.valueOf(left));
		//LOGGER.severe("The forward side distance is " + String.valueOf(forward));
		if (left > 0) {
			theRobot.rotate(Robot.Turn.LEFT);
			//LOGGER.severe("TURNING LEFT");
			theRobot.move(1);
			//LOGGER.severe("MOVING FORWARD");
			// checks if the Robot has stopped for some reason, if so, throw and exception and break out of the method.
			if (theRobot.hasStopped()) {
				throw new Exception("robot stopped due to some problem, e.g. lack of energy");
			} 
			// increment path length if successful move forward
			pathLength++;
		}
		else if (forward > 0) {
			//LOGGER.severe("MOVING FORWARD");
			theRobot.move(1);
			// checks if the Robot has stopped for some reason, if so, throw and exception and break out of the method.
			if (theRobot.hasStopped()) {
				throw new Exception("robot stopped due to some problem, e.g. lack of energy");
			} 
			// increment path length if successful move forward
			pathLength++;
		}
		else {
			theRobot.rotate(Robot.Turn.RIGHT);
			//LOGGER.severe("TURNING RIGHT");
			// checks if the Robot has stopped for some reason, if so, throw and exception and break out of the method.
			if (theRobot.hasStopped()) {
				throw new Exception("robot stopped due to some problem, e.g. lack of energy");
			} 
		}
		// if the cell moved to is the exit position, rotate the robot so that it is
		// staring into eternity.
		if (theRobot.isAtExit()) {			
			if (theRobot.canSeeThroughTheExitIntoEternity(Robot.Direction.RIGHT)) {
				theRobot.rotate(Robot.Turn.RIGHT);
			}
			if (theRobot.canSeeThroughTheExitIntoEternity(Robot.Direction.LEFT)) {
				theRobot.rotate(Robot.Turn.LEFT);
			} 
			// calling hasStopped in case a rotation operation is performed due to the 2 if
			// statements above if true, throw an exception and break out of the method.
			if (theRobot.hasStopped()) {
				throw new Exception("robot stopped due to some problem, e.g. lack of energy");
			} 
			return false;
		}
		// else if the move and or rotate operations are successful return true
		return true;
	}
	
	/**
	 * Starts unreliable sensors background threads
	 */
	public void startUnreliableSensors() {
		
		// store boolean value of whether sensor is operational for each cell
		// we know that a sensor is unreliable if that boolean is originally set to 0
		boolean forward = theRobot.getSensor(Robot.Direction.FORWARD).isOperational();
		boolean backward = theRobot.getSensor(Robot.Direction.BACKWARD).isOperational();
		boolean left = theRobot.getSensor(Robot.Direction.LEFT).isOperational();
		boolean right = theRobot.getSensor(Robot.Direction.RIGHT).isOperational();
		
		
		// if forward sensor is unreliable, start thread
		// if any of the other sensors whose thread haven't been started yet are unreliable, wait 1.3 seconds
		if (forward == false) {
			theRobot.startFailureAndRepairProcess(Robot.Direction.FORWARD, meanTimeBetweenFailures, meanTimeToRepair);
			if ((backward == false) || (left == false) || (right == false)) {	
				try {
					Thread.sleep(1300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		// if backward sensor is unreliable, start thread
				// if any of the other sensors whose thread haven't been started yet are unreliable, wait 1.3 seconds
		if (backward == false) {
			theRobot.startFailureAndRepairProcess(Robot.Direction.BACKWARD, meanTimeBetweenFailures, meanTimeToRepair);
			if ((left == false) || (right == false)) {	
				try {
					Thread.sleep(1300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		// if left sensor is unreliable, start thread
				// if any of the other sensors whose thread haven't been started yet are unreliable, wait 1.3 seconds
		if (left == false) {
			theRobot.startFailureAndRepairProcess(Robot.Direction.LEFT, meanTimeBetweenFailures, meanTimeToRepair);
			if (right == false) {
				try {
					Thread.sleep(1300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		if (right == false) {
			theRobot.startFailureAndRepairProcess(Robot.Direction.RIGHT, meanTimeBetweenFailures, meanTimeToRepair);
		}
	}

	@Override
	public float getEnergyConsumption() {
		// TODO Auto-generated method stub
		return 3500 - theRobot.getBatteryLevel();
	}

	@Override
	public int getPathLength() {
		// TODO Auto-generated method stub
		return pathLength;
	}

}
