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

public class LosingActivity extends AppCompatActivity {

    private static final String TAG = "LosingActivity";

    // restart game button
    private Button restartGame;

    // path length text view
    private TextView pathlengthTextView;
    // shortest path length text view;
    private TextView shortestPathLengthTextView;
    // energy consumption text view;
    private TextView energyConsumptionTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_losing);

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
                Toast.makeText(getApplicationContext(),"restart button pressed",Toast.LENGTH_SHORT).show();
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
        Toast.makeText(getApplicationContext(),"back button pressed",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), AMazeActivity.class);
        startActivity(intent);
    }
}