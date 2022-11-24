package edu.wm.cs301.amazebynoahschulman.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import edu.wm.cs301.amazebynoahschulman.R;

public class WinningActivity extends AppCompatActivity {
    /**
     * TAG field variable for calls to Log.v
     */
    private static final String TAG = "PlayAnimationActivity";
    /**
     * restartGame Button field variable
     */
    private Button restartGame;
    /**
     * PathLengthTextView TextView field variable
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winning);

        // to get passed in values from previous activity
        // previous activity
        Intent previousActivity = getIntent();
        // getting pathLength from previous activity, displays 9999 if nothing found
        int pathLength = previousActivity.getIntExtra("pathLength", 9999);
        // getting shortest path length from previous activity, displays 8888 if nothing found
        int shortestPathLength = previousActivity.getIntExtra("distance2Exit", 88888);
        // getting energy consumption if previous activity was PlayAnimationActivity -- defaults to false
        boolean AnimationBoolean = previousActivity.getBooleanExtra("playAnimation", false);
        // getting energy comsumption from previous activity, displays 77777 if nothing found
        int energyConsumption = previousActivity.getIntExtra("energyConsumption", 77777);

        // THIS IS FOR RESTART GAME BUTTON, switches to State Winning
        restartGame = findViewById(R.id.restartButton1);
        restartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "RESTART GAME button pressed");
                Toast.makeText(getApplicationContext(),"RESTART GAME button pressed",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), AMazeActivity.class);
                // also passing the pathLength and distance2Exit to StateWinning
                startActivity(intent);
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

        // this is to display energy consumption text view if previous activity was playAnimation
        if (AnimationBoolean) {
            energyConsumptionTextView = findViewById(R.id.energyConsumptionTextView1);
            energyConsumptionTextView.setVisibility(View.VISIBLE);
            String energyConsumptionString = String.valueOf(energyConsumption);
            energyConsumptionTextView.append(" ");
            energyConsumptionTextView.append(energyConsumptionString);
        }

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