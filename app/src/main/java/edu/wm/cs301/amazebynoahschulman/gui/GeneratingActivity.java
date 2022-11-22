package edu.wm.cs301.amazebynoahschulman.gui;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

public class GeneratingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "GeneratingActivity";

    // field variable to store which driver is being used
    private int driver;
    // field variable to store which robot config is being used
    private int robotConfig;

    private RadioGroup driverRadioGroup;
    private RadioButton driverRadioButton;

    private Spinner robotConfigSpinner;
    private TextView spinnerText;

    protected ProgressBar buildProgress;

    private backgroundThread thread = new backgroundThread();


    public void startThread() {
        thread.start();
    }

    public void stopThread() {
        thread.interrupt();
    }


    class backgroundThread extends Thread {
        @Override
        public void run() {
            for (int i = 0; i <= 100; i++) {
                try {
                    Thread.sleep(100);
                    buildProgress.incrementProgressBy(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generating);

        buildProgress = findViewById(R.id.progressBar1);

        startThread();




        // implementing driver radio group:
        driverRadioGroup = findViewById(R.id.radioGroup);

        // this is for robot config spinner:
        robotConfigSpinner = findViewById(R.id.robotConfigSpinner1);
        spinnerText = findViewById(R.id.textView2);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.robotConfigList, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        robotConfigSpinner.setAdapter(adapter);
        robotConfigSpinner.setOnItemSelectedListener(this);

    }

    public void checkButton(View v) {

        driver = driverRadioGroup.getCheckedRadioButtonId();
        driverRadioButton = findViewById(driver);
        if (driverRadioButton == findViewById(R.id.radio_wallfollower) || driverRadioButton == findViewById(R.id.radio_wizard)) {
            robotConfigSpinner.setVisibility(View.VISIBLE);
            spinnerText.setVisibility(View.VISIBLE);
        }
        if (driverRadioButton == findViewById(R.id.radio_manual)) {
            robotConfigSpinner.setVisibility(View.INVISIBLE);
            spinnerText.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        robotConfig = i;
        String choice = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}