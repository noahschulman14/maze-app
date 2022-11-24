package edu.wm.cs301.amazebynoahschulman.gui;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.wm.cs301.amazebynoahschulman.R;

/**
 * GeneratingActivity activity class - runs a background thread that
 * builds maze, displays progress of that on screen.
 * Allows user to select a driver and robot configuration while background maze generation
 * thread is running.
 */
public class GeneratingActivity extends AppCompatActivity  {
    /**
     * TAG field variable for Log.v calls
     */
    private static final String TAG = "GeneratingActivity";
    /**
     * Field variable to store which driver is being used
     */
    private String driver;
    /**
     * Field variable to store which robot configuration is being used
     */
    private String robotConfig;
    /**
     * driverRadioGroup RadioGroup field variable
     */
    private RadioGroup driverRadioGroup;
    /**
     * driverRadioButton RadioButton field variable
     */
    private RadioButton driverRadioButton;
    /**
     * buildProgress ProgressBar field variable
     */
    protected ProgressBar buildProgress;
    /**
     * backgroundThread tread field variable/initialization
     */
    private backgroundThread thread = new backgroundThread();
    /**
     * startButton Button field variable
     */
    private Button startButton;

    /**
     * method to start background maze generation thread
     */
    public void startThread() {
        thread.start();
    }

    /**
     * method to stop background maze generation thread
     */
    public void stopThread() {
        thread.interrupt();
    }

    /**
     * Boolean field variable, tells if background maze generation is complete
     */
    private boolean ready = false;
    /**
     * Boolean field variable, tells if maze will be operating in manual mode
     */
    private boolean manual = false;
    /**
     * Boolean field variable, tells if maze will be operated with an automated robot
     */
    private boolean auto = false;
    /**
     * Boolean field variable, tells if a robot has been selected
     */
    private boolean robot = false;
    /**
     * reminder TextView field variable
     */
    private TextView reminder;
    /**
     * robotConfigText TextView field variable
     */
    private TextView robotConfigText;
    /**
     * robotConfigRadioGroup RadioGroup field variable
     */
    private RadioGroup robotConfigRadioGroup;
    /**
     * robotConfigRadioButton RadioButton field variable
     */
    private RadioButton robotConfigRadioButton;
    /**
     * reminder2 TextView field variable
     */
    private TextView reminder2;
    /**
     * shortly TextView field variable
     */
    private TextView shortly;

    class backgroundThread extends Thread {
        @Override
        public void run() {
            Log.v(TAG, "BACKGROUND THREAD RUNNING");
            for (int i = 0; i <= 100; i++) {
                try {
                    Thread.sleep(50);
                    buildProgress.incrementProgressBy(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // ready boolean set to true once background maze generation is finished
            ready = true;
            // if manual radio button is selected and background thread is finished, move to PlayManuallyActivity
            if (manual) {
                Intent intent = new Intent(getApplicationContext(), PlayManuallyActivity.class);
                startActivity(intent);
            }
            // if an automated driver is selected and robot sensor configuration is selected and
            // background maze generation is finished, move to PlayManuallyActivity
            else if (auto && robot) {
                Intent intent = new Intent(getApplicationContext(), PlayAnimationActivity.class);
                startActivity(intent);
            }
            // if automated driver is selected and background maze generation is finished
            // but robot sensor configuration is not selected, display text to remind user to select one
            else if (auto) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        reminder2.setVisibility(View.VISIBLE);
                    }
                });
            }
            // if background maze generation is finished but no driver option is selected
            // display text to remind user to select one
            else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            reminder.setVisibility(View.VISIBLE);
                        }
                    });
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generating);

        buildProgress = findViewById(R.id.progressBar1);
        reminder = findViewById(R.id.textView5);

        robotConfigText = findViewById(R.id.textView6);
        robotConfigRadioGroup = findViewById(R.id.radioGroup2);

        reminder2 = findViewById(R.id.textView7);
        shortly = findViewById(R.id.textView8);

        startThread();

        driverRadioGroup = findViewById(R.id.radioGroup);
    }

    // for driver radio button group
    public void checkButton(View v) {
        int selectedId = driverRadioGroup.getCheckedRadioButtonId();
        driverRadioButton = findViewById(selectedId);
        driver = driverRadioButton.getText().toString();
        Log.v(TAG, driver + " radio button pressed");
        Toast.makeText(getApplicationContext(),driver + " radio button pressed",Toast.LENGTH_SHORT).show();
        if (driverRadioButton == findViewById(R.id.radio_wallfollower) || driverRadioButton == findViewById(R.id.radio_wizard)) {
            MazeInfo.driver = driver;
            // if automated driver is selected, display radio button group to select a robot sensor configuration
            robotConfigRadioGroup.setVisibility(View.VISIBLE);
            robotConfigText.setVisibility(View.VISIBLE);
            // if a robot is not selected/unselected, make text that says maze is starting shortly invisible
            if (!robot) {
                shortly.setVisibility(View.INVISIBLE);
            }
            // if automated maze generation is selected, change manual boolean to false and auto to true
            manual = false;
            auto = true;
            // if a robot is selected, display text to say that game will start shortly
            if (robot) {
                shortly.setVisibility(View.VISIBLE);
            }
            // if maze generation is finished and a robot is selected, move to PlayAnimationActivity
            if (ready && robot) {
                Intent intent = new Intent(getApplicationContext(), PlayAnimationActivity.class);
                startActivity(intent);
            }
            // if maze generation is finished and a robot configuration is not selected,
            // remind user to select one
            else if (ready) {
                reminder2.setVisibility(View.VISIBLE);
            }

        }
        if (driverRadioButton == findViewById(R.id.radio_manual)) {
            // if manual is selected, display text to say game will start shortly
            shortly.setVisibility(View.VISIBLE);
            // if manual is selected, changing from a automated driver selection,
            // make the robot radio group invisible
            robotConfigRadioGroup.setVisibility(View.INVISIBLE);
            robotConfigText.setVisibility(View.INVISIBLE);
            manual = true;
            auto = false;
            // if background maze generation finished after manual is selected,
            // move to PlayManuallyActivity
            if (ready) {
                Intent intent = new Intent(getApplicationContext(), PlayManuallyActivity.class);
                startActivity(intent);
            }
        }
    }

    public void checkButton2(View v) {
        int selectedId = robotConfigRadioGroup.getCheckedRadioButtonId();
        robotConfigRadioButton = findViewById(selectedId);
        robotConfig = robotConfigRadioButton.getText().toString();
        Log.v(TAG,  robotConfig + " radio button pressed");
        Toast.makeText(getApplicationContext(), robotConfig + " radio button pressed",Toast.LENGTH_SHORT).show();
        if (robotConfigRadioButton == findViewById(R.id.radio_premium) || robotConfigRadioButton == findViewById(R.id.radio_mediocre)
                || robotConfigRadioButton == findViewById(R.id.radio_soso) || robotConfigRadioButton == findViewById(R.id.radio_shaky)) {
            MazeInfo.robotConfig = robotConfig;
            robot = true;
            // if robot config is set, display text that says game will start shortly
            shortly.setVisibility(View.VISIBLE);
            // if background thread is finished and an automated driver is selected and robot config,
            // move to PlayAnimationActivity
            if (ready && auto) {
                Intent intent = new Intent(getApplicationContext(), PlayAnimationActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Log.v(TAG, "back button pressed");
        Toast.makeText(getApplicationContext(),"back button pressed",Toast.LENGTH_SHORT).show();
        stopThread();
        super.onBackPressed();
    }
}