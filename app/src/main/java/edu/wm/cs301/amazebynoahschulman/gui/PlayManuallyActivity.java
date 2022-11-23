package edu.wm.cs301.amazebynoahschulman.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;

import edu.wm.cs301.amazebynoahschulman.R;

public class PlayManuallyActivity extends AppCompatActivity {

    // path length
    int pathLength = 0;

    // forward button
    private ImageButton forwardButton;
    // jump button
    private ImageButton jumpButton;
    // left button
    private ImageButton leftButton;
    // right button
    private ImageButton rightButton;

    // show map switch
    private Switch showMapSwitch;
    // show visible walls switch
    private Switch showVisibleWallsSwitch;
    // show solution switch
    private Switch showSolutionSwitch;

    // map size seekBar
    private SeekBar mapSizeSeekBar;


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
                pathLength++;
            }
        });

        // this is for JUMP BUTTON, increases path length by 1
        // IN P7 WILL JUMP ROBOT
        jumpButton = findViewById(R.id.forwardButton);
        jumpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pathLength++;
            }
        });

        // this is for LEFT BUTTON
        // in p7 will move robot left
        leftButton = findViewById(R.id.leftButton);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // this is for RIGHT BUTTON
        // in p7 will move robot right
        rightButton = findViewById(R.id.rightButton);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // this is for SHOW MAP SWITCH
        // in p7 will show map
        showMapSwitch = findViewById(R.id.showMapSwitch);
        showMapSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });

        // this is for SHOW VISIBLE WALLS SWITCH
        // in p7 will show map's visible walls
        showVisibleWallsSwitch = findViewById(R.id.showVisibleWallsSwitch);
        showVisibleWallsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });

        // this is for SHOW SOLUTION SWITCH
        // in p7 will show solution on map
        showSolutionSwitch = findViewById(R.id.showSolutionSwitch);
        showSolutionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

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

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}