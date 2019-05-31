package com.group10app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class DeviceConnection extends AsyncTask<Void, Void, Void> {

  private ProgressDialog progress;
  private boolean isConnected;
  private Context context;
  private String deviceName;
  private String macAddress;

  protected static BluetoothSocket btSocket;

  private static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

  public DeviceConnection(Context context, Intent intent) {
    this.context = context;
    this.isConnected = true;

    deviceName = intent.getStringExtra("EXTRA_NAME");
    macAddress = intent.getStringExtra("EXTRA_ADDRESS");
  }

  @Override
  protected void onPreExecute() {
    progress = ProgressDialog.show(context, "Connecting...", "Please wait!!!");
  }

  @Override
  protected Void doInBackground(Void... devices) {
    try {

      if (btSocket == null || !isConnected) {
        BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice bluetoothDevice = bluetooth.getRemoteDevice(macAddress);
        btSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(myUUID);
        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
        btSocket.connect();
      }

      ((Activity)context).runOnUiThread(new Runnable() {
        @Override
        public void run() {
          TextView name = ((Activity)context).findViewById(R.id.device_name);
          name.setText(deviceName);
          TextView address = ((Activity)context).findViewById(R.id.device_address);
          address.setText(macAddress);
        }
      });
    }
    catch (IOException e) {
      isConnected = false;
    }

    return null;
  }

  @Override
  protected void onPostExecute(Void result) {
    super.onPostExecute(result);

    if (!isConnected) {
      msg("DeviceConnection Failed. Is it a SPP Bluetooth? Try again.");
      ((Activity) context).finish();
    } else {
      msg("Connected.");
      isConnected = true;
    }

    progress.dismiss();
  }

  private void msg(String s) {
    Toast.makeText(context.getApplicationContext(), s, Toast.LENGTH_LONG).show();
  }
}
