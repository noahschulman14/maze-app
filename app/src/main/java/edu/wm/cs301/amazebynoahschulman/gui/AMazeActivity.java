package edu.wm.cs301.amazebynoahschulman.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Random;

import edu.wm.cs301.amazebynoahschulman.R;
import edu.wm.cs301.amazebynoahschulman.generation.Maze;
import edu.wm.cs301.amazebynoahschulman.generation.Order;

/**
 * AMazeActivity activity class - displays title page to select
 * maze size, maze builder, presence of rooms in maze, and whether to
 * play the same maze as last game or a new one.
 */
public class AMazeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    /**
     * TAG field variable for Log.v calls
     */
    private static final String TAG = "GeneratingActivity";
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
    /**
     * Map size seekbar field variable
     */
    private SeekBar seekBar;
    /**
     * seekBarProgress TextView field variable
     */
    private TextView seekBarProgress;
    /**
     * builderAlgoSpinner Spinner field variable
     */
    private Spinner builderAlgoSpinner;
    /**
     * roomsSwitch Switch field variable
     */
    private Switch roomsSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amaze);
        MazeInfo.rooms = false;
        MazeInfo.builderAlgo = Order.Builder.Boruvka;

        // this is for the exploreButton, moves it to GeneratingActivity when clicked
        final Button exploreButton = findViewById(R.id.exploreButton);
        exploreButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v(TAG, "Explore button pressed");

                MazeInfo.started = true;

                // Code here executes on main thread after user presses button


                Random r = new Random();
                MazeInfo.randomSeed = r.nextInt(500);

                // saving maze config settings in SharedPreferences
                //SharedPreferences sharedPreferences = AMazeActivity.this.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("size", size);
                editor.putBoolean("rooms", rooms);
                editor.putString("builderAlgo", builderAlgo);
                editor.putInt("randomSeed", MazeInfo.randomSeed);
                editor.putBoolean("started", true);
                editor.apply();

//                MazeInfo.rooms = rooms;
//                MazeInfo.size = size;
//
//                // setting maze's builder algorithm according to selected builderAlgo string
//                if (builderAlgo.equals("Boruvka")) {
//                    MazeInfo.builderAlgo = Order.Builder.Boruvka;
//                }
//                if (builderAlgo.equals("Prim")) {
//                    MazeInfo.builderAlgo = Order.Builder.Prim;
//                }
//                if (builderAlgo.equals("DFS")) {
//                    MazeInfo.builderAlgo = Order.Builder.DFS;
//                }


                Intent intent = new Intent(getApplicationContext(), GeneratingActivity.class);
                startActivity(intent);

            }
        });

        // this is for the revisitButton, moves it to GeneratingActivity when clicked
        final Button revisitButton = findViewById(R.id.revisitButton);
        revisitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v(TAG, "Revisit button pressed");
                // Code here executes on main thread after user presses button
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                if (sharedPreferences.getBoolean("started", false) == false) {
                    Random r = new Random();
                    MazeInfo.randomSeed = r.nextInt(500);

                    // saving maze config settings in SharedPreferences
                    //SharedPreferences sharedPreferences = AMazeActivity.this.getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("size", size);
                    editor.putBoolean("rooms", rooms);
                    editor.putString("builderAlgo", builderAlgo);
                    editor.putInt("randomSeed", MazeInfo.randomSeed);
                    editor.putBoolean("started", true);
                    editor.apply();
                }

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
                Log.v(TAG, "Size seekbar pressed");
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
        roomsSwitch = (Switch) findViewById(R.id.roomsSwitch);
        roomsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.v(TAG, "Room switch toggled ON");
                        rooms = false;

                } else {
                    Log.v(TAG, "Room switch toggled OFF");
                       rooms = true;
                }
            }
        });


    }

    // this is for the builder algo spinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String choice = adapterView.getItemAtPosition(i).toString();
        builderAlgo = choice;
        Log.v(TAG, "Generation algo spinner set to " + builderAlgo);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}