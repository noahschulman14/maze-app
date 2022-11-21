package edu.wm.cs301.amazebynoahschulman.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import edu.wm.cs301.amazebynoahschulman.R;

public class GeneratingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // field variable to store which driver is being used
    private int driver;
    // field variable to store which robot config is being used
    private int robotConfig;

    private RadioGroup driverRadioGroup;
    private RadioButton driverRadioButton;

    private Spinner robotConfigSpinner;
    private TextView spinnerText;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generating);

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