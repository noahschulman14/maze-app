package edu.wm.cs301.amazebynoahschulman.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import edu.wm.cs301.amazebynoahschulman.R;

public class GeneratingActivity extends AppCompatActivity {

    // field variable to store which driver is being used
    private int driver;
    // field variable to store which robot config is being used
    private int robotConfig;

    private RadioGroup driverRadioGroup;
    private RadioButton driverRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generating);

        // implementing driver radio group:
        driverRadioGroup = findViewById(R.id.radioGroup);

    }

    public void checkButton(View v) {
        driver = driverRadioGroup.getCheckedRadioButtonId();
        driverRadioButton = findViewById(driver);
    }
}