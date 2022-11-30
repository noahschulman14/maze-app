package edu.wm.cs301.amazebynoahschulman.gui;

import java.util.logging.Logger;

import edu.wm.cs301.amazebynoahschulman.generation.CardinalDirection;
/**
 * Class: UnreliableSensor
 * 
 * Responsibilities:
 * Identify the distance between a sensor and an object (Maze wallboard).
 * Receive current Maze object to perform calculations on its properties.
 * Set the direction of the sensor relative to the direction it is mounted to on the Robot.
 * Return amount of battery energy the sensor is using to identify a distance to an obstacle.
 * Allow sensor to fail and repair itself - specifying how long such failures and repairs last.
 * Stop repair process and make sensor operational again.
 * 
 * 
 * Collaborators:
 * Implements DistanceSensor.java class.
 * ReliableRobot.java class (NOTE: to receive Robot's current coordinate position and cardinal direction and battery info)
 * Control.java class (NOTE: to get access to current Maze object)
 * Subclass on ReliableSensor.
 * 
 * 
 * @author Noah Schulman
 *
 */
public class UnreliableSensor extends ReliableSensor implements Runnable {
	
	//private static final Logger LOGGER = Logger.getLogger(ReliableRobot.class.getName());
	/**
	 * Boolean value that tells whether sensor is operational currently or not.
	 * This boolean is used in the distanceToObstacle method. That method will throw an exception and not run if operational = false
	 */
	protected boolean operational;
	/**
	 * Can't pass in time to repair as a parameter to the run() method, so it is stored as a private field.
	 */
	private int tRepair;
	/**
	 * Can't pass in time between failures as a parameter to the run() method, so it is stored as a private field.
	 */
	private int tBetweenFailures;
	
	/**
	 * Field that initializes the thread for the failure and repair process.
	 */
	protected Thread failureRepairProcess;
	
	/**
	 * Field that indicates if background thread is running or not
	 */
	private boolean running;
	
	
	public UnreliableSensor() {
		// TODO Auto-generated constructor stub
		super();
		// setting operational to true initially
		operational = false;
	}
	
	@Override
	public int distanceToObstacle(int[] currentPosition, CardinalDirection currentDirection, float[] powersupply)
			throws Exception {
		// exception throwing:
		// throwing exception for if sensor is not operational:
		if (operational == false) {
			//LOGGER.severe("********* TRYING TO FIND DISTANCE ON NON-OPERATIONAL SENSOR ************");
			throw new Exception("SensorFailure");
		}
		if (sensorDirection == null)
			throw new Exception("SensorFailure");
		// throwing exception if powersupply is insufficient
		if (powersupply[0] < 1)
			throw new Exception("PowerFailure");
		// throwing exception if passed in currentPosition is null
		if (currentPosition == null)
			throw new IllegalArgumentException("currentPosition is null");
		// throwing exception if passed in currentDirection is null
		if (currentDirection == null) 
			throw new IllegalArgumentException("currentDirection is null");
		// throwing exception if passed in currentPosition is not a valid position in the Maze
		if (!theMaze.isValidPosition(currentPosition[0], currentPosition[1]))
			throw new IllegalArgumentException("currentPosition is out of bounds");
		// saving the passed in currentPosition in shortcut variables
		int iBeingChecked = currentPosition[0];
		int jBeingChecked = currentPosition[1];
		// initialize a distance counter
		int distance = 0;
		// Switch statement case for each possible currentDirection that could be passed in
		// Inside each case there is a while loop
		// While condition is if the currentPosition shortcuts and current direction has a wall in
		// the Maze's floorplan
		// While this condition is met, the distance is incremented up and the currentPosition shortcut X or Y
		// index will be incremented up or down one depending on which CardinalDirection this method call is checking for.
		// Also in the loop, if the position being checked is not a valid position in the maze,
		// infinity is returned as that means that the sensor is staring out into eternity.
		// Inside each case, after the while loop is terminated, the distance to the obstacle is returned.
		switch(currentDirection) {
		case North:
			while (theMaze.getFloorplan().hasNoWall(iBeingChecked, jBeingChecked, currentDirection)) {
				distance++;
				jBeingChecked--;
				if (!theMaze.isValidPosition(iBeingChecked, jBeingChecked))
					return Integer.MAX_VALUE;
			}
			return distance;
		case South:
			while (theMaze.getFloorplan().hasNoWall(iBeingChecked, jBeingChecked, currentDirection)) {
				distance++;
				jBeingChecked++;
				if (!theMaze.isValidPosition(iBeingChecked, jBeingChecked)) 
					return Integer.MAX_VALUE;
			}
			return distance;
		case East:
			while (theMaze.getFloorplan().hasNoWall(iBeingChecked, jBeingChecked, currentDirection)) {
				distance++;
				iBeingChecked++;
				if (!theMaze.isValidPosition(iBeingChecked, jBeingChecked)) 
					return Integer.MAX_VALUE;
			}
			return distance;
		case West:
			while (theMaze.getFloorplan().hasNoWall(iBeingChecked, jBeingChecked, currentDirection)) {
				distance++;
				iBeingChecked--;
				if (!theMaze.isValidPosition(iBeingChecked, jBeingChecked)) 
					return Integer.MAX_VALUE;
			}
			return distance;
		default:
			throw new Exception("currentDirection is null");
		}
			
	}
	/**
	 * Method starts a concurrent, independent failure and repair
	 * process that makes the sensor fail and repair itself.
	 * This creates alternating time periods of up time and down time.
	 * Up time: The duration of a time period when the sensor is in 
	 * operational is characterized by a distribution
	 * whose mean value is given by parameter meanTimeBetweenFailures.
	 * Down time: The duration of a time period when the sensor is in repair
	 * and not operational is characterized by a distribution
	 * whose mean value is given by parameter meanTimeToRepair.
	 * 
	 * This an optional operation. If not implemented, the method
	 * throws an UnsupportedOperationException.
	 * 
	 * @param meanTimeBetweenFailures is the mean time in seconds, must be greater than zero
	 * @param meanTimeToRepair is the mean time in seconds, must be greater than zero
	 * @throws UnsupportedOperationException if method not supported
	 */
	@Override
	public void startFailureAndRepairProcess(int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		if (failureRepairProcess == null) {
			// creating new background thread
			failureRepairProcess = new Thread(this);
			running = true;
			operational = true;
			// these are multiplied by 1000 because it is passed into the Thread.sleep() method which takes in milliseconds as a parameter.
			tRepair = meanTimeToRepair;
			tBetweenFailures = meanTimeBetweenFailures;
			failureRepairProcess.start();
		}

	}
	/**
	 * This method stops a failure and repair process and
	 * leaves the sensor in an operational state.
	 * 
	 * It is complementary to starting a 
	 * failure and repair process. 
	 * 
	 * Intended use: If called after starting a process, this method
	 * will stop the process as soon as the sensor is operational.
	 * 
	 * If called with no running failure and repair process, 
	 * the method will return an UnsupportedOperationException.
	 * 
	 * This an optional operation. If not implemented, the method
	 * throws an UnsupportedOperationException.
	 * 
	 * @throws UnsupportedOperationException if method not supported
	 */
	@Override
	public void stopFailureAndRepairProcess() throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		operational = false;
		running = false;
		failureRepairProcess.interrupt();
		
	}

	@Override
	public void run() {
		// can't pass in time to repair or time between failures by parameter, so a private field
		// for this class is made to store those values.
		// this code will run as long as the thread is active
		// in other words, while the failure and repair process is active, this code will run
		// it should only stop when the thread is terminated by the stopFailureAndRepairProcess method
		while (running) {
			try {
				// makes sensor active (operational = true) for tBetweenFailures seconds.
				operational = true;
				Thread.sleep(tBetweenFailures);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				// Makes sensor inactive (operational = false) for tRepair seconds.
				operational = false;
				Thread.sleep(tRepair);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * Method to see if sensor is operational or not
	 * @return boolean operational status
	 */
	public boolean isOperational() {
		return operational;
	}
	
	@Override
	public boolean isRunning() {
		return running;
	}
	

}
