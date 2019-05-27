package com.group10app;

import android.bluetooth.BluetoothClass;
import android.support.design.animation.MotionTiming;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;
import java.nio.charset.Charset;

public class ManualMode extends AppCompatActivity {

  private ImageView forwardArrow;
  private ImageView backwardArrow;
  private ImageView rightArrow;
  private ImageView leftArrow;
  private Button stopButton;
  String outputMessage;
  boolean forward;


  private final String LOG_TAG = ManualMode.class.getSimpleName();
  // attributes for testing arrow click
  private boolean leftArrowClicked = false;
  private boolean rightArrowClicked = false;
  private boolean fwdArrowClicked = false;
  private boolean bckwArrowClicked = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_manual_mode);

    forwardArrow = findViewById(R.id.forwardArrow);
    backwardArrow = findViewById(R.id.backwardArrow);
    rightArrow = findViewById(R.id.rightArrow);
    leftArrow = findViewById(R.id.leftArrow);
    stopButton = findViewById(R.id.stopButton);

    forwardArrow.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        outputMessage = "f";
        //AutonomousMode.showArduinoSpeed();
        forward = true;
        try {
          DeviceConnection.btSocket.getOutputStream().write(outputMessage.getBytes());
        } catch (IOException e) {
          e.printStackTrace();
        }
        connectionData("Sent: " + outputMessage);
        bckwArrowClicked = true;
        rightArrowClicked = false;
        leftArrowClicked = false;
        fwdArrowClicked = false;
      }
    });

    backwardArrow.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        outputMessage = "b";
        forward = false;
        try {
          DeviceConnection.btSocket.getOutputStream().write(outputMessage.getBytes());
        } catch (IOException e) {
          e.printStackTrace();
        }
        connectionData("Sent: " + outputMessage);
        bckwArrowClicked = true;
        rightArrowClicked = false;
        leftArrowClicked = false;
        fwdArrowClicked = false;
      }
    });

    rightArrow.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
          outputMessage = "r";
          try {
            DeviceConnection.btSocket.getOutputStream().write(outputMessage.getBytes());
          } catch (IOException e) {
            e.printStackTrace();
          }
          return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
          if (forward == true) {
            outputMessage = "f";

          } else  {
            outputMessage = "b";
          }
          try {
            DeviceConnection.btSocket.getOutputStream().write(outputMessage.getBytes());
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
        return true;
      }

    });


    leftArrow.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
          outputMessage = "l";
          try {
            DeviceConnection.btSocket.getOutputStream().write(outputMessage.getBytes());
          } catch (IOException e) {
            e.printStackTrace();
          }
          return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
          if (forward == true) {
            outputMessage = "f";

          } else  {
            outputMessage = "b";
          }
          try {
            DeviceConnection.btSocket.getOutputStream().write(outputMessage.getBytes());
          } catch (IOException e) {
            e.printStackTrace();
          }

        }
        return true;

      }
    });

    stopButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        outputMessage = "x";
        try {
          DeviceConnection.btSocket.getOutputStream().write(outputMessage.getBytes());
        } catch (IOException e) {
          e.printStackTrace();
        }
        connectionData("Sent: " + outputMessage);
        leftArrowClicked = true;
        fwdArrowClicked = false;
        bckwArrowClicked = false;
        rightArrowClicked = false;
      }
    });


    SeekBar speedControl = findViewById(R.id.seekBar2);
    speedControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        try{
        switch (progress){
          case 0:
            outputMessage = "x";
            break;
          case 1:
            outputMessage = "2";
            break;
          case 2:
            outputMessage = "4";
            break;
          case 3:
            outputMessage = "6";
            break;
          case 4:
            outputMessage = "8";
            break;
          case 5:
            outputMessage = "0";
            break;
        }
          DeviceConnection.btSocket.getOutputStream().write(outputMessage.getBytes());
        } catch (IOException e) {
          e.printStackTrace();
        }


        Log.i("Seekbar value",Integer.toString(progress));
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {

      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {

      }
    });
  }

  public void onDestroy(){
    super.onDestroy();
    outputMessage = "q";
    Log.e("BT output",outputMessage);
    try {
      DeviceConnection.btSocket.getOutputStream().write(outputMessage.getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void connectionData(final CharSequence text) {
    Log.e(LOG_TAG, text.toString());

  }


  // -------------- getters and setters for testing ---------------------
  public ImageView getFwdArrow(){
    return forwardArrow;
  }

  public ImageView getBckwArrow(){
    return backwardArrow;
  }

  public ImageView getLeftArrow(){
    return leftArrow;
  }

  public ImageView getRightArrow(){
    return rightArrow;
  }

  public boolean getFwdArrowClicked(){
    return fwdArrowClicked;
  }

  public boolean getBckwArrowClicked(){
    return bckwArrowClicked;
  }

  public boolean getLeftArrowClicked(){
    return leftArrowClicked;
  }

  public boolean getRightArrowClicked(){
    return rightArrowClicked;
  }
}