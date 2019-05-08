package com.group10app;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * For a given BLE device, this Activity provides the user interface to connect, display data,
 * and display GATT services and characteristics supported by the device.
 */
public class DeviceControlActivity extends Activity {
  private final static String TAG = DeviceControlActivity.class.getSimpleName();

  public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
  public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
  private TextView isSerial;
  private TextView connectionState;
  private ToggleButton bluetoothConnectButton;
  private String deviceName;
  private String deviceAddress;
  private BluetoothService bluetoothService;
  private boolean connected = false;
  private BluetoothGattCharacteristic characteristicTX;
  private BluetoothGattCharacteristic characteristicRX;

  public final static UUID HM_RX_TX =
      UUID.fromString(GattAttributes.HM_RX_TX);

  private final String LIST_NAME = "NAME";
  private final String LIST_UUID = "UUID";

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.gatt_services_characteristics);

    if(getActionBar() != null){
      ActionBar bar = getActionBar();
      bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3F51B5")));
      getActionBar().setTitle(deviceName);
      getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    Window window = getWindow();
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
    window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));

    final Intent intent = getIntent();
    deviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
    deviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

    // Sets up UI references.
    ((TextView) findViewById(R.id.device_address)).setText(deviceAddress);
    connectionState = findViewById(R.id.connection_state);
    // is serial present?
    isSerial = findViewById(R.id.isSerial);

    Intent gattServiceIntent = new Intent(this, BluetoothService.class);
    bindService(gattServiceIntent, serviceConnection, BIND_AUTO_CREATE);

    // todo this is not correct
    bluetoothConnectButton = findViewById(R.id.bluetooth_connect_button);
    bluetoothConnectButton.setChecked(true);
    bluetoothConnectButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
          if (connected) {
            Log.d("Bluetooth connection", "already connected");
          } else {
            bluetoothService.connect(deviceAddress);
            Log.d("Bluetooth connection", "connected to new device");
          }
        } else {
          bluetoothService.disconnect();
          bluetoothConnectButton.setChecked(false);
        }
      }
    });
  }

  // Code to manage Service lifecycle.
  private final ServiceConnection serviceConnection = new ServiceConnection() {

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder service) {
      bluetoothService = ((BluetoothService.LocalBinder) service).getService();
      if (!bluetoothService.initialize()) {
        Log.e(TAG, "Unable to initialize Bluetooth");
        finish();
      }

      // Automatically connects to the device upon successful start-up initialization.
      bluetoothService.connect(deviceAddress);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
      bluetoothService = null;
    }
  };

  // Handles various events fired by the Service.
  // ACTION_GATT_CONNECTED: connected to a GATT server.
  // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
  // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
  // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
  //                        or notification operations.
  private final BroadcastReceiver gattUpdateReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      final String action = intent.getAction();
      if (BluetoothService.ACTION_GATT_CONNECTED.equals(action)) {
        connected = true;
        updateConnectionState(R.string.connected);
        invalidateOptionsMenu();
      } else if (BluetoothService.ACTION_GATT_DISCONNECTED.equals(action)) {
        connected = false;
        updateConnectionState(R.string.disconnected);
        invalidateOptionsMenu();
      } else if (BluetoothService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
        // Show all the supported services and characteristics on the user interface.
        displayGattServices(bluetoothService.getSupportedGattServices());
      }
    }
  };

  @Override
  protected void onResume() {
    super.onResume();
    registerReceiver(gattUpdateReceiver, makeGattUpdateIntentFilter());
    if (bluetoothService != null) {
      final boolean result = bluetoothService.connect(deviceAddress);
      Log.d(TAG, "Connect request result = " + result);
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
    unregisterReceiver(gattUpdateReceiver);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    unbindService(serviceConnection);
    bluetoothService = null;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.gatt_services, menu);
    if (connected) {
      menu.findItem(R.id.menu_connect).setVisible(false);
      menu.findItem(R.id.menu_disconnect).setVisible(true);
    } else {
      menu.findItem(R.id.menu_connect).setVisible(true);
      menu.findItem(R.id.menu_disconnect).setVisible(false);
    }
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_connect:
        bluetoothService.connect(deviceAddress);
        return true;
      case R.id.menu_disconnect:
        bluetoothService.disconnect();
        return true;
      case android.R.id.home:
        onBackPressed();
        return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private void updateConnectionState(final int resourceId) {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        connectionState.setText(resourceId);
      }
    });
  }

  private void displayGattServices(List<BluetoothGattService> gattServices) {
    if (gattServices == null) return;
    String uuid;
    String unknownServiceString = getResources().getString(R.string.unknown_service);
    ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<>();

    // Loops through available GATT Services.
    for (BluetoothGattService gattService : gattServices) {
      HashMap<String, String> currentServiceData = new HashMap<>();
      uuid = gattService.getUuid().toString();
      currentServiceData.put(
          LIST_NAME, GattAttributes.lookup(uuid, unknownServiceString));

      // If the service exists for HM 10 Serial, say so.
      if (GattAttributes.lookup(uuid, unknownServiceString).equals("HM 10 Serial")) {
        isSerial.setText("Yes");
      } else {
        isSerial.setText("No");
      }

      currentServiceData.put(LIST_UUID, uuid);
      gattServiceData.add(currentServiceData);

      // get characteristic when UUID matches RX/TX UUID
      characteristicTX = gattService.getCharacteristic(BluetoothService.UUID_HM_RX_TX);
      characteristicRX = gattService.getCharacteristic(BluetoothService.UUID_HM_RX_TX);
    }
  }

  private static IntentFilter makeGattUpdateIntentFilter() {
    final IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(BluetoothService.ACTION_GATT_CONNECTED);
    intentFilter.addAction(BluetoothService.ACTION_GATT_DISCONNECTED);
    intentFilter.addAction(BluetoothService.ACTION_GATT_SERVICES_DISCOVERED);
    intentFilter.addAction(BluetoothService.ACTION_DATA_AVAILABLE);
    return intentFilter;
  }
}