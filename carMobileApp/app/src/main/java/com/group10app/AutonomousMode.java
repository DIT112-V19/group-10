package com.group10app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

public class AutonomousMode extends AppCompatActivity {

  private boolean isStopped = true;
  private boolean obstacleDetected = false;
  // Current status options
  private TextView currentStatus;
  private static final String OBS = "obstacle detected";
  private static final String FINE = "driving";
  private static final String STOPPED = "stopped";
  String outputMessage;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_autonomous_mode);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
  }

  public void setIsStopped(boolean isStopped) {
    this.isStopped = isStopped;
  }

  // Updates the current status
  public void getStatus() {
    currentStatus = findViewById(R.id.updatedStatus);

    if (obstacleDetected) currentStatus.setText(OBS);
    else if(isStopped) currentStatus.setText(STOPPED);
    else currentStatus.setText(FINE);
  }

  // boolean for easier testing
  public boolean buttonPress() {
    return isStopped ? startCar() : stopCar();
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
    outputMessage = "d";

    try {
      DeviceConnection.btSocket.getOutputStream().write(outputMessage.getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void followLine(View view) {
    outputMessage = "c";

    try {
      DeviceConnection.btSocket.getOutputStream().write(outputMessage.getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public void startTriangle(View view){
    outputMessage = "t";

    try {
      DeviceConnection.btSocket.getOutputStream().write(outputMessage.getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void stop(View view){
    outputMessage = "s";

    try {
      DeviceConnection.btSocket.getOutputStream().write(outputMessage.getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void onBackPressed(){
    super.onBackPressed();
    outputMessage = "q";

    try {
      DeviceConnection.btSocket.getOutputStream().write((outputMessage.getBytes()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
