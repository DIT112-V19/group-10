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

    Button forwardArrow;
    Button backwardArrow;
    Button rightArrow;
    Button leftArrow;
    final BluetoothGattCharacteristic BGC =null ;
    final BluetoothGatt gatt = null;
    final String LOG_TAG = ManualMode.class.getSimpleName();

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_mode);

        forwardArrow = (Button) findViewById(R.id.forwardArrow);
        backwardArrow = (Button) findViewById(R.id.backwardArrow);
        rightArrow = (Button) findViewById(R.id.rightArrow);
        leftArrow = (Button) findViewById(R.id.leftArrow);

        forwardArrow.setOnClickListener(new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onClick(View v) {
                String outputMessage = "/forward/";
                BGC.setValue(outputMessage.getBytes(Charset.forName("UTF-8")));
                if (gatt.writeCharacteristic(BGC)) {
                    connectionData("Sent: " + outputMessage);
                } else {
                    connectionData("unable to write BGC characteristic");
                }

            }
        });

        backwardArrow.setOnClickListener(new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onClick(View v) {
                String outputMessage = "/backward/";
                BGC.setValue((outputMessage.getBytes(Charset.forName("UTF-8"))));
                if(gatt.writeCharacteristic(BGC)) {
                    connectionData("Sent: "+ outputMessage);
                }else{
                    connectionData("unable to write BGC characteristic");
                }
            }
        });

        rightArrow.setOnClickListener(new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onClick(View v) {
                String outputMessage = "/right/";
                BGC.setValue(outputMessage.getBytes(Charset.forName("UTF-8")));
                if(gatt.writeCharacteristic(BGC)) {
                    connectionData("Sent: " + outputMessage);
                }else{
                    connectionData("unable to write BGC characteristic");
                }
            }
        });

        leftArrow.setOnClickListener(new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onClick(View v) {
                String outputMessage = "/left/";
                BGC.setValue(outputMessage.getBytes(Charset.forName("UTF-8")));
                if(gatt.writeCharacteristic(BGC)) {
                    connectionData("Sent: "+ outputMessage);
                }else{
                    connectionData("unable to write BGC characteristic");
                }
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
}
