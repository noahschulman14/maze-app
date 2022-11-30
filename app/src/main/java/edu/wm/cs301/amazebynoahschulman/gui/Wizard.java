package edu.wm.cs301.amazebynoahschulman.gui;

import java.util.Arrays;
import java.util.logging.Logger;

import edu.wm.cs301.amazebynoahschulman.generation.CardinalDirection;
import edu.wm.cs301.amazebynoahschulman.generation.Maze;

/**
 * Class: Wizard
 * 
 * Responsibilities:
 * Assigns a robot platform to the driver.
 * Wizard, the driver, will drive the robot in such a way that it follows the Wizard algorithm.
 * If the Wizard is able to make the Robot's forward direction point towards the exit, return True.
 * If the Wizard makes the Robot crash or run out of battery energy, throw an exception.
 * If the Wizard can not find the exit due to a cycle or some other reason, return False.
 * Identify the total battery energy consumption of the Robot's journey.
 * Identify total number of cells traveled over in Robot's journey. 
 * 
 * 
 * Collaborators:
 * Implements RobotDriver.java class.
 * ReliableRobot.java class.
 * Control.java class (NOTE: to access Maze object)
 * 
 * @author Noah Schulman
 *
 */
public class Wizard implements RobotDriver {
	
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
	 * Constructor - empty
	 */
	public Wizard() {
	}

	/**
	 * Assigns a robot platform to the driver. 
	 * The driver uses a robot to perform, this method provides it with this necessary information.
	 * @param r robot to operate
	 */
	@Override
	public void setRobot(Robot r) {
		// TODO Auto-generated method stub
		theRobot = r;
		pathLength = 0;
	}
	
	/**
	 * This method is only used in Junit test classes.
	 * @return Wizard's corresponding Robot object
	 */
	public Robot getRobotForTest() {
		return theRobot;
	}
	
	/**
	 * Provides the robot driver with the maze information.
	 * Only some drivers such as the wizard rely on this information to find the exit.
	 * @param maze represents the maze, must be non-null and a fully functional maze object.
	 */
	@Override
	public void setMaze(Maze maze) {
		theMaze = maze;

	}
	/**
	 * This method is only used in Junit test classes.
	 * @return Wizard's corresponding Maze object
	 */
	public Maze getMazeForTest() {
		return theMaze;
	}

	/**
	 * Drives the robot towards the exit following
	 * its solution strategy and given the exit exists and  
	 * given the robot's energy supply lasts long enough. 
	 * When the robot reached the exit position and its forward
	 * direction points to the exit the search terminates and 
	 * the method returns true.
	 * If the robot failed due to lack of energy or crashed, the method
	 * throws an exception.
	 * If the method determines that it is not capable of finding the
	 * exit it returns false, for instance, if it determines it runs
	 * in a cycle and can't resolve this.
	 * @return true if driver successfully reaches the exit, false otherwise
	 * @throws Exception thrown if robot stopped due to some problem, e.g. lack of energy
	 */
	@Override
	public boolean drive2Exit() throws Exception {
		// while condition is a boolean value "working"
		// this condition starts out as true, it is updated in the while loop
		// as it is set to what the drive1Step2Exit() method returns.
		// If drive1Step2Exit returns true, that means that the Wizard's robot's current
		// position is not at the exit position, so there are still steps to be taken.
		// If drive1Step2Exit returns false that means that the Wizard's robot's current
		// position is at the exit and is facing into eternity.
		// So therefore working will be set to false when the Wizard finishes and the while loop terminates.
		
		// make the map and solution show up
		boolean working = true;
		while (working) {
			try {
				working = drive1Step2Exit();
			}
			catch (Exception e){
				throw new Exception("robot stopped due to some problem, e.g. lack of energy");
			}
		}
		return true;
	}
	

	/**
	 * Drives the robot one step towards the exit following
	 * its solution strategy and given the exists and 
	 * given the robot's energy supply lasts long enough.
	 * It returns true if the driver successfully moved
	 * the robot from its current location to an adjacent
	 * location.
	 * At the exit position, it rotates the robot 
	 * such that if faces the exit in its forward direction
	 * and returns false. 
	 * If the robot failed due to lack of energy or crashed, the method
	 * throws an exception. 
	 * @return true if it moved the robot to an adjacent cell, false otherwise
	 * @throws Exception thrown if robot stopped due to some problem, e.g. lack of energy
	 */
	@Override
	public boolean drive1Step2Exit() throws Exception {
		// checks if the Robot has stopped for some reason, if so, throw and exception and break out of the method.
		if (theRobot.hasStopped()) {
			throw new Exception("robot stopped due to some problem, e.g. lack of energy");
		} 
		// index shortcuts for current cell
		int robotCurX = theRobot.getCurrentPosition()[0];
		int robotCurY = theRobot.getCurrentPosition()[1];
		// index shortcuts for neighbor cell to move to
		int neighborX = theMaze.getNeighborCloserToExit(robotCurX, robotCurY)[0];
		int neighborY = theMaze.getNeighborCloserToExit(robotCurX, robotCurY)[1];
		// Need to identify what cardinal direction the neighbor cell is at
		// This is determined by seeing which of the Robot's current indexes are 
		// incremented or decremented to become the neighboring cell.
		// That is shown in the if statements below.
		CardinalDirection neighborCD = null;
		if (neighborX == robotCurX - 1)
			neighborCD = CardinalDirection.West;
		if (neighborX == robotCurX + 1)
			neighborCD = CardinalDirection.East;
		if (neighborY == robotCurY + 1)
			neighborCD = CardinalDirection.South;
		if (neighborY == robotCurY - 1)
			neighborCD = CardinalDirection.North;
		
		if (neighborCD == null) 
			throw new Exception("neighborCD is null");
			
		// check what cardinal direction theRobot is currently facing and then turn 
		// theRobot so that it's cardinal direction is facing the neighboring cell to move to.
		// this is done through a bunch of if statements
		// There is essentially a case for each of the Robot's possible CardinalDirections
		// If that CardinalDirection matches the CardinalDirection of the neighboring cell
		// then theRobot doesn't have to turn.
		// If that's not the case, theRobot will turn depending in it's current CardinalDirection
		// and the CardinalDirection of the neighboring cell to move to.
		if (theRobot.getCurrentDirection() != neighborCD) {
			if (theRobot.getCurrentDirection() == CardinalDirection.North) {
				if (neighborCD == CardinalDirection.South)
					theRobot.rotate(Robot.Turn.AROUND);
				if (neighborCD == CardinalDirection.East)
					theRobot.rotate(Robot.Turn.LEFT);
				if (neighborCD == CardinalDirection.West)
					theRobot.rotate(Robot.Turn.RIGHT);
			}
			if (theRobot.getCurrentDirection() == CardinalDirection.South) {
				if (neighborCD == CardinalDirection.North)
					theRobot.rotate(Robot.Turn.AROUND);
				if (neighborCD == CardinalDirection.East)
					theRobot.rotate(Robot.Turn.RIGHT);
				if (neighborCD == CardinalDirection.West)
					theRobot.rotate(Robot.Turn.LEFT);
			}
			if (theRobot.getCurrentDirection() == CardinalDirection.East) {
				if (neighborCD == CardinalDirection.North)
					theRobot.rotate(Robot.Turn.RIGHT);
				if (neighborCD == CardinalDirection.South)
					theRobot.rotate(Robot.Turn.LEFT);
				if (neighborCD == CardinalDirection.West)
					theRobot.rotate(Robot.Turn.AROUND);
			}
			if (theRobot.getCurrentDirection() == CardinalDirection.West) {
				if (neighborCD == CardinalDirection.North)
					theRobot.rotate(Robot.Turn.LEFT);
				if (neighborCD == CardinalDirection.South)
					theRobot.rotate(Robot.Turn.RIGHT);
				if (neighborCD == CardinalDirection.East)
					theRobot.rotate(Robot.Turn.AROUND);
			}
		}
		// calling hasStopped in case the rotate operation depleted the Robot's battery
		// if the battery is depleted, if true throw an exception and break out of the method.
		if (theRobot.hasStopped()) {
			throw new Exception("robot stopped due to some problem, e.g. lack of energy");
		} 

		
		// now that theRobot is facing the direction of the neighbor, move forward 1 to reach the neighbor
		theRobot.move(1);
		
		// calling hasStopped in case the move operation depleted the Robot's battery
		// or crashed the Robot, if true throw an exception and break out of the method.
		if (theRobot.hasStopped()) {
			throw new Exception("robot stopped due to some problem, e.g. lack of energy");
		} 
		// increment the path method if there is a successful move forward
		pathLength++;
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
		// else if the move operation is successful return true
		return true;
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
