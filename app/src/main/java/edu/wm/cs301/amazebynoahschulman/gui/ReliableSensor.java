package edu.wm.cs301.amazebynoahschulman.gui;

import java.util.logging.Logger;

import edu.wm.cs301.amazebynoahschulman.generation.CardinalDirection;
import edu.wm.cs301.amazebynoahschulman.generation.Maze;
/**
 * Class: ReliableSensor
 * 
 * Responsibilities:
 * Identify the distance between a sensor and an object (Maze wallboard).
 * Receive current Maze object to perform calculations on its properties.
 * Set the direction of the sensor relative to the direction it is mounted to on the Robot.
 * Return amount of battery energy the sensor is using to identify a distance to an obstacle.
 * 
 * Collaborators:
 * Implements DistanceSensor.java class.
 * ReliableRobot.java class (NOTE: to receive Robot's current coordinate position and cardinal direction and battery info)
 * Control.java class (NOTE: to get access to current Maze object)
 * 
 * @author Noah Schulman
 *
 */
public class ReliableSensor implements DistanceSensor {
	
	//private static final Logger LOGGER = Logger.getLogger(Control.class.getName());

	/**
	 * Field to store the sensor's corresponding Maze
	 */
	protected Maze theMaze;
	
	/**
	 * Field to store the sensor's corresponding Direction
	 */
	protected Robot.Direction sensorDirection;

	/**
	 * ReliableSensor constructor - empty
	 */
	public ReliableSensor() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Tells the distance to an obstacle (a wallboard) that the sensor
	 * measures. The sensor is assumed to be mounted in a particular
	 * direction relative to the forward direction of the robot.
	 * Distance is measured in the number of cells towards that obstacle, 
	 * e.g. 0 if the current cell has a wallboard in this direction, 
	 * 1 if it is one step in this direction before directly facing a wallboard,
	 * Integer.MaxValue if one looks through the exit into eternity.
	 * 
	 * This method requires that the sensor has been given a reference
	 * to the current maze and a mountedDirection by calling 
	 * the corresponding set methods with a parameterized constructor.
	 * 
	 * @param currentPosition is the current location as (x,y) coordinates
	 * @param currentDirection specifies the direction of the robot
	 * @param powersupply is an array of length 1, whose content is modified 
	 * to account for the power consumption for sensing
	 * @return number of steps towards obstacle if obstacle is visible 
	 * in a straight line of sight, Integer.MAX_VALUE otherwise.
	 * @throws Exception with message 
	 * SensorFailure if the sensor is currently not operational
	 * PowerFailure if the power supply is insufficient for the operation
	 * @throws IllegalArgumentException if any parameter is null
	 * or if currentPosition is outside of legal range
	 * ({@code currentPosition[0] < 0 || currentPosition[0] >= width})
	 * ({@code currentPosition[1] < 0 || currentPosition[1] >= height}) 
	 * @throws IndexOutOfBoundsException if the powersupply is out of range
	 * ({@code powersupply < 0}) 
	 */
	@Override
	public int distanceToObstacle(int[] currentPosition, CardinalDirection currentDirection, float[] powersupply)
			throws Exception {
		// exception throwing:
		// throwing exception for if sensor is not operational:
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
	 * Provides the maze information that is necessary to make
	 * a DistanceSensor able to calculate distances.
	 * @param maze the maze for this game
	 * @throws IllegalArgumentException if parameter is null
	 * or if it does not contain a floor plan
	 */
	@Override
	public void setMaze(Maze maze) {
		// setting class' Maze field to the passed in maze.
		theMaze = maze;
		// exceptions are thrown if passed in Maze is null or if its floorplan is null
		if (maze == null) 
			throw new IllegalArgumentException("maze is null");
		if (maze.getFloorplan() == null) 
			throw new IllegalArgumentException("maze has no floorplan");
	}
	
	/**
	 * This method is only used for Junit testing
	 * @return class' corresponding maze.
	 */
	public Maze getMazeForTest() {
		return theMaze;
	}

	
	/**
	 * Provides the angle, the relative direction at which this 
	 * sensor is mounted on the robot.
	 * If the direction is left, then the sensor is pointing
	 * towards the left hand side of the robot at a 90 degree
	 * angle from the forward direction. 
	 * @param mountedDirection is the sensor's relative direction
	 * @throws IllegalArgumentException if parameter is null
	 */
	@Override
	public void setSensorDirection(Robot.Direction mountedDirection) {
		if (mountedDirection == null)
			throw new IllegalArgumentException("mountedDirection is null");
		// setting class' sensorDirection field to passed in mountedDirection
		sensorDirection = mountedDirection;
	}
	
	/**
	 * Method only used in Junit testing
	 * @return sensor's direction
	 */
	public Robot.Direction getSensorDirectionForTest() {
		return sensorDirection;
	}

	/**
	 * Returns the amount of energy this sensor uses for 
	 * calculating the distance to an obstacle exactly once.
	 * This amount is a fixed constant for a sensor.
	 * @return the amount of energy used for using the sensor once
	 */
	@Override
	public float getEnergyConsumptionForSensing() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public void startFailureAndRepairProcess(int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopFailureAndRepairProcess() throws UnsupportedOperationException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isOperational() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean isRunning() {
		return false;
	}

}
