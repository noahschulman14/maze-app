package edu.wm.cs301.amazebynoahschulman.gui;

import edu.wm.cs301.amazebynoahschulman.generation.Distance;
import edu.wm.cs301.amazebynoahschulman.generation.Maze;
import edu.wm.cs301.amazebynoahschulman.generation.Order;

/**
 * MazeInfo class - stores information about the maze/maze game to be played/generated
 */
public class MazeInfo {
    /**
     * Field variable to store maze size
     */
    protected static int size;
    /**
     * Field variable to store whether maze has rooms
     */
    protected static boolean rooms;
    /**
     * Field variable to store maze builder algorithm
     */
    protected static Order.Builder builderAlgo;
    /**
     * Field variable to store the driver type
     */
    protected static RobotDriver driver;
    /**
     * boolean Field variable for driver type
     */
    protected static boolean wizard;
    /**
     * Field variable to store unreliable robot
     */
    protected static UnreliableRobot robot = new UnreliableRobot();
    /**
     * Field variable to store forward sensor
     */
    protected static DistanceSensor sensorForward;
    /**
     * Field variable to store backward sensor
     */
    protected static DistanceSensor sensorBackward;
    /**
     * Field variable to store left sensor
     */
    protected static DistanceSensor sensorLeft;
    /**
     * Field variable to store right sensor
     */
    protected static DistanceSensor sensorRight;
    /**
     * Field variable to store the maze's random seed
     */
    protected static int randomSeed;
    /**
     * boolean field variable to tell if a maze has been generated yet
     */
    protected static boolean started = false;
    /**
     * Field variable to store the maze object
     */
    protected static Maze maze;
    /**
     * Field variable to store the unreliable robot
     */
    //
}
