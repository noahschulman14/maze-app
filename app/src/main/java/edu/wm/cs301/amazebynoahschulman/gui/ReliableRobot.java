package edu.wm.cs301.amazebynoahschulman.gui;

import java.util.logging.Logger;

import edu.wm.cs301.amazebynoahschulman.generation.CardinalDirection;


/**
 * Class: ReliableRobot
 * 
 * Responsibilities:
 * Initialize current Control object, which gives access to current Maze being operated on.
 * Initialize 4 distance sensors for each relative direction.
 * Identify current coordinates of the Robot in the Maze.
 * Identify Robot's current CardinalDirection with respect to the Maze.
 * Identify the Robot's current battery level.
 * Initialize Robot's battery level.
 * Update Robot's battery level after Robot's corresponding actions. (NOTE: UNIQUE TO THIS IMPLEMENTING CLASS,
 *  need new, non overwritten methods  (???))
 * Initialize and update an Odometer to identify distance the Robot has traveled. Also be able to reset Odometer.
 * Rotate the Robot in a specified relative direction.
 * Move the Robot forward a specified distance.
 * Jump Robot over a wall.
 * Identifies if Robot is at exit position.
 * Identifies if Robot is in a room.
 * Identifies if Robot has stopped due to battery depletion, hitting an obstacle, etc.
 * Identifies Robot's current distance to an obstacle at a given relative direction.
 * Identify if Maze's exit is forward relative to the Robot.
 * 
 * 
 * Collaborators: 
 * Implements Robot class
 * Control.java class (NOTE: To access the maze, and actuator functionality)
 * ReliableSensor.java class (NOTE: will need 4 sensors)
 * 
 * @author Noah Schulman
 *
 */
public class ReliableRobot implements Robot {
	private static final Logger LOGGER = Logger.getLogger(ReliableRobot.class.getName());
	/**
	 * Field for the ReliableRobot's corresponding Control object
	 */
	//public Control roboController;
	/**
	 * Boolean field variable initialized to false.
	 * Will be changed to true when a method encounters a scenario in which the ReliableRobot should stop.
	 */
	protected boolean hasStopped = false;
	/**
	 * Field variable that initializes and stores the Robot's forward sensor
	 */
	protected DistanceSensor sensorForward;
	/**
	 * Field variable that initializes and stores the Robot's backward sensor
	 */
	protected DistanceSensor sensorBackward;
	/**
	 * Field variable that initializes and stores the Robot's right sensor
	 */
	protected DistanceSensor sensorRight;
	/**
	 * Field variable that initializes and stores the Robot's left sensor
	 */
	protected DistanceSensor sensorLeft;
	/**
	 * Field variable that keeps track of the ReliableRobot's corresponding battery level
	 */
	protected float[] batteryLevel = new float[1];
	/**
	 * Field variable that keeps track of odometer reading
	 */
	protected int odometer;

	protected StatePlaying statePlaying;
	
	/**
	 * ReliableRobot constructor.
	 * Sets the battery level to its normal initial value of 3500.
	 * Also sets the odometer to 0.
	 * Also creates a new ReliableSensor object for each of the class' field values for the 4 possible directional sensors.
	 */
	public ReliableRobot() {
		setBatteryLevel(3500);
		resetOdometer();
		sensorForward = new ReliableSensor();
		sensorBackward = new ReliableSensor();
		sensorRight = new ReliableSensor();
		sensorLeft = new ReliableSensor();
	}
	
	/**
	 * Provides the robot with a reference to the controller to cooperate with.
	 * The robot memorizes the controller such that this method is most likely called only once
	 * and for initialization purposes. The controller serves as the main source of information
	 * for the robot about the current position, the presence of walls, the reaching of an exit.
	 * The controller is assumed to be in the playing state.
	 * @throws IllegalArgumentException if controller is null,
	 * or if controller is not in playing state, 
	 * or if controller does not have a maze
	 */
	@Override
	public void setStatePlaying(StatePlaying statePlaying1) {
		statePlaying = statePlaying1;
		if (statePlaying1 == null) {
			throw new IllegalArgumentException("StatePlaying must not be null.");
		}
		// after controller is set, sensors can be initialized
		addDistanceSensor(sensorForward, Direction.FORWARD);
		addDistanceSensor(sensorBackward, Direction.BACKWARD);
		addDistanceSensor(sensorRight, Direction.RIGHT);
		addDistanceSensor(sensorLeft, Direction.LEFT);
	}
	

	/**
	 * Adds a distance sensor to the robot such that it measures in the given direction.
	 * This method is used when a robot is initially configured to get ready for operation.
	 * The point of view is that one mounts a sensor on the robot such that the robot
	 * can measure distances to obstacles or walls in that particular direction.
	 * For example, if one mounts a sensor in the forward direction, the robot can tell
	 * with the distance to a wall for its current forward direction, more technically,
	 * a method call distanceToObstacle(FORWARD) will return a corresponding distance.
	 * So a robot with a left and forward sensor will internally have 2 DistanceSensor
	 * objects at its disposal to calculate distances, one for the forward, one for the
	 * left direction.
	 * A robot can have at most four sensors in total, and at most one for any direction.
	 * If a robot already has a sensor for the given mounted direction, adding another
	 * sensor will replace/overwrite the current one for that direction with the new one.
	 * @param sensor is the distance sensor to be added
	 * @param mountedDirection is the direction that it points to relative to the robot's forward direction
	 */
	@Override
	public void addDistanceSensor(DistanceSensor sensor, Direction mountedDirection) {
		sensor.setSensorDirection(mountedDirection);
		sensor.setMaze(MazeInfo.maze);
		// depending on which Direction is passed in, the field variable for that directional
		// sensor will be set to the sensor that is passed in.
		switch(mountedDirection) {
		case FORWARD:
			sensorForward = sensor;
			break;
		case BACKWARD:
			sensorBackward = sensor;
			break;
		case RIGHT:
			sensorRight = sensor;
			break;
		case LEFT:
			sensorLeft = sensor;
			break;
		}
	}

	/**
	 * Provides the current position as (x,y) coordinates for 
	 * the maze as an array of length 2 with [x,y].
	 * @return array of length 2, x = array[0], y = array[1]
	 * and ({@code 0 <= x < width, 0 <= y < height}) of the maze
	 * @throws Exception if position is outside of the maze
	 */
	@Override
	public int[] getCurrentPosition() throws Exception {
		int curX = statePlaying.getCurrentPosition()[0];
		int curY = statePlaying.getCurrentPosition()[1];
		if (!MazeInfo.maze.isValidPosition(curX, curY))
			throw new Exception("Current position is out of bounds");
		// uses the Robot's controller's getCurrentPosition method to find the Robot's current position.
		return statePlaying.getCurrentPosition();
	}

	/**
	 * Provides the robot's current direction.
	 * @return cardinal direction is the robot's current direction in absolute terms
	 */	
	@Override
	public CardinalDirection getCurrentDirection() {
		return statePlaying.getCurrentDirection();
	}

	/**
	 * Returns the current battery level.
	 * The robot has a given battery level (energy level) 
	 * that it draws energy from during operations. 
	 * The particular energy consumption is device dependent such that a call 
	 * for sensor distance2Obstacle may use less energy than a move forward operation.
	 * If battery {@code level <= 0} then robot stops to function and hasStopped() is true.
	 * @return current battery level, {@code level > 0} if operational. 
	 */
	@Override
	public float getBatteryLevel() {
		return batteryLevel[0];
	}

	/**
	 * Sets the current battery level.
	 * The robot has a given battery level (energy level) 
	 * that it draws energy from during operations. 
	 * The particular energy consumption is device dependent such that a call 
	 * for distance2Obstacle may use less energy than a move forward operation.
	 * If battery {@code level <= 0} then robot stops to function and hasStopped() is true.
	 * @param level is the current battery level
	 * @throws IllegalArgumentException if level is negative 
	 */
	@Override
	public void setBatteryLevel(float level) {
		if (level < 0)
			throw new IllegalArgumentException("can not set battery level to a negative value");
		batteryLevel[0] = level;
	}

	/**
	 * Gives the energy consumption for a full 360 degree rotation.
	 * Scaling by other degrees approximates the corresponding consumption. 
	 * @return energy for a full rotation
	 */
	@Override
	public float getEnergyForFullRotation() {
		return 12;
	}

	/**
	 * Gives the energy consumption for moving forward for a distance of 1 step.
	 * For simplicity, we assume that this equals the energy necessary 
	 * to move 1 step and that for moving a distance of n steps 
	 * takes n times the energy for a single step.
	 * @return energy for a single step forward
	 */
	@Override
	public float getEnergyForStepForward() {
		return 6;
	}

	/** 
	 * Gets the distance traveled by the robot.
	 * The robot has an odometer that calculates the distance the robot has moved.
	 * Whenever the robot moves forward, the distance 
	 * that it moves is added to the odometer counter.
	 * The odometer reading gives the path length if its setting is 0 at the start of the game.
	 * The counter can be reset to 0 with resetOdomoter().
	 * @return the distance traveled measured in single-cell steps forward
	 */
	@Override
	public int getOdometerReading() {
		return odometer;
	}

	/** 
     * Resets the odometer counter to zero.
     * The robot has an odometer that calculates the distance the robot has moved.
     * Whenever the robot moves forward, the distance 
     * that it moves is added to the odometer counter.
     * The odometer reading gives the path length if its setting is 0 at the start of the game.
     */
	@Override
	public void resetOdometer() {
		odometer = 0;
	}

	/**
	 * Moves robot forward a given number of steps. A step matches a single cell.
	 * If the robot runs out of energy somewhere on its way, it stops, 
	 * which can be checked by hasStopped() == true and by checking the battery level. 
	 * If the robot hits an obstacle like a wall, it remains at the position in front 
	 * of the obstacle and also hasStopped() == true as this is not supposed to happen.
	 * This is also helpful to recognize if the robot implementation and the actual maze
	 * do not share a consistent view on where walls are and where not.
	 * @throws IllegalArgumentException if distance not positive
	 */
	@Override
	public void rotate(Turn turn) {
		// Case for each possible turn.
		// For each case, the battery level will be decremented by 3.
		// If the battery is depleted by this, then set the hasStopped boolean field value to true and
		// return out of the method.
		// Otherwise, call the Robot's Controller's corresponding rotate method for that particular turn case.
		switch (turn) {
		case LEFT:
			batteryLevel[0] = batteryLevel[0] - 3;
			if (batteryLevel[0] <= 0) {
				hasStopped = true;
				return;
			}
			statePlaying.handleUserInput(Constants.UserInput.LEFT, 1);
			break;
		case RIGHT:
			batteryLevel[0] = batteryLevel[0] - 3;
			if (batteryLevel[0] <= 0) {
				hasStopped = true;
				return;
			}
			statePlaying.handleUserInput(Constants.UserInput.RIGHT, 1);
			break;
		case AROUND:
			batteryLevel[0] = batteryLevel[0] - 6;
			if (batteryLevel[0] <= 0) {
				hasStopped = true;
				return;
			}
			// turns left twice which turns Robot around 180 degrees
			statePlaying.handleUserInput(Constants.UserInput.LEFT, 1);
			statePlaying.handleUserInput(Constants.UserInput.LEFT, 1);
			break;
		}
	}

	/**
	 * Makes robot move in a forward direction even if there is a wall
	 * in front of it. In this sense, the robot jumps over the wall
	 * if necessary. The distance is always 1 step and the direction
	 * is always forward.
	 * If the robot runs out of energy somewhere on its way, it stops, 
	 * which can be checked by hasStopped() == true and by checking the battery level.
	 * If the robot tries to jump over an exterior wall and
	 * would land outside of the maze that way,  
	 * it remains at its current location and direction,
	 * hasStopped() == true as this is not supposed to happen.
	 */
	@Override
	public void move(int distance) {
		// for loop from 0 to passed in distance.
		// inside loop, first decrements battery by 6 (movement energy cost)
		// if that depletes the battery's energy, cancel the movement by returning
		// and set hasStopped to true as the robot has run out of energy and can't move.
		// Also checks to see if the distance to the obstacle at any point during the movement is 0.
		// If that is the case, that means that this movement operation is running the robot into a wall
		// and the robot has crashed, so set hasStopped to true.
		// If those two hasStopped checks don't apply, then call the Controller's method to walk forward 1 cell
		// and increment the odometer counter field.
		for (int i = 0; i < distance; i++) {
			batteryLevel[0] = batteryLevel[0] - 6; 
			if (batteryLevel[0] <= 0) {
				hasStopped = true;
				return;
			}
			try {
				if (distanceToObstacle(Direction.FORWARD)== 0) {
					hasStopped =  true;
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			statePlaying.handleUserInput(Constants.UserInput.UP, 1);
			odometer++;
		}
	}

	/**
	 * Tells if the current position is right at the exit but still inside the maze. 
	 * The exit can be in any direction. It is not guaranteed that 
	 * the robot is facing the exit in a forward direction.
	 * @return true if robot is at the exit, false otherwise
	 */
	@Override
	public void jump() {
		// first decrements battery level by 40, if that depletes battery, set hasStopped to true and return out of method.
		// otherwise, call the Robot's controller's jump method and increment the odometer field value by 1.
		batteryLevel[0] = batteryLevel[0] - 40;
		if (batteryLevel[0] <= 0) {
			hasStopped = true;
			return;
			}
		statePlaying.handleUserInput(Constants.UserInput.JUMP, 1);
		odometer++;
		
	}
	// might need to change from robot to realiable robot in wizard
	public void showMap() {
		statePlaying.handleUserInput(Constants.UserInput.TOGGLELOCALMAP, 1);
	}
	
	public void showFullMap() {
		statePlaying.handleUserInput(Constants.UserInput.TOGGLEFULLMAP, 1);
	}
	
	public void showSolution() {
		statePlaying.handleUserInput(Constants.UserInput.TOGGLESOLUTION, 1);
	}

	/**
	 * Tells if the current position is right at the exit but still inside the maze. 
	 * The exit can be in any direction. It is not guaranteed that 
	 * the robot is facing the exit in a forward direction.
	 * @return true if robot is at the exit, false otherwise
	 */
	@Override
	public boolean isAtExit() {
		return MazeInfo.maze.getFloorplan().isExitPosition(statePlaying.getCurrentPosition()[0],
				statePlaying.getCurrentPosition()[1]);
	}

	/**
	 * Tells if current position is inside a room. 
	 * @return true if robot is inside a room, false otherwise
	 */	
	@Override
	public boolean isInsideRoom() {
		return MazeInfo.maze.isInRoom(statePlaying.getCurrentPosition()[0],
				statePlaying.getCurrentPosition()[1]);
	}

	/**
	 * Tells if the robot has stopped for reasons like lack of energy, 
	 * hitting an obstacle, etc.
	 * Once a robot is has stopped, it does not rotate or 
	 * move anymore.
	 * @return true if the robot has stopped, false otherwise
	 */
	@Override
	public boolean hasStopped() {
		// TODO Auto-generated method stub
		return hasStopped;
	}
	

	/**
	 * Tells the distance to an obstacle (a wall) 
	 * in the given direction.
	 * The direction is relative to the robot's current forward direction.
	 * Distance is measured in the number of cells towards that obstacle, 
	 * e.g. 0 if the current cell has a wallboard in this direction, 
	 * 1 if it is one step forward before directly facing a wallboard,
	 * Integer.MaxValue if one looks through the exit into eternity.
	 * The robot uses its internal DistanceSensor objects for this and
	 * delegates the computation to the DistanceSensor which need
	 * to be installed by calling the addDistanceSensor() when configuring
	 * the robot.
	 * @param direction specifies the direction of interest
	 * @return number of steps towards obstacle if obstacle is visible 
	 * in a straight line of sight, Integer.MAX_VALUE otherwise
	 * @throws UnsupportedOperationException if robot has no sensor in this direction
	 * or the sensor exists but is currently not operational
	 */
	@Override
	public int distanceToObstacle(Direction direction) throws UnsupportedOperationException {
		// by default, this CardinalDirection will correspond with the Robot's forward direction.
		CardinalDirection cdirBeingChecked = getCurrentDirection();
		// Cases for each possible Direction passed in as a parameter
		// In each case, the battery level is decremented by 1, if that depletes the battery, cancel method by returning.
		// Then distanceToObstacle is called on the Robot's corresponding Direction for the switch cases' Direction.
		// The returned value of that method is then returned as the distance to obstacle.
		switch(direction) {
		case FORWARD:
			batteryLevel[0]--;
			if (batteryLevel[0] <= 0) {
				hasStopped = true;
				break;
			}
			try {
				return sensorForward.distanceToObstacle(getCurrentPosition(), getCurrentDirection(), batteryLevel);
			}
			catch (Exception e){
				throw new UnsupportedOperationException("Forward sensor currently does not exist/is not operational or "
						+ "insufficient power");
			}
		case BACKWARD:
			batteryLevel[0]--;
			if (batteryLevel[0] <= 0) {
				hasStopped = true;
				break;
			}
			// CardinalDirection is flipped to correspond with the Robot's backward direction.
			cdirBeingChecked = cdirBeingChecked.oppositeDirection();
			try {
				return sensorBackward.distanceToObstacle(getCurrentPosition(), cdirBeingChecked, batteryLevel);
			}
			catch (Exception e){
				throw new UnsupportedOperationException("Backward sensor currently does not exist/is not operational or "
						+ "insufficient power");
			}
		case RIGHT:
			// CardinalDirection is rotated to correspond with the Robot's right direction.
			cdirBeingChecked = cdirBeingChecked.rotateClockwise();
			cdirBeingChecked = cdirBeingChecked.rotateClockwise();
			cdirBeingChecked = cdirBeingChecked.rotateClockwise();
			batteryLevel[0]--;
			if (batteryLevel[0] <= 0) {
				hasStopped = true;
				break;
			}
			try {
				return sensorRight.distanceToObstacle(getCurrentPosition(), cdirBeingChecked, batteryLevel);
			}
			catch (Exception e){
				throw new UnsupportedOperationException("Right sensor currently does not exist/is not operational or "
						+ "insufficient power");
			}
		case LEFT:
			// CardinalDirection is rotated to correspond with the Robot's left direction.
			cdirBeingChecked = cdirBeingChecked.rotateClockwise();
			batteryLevel[0]--;
			if (batteryLevel[0] <= 0) {
				hasStopped = true;
				break;
			}
			try {
				return sensorLeft.distanceToObstacle(getCurrentPosition(), cdirBeingChecked, batteryLevel);
			}
			catch (Exception e){
				LOGGER.severe(String.valueOf(batteryLevel[0]));
				throw new UnsupportedOperationException("Left sensor currenlty does not exist/is not operational or "
						+ "insufficient power");
			}
		}	
		return 0;
	}
	
	public DistanceSensor getSensor(Direction direction) {
		switch (direction) {
		case FORWARD:
			return sensorForward;
		case BACKWARD:
			return sensorBackward;
		case RIGHT:
			return sensorRight;
		default:
			return sensorLeft;
		}
	}

	/**
	 * Tells if a sensor can identify the exit in the given direction relative to 
	 * the robot's current forward direction from the current position.
	 * It is a convenience method is based on the distanceToObstacle() method and transforms
	 * its result into a boolean indicator.
	 * @param direction is the direction of the sensor
	 * @return true if the exit of the maze is visible in a straight line of sight
	 * @throws UnsupportedOperationException if robot has no sensor in this direction
	 * or the sensor exists but is currently not operational
	 */
	@Override
	public boolean canSeeThroughTheExitIntoEternity(Direction direction) throws UnsupportedOperationException {
		if (distanceToObstacle(direction) == Integer.MAX_VALUE) {
			return true;
		}
		return false;
	}

	@Override
	public void startFailureAndRepairProcess(Direction direction, int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopFailureAndRepairProcess(Direction direction) throws UnsupportedOperationException {
		// TODO Auto-generated method stub

	}

}
