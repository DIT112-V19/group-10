package com.group10app;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.UUID;

public class DeviceControl extends AppCompatActivity {
  private static final String TAG = "BluetoothConnectionServ";
  private static final String NAME = "Car Mobile";

  private final BluetoothAdapter bluetoothAdapter;
  private ProgressDialog progressDialog;
  private Context context;

  private AcceptThread acceptThread;
  private ConnectThread connectThread;
  private ConnectedThread connectedThread;

  private static final UUID UUID_INSECURE =
    UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

  public DeviceControl() {
    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
  }

  public DeviceControl(Context context) {
    this.context = context;
    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
  }

  public class AcceptThread extends Thread {
    private final BluetoothServerSocket bluetoothServerSocket;

    AcceptThread() {
      BluetoothServerSocket socket = null;

      try {
        socket = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(NAME, UUID_INSECURE);
      } catch (IOException e) {
        Log.e(TAG, "Socket's listen() method failed" + e);
      }

      bluetoothServerSocket = socket;
    }

    public void run() {
      BluetoothSocket socket;

      while (true) {
        try {
          socket = bluetoothServerSocket.accept();
        } catch (IOException e) {
          Log.e(TAG, "Sockets's accept method failed" + e);
          break;
        }

        if (socket != null) {
          Connected(socket);

          try {
            bluetoothServerSocket.close();
          } catch (IOException e) {
            e.printStackTrace();
          }

          break;
        }

        Log.i(TAG, "END mAcceptThread ");
      }
    }

    public void cancel() {
      try {
        bluetoothServerSocket.close();
      } catch (IOException e) {
        Log.e(TAG, "Could not close the socket" + e);
      }
    }
  }

  private class ConnectThread extends Thread {
    private BluetoothSocket bluetoothSocket;
    private BluetoothDevice device;

    ConnectThread(BluetoothDevice device) {
      BluetoothSocket tmp = null;
      this.device = device;

      try {
        tmp = device.createRfcommSocketToServiceRecord(UUID_INSECURE);
      } catch (IOException e) {
        Log.e(TAG, "Socket's create() method failed", e);
      }

      bluetoothSocket = tmp;
    }

    public void run() {
      bluetoothAdapter.cancelDiscovery();

      try {
        bluetoothSocket.connect();
      } catch (IOException e) {

        try {
          bluetoothSocket.close();
        } catch (IOException closeException) {
          Log.e(TAG, "Could not close the client socket " + closeException);
        }

        return;
      }

      Connected(bluetoothSocket);
    }

    void cancel() {
      try {
        bluetoothSocket.close();
      } catch (IOException e) {
        Log.e(TAG, "Could not close the client socket " + e);
      }
    }
  }

  public synchronized void start(){
    Log.d(TAG, "start");

    if(connectThread != null) {
      connectThread.cancel();
      connectThread = null;
    }
    if(acceptThread == null) {
      acceptThread = new AcceptThread();
      acceptThread.start();
    }
  }

  public void startClient(BluetoothDevice device) {
    Log.d(TAG, "startClient: Started.");

    progressDialog = ProgressDialog.show(context,"Connecting Bluetooth"
      ,"Please Wait...",true);

    connectThread = new ConnectThread(device);
    connectThread.start();
  }

  private class ConnectedThread extends Thread {
    private final BluetoothSocket bluetoothSocket;
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private byte[] buffer;

    ConnectedThread(BluetoothSocket socket) {
      bluetoothSocket = socket;
      InputStream tmpIn = null;
      OutputStream tmpout = null;
      progressDialog.dismiss();

      try {
        tmpIn = bluetoothSocket.getInputStream();
      } catch (IOException e) {
        Log.e(TAG, "Error occurred when creating input stream", e);
      }

      try {
        tmpout = bluetoothSocket.getOutputStream();
      } catch (IOException e) {
        Log.e(TAG, "Error occurred when creating output stream", e);
      }

      inputStream = tmpIn;
      outputStream = tmpout;
    }

    public void run() {
      buffer = new byte[1024];
      int numberOfBytes; // bytes returned form read

      while (true) {
        try {
          numberOfBytes = inputStream.read(buffer);
          String incomingMessage = new String(buffer, 0, numberOfBytes);
          Log.d(TAG, "InputStream: " + incomingMessage);
        } catch (IOException e) {
          Log.e(TAG, "Input stream was disconnected", e);
          break;
        }
      }
    }

    void write(byte[] bytes) {
      String text = new String(bytes, Charset.defaultCharset());

      try {
        outputStream.write(bytes);
      } catch (IOException e) {
        Log.e(TAG, "write: Error writing to output stream " + e);
      }
    }

    public void cancel() {
      try {
        bluetoothSocket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void Connected(BluetoothSocket mmSocket) {
    Log.d(TAG, "connected: Starting");

    connectedThread = new ConnectedThread(mmSocket);
    connectedThread.start();
  }

  public void write(String msg) {
    byte[] bytes = msg.getBytes(Charset.defaultCharset());

    connectedThread.write(bytes);
  }
}
