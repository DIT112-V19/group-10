package com.group10app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AutonomousMode extends AppCompatActivity {

    // Current status options
    private static final String OBS = "obstacle detected";
    private static final String FINE = "fine";
    private static final String STOPPED = "stopped";

    // Toggle between START and STOP
    private static boolean IS_STOPPED = true;
    private static final String START = "start";
    private static final String STOP = "stop";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autonomous_mode);
    }

    // Updates the current status
    public void getStatus(View view) {
        TextView currentStatus = findViewById(R.id.updatedStatus);
        currentStatus.setText(FINE);
    }

    // Start car
    public void startCar() {

    }

    // Stop car
    public void stopCar() {

    }

    // Updates START/STOP button when pressed
    public void toggleButton(View view) {
        Button button = findViewById(R.id.button);
        if (IS_STOPPED) {
            IS_STOPPED = false;
            button.setText(STOP);
        } else {
            IS_STOPPED = true;
            button.setText(START);
        }

    }
}
