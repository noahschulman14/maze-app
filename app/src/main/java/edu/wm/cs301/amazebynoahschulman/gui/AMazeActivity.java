package edu.wm.cs301.amazebynoahschulman.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.wm.cs301.amazebynoahschulman.R;

public class AMazeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    /**
     * Field variable to store maze size
     */
    private int size;
    /**
     * Field variable to store whether maze has rooms
     */
    private boolean rooms;
    /**
     * Field variable to store maze builder algorithm
     * FOR PROJECT 6 THIS IS AN INT
     * IN PROJECT 7 IT WILL BE TYPE BUILDER
     */
    private String builderAlgo;

    private SeekBar seekBar;
    private TextView seekBarProgress;
    private Spinner builderAlgoSpinner;
    private Switch roomsSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amaze);

        // this is for the exploreButton, moves it to GeneratingActivity when clicked
        final Button exploreButton = findViewById(R.id.exploreButton);
        exploreButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                MazeInfo.rooms = rooms;
                MazeInfo.size = size;
                MazeInfo.builderAlgo = builderAlgo;
                Intent intent = new Intent(getApplicationContext(), GeneratingActivity.class);
                startActivity(intent);
            }
        });

        // this is for the revisitButton, moves it to GeneratingActivity when clicked
        final Button revisitButton = findViewById(R.id.revisitButton);
        revisitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(getApplicationContext(), GeneratingActivity.class);
                startActivity(intent);
            }
        });

        // making the seek bar's value update on screen in a textview
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBarProgress = (TextView) findViewById(R.id.seekBarProgress);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                size = progress;
                seekBarProgress.setText("SIZE: "+ progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // this is for builder algorithm spinner:
        builderAlgoSpinner = findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.builder, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        builderAlgoSpinner.setAdapter(adapter);
        builderAlgoSpinner.setOnItemSelectedListener(this);

        // this is for room switch:
        roomsSwitch = (Switch) findViewById(R.id.switch1);
        roomsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                        rooms = true;
                } else {
                       rooms = false;
                }
            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String choice = adapterView.getItemAtPosition(i).toString();
        builderAlgo = choice;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}