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

public class PlayManuallyActivity extends AppCompatActivity {

    private static final String TAG = "PlayAnimationActivity";

    // path length
    int pathLength = 0;

    // distance to exit
    int distanceToExit = 0;

    // forward button
    private ImageButton forwardButton;
    // jump button
    private ImageButton jumpButton;
    // left button
    private ImageButton leftButton;
    // right button
    private ImageButton rightButton;

    // shortcut button
    private Button shortCutButton;

    // show map switch
    private Switch showMapSwitch;
    // show visible walls switch
    private Switch showVisibleWallsSwitch;
    // show solution switch
    private Switch showSolutionSwitch;

    // map size seekBar
    private SeekBar mapSizeSeekBar;

    // show map boolean
    private boolean showMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_manually);

        // this is for FORWARD BUTTON, increases path length by 1
        // IN P7 WILL MOVE ROBOT FORWARD
        forwardButton = findViewById(R.id.forwardButton);
        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "FORWARD button pressed");
                Toast.makeText(getApplicationContext(),"FORWARD button pressed",Toast.LENGTH_SHORT).show();
                pathLength++;
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
                } else {
                    Log.v(TAG, "SHOW MAP switch toggled OFF");
                    Toast.makeText(getApplicationContext(),"SHOW MAP switch toggled OFF",Toast.LENGTH_SHORT).show();
                    showMap = false;
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
                } else {
                    Log.v(TAG, "SHOW SOLUTION switch toggled OFF");
                    Toast.makeText(getApplicationContext(),"SHOW SOLUTION switch toggled OFF",Toast.LENGTH_SHORT).show();
                    showMap = false;
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
                } else {
                    Log.v(TAG, "SHOW VISIBLE WALLS switch toggled OFF");
                    Toast.makeText(getApplicationContext(),"SHOW VISIBLE WALLS switch toggled OFF",Toast.LENGTH_SHORT).show();
                    showMap = false;
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