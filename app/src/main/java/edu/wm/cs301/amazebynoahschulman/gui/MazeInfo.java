package edu.wm.cs301.amazebynoahschulman.gui;

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
    protected static String driver;
    /**
     * Field variable to store the robot sensor configuration
     */
    protected static String robotConfig;
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
}
