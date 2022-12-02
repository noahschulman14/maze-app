package edu.wm.cs301.amazebynoahschulman.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import edu.wm.cs301.amazebynoahschulman.R;

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
     * startButton Button field variable
     */
    private Button startButton;
    /**
     * pauseButton Button field variable
     */
    private Button pauseButton;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_animation);

        // initialize start and stop buttons
        startButton = findViewById(R.id.startButton);
        pauseButton = findViewById(R.id.pauseButton);

        // START BUTTON
        // on press becomes invisible, makes pause button visible
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "START button pressed");
                Toast.makeText(getApplicationContext(),"START button pressed",Toast.LENGTH_SHORT).show();
                startButton.setVisibility(View.INVISIBLE);
                pauseButton.setVisibility(View.VISIBLE);
            }
        });

        // PAUSE BUTTON
        // on press become invisible, makes start button visible
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "PAUSE button pressed");
                Toast.makeText(getApplicationContext(),"PAUSE button pressed",Toast.LENGTH_SHORT).show();
                pauseButton.setVisibility(View.INVISIBLE);
                startButton.setVisibility(View.VISIBLE);
            }
        });

        // GO2WINNING BUTTON
        go2WinningButton = findViewById(R.id.go2WinningButton);
        go2WinningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "GO2WINNING button pressed");
                Toast.makeText(getApplicationContext(),"GO2WINNING button pressed",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), WinningActivity.class);
                // also passing the pathLength and distance2Exit to StateWinning
                intent.putExtra("pathLength", pathLength);
                intent.putExtra("distance2Exit", distanceToExit);
                intent.putExtra("energyConsumption", energyConsumption);
                intent.putExtra("playAnimation", true);
                startActivity(intent);
            }
        });

        // GO2LOSING BUTTON
        go2LosingButton = findViewById(R.id.go2LosingButton);
        go2LosingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "GO2LOSING button pressed");
                Toast.makeText(getApplicationContext(),"GO2LOSING button pressed",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), LosingActivity.class);
                // also passing the pathLength and distance2Exit to StateWinning
                intent.putExtra("pathLength", pathLength);
                intent.putExtra("distance2Exit", distanceToExit);
                intent.putExtra("energyConsumption", energyConsumption);
                intent.putExtra("playAnimation", true);
                startActivity(intent);
            }
        });

        // this is for SHOW MAP switch:
        showMapSwitch = (Switch) findViewById(R.id.showMapSwitch);
        showMapSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.v(TAG, "SHOW MAP switch toggled ON");
                    Toast.makeText(getApplicationContext(),"SHOW MAP switch toggle ON",Toast.LENGTH_SHORT).show();
                } else {
                    Log.v(TAG, "SHOW MAP switch toggled OFF");
                    Toast.makeText(getApplicationContext(),"SHOW MAP switch toggled OFF",Toast.LENGTH_SHORT).show();
                }
            }
        });

        // this is for map size seek bar
        mapSizeSeekBar = findViewById(R.id.mapSizeSeekBar);
        mapSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.v(TAG, "MAP SIZE SEEKBAR touched");
                Toast.makeText(getApplicationContext(),"MAP SIZE SEEKBAR touched",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // this is for ANIMATION SPEED seek bar
        animationSpeedSeekBar = findViewById(R.id.animationSpeedSeekBar);
        animationSpeedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.v(TAG, "ANIMATION SPEED SEEKBAR touched");
                Toast.makeText(getApplicationContext(),"ANIMATION SPEED SEEKBAR touched",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    // back button functionality, moves to StateTitle
    @Override
    public void onBackPressed() {
        Log.v(TAG, "BACK button pressed");
        Toast.makeText(getApplicationContext(),"BACK button pressed",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), AMazeActivity.class);
        startActivity(intent);
    }



}