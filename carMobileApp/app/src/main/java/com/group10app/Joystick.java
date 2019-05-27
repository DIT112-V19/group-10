package com.group10app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import java.io.IOException;
import io.github.controlwear.virtual.joystick.android.JoystickView;

public class Joystick extends AppCompatActivity {

  private TextView angleText;
  private int angleValue = 0;
  private int strengthValue = 0;
  private String outputMessage; // char being sent to arduino. Will be converted to bytes but method getBytes() doesn't work with char.

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_joystick);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    // Joystick control
    JoystickView joystick = (JoystickView) findViewById(R.id.joystickView);
    angleText = (TextView) findViewById(R.id.joystickNumber);

    joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
      @Override
      public void onMove(int angle, int strength) {

        angleValue = angle;
        strengthValue = strength;
        angleText.setText("angle " + angleValue + "°" + "strength " + strengthValue + "%" + " outputMessage " + outputMessage);

        try {

          if (angle <= 22.5 || angle > 337.5) {
            outputMessage = "f";
          } else if (angle > 292.5 && angle <= 337.5){
            outputMessage = "k"; // forward right
          } else if (angle > 247.5 && angle <= 292.5) {
            outputMessage = "r";
          } else if (angle > 202.5 && angle <= 247.5) {
            outputMessage = "m"; // backward right
          } else if (angle > 157.5 && angle < 202.5) {
            outputMessage = "b";
          } else if (angle > 112.5 && angle <= 157.5) {
            outputMessage = "n"; // backward left
          } else if (angle > 67.5 && angle <= 112.5) {
            outputMessage = "l";
          } else {
            outputMessage = "j"; // forward left
          }

          DeviceConnection.btSocket.getOutputStream().write(outputMessage.getBytes());

        } catch (IOException e) {

          e.getMessage();

        }



      }
    });

  }



}