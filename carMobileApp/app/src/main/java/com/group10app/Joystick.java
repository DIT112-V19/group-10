package com.group10app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class Joystick extends AppCompatActivity {

  private TextView angleText;
  private int angleValue = 0;
  private int strengthValue = 0;
  private char direction = 'i';

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
        angleText.setText("angle " + angleValue + "°" + "strength " + strengthValue + "%" + " direction " + direction);

        if (angle <= 22.5 || angle > 337.5) {
          direction = 'f';
        } else if (angle > 292.5 && angle <= 337.5){
          direction = 'k'; // forward right
        } else if (angle > 247.5 && angle <= 292.5) {
          direction = 'r';
        } else if (angle > 202.5 && angle <= 247.5) {
          direction = 'm'; // backward right
        } else if (angle > 157.5 && angle < 202.5) {
          direction = 'b';
        } else if (angle > 112.5 && angle <= 157.5) {
          direction = 'n';
        } else if (angle > 67.5 && angle <= 112.5) {
            direction = 'l';
        } else {
            direction = 'j';
        }
      }
    });

  }



}