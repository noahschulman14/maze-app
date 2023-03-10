package edu.wm.cs301.amazebynoahschulman.gui;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import edu.wm.cs301.amazebynoahschulman.R;
import edu.wm.cs301.amazebynoahschulman.generation.DefaultOrder;
import edu.wm.cs301.amazebynoahschulman.generation.Factory;
import edu.wm.cs301.amazebynoahschulman.generation.Maze;
import edu.wm.cs301.amazebynoahschulman.generation.MazeFactory;
import edu.wm.cs301.amazebynoahschulman.generation.Order;

/**
 * GeneratingActivity activity class - runs a background thread that
 * builds maze, displays progress of that on screen.
 * Allows user to select a driver and robot configuration while background maze generation
 * thread is running.
 * @author Noah Schulman
 */
public class GeneratingActivity extends AppCompatActivity  {
    /**
     * TAG field variable for Log.v calls
     */
    private static final String TAG = "GeneratingActivity";
    /**
     * Field variable to store which driver is being used
     */
    private String driver;
    /**
     * Field variable to store which robot configuration is being used
     */
    private String robotConfig;
    /**
     * driverRadioGroup RadioGroup field variable
     */
    private RadioGroup driverRadioGroup;
    /**
     * driverRadioButton RadioButton field variable
     */
    private RadioButton driverRadioButton;
    /**
     * buildProgress ProgressBar field variable
     */
    protected ProgressBar buildProgress;
    /**
     * backgroundThread tread field variable/initialization
     */
    private backgroundThread thread = new backgroundThread();
    /**
     * method to start background maze generation thread
     */
    public void startThread() {
        thread.start();
    }
    /**
     * method to stop background maze generation thread
     */
    public void stopThread() {
        thread.interrupt();
    }
    /**
     * Boolean field variable, tells if background maze generation is complete
     */
    private boolean ready = false;
    /**
     * Boolean field variable, tells if maze will be operating in manual mode
     */
    private boolean manual = false;
    /**
     * Boolean field variable, tells if maze will be operated with an automated robot
     */
    private boolean auto = false;
    /**
     * Boolean field variable, tells if a robot has been selected
     */
    private boolean robot = false;
    /**
     * reminder TextView field variable
     */
    private TextView reminder;
    /**
     * robotConfigText TextView field variable
     */
    private TextView robotConfigText;
    /**
     * robotConfigRadioGroup RadioGroup field variable
     */
    private RadioGroup robotConfigRadioGroup;
    /**
     * robotConfigRadioButton RadioButton field variable
     */
    private RadioButton robotConfigRadioButton;
    /**
     * reminder2 TextView field variable
     */
    private TextView reminder2;
    /**
     * shortly TextView field variable
     */
    private TextView shortly;
    /**
     * field variable for the maze order
     */
    private DefaultOrder order;
    /**
     * field variable for the maze factory
     */
    Factory mazeFactory;
    /**
     * Field variable for buildingSounds MediaPlayer object
     */
    private MediaPlayer buildingSounds;
    /**
     * Field variable for screamingSounds MediaPlayer object
     */
    private MediaPlayer screamingSounds;

    /**
     * background maze generation thread class
     */
    class backgroundThread extends Thread {
        @Override
        public void run() {
            Log.v(TAG, "BACKGROUND THREAD RUNNING");
            // UPDATE buildProgress VIA Order.getProgress()
            while (order.getProgress() != 100) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                buildProgress.setProgress(order.getProgress());
            }
            mazeFactory.waitTillDelivered();
            // set maze to created maze
            MazeInfo.maze = order.getMaze();

            // ready boolean set to true once background maze generation is finished
            ready = true;
            // if manual radio button is selected and background thread is finished, move to PlayManuallyActivity
            if (manual) {
                Intent intent = new Intent(getApplicationContext(), PlayManuallyActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
            // if an automated driver is selected and robot sensor configuration is selected and
            // background maze generation is finished, move to PlayManuallyActivity
            else if (auto && robot) {
                Intent intent = new Intent(getApplicationContext(), PlayAnimationActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
            // if automated driver is selected and background maze generation is finished
            // but robot sensor configuration is not selected, display text to remind user to select one
            else if (auto) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        reminder2.setVisibility(View.VISIBLE);
                    }
                });
            }
            // if background maze generation is finished but no driver option is selected
            // display text to remind user to select one
            else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            reminder.setVisibility(View.VISIBLE);
                        }
                    });
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generating);
        // starting building sound effects
        buildingSounds = MediaPlayer.create(GeneratingActivity.this, R.raw.building_sounds);
        buildingSounds.setVolume(1.0f, 1.0f);
        buildingSounds.setLooping(true);
        buildingSounds.start();
        // starting screaming sound effects
        screamingSounds = MediaPlayer.create(GeneratingActivity.this, R.raw.screaming_sounds);
        screamingSounds.setVolume(1.0f, 1.0f);
        screamingSounds.setLooping(true);
        screamingSounds.start();

        // initializing UI objects
        buildProgress = findViewById(R.id.progressBar1);
        reminder = findViewById(R.id.textView5);
        robotConfigText = findViewById(R.id.textView6);
        robotConfigRadioGroup = findViewById(R.id.radioGroup2);
        reminder2 = findViewById(R.id.textView7);
        shortly = findViewById(R.id.textView8);
        driverRadioGroup = findViewById(R.id.radioGroup);

        // getting maze config info from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        // getting builderAlgo
        if (sharedPreferences.getString("builderAlgo", "Boruvka").equals("Boruvka")) {
                    MazeInfo.builderAlgo = Order.Builder.Boruvka;
                }
        if (sharedPreferences.getString("builderAlgo", "Boruvka").equals("Prim")) {
                    MazeInfo.builderAlgo = Order.Builder.Prim;
                }
        if (sharedPreferences.getString("builderAlgo", "Boruvka").equals("DFS")) {
                    MazeInfo.builderAlgo = Order.Builder.DFS;
                }
        // getting size
        MazeInfo.size = sharedPreferences.getInt("size", 0);
        // getting rooms
        MazeInfo.rooms = sharedPreferences.getBoolean("rooms", false);
        // getting random seed
        MazeInfo.randomSeed = sharedPreferences.getInt("randomSeed", 20);

        Log.v(TAG, "ROOMS IS SELECTED TO " + MazeInfo.rooms);
        Log.v(TAG, "SIZE IS SELECTED TO " + MazeInfo.size);
        Log.v(TAG, "RANDOM SEED IS SELECTED TO " + MazeInfo.randomSeed);
        Log.v(TAG, "BUILDER ALGO IS SELECTED TO " + MazeInfo.builderAlgo);

        // here is where I create the maze
        order = new DefaultOrder(MazeInfo.size, MazeInfo.builderAlgo, MazeInfo.rooms, MazeInfo.randomSeed);
        mazeFactory = new MazeFactory();
        mazeFactory.order(order);
        // THEN START BACKGROUND THREAD THAT UPDATES ACTIVITY'S PROGRESS BAR BASED ON Order.UpdateProgress()
        startThread();
    }

    // for driver radio button group
    public void checkButton(View v) {
        int selectedId = driverRadioGroup.getCheckedRadioButtonId();
        driverRadioButton = findViewById(selectedId);
        driver = driverRadioButton.getText().toString();
        Log.v(TAG, driver + " radio button pressed");
        // set maze driver based on radio button selection
        if (driverRadioButton == findViewById(R.id.radio_wallfollower) || driverRadioButton == findViewById(R.id.radio_wizard)) {
            if (driverRadioButton == findViewById(R.id.radio_wallfollower)) {
                MazeInfo.robot = new UnreliableRobot();
                MazeInfo.driver = new WallFollower();
            }
            else {
                MazeInfo.robot = new UnreliableRobot();
                MazeInfo.driver = new Wizard();
            }
            // if automated driver is selected, display radio button group to select a robot sensor configuration
            robotConfigRadioGroup.setVisibility(View.VISIBLE);
            robotConfigText.setVisibility(View.VISIBLE);
            // if a robot is not selected/unselected, make text that says maze is starting shortly invisible
            if (!robot) {
                shortly.setVisibility(View.INVISIBLE);
            }
            // if automated maze generation is selected, change manual boolean to false and auto to true
            manual = false;
            auto = true;
            // if a robot is selected, display text to say that game will start shortly
            if (robot) {
                shortly.setVisibility(View.VISIBLE);
            }
            // if maze generation is finished and a robot is selected, move to PlayAnimationActivity
            if (ready && robot) {
                Intent intent = new Intent(getApplicationContext(), PlayAnimationActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
            // if maze generation is finished and a robot configuration is not selected,
            // remind user to select one
            else if (ready) {
                reminder2.setVisibility(View.VISIBLE);
            }

        }
        if (driverRadioButton == findViewById(R.id.radio_manual)) {
            // if manual is selected, display text to say game will start shortly
            shortly.setVisibility(View.VISIBLE);
            // if manual is selected, changing from a automated driver selection,
            // make the robot radio group invisible
            robotConfigRadioGroup.setVisibility(View.INVISIBLE);
            robotConfigText.setVisibility(View.INVISIBLE);
            manual = true;
            auto = false;
            // if background maze generation finished after manual is selected,
            // move to PlayManuallyActivity
            if (ready) {
                Intent intent = new Intent(getApplicationContext(), PlayManuallyActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        }
    }

    public void checkButton2(View v) {
        int selectedId = robotConfigRadioGroup.getCheckedRadioButtonId();
        robotConfigRadioButton = findViewById(selectedId);
        robotConfig = robotConfigRadioButton.getText().toString();
        Log.v(TAG,  robotConfig + " radio button pressed");
        // set robot's sensor configuration based on radio button selection
        if (robotConfigRadioButton == findViewById(R.id.radio_premium) || robotConfigRadioButton == findViewById(R.id.radio_mediocre)
                || robotConfigRadioButton == findViewById(R.id.radio_soso) || robotConfigRadioButton == findViewById(R.id.radio_shaky)) {
            if (robotConfigRadioButton == findViewById(R.id.radio_premium)) {
                // premium has 4 reliable sensors
                MazeInfo.sensorForward = new ReliableSensor();
                MazeInfo.sensorBackward = new ReliableSensor();
                MazeInfo.sensorLeft = new ReliableSensor();
                MazeInfo.sensorRight = new ReliableSensor();
            }
            if (robotConfigRadioButton == findViewById(R.id.radio_mediocre)) {
                // mediocre has reliable front & back sensors, unreliable left and right sensors
                MazeInfo.sensorForward = new ReliableSensor();
                MazeInfo.sensorBackward = new ReliableSensor();
                MazeInfo.sensorLeft = new UnreliableSensor();
                MazeInfo.sensorRight = new UnreliableSensor();
            }
            if (robotConfigRadioButton == findViewById(R.id.radio_soso)) {
                // soso has reliable left & right sensors, unreliable front and back sensors
                MazeInfo.sensorForward = new UnreliableSensor();
                MazeInfo.sensorBackward = new UnreliableSensor();
                MazeInfo.sensorLeft = new ReliableSensor();
                MazeInfo.sensorRight = new ReliableSensor();
            }
            if (robotConfigRadioButton == findViewById(R.id.radio_shaky)) {
                // shaky has 4 unreliable sensors
                MazeInfo.sensorForward = new UnreliableSensor();
                MazeInfo.sensorBackward = new UnreliableSensor();
                MazeInfo.sensorLeft = new UnreliableSensor();
                MazeInfo.sensorRight = new UnreliableSensor();
            }
            robot = true;
            // if robot config is set, display text that says game will start shortly
            shortly.setVisibility(View.VISIBLE);
            // if background thread is finished and an automated driver is selected and robot config,
            // move to PlayAnimationActivity
            if (ready && auto) {
                Intent intent = new Intent(getApplicationContext(), PlayAnimationActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Log.v(TAG, "BACK button pressed");
        Intent intent = new Intent(getApplicationContext(), AMazeActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    protected void onPause() {
        super.onPause();
        buildingSounds.release();
        screamingSounds.release();
    }
}