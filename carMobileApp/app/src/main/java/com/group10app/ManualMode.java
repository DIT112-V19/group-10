package com.group10app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.nio.charset.Charset;

public class ManualMode extends AppCompatActivity {

  private ImageView forwardArrow;
  private ImageView backwardArrow;
  private ImageView rightArrow;
  private ImageView leftArrow;

  private final String LOG_TAG = ManualMode.class.getSimpleName();
  private DeviceControl deviceControl;
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

    deviceControl = new DeviceControl(this);

    forwardArrow.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        String outputMessage = "f";
        deviceControl.write(outputMessage);
        connectionData("Sent: " + outputMessage);
        fwdArrowClicked = true;
        bckwArrowClicked = false;
        rightArrowClicked = false;
        leftArrowClicked = false;
      }
    });

    backwardArrow.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        String outputMessage = "b";
        deviceControl.write(outputMessage);
        connectionData("Sent: " + outputMessage);
        bckwArrowClicked = true;
        rightArrowClicked = false;
        leftArrowClicked = false;
        fwdArrowClicked = false;
      }
    });

    rightArrow.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        String outputMessage = "r";
        deviceControl.write(outputMessage);
        connectionData("Sent: " + outputMessage);
        rightArrowClicked = true;
        leftArrowClicked = false;
        fwdArrowClicked = false;
        bckwArrowClicked = false;
      }
    });

    leftArrow.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        String outputMessage = "l";
        deviceControl.write(outputMessage);
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