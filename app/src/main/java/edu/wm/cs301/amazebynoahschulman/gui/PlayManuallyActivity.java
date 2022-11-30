package edu.wm.cs301.amazebynoahschulman.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import edu.wm.cs301.amazebynoahschulman.R;
import edu.wm.cs301.amazebynoahschulman.generation.Maze;

/**
 * PlayManuallyActivity class -
 * in P6 displays a placeholder custom view and shortcut button to winning screen.
 * Includes up, left, right, and jump movement control buttons, toggle switches to show map,
 * map solution, and map visible walls, also a slider to adjust map size.
 */
public class PlayManuallyActivity extends AppCompatActivity {
    /**
     * TAG field variable for calls to Log.v
     */
    private static final String TAG = "PlayAnimationActivity";
    /**
     * Field variable to store path length, initialized to 0
     */
    int pathLength = 0;
    /**
     * Field variable to store shortest possible path length
     */
    int shortestPossiblePathLength;
    /**
     * field variable to store distance to exit, initialized to 0
     */
    int distanceToExit = 0;
    /**
     * forwardButton ImageButton field variable
     */
    private ImageButton forwardButton;
    /**
     * jumpButton ImageButton field variable
     */
    private ImageButton jumpButton;
    /**
     * leftButton ImageButton field variable
     */
    private ImageButton leftButton;
    /**
     * rightButton ImageButton field variable
     */
    private ImageButton rightButton;
    /**
     * shortCutButton Button field variable
     */
    private Button shortCutButton;
    /**
     * showMapSwitch Switch field variable
     */
    private Switch showMapSwitch;
    /**
     * showVisibleWallsSwitch Switch field variable
     */
    private Switch showVisibleWallsSwitch;
    /**
     * showSolutionSwitch Switch field variable
     */
    private Switch showSolutionSwitch;
    /**
     * mapSizeSeekBar SeekBar field variable
     */
    private SeekBar mapSizeSeekBar;
    /**
     * mazePanel MazePanel field variable
     */
    private MazePanel mazePanel;
    /**
     * boolean field variable, tells whether map is shown or not
     */
    private boolean showMap;


    private StatePlaying statePlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_manually);

        // set the maze to one stored in MazeInfo

        // create new StatePlaying object
        statePlaying = new StatePlaying();
        // set the maze, activity, and maze panel for statePlaying
        mazePanel = findViewById(R.id.mazePanel);
        statePlaying.setMaze(MazeInfo.maze);
        statePlaying.setPlayManuallyActivity(this);

        // set the shortest possible path length to be passed to winning screen
        int[] startingCoords = MazeInfo.maze.getStartingPosition();
        shortestPossiblePathLength = MazeInfo.maze.getDistanceToExit(startingCoords[0], startingCoords[1]);

        // this is for FORWARD BUTTON, increases path length by 1
        // IN P7 WILL MOVE ROBOT FORWARD
        forwardButton = findViewById(R.id.forwardButton);
        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "FORWARD button pressed");
                Toast.makeText(getApplicationContext(),"FORWARD button pressed",Toast.LENGTH_SHORT).show();
                pathLength++;
                statePlaying.handleUserInput(Constants.UserInput.UP, 1);
            }
        });

        // this is for JUMP BUTTON, increases path length by 1
        // IN P7 WILL JUMP ROBOT
        jumpButton = findViewById(R.id.jumpButton);
        jumpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "JUMP button pressed");
                Toast.makeText(getApplicationContext(),"JUMP button pressed",Toast.LENGTH_SHORT).show();
                pathLength++;
                statePlaying.handleUserInput(Constants.UserInput.JUMP, 1);
            }
        });

        // this is for LEFT BUTTON
        // in p7 will move robot left
        leftButton = findViewById(R.id.leftButton);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "LEFT button pressed");
                Toast.makeText(getApplicationContext(),"LEFT button pressed",Toast.LENGTH_SHORT).show();
                statePlaying.handleUserInput(Constants.UserInput.LEFT, 1);
            }
        });

        // this is for RIGHT BUTTON
        // in p7 will move robot right
        rightButton = findViewById(R.id.rightButton);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "RIGHT button pressed");
                Toast.makeText(getApplicationContext(),"RIGHT button pressed",Toast.LENGTH_SHORT).show();
                statePlaying.handleUserInput(Constants.UserInput.RIGHT, 1);
            }
        });

        // THIS IS FOR SHORTCUT BUTTON, switches to State Winning
        shortCutButton = findViewById(R.id.shortCutButton);
        shortCutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "SHORTCUT button pressed");
                Toast.makeText(getApplicationContext(),"SHORTCUT button pressed",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), WinningActivity.class);
                // also passing the pathLength and distance2Exit to StateWinning
                intent.putExtra("pathLength", pathLength);
                intent.putExtra("distance2Exit", distanceToExit);
                intent.putExtra("playAnimation", false);
                // passing energy consumption to avoid errors in next state
                intent.putExtra("energyConsumption", 99999999);
                startActivity(intent);
            }
        });

        // this is for SHOW MAP SWITCH
        // in p7 will show map
        showMapSwitch = findViewById(R.id.showMapSwitch);
        showMapSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    Log.v(TAG, "SHOW MAP switch toggled ON");
                    Toast.makeText(getApplicationContext(),"SHOW MAP switch toggle ON",Toast.LENGTH_SHORT).show();
                    showMap = true;
                    statePlaying.handleUserInput(Constants.UserInput.TOGGLEFULLMAP, 1);
                } else {
                    Log.v(TAG, "SHOW MAP switch toggled OFF");
                    Toast.makeText(getApplicationContext(),"SHOW MAP switch toggled OFF",Toast.LENGTH_SHORT).show();
                    showMap = false;
                    statePlaying.handleUserInput(Constants.UserInput.TOGGLEFULLMAP, 1);
                }
            }
        });

        // this is for SHOW VISIBLE WALLS SWITCH
        // in p7 will show map's visible walls
        showVisibleWallsSwitch = findViewById(R.id.showVisibleWallsSwitch);
        showVisibleWallsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    Log.v(TAG, "SHOW SOLUTION switch toggled ON");
                    Toast.makeText(getApplicationContext(),"SHOW SOLUTION switch toggle ON",Toast.LENGTH_SHORT).show();
                    showMap = true;
                    statePlaying.handleUserInput(Constants.UserInput.TOGGLELOCALMAP, 1);
                } else {
                    Log.v(TAG, "SHOW SOLUTION switch toggled OFF");
                    Toast.makeText(getApplicationContext(),"SHOW SOLUTION switch toggled OFF",Toast.LENGTH_SHORT).show();
                    showMap = false;
                    statePlaying.handleUserInput(Constants.UserInput.TOGGLELOCALMAP, 1);
                }
            }
        });

        // this is for SHOW SOLUTION SWITCH
        // in p7 will show solution on map
        showSolutionSwitch = findViewById(R.id.showSolutionSwitch);
        showSolutionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    Log.v(TAG, "SHOW VISIBLE WALLS switch toggled ON");
                    Toast.makeText(getApplicationContext(),"SHOW VISIBLE WALLS switch toggle ON",Toast.LENGTH_SHORT).show();
                    showMap = true;
                    statePlaying.handleUserInput(Constants.UserInput.TOGGLESOLUTION, 1);
                } else {
                    Log.v(TAG, "SHOW VISIBLE WALLS switch toggled OFF");
                    Toast.makeText(getApplicationContext(),"SHOW VISIBLE WALLS switch toggled OFF",Toast.LENGTH_SHORT).show();
                    showMap = false;
                    statePlaying.handleUserInput(Constants.UserInput.TOGGLESOLUTION, 1);
                }
            }
        });

        // this is for the MAP SIZE SEEKBAR
        // in p7 this will change the map size
        mapSizeSeekBar = findViewById(R.id.mapSizeSeekBar);
        mapSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                // IN P7 MAP'S SIZE WILL BE SET TO progress
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.v(TAG, "MAP SIZE seekbar touched");
                Toast.makeText(getApplicationContext(),"MAP SIZE seekbar touched",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        statePlaying.start(mazePanel);


    }

    // back button functionality, moves to StateTitle
    @Override
    public void onBackPressed() {
        Log.v(TAG, "BACK button pressed");
        Toast.makeText(getApplicationContext(),"BACK button pressed",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), AMazeActivity.class);
        startActivity(intent);
    }

    public void startWinningActivity() {
        Intent intent = new Intent(getApplicationContext(), WinningActivity.class);
        // also passing the pathLength and distance2Exit to StateWinning
        intent.putExtra("pathLength", pathLength);
        intent.putExtra("distance2Exit", distanceToExit);
        intent.putExtra("playAnimation", false);
        // passing energy consumption to avoid errors in next state
        intent.putExtra("energyConsumption", 99999999);
        startActivity(intent);
    }


}