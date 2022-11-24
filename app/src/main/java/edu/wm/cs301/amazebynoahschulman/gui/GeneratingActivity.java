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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.wm.cs301.amazebynoahschulman.R;

public class GeneratingActivity extends AppCompatActivity  {

    private static final String TAG = "GeneratingActivity";

    // field variable to store which driver is being used
    private String driver;
    // field variable to store which robot config is being used
    private String robotConfig;

    private RadioGroup driverRadioGroup;
    private RadioButton driverRadioButton;


    protected ProgressBar buildProgress;

    private backgroundThread thread = new backgroundThread();

    private Button startButton;


    public void startThread() {
        thread.start();
    }

    public void stopThread() {
        thread.interrupt();
    }

    private boolean ready = false;

    private boolean manual = false;

    private boolean auto = false;

    private boolean robot = false;

    private TextView reminder;

    private TextView robotConfigText;

    private RadioGroup robotConfigRadioGroup;

    private RadioButton robotConfigRadioButton;

    private TextView reminder2;

    private TextView shortly;

    class backgroundThread extends Thread {
        @Override
        public void run() {
            for (int i = 0; i <= 100; i++) {
                try {
                    Thread.sleep(50);
                    buildProgress.incrementProgressBy(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            ready = true;
            if (manual) {
                Intent intent = new Intent(getApplicationContext(), PlayManuallyActivity.class);
                startActivity(intent);
            }
            else if (auto && robot) {
                Intent intent = new Intent(getApplicationContext(), PlayAnimationActivity.class);
                startActivity(intent);
            }
            else if (auto) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        reminder2.setVisibility(View.VISIBLE);
                    }
                });
            }
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




        // implementing driver radio group:
        driverRadioGroup = findViewById(R.id.radioGroup);



    }

    public void checkButton(View v) {

        int selectedId = driverRadioGroup.getCheckedRadioButtonId();
        driverRadioButton = findViewById(selectedId);
        driver = driverRadioButton.getText().toString();
        if (driverRadioButton == findViewById(R.id.radio_wallfollower) || driverRadioButton == findViewById(R.id.radio_wizard)) {
            MazeInfo.driver = driver;
            robotConfigRadioGroup.setVisibility(View.VISIBLE);
            robotConfigText.setVisibility(View.VISIBLE);
            if (!robot) {
                shortly.setVisibility(View.INVISIBLE);
            }
            manual = false;
            auto = true;
            if (robot) {
                shortly.setVisibility(View.VISIBLE);
            }
            if (ready && robot) {
                Intent intent = new Intent(getApplicationContext(), PlayAnimationActivity.class);
                startActivity(intent);
            }
            else if (ready) {
                reminder2.setVisibility(View.VISIBLE);
            }

        }
        if (driverRadioButton == findViewById(R.id.radio_manual)) {
            shortly.setVisibility(View.VISIBLE);
            robotConfigRadioGroup.setVisibility(View.INVISIBLE);
            robotConfigText.setVisibility(View.INVISIBLE);
            manual = true;
            auto = false;
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
        if (robotConfigRadioButton == findViewById(R.id.radio_premium) || robotConfigRadioButton == findViewById(R.id.radio_mediocre)
                || robotConfigRadioButton == findViewById(R.id.radio_soso) || robotConfigRadioButton == findViewById(R.id.radio_shaky)) {
            MazeInfo.robotConfig = robotConfig;
            robot = true;
            shortly.setVisibility(View.VISIBLE);
            if (ready && auto) {
                Intent intent = new Intent(getApplicationContext(), PlayAnimationActivity.class);
                startActivity(intent);
            }

        }
    }



    @Override
    public void onBackPressed() {
        stopThread();
        super.onBackPressed();
    }
}