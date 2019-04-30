package com.group10app;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import java.nio.charset.Charset;

public class ManualMode extends AppCompatActivity {

    ImageView forwardArrow;
    ImageView backwardArrow;
    ImageView rightArrow;
    ImageView leftArrow;
    //final BluetoothGattCharacteristic BGC =null ;
    //final BluetoothGatt gatt = null;
    final String LOG_TAG = ManualMode.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_mode);

        forwardArrow = (ImageView) findViewById(R.id.forwardArrow);
        backwardArrow = (ImageView) findViewById(R.id.backwardArrow);
        rightArrow = (ImageView) findViewById(R.id.rightArrow);
        leftArrow = (ImageView) findViewById(R.id.leftArrow);

        forwardArrow.setOnClickListener(new OnClickListener() {
            //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onClick(View v) {
                String outputMessage = "/forward/";
                //BGC_value(outputMessage);
                connectionData("Sent: " + outputMessage);


            }
        });

        backwardArrow.setOnClickListener(new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onClick(View v) {
                String outputMessage = "/backward/";
                //BGC_value(outputMessage);
                connectionData("Sent: " + outputMessage);
            }
        });

        rightArrow.setOnClickListener(new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onClick(View v) {
                String outputMessage = "/right/";
                //BGC_value(outputMessage);
                connectionData("Sent: " + outputMessage);
            }
        });

        leftArrow.setOnClickListener(new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onClick(View v) {
                String outputMessage = "/left/";
                //BGC_value(outputMessage);
                connectionData("Sent: " + outputMessage);
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
    /*private void BGC_value (String outputMessage){
        BGC.setValue(outputMessage.getBytes(Charset.forName("UTF-8")));
        if(gatt.writeCharacteristic(BGC)) {
            connectionData("Sent: " + outputMessage);
        }else{
            connectionData("Unable to write BGC characteristic");
        }
    }*/

}