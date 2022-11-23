package edu.wm.cs301.amazebynoahschulman.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;

import edu.wm.cs301.amazebynoahschulman.R;

public class PlayAnimationActivity extends AppCompatActivity {

    // path length
    int pathLength = 0;

    // distance to exit
    int distanceToExit = 0;

    // start button
    private Button startButton;
    // pause button
    private Button pauseButton;

    //P6
    // go2Winning button
    private Button go2WinningButton;
    // go2Losing button
    private Button go2LosingButton;

    // show map switch
    private Switch showMapSwitch;

    // map size seekbar
    private SeekBar mapSizeSeekBar;
    // animation speed seekbar
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
                startButton.setVisibility(View.INVISIBLE);
                pauseButton.setVisibility(View.VISIBLE);
            }
        });

        // PAUSE BUTTON
        // on press become invisible, makes start button visible
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseButton.setVisibility(View.INVISIBLE);
                startButton.setVisibility(View.VISIBLE);
            }
        });

        // GO2WINNING BUTTON
        go2WinningButton = findViewById(R.id.go2WinningButton);
        go2WinningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WinningActivity.class);
                // also passing the pathLength and distance2Exit to StateWinning
                intent.putExtra("pathLength", pathLength);
                intent.putExtra("distance2Exit", distanceToExit);
                startActivity(intent);
            }
        });

        // GO2LOSING BUTTON
        go2LosingButton = findViewById(R.id.go2LosingButton);
        go2LosingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LosingActivity.class);
                // also passing the pathLength and distance2Exit to StateWinning
                intent.putExtra("pathLength", pathLength);
                intent.putExtra("distance2Exit", distanceToExit);
                startActivity(intent);
            }
        });

    }

    // back button functionality, moves to StateTitle
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), AMazeActivity.class);
        startActivity(intent);
    }



}