package com.group10app;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import java.util.ArrayList;
import java.util.Set;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DeviceList extends AppCompatActivity {

  private static final int REQUEST_ENABLE_BT = 1;
  private TextView btStatusText;
  private ListView listView;
  private BluetoothAdapter bluetoothAdapter;

  private BluetoothSocket btSocket = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.bluetooth_connection);

    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    btStatusText = findViewById(R.id.status_text);
    Button turnBTOnButton = findViewById(R.id.turnOn);
    Button turnBTOffButton = findViewById(R.id.turnOff);
    Button listPairedDevicesButton = findViewById(R.id.paired);
    listView = findViewById(R.id.listView);

    if (bluetoothAdapter == null) {
      turnBTOnButton.setEnabled(false);
      turnBTOffButton.setEnabled(false);
      listPairedDevicesButton.setEnabled(false);
      btStatusText.setText("BT Status: Not Supported");

      Toast.makeText(getApplicationContext(), "Device does not support Bluetooth",
          Toast.LENGTH_LONG).show();

    } else {
      if (!bluetoothAdapter.isEnabled()) btStatusText.setText("Status: BT Off");
      else btStatusText.setText("BT Status: On");


      turnBTOnButton.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
          turnOnBT(view);
        }
      });

      turnBTOffButton.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          turnOffBT(v);
        }
      });

      listPairedDevicesButton.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          getPairedDevices();
        }
      });
    }
  }

  public void turnOnBT(View view) {
    if (!bluetoothAdapter.isEnabled()) {
      Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
      startActivityForResult(intent, REQUEST_ENABLE_BT);

      Toast.makeText(getApplicationContext(), "Bluetooth turned On", Toast.LENGTH_SHORT).show();
    } else {
      Toast.makeText(getApplicationContext(), "Bluetooth is already On",
          Toast.LENGTH_SHORT).show();
    }
  }

  public void turnOffBT(View view) {
    if (!bluetoothAdapter.isEnabled()) {
      Toast.makeText(getApplicationContext(), "Bluetooth is already Off", Toast.LENGTH_SHORT).show();
    } else {
      bluetoothAdapter.disable();
      btStatusText.setText("BT Status: Off");

      Toast.makeText(getApplicationContext(), "Bluetooth turned Off", Toast.LENGTH_SHORT).show();
    }
  }

  private void getPairedDevices() {
    Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
    ArrayList<String> list = new ArrayList<>();

    if (pairedDevices.size() > 0) {

      for(BluetoothDevice device : pairedDevices) {
        list.add(device.getName() + "\n" + device.getAddress());
      }
    }
    else {
      Toast.makeText(getApplicationContext(), "No Paired Devices Found.", Toast.LENGTH_SHORT).show();
    }

    final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, list);
    listView.setAdapter(adapter);
    listView.setOnItemClickListener(onItemClickListener);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    if (requestCode == REQUEST_ENABLE_BT) {
      if(bluetoothAdapter.isEnabled()) {
        btStatusText.setText("BT Status: On");
      }
    }
  }

  private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      String info = ((TextView) view).getText().toString();
      String name = info.substring(0, info.length() - 17);
      String address = info.substring(info.length() - 17);

      Intent i = new Intent(DeviceList.this, MainActivity.class);

      i.putExtra("EXTRA_NAME", name);
      i.putExtra("EXTRA_ADDRESS", address);
      startActivity(i);
    }
  };
}