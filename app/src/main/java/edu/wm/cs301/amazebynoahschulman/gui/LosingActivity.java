package edu.wm.cs301.amazebynoahschulman.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import edu.wm.cs301.amazebynoahschulman.R;

/**
 * LosingActivity activity class - losing screen which tells the user that the robot lost the game,
 * the robot's path length, reason for loss, shortest possible path length, and energy consumed.
 * Also allows user to restart game/return to title screen.
 * @author Noah Schulman
 */
public class LosingActivity extends AppCompatActivity {
    /**
     * TAG field variable for Log.V calls
     */
    private static final String TAG = "LosingActivity";
    /**
     * restartGame Button field variable
     */
    private Button restartGame;
    /**
     * pathlengthTextView TextView field variable
     */
    private TextView pathlengthTextView;
    /**
     * shortestPathLengthTextView TextView field variable
     */
    private TextView shortestPathLengthTextView;
    /**
     * energyConsumptionTextView TextView field variable
     */
    private TextView energyConsumptionTextView;
    /**
     * Field variable for tortureSounds MediaPlayer object
     */
    private MediaPlayer tortureSounds;
    /**
     * Field variable for fireSounds MediaPlayer object
     */
    private MediaPlayer fireSounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_losing);
        // start tortureSounds sound effects
        tortureSounds = MediaPlayer.create(LosingActivity.this, R.raw.torture);
        tortureSounds.setVolume(1.0f, 1.0f);
        tortureSounds.setLooping(true);
        tortureSounds.start();
        // start fireSounds sound effects
        fireSounds = MediaPlayer.create(LosingActivity.this, R.raw.fire);
        fireSounds.setVolume(1.0f, 1.0f);
        fireSounds.setLooping(true);
        fireSounds.start();

        // to get passed in values from previous activity
        // previous activity
        Intent previousActivity = getIntent();
        // getting pathLength from previous activity, displays 9999 if nothing found
        int pathLength = previousActivity.getIntExtra("pathLength", 9999);
        // getting shortest path length from previous activity, displays 8888 if nothing found
        int shortestPathLength = previousActivity.getIntExtra("distance2Exit", 88888);
        // getting energy comsumption from previous activity, displays 77777 if nothing found
        int energyConsumption = previousActivity.getIntExtra("energyConsumption", 77777);

        // THIS IS FOR RESTART GAME BUTTON, switches to State Winning
        restartGame = findViewById(R.id.restartButton1);
        restartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "restart button pressed");
                Intent intent = new Intent(getApplicationContext(), AMazeActivity.class);
                // also passing the pathLength and distance2Exit to StateWinning
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        // this is to display path length text view
        pathlengthTextView = findViewById(R.id.pathLengthTextView1);
        String pathLengthString = String.valueOf(pathLength);
        pathlengthTextView.append(" ");
        pathlengthTextView.append(pathLengthString);

        // this is to display shortest path length text view
        shortestPathLengthTextView = findViewById(R.id.shortestPathLengthTextView1);
        String shortestPathLengthString = String.valueOf(shortestPathLength);
        shortestPathLengthTextView.append(" ");
        shortestPathLengthTextView.append(shortestPathLengthString);

        // this is to display energy consumption textView
        energyConsumptionTextView = findViewById(R.id.energyConsumptionTextView1);
        String energyConsumptionString = String.valueOf(energyConsumption);
        energyConsumptionTextView.append(" ");
        energyConsumptionTextView.append(energyConsumptionString);
    }

    // back button functionality, moves to StateTitle
    @Override
    public void onBackPressed() {
        Log.v(TAG, "back button pressed");
        Intent intent = new Intent(getApplicationContext(), AMazeActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    protected void onPause() {
        super.onPause();
        tortureSounds.release();
        fireSounds.release();
    }
}