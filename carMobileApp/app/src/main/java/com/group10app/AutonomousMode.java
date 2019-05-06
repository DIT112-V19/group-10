package com.group10app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AutonomousMode extends AppCompatActivity {

    private static boolean isStopped = true;  // why is it static?
    private static boolean obstacleDetected = false;  // why is it static?
    // Current status options
    private TextView currentStatus;
    private static final String OBS = "obstacle detected";
    private static final String FINE = "driving";
    private static final String STOPPED = "stopped";
    private static String test = ".";

    private UpdateStatus update = new UpdateStatus();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autonomous_mode);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    // -------------- setters and getters for testing -------------------
    // probably better to be non-static
    public static void setIsStopped(boolean isStopped) {
        AutonomousMode.isStopped = isStopped;
    }

    public static void setObstacleDetected(boolean obstacleDetected) {
        AutonomousMode.obstacleDetected = obstacleDetected;
    }

    public TextView getCurrentStatus() {
        return currentStatus;
    }

    public static boolean getObstacleDetected(){
        return obstacleDetected;
    }
    
    // ----------------------------------------------------------------

    // Updates the current status
    public void getStatus() {
        currentStatus = findViewById(R.id.updatedStatus);
        if (obstacleDetected)
            currentStatus.setText(OBS);
        else if(isStopped)
            currentStatus.setText(STOPPED);
        else
            currentStatus.setText(FINE);
    }

    // boolean for easier testing
    public boolean buttonPress() {
        boolean carStopped;
        if (isStopped) {
            carStopped = startCar();
        } else {
            carStopped = stopCar();
        }
        return carStopped;
    }

    // Start car
    public boolean startCar() {
        return false;
    }

    // Stop car
    public boolean stopCar() {
        return true;
    }

    // Updates START/STOP button when pressed
    public void toggleButton(View view) {
        Button button = findViewById(R.id.button);
        button.setText(update.getButton(isStopped));

        if(isStopped)
            isStopped = false;
        else
            isStopped = true;

        getStatus();
    }

    public void getSpeed(View view) {
        currentStatus = findViewById(R.id.updatedCurrentSpeed);
        currentStatus.setText(update.getSpeed());
    }
}
