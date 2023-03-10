package edu.wm.cs301.amazebynoahschulman.gui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import edu.wm.cs301.amazebynoahschulman.R;

/**
 * PlayManuallyActivity class -
 * in P6 displays a placeholder custom view and shortcut button to winning screen.
 * Includes up, left, right, and jump movement control buttons, toggle switches to show map,
 * map solution, and map visible walls, also a slider to adjust map size.
 * @author Noah Schulman
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
     * zoomInButton Button field variable
     */
    private Button zoomInButton;
    /**
     * zoomOut Button field variable
     */
    private Button zoomOutButton;
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
     * mazePanel MazePanel field variable
     */
    private MazePanel mazePanel;
    /**
     * mapSize TextView field variable
     */
    private TextView mapSize;
    /**
     * StatePlaying field variable
     */
    private StatePlaying statePlaying;
    /**
     * Field variable for deathMetal MediaPlayer object
     */
    private MediaPlayer deathMetal;
    /**
     * Field variable for the screen ConstraintLayout
     */
    private ConstraintLayout screen;
    /**
     * Field variable for swipeListener object
     */
    private SwipeListener swipeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_manually);
        // start deathMetal music
        deathMetal = MediaPlayer.create(PlayManuallyActivity.this, R.raw.death_metal);
        deathMetal.setVolume(1.0f, 1.0f);
        deathMetal.setLooping(true);
        deathMetal.start();

        // create new StatePlaying object
        statePlaying = new StatePlaying();
        // set the maze, activity, and maze panel for statePlaying
        mazePanel = findViewById(R.id.mazePanel);
        statePlaying.setMaze(MazeInfo.maze);
        statePlaying.setPlayManuallyActivity(this);
        statePlaying.start(mazePanel);

        // set the shortest possible path length to be passed to winning screen
        int[] startingCoords = MazeInfo.maze.getStartingPosition();
        distanceToExit = MazeInfo.maze.getDistanceToExit(startingCoords[0], startingCoords[1]);

        // initialize screen ConstraintLayout
        screen = findViewById(R.id.screen);

        // initialize swipeListener
        swipeListener = new SwipeListener(screen);


        // this is for FORWARD BUTTON, increases path length by 1
        forwardButton = findViewById(R.id.forwardButton);
        forwardButton.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "FORWARD button pressed");
                pathLength++;
                statePlaying.handleUserInput(Constants.UserInput.UP, 1);
            }
        });

        // this is for JUMP BUTTON, increases path length by 1
        jumpButton = findViewById(R.id.jumpButton);
        jumpButton.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        jumpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "JUMP button pressed");
                pathLength++;
                statePlaying.handleUserInput(Constants.UserInput.JUMP, 1);
            }
        });

        // this is for LEFT BUTTON
        leftButton = findViewById(R.id.leftButton);
        leftButton.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "LEFT button pressed");
                statePlaying.handleUserInput(Constants.UserInput.LEFT, 1);
            }
        });

        // this is for RIGHT BUTTON
        rightButton = findViewById(R.id.rightButton);
        rightButton.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "RIGHT button pressed");
                statePlaying.handleUserInput(Constants.UserInput.RIGHT, 1);
            }
        });

        // this is for SHOW MAP SWITCH
        showMapSwitch = findViewById(R.id.showMapSwitch);
        showMapSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    Log.v(TAG, "SHOW MAP switch toggled ON");
                    statePlaying.handleUserInput(Constants.UserInput.TOGGLEFULLMAP, 1);
                } else {
                    Log.v(TAG, "SHOW MAP switch toggled OFF");
                    statePlaying.handleUserInput(Constants.UserInput.TOGGLEFULLMAP, 1);
                }
            }
        });

        // this is for SHOW VISIBLE WALLS SWITCH
        showVisibleWallsSwitch = findViewById(R.id.showVisibleWallsSwitch);
        // initializing mapsizeTextView, will become visible when show visible walls
        mapSize = findViewById(R.id.mapSizeTextView);
        showVisibleWallsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    Log.v(TAG, "SHOW SOLUTION switch toggled ON");
                    showSolutionSwitch.setVisibility(View.VISIBLE);
                    showMapSwitch.setVisibility(View.VISIBLE);
                    zoomInButton.setVisibility(View.VISIBLE);
                    zoomOutButton.setVisibility(View.VISIBLE);
                    mapSize.setVisibility(View.VISIBLE);
                    statePlaying.handleUserInput(Constants.UserInput.TOGGLELOCALMAP, 1);
                } else {
                    Log.v(TAG, "SHOW SOLUTION switch toggled OFF");
                    showSolutionSwitch.setVisibility(View.INVISIBLE);
                    showMapSwitch.setVisibility(View.INVISIBLE);
                    statePlaying.handleUserInput(Constants.UserInput.TOGGLELOCALMAP, 1);
                }
            }
        });

        // this is for SHOW SOLUTION SWITCH
        showSolutionSwitch = findViewById(R.id.showSolutionSwitch);
        showSolutionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    Log.v(TAG, "SHOW VISIBLE WALLS switch toggled ON");
                    statePlaying.handleUserInput(Constants.UserInput.TOGGLESOLUTION, 1);
                } else {
                    Log.v(TAG, "SHOW VISIBLE WALLS switch toggled OFF");
                    statePlaying.handleUserInput(Constants.UserInput.TOGGLESOLUTION, 1);
                }
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

    // back button functionality, moves to StateTitle
    @Override
    public void onBackPressed() {
        Log.v(TAG, "BACK button pressed");
        Intent intent = new Intent(getApplicationContext(), AMazeActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
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
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    protected void onPause() {
        super.onPause();
        deathMetal.release();
    }

    /**
     * Custom swipeListener listener object
     * Detects swiping gestures on screen
     *
     * Inspiration from: https://www.youtube.com/watch?v=vNJyU-XW8_Y
     */
    private class SwipeListener implements View.OnTouchListener {
        // initialize GestureDetector
        GestureDetector gesture;

        // constructor
        SwipeListener(View view) {
            // threshold values
            int threshold = 100;
            int velocity_threshold = 100;

            // gesture listener
            GestureDetector.SimpleOnGestureListener listener =
            new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDown(MotionEvent e) {
                    return true;
                }
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    // getting x and y difference
                    float xDifference = e2.getX() - e1.getX();
                    float yDifference = e2.getY() - e1.getY();
                    if (Math.abs(xDifference) > Math.abs(yDifference)) {
                        // when x is greater than y
                        if (Math.abs(xDifference) > threshold && Math.abs(velocityX) > velocity_threshold) {
                            if (xDifference > 0) {
                                // when right swipe
                                Log.v(TAG, "right swipe detected");
                                statePlaying.handleUserInput(Constants.UserInput.RIGHT, 1);
                            }
                            else {
                                // when left swipe
                                Log.v(TAG, "left swipe detected");
                                statePlaying.handleUserInput(Constants.UserInput.LEFT, 1);
                            }
                            return true;
                        }
                    }
                    else {
                        // when y is greater than x
                        if (Math.abs(yDifference) > threshold && Math.abs(velocityY) > velocity_threshold) {
                             if (yDifference > 0) {
                                 // when down swipe
                                 Log.v(TAG, "down swipe detected, robot jumping");
                                 pathLength++;
                                 statePlaying.handleUserInput(Constants.UserInput.JUMP, 1);
                             }
                             else {
                                 // when up swipe
                                 Log.v(TAG, "up swipe detected");
                                 pathLength++;
                                 statePlaying.handleUserInput(Constants.UserInput.UP, 1);
                             }
                             return true;
                        }
                    }
                    return false;
                }
            };
            // Initializing gesture detector
            gesture = new GestureDetector(listener);
            view.setOnTouchListener(this);
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return gesture.onTouchEvent(motionEvent);
        }
    }
}