package com.group10app;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Service for managing connection and data communication with a GATT server hosted on a
 * given Bluetooth LE device.
 */
public class BluetoothService extends Service {
  private final static String TAG = BluetoothService.class.getSimpleName();

  private BluetoothManager bluetoothManager;
  private BluetoothAdapter bluetoothAdapter;
  private String deviceAddress;
  private BluetoothGatt bluetoothGatt;
  private int connectionState = STATE_DISCONNECTED;

  private static final int STATE_DISCONNECTED = 0;
  private static final int STATE_CONNECTING = 1;
  private static final int STATE_CONNECTED = 2;

  public final static String ACTION_GATT_CONNECTED =
      "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
  public final static String ACTION_GATT_DISCONNECTED =
      "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
  public final static String ACTION_GATT_SERVICES_DISCOVERED =
      "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
  public final static String ACTION_DATA_AVAILABLE =
      "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
  public final static String EXTRA_DATA =
      "com.example.bluetooth.le.EXTRA_DATA";

  public final static UUID UUID_HM_RX_TX =
      UUID.fromString(GattAttributes.HM_RX_TX);

  // callback methods for GATT events
  private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
      String intentAction;
      if (newState == BluetoothProfile.STATE_CONNECTED) {
        intentAction = ACTION_GATT_CONNECTED;
        connectionState = STATE_CONNECTED;
        broadcastUpdate(intentAction);
        Log.i(TAG, "Connected to GATT server.");
        // Attempts to discover services after successful connection.
        Log.i(TAG, "Attempting to start service discovery:" +
            bluetoothGatt.discoverServices());

      } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
        intentAction = ACTION_GATT_DISCONNECTED;
        connectionState = STATE_DISCONNECTED;
        Log.i(TAG, "Disconnected from GATT server.");
        broadcastUpdate(intentAction);
      }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
      if (status == BluetoothGatt.GATT_SUCCESS) {
        broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
      } else {
        Log.w(TAG, "onServicesDiscovered received: " + status);
      }
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt,
                                     BluetoothGattCharacteristic characteristic,
                                     int status) {
      if (status == BluetoothGatt.GATT_SUCCESS) {
        broadcastUpdate(characteristic);
      }
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt,
                                        BluetoothGattCharacteristic characteristic) {
      broadcastUpdate(characteristic);
    }
  };

  private void broadcastUpdate(final String action) {
    final Intent intent = new Intent(action);
    sendBroadcast(intent);
  }

  private void broadcastUpdate(final BluetoothGattCharacteristic characteristic) {
    final Intent intent = new Intent(BluetoothService.ACTION_DATA_AVAILABLE);

    // For all other profiles, writes the data formatted in HEX.
    final byte[] data = characteristic.getValue();
    Log.i(TAG, "data" + Arrays.toString(characteristic.getValue()));

    if (data != null && data.length > 0) {
      final StringBuilder stringBuilder = new StringBuilder(data.length);
      for(byte byteChar : data)
        stringBuilder.append(String.format("%02X ", byteChar));
      Log.d(TAG, String.format("%s", new String(data)));
      // getting cut off when longer, need to push on new line, 0A
      intent.putExtra(EXTRA_DATA,String.format("%s", new String(data)));

    }

    sendBroadcast(intent);
  }

  class LocalBinder extends Binder {
    BluetoothService getService() {
      return BluetoothService.this;
    }
  }

  @Override
  public IBinder onBind(Intent intent) {
    return binder;
  }

  @Override
  public boolean onUnbind(Intent intent) {
    // clean up resources
    close();
    return super.onUnbind(intent);
  }

  private final IBinder binder = new LocalBinder();

  /**
   * Initializes a reference to the local Bluetooth adapter.
   *
   * @return Return true if the initialization is successful.
   */
  public boolean initialize() {
    // For API level 18 and above, get a reference to BluetoothAdapter through
    // BluetoothManager.
    if (bluetoothManager == null) {
      bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
      if (bluetoothManager == null) {
        Log.e(TAG, "Unable to initialize BluetoothManager.");
        return false;
      }
    }

    bluetoothAdapter = bluetoothManager.getAdapter();
    if (bluetoothAdapter == null) {
      Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
      return false;
    }

    return true;
  }

  /**
   * Connects to the GATT server hosted on the Bluetooth LE device.
   *
   * @param address The device address of the destination device.
   *
   * @return Return true if the connection is initiated successfully.
   */
  public boolean connect(final String address) {
    if (bluetoothAdapter == null || address == null) {
      Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
      return false;
    }

    // Previously connected device. Try to reconnect.
    if (address.equals(deviceAddress)
        && bluetoothGatt != null) {
      Log.d(TAG, "Trying to use an existing bluetoothGatt for connection.");
      if (bluetoothGatt.connect()) {
        connectionState = STATE_CONNECTING;
        return true;
      } else {
        final BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
        bluetoothGatt = device.connectGatt(this, false, gattCallback);
        deviceAddress = address;
        return false;
      }
    }

    final BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
    if (device == null) {
      Log.w(TAG, "Device not found.  Unable to connect.");
      return false;
    }
    // we want to directly connect to the device, set autoConnect parameter to false.
    bluetoothGatt = device.connectGatt(this, false, gattCallback);
    Log.d(TAG, "Trying to create a new connection.");
    deviceAddress = address;
    connectionState = STATE_CONNECTING;
    return true;
  }

  /**
   * Disconnects an existing connection or cancel a pending connection.
   */
  public void disconnect() {
    if (bluetoothAdapter == null || bluetoothGatt == null) {
      Log.w(TAG, "BluetoothAdapter not initialized");
      return;
    }

    bluetoothGatt.disconnect();
  }

  /**
   * After using a given BLE device, the app must call this method to ensure resources are
   * released properly.
   */
  public void close() {
    if (bluetoothGatt == null) {
      return;
    }

    bluetoothGatt.close();
    bluetoothGatt = null;
  }

  /**
   * Request a read on a given BluetoothGattCharacteristic}.
   *
   * @param characteristic The characteristic to read from.
   */
  public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
    if (bluetoothAdapter == null || bluetoothGatt == null) {
      Log.w(TAG, "BluetoothAdapter not initialized");
      return;
    }

    bluetoothGatt.readCharacteristic(characteristic);
  }

  /**
   * Write to a given char
   *
   * @param characteristic The characteristic to write to
   */
  public void writeCharacteristic(BluetoothGattCharacteristic characteristic) {
    if (bluetoothAdapter == null || bluetoothGatt == null) {
      Log.w(TAG, "BluetoothAdapter not initialized");
      return;
    }

    bluetoothGatt.writeCharacteristic(characteristic);
  }

  /**
   * Enables or disables notification on a give characteristic.
   *
   * @param characteristic Characteristic to act on.
   * @param enabled If true, enable notification.  False otherwise.
   */
  public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                            boolean enabled) {
    if (bluetoothAdapter == null || bluetoothGatt == null) {
      Log.w(TAG, "BluetoothAdapter not initialized");
      return;
    }
    bluetoothGatt.setCharacteristicNotification(characteristic, enabled);

    // This is specific to Heart Rate Measurement.
    if (UUID_HM_RX_TX.equals(characteristic.getUuid())) {
      BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
          UUID.fromString(GattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
      descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
      bluetoothGatt.writeDescriptor(descriptor);
    }
  }

  /**
   * Retrieves a list of supported GATT services on the connected device.
   *
   * @return A List of supported services.
   */
  public List<BluetoothGattService> getSupportedGattServices() {
    if (bluetoothGatt == null) return null;

    return bluetoothGatt.getServices();
  }
}