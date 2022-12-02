package edu.wm.cs301.amazebynoahschulman.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import edu.wm.cs301.amazebynoahschulman.R;
import edu.wm.cs301.amazebynoahschulman.generation.Maze;

/**
 * PlayAnimationActivity class.
 * Screen that shows maze being played, status of the sensors,
 * ability to toggle map and change mpa size, ability to change animation speed,
 * remaining energy for the robot, and ability to play and pause the animation.
 */
public class PlayAnimationActivity extends AppCompatActivity {
    /**
     * TAG field variable for Log.v calls
     */
    private static final String TAG = "PlayAnimationActivity";
    /**
     * Field variable to store path length, initialized to 0
     */
    int pathLength = 0;
    /**
     * Field variable to store distanceToExit, initialized to 0
     */
    int distanceToExit = 0;
    /**
     * Field variable to store energy consumption, initialized to 0;
     */
    int energyConsumption = 0;
    /**
     * play and pause ToggleButton field variable
     */
    private ToggleButton playPauseToggleButton;

    //P6
    /**
     * go2WinningButton Buttonn field variable
     */
    private Button go2WinningButton;
    /**
     * go2LosingButton Button field variable
     */
    private Button go2LosingButton;
    /**
     * showMapSwitch Switch field variable
     */
    private Switch showMapSwitch;
    /**
     * mapSizeSeekBar SeekBar field variable
     */
    private SeekBar mapSizeSeekBar;
    /**
     * animationSpeedSeekBar SeekBar field variable
     */
    private SeekBar animationSpeedSeekBar;

    private Button zoomOutButton;

    private Button zoomInButton;

    private ProgressBar energyProgressBar;

    private TextView mapSizeTextView;

    private StatePlaying statePlaying;

    private MazePanel mazePanel;

    private boolean gameFinished = false;

    private Handler handler;

    private drive2ExitBackgroundThread drive2exit;

    private ImageView frontON;
    private ImageView frontOFF;
    private ImageView backON;
    private ImageView backOFF;
    private ImageView leftON;
    private ImageView leftOFF;
    private ImageView rightON;
    private ImageView rightOFF;

    private int[] speeds = {1050, 1000, 800, 600, 400, 200, 100, 50, 20};
    private int speed = 400;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_animation);

        // create new StatePlaying object
        statePlaying = new StatePlaying();
        // set the maze, activity, and maze panel for statePlaying
        mazePanel = findViewById(R.id.mazePanel);
        statePlaying.setMaze(MazeInfo.maze);
        statePlaying.setPlayAnimationActivity(this);

        // set up - copying what is in old StateGenerating
        MazeInfo.robot.setStatePlaying(statePlaying);
        MazeInfo.robot.addDistanceSensor(MazeInfo.sensorBackward, Robot.Direction.BACKWARD);
        MazeInfo.robot.addDistanceSensor(MazeInfo.sensorForward, Robot.Direction.FORWARD);
        MazeInfo.robot.addDistanceSensor(MazeInfo.sensorLeft, Robot.Direction.LEFT);
        MazeInfo.robot.addDistanceSensor(MazeInfo.sensorRight, Robot.Direction.RIGHT);
        MazeInfo.robot.setBatteryLevel(3500);
        MazeInfo.driver.setRobot(MazeInfo.robot);
        MazeInfo.driver.setMaze(MazeInfo.maze);


        drive2exit = new drive2ExitBackgroundThread();

        // create handler
        handler = new Handler(Looper.getMainLooper());

        statePlaying.start(mazePanel);

        // start failure and repair process
        MazeInfo.driver.startUnreliableSensors();
        // start animation
        //handler.post(drive2exit);
        statePlaying.handleUserInput(Constants.UserInput.TOGGLELOCALMAP, 1);
        statePlaying.handleUserInput(Constants.UserInput.TOGGLEFULLMAP, 1);
        statePlaying.handleUserInput(Constants.UserInput.TOGGLESOLUTION, 1);




        // set the shortest possible path length to be passed to winning screen
        int[] startingCoords = MazeInfo.maze.getStartingPosition();
        distanceToExit = MazeInfo.maze.getDistanceToExit(startingCoords[0], startingCoords[1]) - 1;

        // initialize start and stop buttons
        playPauseToggleButton = findViewById(R.id.playPauseToggleButton);

        // initialize energy progress bar
        energyProgressBar = findViewById(R.id.energyProgressBar);

        // initialize sensor images
        frontON = findViewById(R.id.frontON);
        frontOFF = findViewById(R.id.frontOFF);
        backON = findViewById(R.id.backON);
        backOFF = findViewById(R.id.backOFF);
        leftON = findViewById(R.id.leftON);
        leftOFF = findViewById(R.id.leftOFF);
        rightON = findViewById(R.id.rightON);
        rightOFF = findViewById(R.id.rightOFF);



        playPauseToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (playPauseToggleButton.isChecked()) {
                    // start background thread if set to start
                    handler.post(drive2exit);
                }
                else {
                    // stop background animation if set to pause
                    handler.removeCallbacks(drive2exit);
                }
            }
        });


        mapSizeTextView = findViewById(R.id.mapSizeTextView);
        // this is for SHOW MAP switch:
        showMapSwitch = (Switch) findViewById(R.id.showMapSwitch);
        showMapSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.v(TAG, "SHOW MAP switch toggled ON");
                    statePlaying.handleUserInput(Constants.UserInput.TOGGLELOCALMAP, 1);
                    statePlaying.handleUserInput(Constants.UserInput.TOGGLEFULLMAP, 1);
                    statePlaying.handleUserInput(Constants.UserInput.TOGGLESOLUTION, 1);
                    zoomInButton.setVisibility(View.VISIBLE);
                    zoomOutButton.setVisibility(View.VISIBLE);
                    mapSizeTextView.setVisibility(View.VISIBLE);
                } else {
                    Log.v(TAG, "SHOW MAP switch toggled OFF");
                    statePlaying.handleUserInput(Constants.UserInput.TOGGLELOCALMAP, 1);
                    statePlaying.handleUserInput(Constants.UserInput.TOGGLEFULLMAP, 1);
                    statePlaying.handleUserInput(Constants.UserInput.TOGGLESOLUTION, 1);
                    zoomInButton.setVisibility(View.INVISIBLE);
                    zoomOutButton.setVisibility(View.INVISIBLE);
                    mapSizeTextView.setVisibility(View.INVISIBLE);

                }
            }
        });



        // this is for ANIMATION SPEED seek bar
        animationSpeedSeekBar = findViewById(R.id.animationSpeedSeekBar);
        animationSpeedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                speed = speeds[i];
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.v(TAG, "ANIMATION SPEED SEEKBAR touched");
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // this is for zoomOut BUTTON, increases path length by 1
        zoomOutButton = findViewById(R.id.zoomOutButton);
        zoomOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "zoomOut button pressed");
                statePlaying.handleUserInput(Constants.UserInput.ZOOMOUT, 1);
            }
        });

        // this is for zoomIn BUTTON, increases path length by 1
        zoomInButton = findViewById(R.id.zoomInButton);
        zoomInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "zoomIn button pressed");
                statePlaying.handleUserInput(Constants.UserInput.ZOOMIN, 1);
            }
        });

    }

    // FOR WALLFOLLOWER, CALL METHOD TO START SENSOR BACKGROUND THREADS BEFORE THIS BACKGROUND THREAD

    private class drive2ExitBackgroundThread implements Runnable {
        @Override
        public void run() {

            // NEED TO UPDATE SENSOR STATUS ON SCREEN
            updateSensorImage();
            // take a step to the exit
            // if that results in driver being at exit, the game is finished
            try {
                if (!MazeInfo.driver.drive1Step2Exit()) {
                    // PUT IN CONDITION TO STOP SENSOR START/FAILURE THREAD
                    go2Winning();
                    return;
                }
                else {
                    // need to update energy on screen
                    energyProgressBar.setProgress((int) MazeInfo.robot.getBatteryLevel());
                }
            } catch (Exception e) {
                go2Losing();
                return;
            }

            // THIS IS HOW THE ANIMATION SPEED WORKS
            // wait an amount of time before calling background thread again
            handler.postDelayed(drive2exit, speed);
        }
    }



    private void go2Winning() {
        MazeInfo.robot.stopFailureAndRepairProcess(Robot.Direction.FORWARD);
        MazeInfo.robot.stopFailureAndRepairProcess(Robot.Direction.LEFT);
        MazeInfo.robot.stopFailureAndRepairProcess(Robot.Direction.RIGHT);
        MazeInfo.robot.stopFailureAndRepairProcess(Robot.Direction.BACKWARD);


        Intent intent = new Intent(getApplicationContext(), WinningActivity.class);
        // also passing the pathLength and distance2Exit to StateWinning
        intent.putExtra("pathLength", MazeInfo.driver.getPathLength());
        intent.putExtra("distance2Exit", distanceToExit);
        intent.putExtra("energyConsumption", (int)MazeInfo.driver.getEnergyConsumption());
        intent.putExtra("playAnimation", true);
        startActivity(intent);
        finish();
    }

    private void go2Losing() {

        MazeInfo.robot.stopFailureAndRepairProcess(Robot.Direction.FORWARD);
        MazeInfo.robot.stopFailureAndRepairProcess(Robot.Direction.LEFT);
        MazeInfo.robot.stopFailureAndRepairProcess(Robot.Direction.RIGHT);
        MazeInfo.robot.stopFailureAndRepairProcess(Robot.Direction.BACKWARD);

        Intent intent = new Intent(getApplicationContext(), LosingActivity.class);
        // also passing the pathLength and distance2Exit to StateWinning
        intent.putExtra("pathLength", MazeInfo.driver.getPathLength());
        intent.putExtra("distance2Exit", distanceToExit);
        intent.putExtra("energyConsumption", (int)MazeInfo.driver.getEnergyConsumption());
        intent.putExtra("playAnimation", true);
        startActivity(intent);
        finish();
    }






    // back button functionality, moves to StateTitle
    // ADD SOMETHING TO STOP BACKGROUND THREAD
    @Override
    public void onBackPressed() {
        Log.v(TAG, "BACK button pressed");
        Intent intent = new Intent(getApplicationContext(), AMazeActivity.class);
        startActivity(intent);
        finish();
    }

    private void updateSensorImage() {
        if (MazeInfo.sensorForward.isOperational()) {
            frontON.setVisibility(View.VISIBLE);
            frontOFF.setVisibility(View.INVISIBLE);
        }
        if (!MazeInfo.sensorForward.isOperational()) {
            frontON.setVisibility(View.INVISIBLE);
            frontOFF.setVisibility(View.VISIBLE);
        }
        if (MazeInfo.sensorBackward.isOperational()) {
            backON.setVisibility(View.VISIBLE);
            backOFF.setVisibility(View.INVISIBLE);
        }
        if (!MazeInfo.sensorBackward.isOperational()) {
            backON.setVisibility(View.INVISIBLE);
            backOFF.setVisibility(View.VISIBLE);
        }
        if (MazeInfo.sensorLeft.isOperational()) {
            leftON.setVisibility(View.VISIBLE);
            leftOFF.setVisibility(View.INVISIBLE);
        }
        if (!MazeInfo.sensorLeft.isOperational()) {
            leftON.setVisibility(View.INVISIBLE);
            leftOFF.setVisibility(View.VISIBLE);
        }
        if (MazeInfo.sensorRight.isOperational()) {
            rightON.setVisibility(View.VISIBLE);
            rightOFF.setVisibility(View.INVISIBLE);
        }
        if (!MazeInfo.sensorRight.isOperational()) {
            rightON.setVisibility(View.INVISIBLE);
            rightOFF.setVisibility(View.VISIBLE);
        }

    }



}