package com.group10app;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Activity for scanning and displaying available Bluetooth LE devices.
 */
public class DeviceScanActivity extends ListActivity {

  int MY_PERMISSIONS_REQUEST_LOCATION = 0;
  private DeviceListAdapter deviceListAdapter;
  private BluetoothAdapter bluetoothAdapter;
  private boolean scanning;
  private Handler handler;
  private static final int REQUEST_ENABLE_BT = 1;
  // Stops scanning after 10 seconds.
  private static final long SCAN_PERIOD = 20000;

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if(getActionBar() != null){
      getActionBar().setTitle(R.string.title_devices);
      ActionBar bar = getActionBar();
      bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3F51B5")));
    }

    handler = new Handler();
    Window window = getWindow();
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
    window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));

    // Ask for permission at runtime
    // https://stackoverflow.com/questions/32708374/bluetooth-le-scan-doesnt-work-on-android-m-in-the-background
    if (ContextCompat.checkSelfPermission(this,
        Manifest.permission.READ_CONTACTS)
        != PackageManager.PERMISSION_GRANTED) {

      if (ActivityCompat.shouldShowRequestPermissionRationale(this,
          Manifest.permission.READ_CONTACTS)) {
      } else {
        ActivityCompat.requestPermissions(this,
            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
            MY_PERMISSIONS_REQUEST_LOCATION);
      }
    }

    // check whether BLE is supported or not
    if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
      Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
      finish();
    }

    // Initializes a Bluetooth adapter
    final BluetoothManager bluetoothManager =
        (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
    bluetoothAdapter = bluetoothManager.getAdapter();

    // Checks if Bluetooth is supported on the device.
    if (bluetoothAdapter == null) {
      Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
      finish();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    if (!scanning) {
      menu.findItem(R.id.menu_stop).setVisible(false);
      menu.findItem(R.id.menu_scan).setVisible(true);
      menu.findItem(R.id.menu_refresh).setActionView(null);
    } else {
      menu.findItem(R.id.menu_stop).setVisible(true);
      menu.findItem(R.id.menu_scan).setVisible(false);
      menu.findItem(R.id.menu_refresh).setActionView(
          R.layout.in_progress_bar);
    }
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_scan:
        deviceListAdapter.clear();
        scanLeDevice(true);
        break;
      case R.id.menu_stop:
        scanLeDevice(false);
        break;
    }
    return true;
  }

  @Override
  protected void onResume() {
    super.onResume();
    // Ensures Bluetooth is enabled on the device.
    if (!bluetoothAdapter.isEnabled()) {
      if (!bluetoothAdapter.isEnabled()) {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
      }
    }

    // Initializes list view adapter.
    deviceListAdapter = new DeviceListAdapter(this);
    setListAdapter(deviceListAdapter);
    scanLeDevice(true);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    // User chose not to enable Bluetooth.
    if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
      finish();
      return;
    }

    super.onActivityResult(requestCode, resultCode, data);
  }

  @Override
  protected void onPause() {
    super.onPause();
    scanLeDevice(false);
    deviceListAdapter.clear();
  }

  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    final BluetoothDevice device = deviceListAdapter.getDevice(position);
    if (device == null) return;

    final Intent intent = new Intent(this, DeviceControlActivity.class);
    intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, device.getName());
    intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, device.getAddress());

    if (scanning) {
      bluetoothAdapter.stopLeScan(mLeScanCallback);
      scanning = false;
    }

    startActivity(intent);
  }

  private void scanLeDevice(final boolean enable) {
    if (enable) {
      // Stops scanning after a pre-defined scan period.
      handler.postDelayed(new Runnable() {
        @Override
        public void run() {
          scanning = false;
          bluetoothAdapter.stopLeScan(mLeScanCallback);
          invalidateOptionsMenu();
        }
      }, SCAN_PERIOD);
      scanning = true;
      bluetoothAdapter.startLeScan(mLeScanCallback);
    } else {
      scanning = false;
      bluetoothAdapter.stopLeScan(mLeScanCallback);
    }

    invalidateOptionsMenu();
  }

  // Device scan callback.
  private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
    @Override
    public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
      runOnUiThread(new Runnable() {
        @Override
        public void run() {
          deviceListAdapter.addDevice(device);
          deviceListAdapter.notifyDataSetChanged();
        }
      });
    }
  };
}