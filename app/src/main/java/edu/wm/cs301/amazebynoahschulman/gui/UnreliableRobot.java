package edu.wm.cs301.amazebynoahschulman.gui;

import java.util.logging.Logger;


/**
 * Class: UnreliableRobot
 * 
 * Responsibilities:
 * Be able to start the failure and repair process for any of its directional unreliable sensors.
 * Be able to stop the failure and repair process for any of its directional unreliable sensors.
 * Be able to identify which of it's sensors are operational/not operational
 * 
 * Also need a method to re-orient the robot when a sensor or sensors fail so that there is an operational left and forward sensor
 * 
 * 
 * Collaborators:
 * ReliableRobot is super class.
 * Implements Robot class interface.
 * Will use ReliableSensor and/or UnreliableSensor for sensors.
 * Control class for actuator methods and initializing which sensors are reliable/unreliable
 * 
 * @author Noah Schulman
 *
 */
public class UnreliableRobot extends ReliableRobot {
	private static final Logger LOGGER = Logger.getLogger(ReliableRobot.class.getName());
	/**
	 * Field that stores the mean time to repair.
	 */
	int tRepair = 2;
	
	/**
	 * Field that stores the mean time between failures.
	 */
	int tBetweenFailures = 4;
	
	/**
	 * Empty constructor
	 */
	public UnreliableRobot() {
		// TODO Auto-generated constructor stub
		super();
	}

	
	@Override
	public void startFailureAndRepairProcess(Direction direction, int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		switch (direction) {
		case FORWARD:
			sensorForward.startFailureAndRepairProcess(meanTimeBetweenFailures, meanTimeToRepair);
			break;
		case BACKWARD:
			sensorBackward.startFailureAndRepairProcess(meanTimeBetweenFailures, meanTimeToRepair);
			break;
		case RIGHT:
			sensorRight.startFailureAndRepairProcess(meanTimeBetweenFailures, meanTimeToRepair);
			break;
		case LEFT:
			sensorLeft.startFailureAndRepairProcess(meanTimeBetweenFailures, meanTimeToRepair);
			break;
		}
	}

	@Override
	public void stopFailureAndRepairProcess(Direction direction) throws UnsupportedOperationException {
		switch (direction) {
		case FORWARD:
			sensorForward.stopFailureAndRepairProcess();
			break;
		case BACKWARD:
			sensorBackward.stopFailureAndRepairProcess();
			break;
		case RIGHT:
			sensorRight.stopFailureAndRepairProcess();
			break;
		case LEFT:
			sensorLeft.stopFailureAndRepairProcess();
			break;
		}

	}
	
	@Override
	public void setStatePlaying(StatePlaying statePlaying1) {
		statePlaying = statePlaying1;
		if (statePlaying1 == null) {
			throw new IllegalArgumentException("StatePlaying must not be null.");
		}
		
		
		
	}

	@Override
	public int distanceToObstacle(Direction direction) throws UnsupportedOperationException {
		// first need to check if the sensor in the given direction is operational
		
		if (isSensorOperational(direction)) {
			return super.distanceToObstacle(direction);
		}
		// if the sensor in this direction is not operational, then we need to find a sensor that is operational
		Direction workingSensor = findOperationalSensor();
		// we then need to move this working sensor so that it is facing the direction of the broken one
		// so lets calculate the number of left turns needed for the working sensors direction to become the broken sensors current direction
		int turns = turnsToNewDirection(direction, workingSensor);
		// after turning the robot so that a working sensor is facing the desired direction to be senses,
		// get that distanceToObstacle and return the robot back to its original position
		int result;
		switch (turns) {
		case 1:
			// 1 left turn indicates a turn to the left, will then turn right to get back to original position
			rotate(Turn.LEFT);
			result = super.distanceToObstacle(workingSensor);
			rotate(Turn.RIGHT);
			return result;
		
		case 2:
			// two turns correlates with turning around, will then turn around again to get back to original position
			rotate(Turn.AROUND);
			result = super.distanceToObstacle(workingSensor);
			rotate(Turn.AROUND);
			return result;
		
		case 3:
			// three left turns indicates a turn to the right, will then turn left to get back to original position
			rotate(Turn.RIGHT);
			result = super.distanceToObstacle(workingSensor);
			rotate(Turn.LEFT);
			return result;
		default:
			result = super.distanceToObstacle(workingSensor);
			return result;
		}	
	}
	
	/**
	 * Private helper method to find how many turns are required to make a working sensor reach the direction
	 * of a broken sensor.
	 * @param brokenSensor direction
	 * @param workingSensor direction
	 * @return number of left turns required for the broken sensor direction to reach the working sensor direction
	 */
	private int turnsToNewDirection(Direction brokenSensor, Direction workingSensor){
		switch(workingSensor) {
		case FORWARD:
			switch(brokenSensor) {
			case LEFT:
				return 1;
			case BACKWARD:
				return 2;
			case RIGHT:
				return 3;
			default:
				return 0;
			}
		case BACKWARD:
			switch(brokenSensor) {
			case RIGHT:
				return 1;
			case FORWARD:
				return 2;
			case LEFT:
				return 3;
			default:
				return 0;
			}
		case LEFT:
			switch(brokenSensor) {
			case BACKWARD:
				return 1;
			case RIGHT:
				return 2;
			case FORWARD:
				return 3;
			default:
				return 0;
			}
		case RIGHT:
			switch(brokenSensor) {
			case FORWARD:
				return 1;
			case LEFT:
				return 2;
			case BACKWARD:
				return 3;
			default:
				return 0;
			}
			}
		return 0;
		
	}
	
	/**
	 * Method to determine if a sensor is operational or not.
	 * @param direction of sensor
	 * @return operational status of sensor
	 */
	protected boolean isSensorOperational(Direction direction) {
		switch (direction) {
		case FORWARD:
			return sensorForward.isOperational();
		case BACKWARD:
			return sensorBackward.isOperational();
		case LEFT:
			return sensorLeft.isOperational();
		case RIGHT:
			return sensorRight.isOperational();
		}
		return false;
		
	}
	
	/**
	 * Method that finds the direction of an operational sensor
	 * @return direction of operational sensor
	 * If none of the sensors are operational, wait until one becomes operational
	 */
	protected Direction findOperationalSensor() {
		// IF ALL THE SENSORS ARE NOT-OPERATIONAL, WAIT UNTIL ONE BECOMES OPERATIONAL
		while(allSensorsDown()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		if (sensorForward.isOperational()) {
			return Direction.FORWARD;
		}
		else if (sensorRight.isOperational()) {
			return Direction.RIGHT;
		}
		else if (sensorLeft.isOperational()) {
			return Direction.LEFT;
		}
		else if (sensorBackward.isOperational()) {
			return Direction.BACKWARD;
		}
		// IF ALL OF THE SENSORS ARE NOT-OPERATIONAL CURRENTLY, CALL THE METHOD UNTIL ONE IS OPERATIONAL
		else {
			return findOperationalSensor();
		}	
	}
	
	/**
	 * Method to determine whether or not all of the sensors are non operational 
	 * @return boolean for whether or not all the sensors are not operational
	 */
	protected boolean allSensorsDown() {
		if ((sensorLeft.isOperational() == false) &&
				(sensorRight.isOperational() == false) &&
				(sensorBackward.isOperational() == false) &&
				(sensorForward.isOperational() == false)) {
			return true;
		}
		return false;
	}
	

}
