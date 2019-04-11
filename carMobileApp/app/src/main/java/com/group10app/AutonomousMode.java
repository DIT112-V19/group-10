package com.group10app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AutonomousMode extends AppCompatActivity {

    private static boolean isStopped = true;
    private static boolean obstacleDetected = false;
    // Current status options
    private static final String OBS = "obstacle detected";
    private static final String FINE = "driving";
    private static final String STOPPED = "stopped";

    private UpdateStatus update = new UpdateStatus();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autonomous_mode);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    // Updates the current status
    public void getStatus(Boolean isStopped) {
        TextView currentStatus = findViewById(R.id.updatedStatus);
        if (obstacleDetected)
            currentStatus.setText(OBS);
        else if(isStopped)
            currentStatus.setText(STOPPED);
        else
            currentStatus.setText(FINE);
    }

    public void buttonPress() {
        if (isStopped) {
            startCar();
        } else {
            stopCar();
        }
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
        button.setText(update.getButton(isStopped));

        if(isStopped)
            isStopped = false;
        else
            isStopped = true;

        getStatus(isStopped);
    }

    public void getSpeed(View view) {
        TextView textView = findViewById(R.id.updatedCurrentSpeed);
        textView.setText(update.getSpeed());
    }
}
