package com.group10app;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DeviceListAdapter extends BaseAdapter {
  private Activity context;
  private ArrayList<BluetoothDevice> devices;

  public DeviceListAdapter(Activity context) {
    super();
    this.context = context;
    devices = new ArrayList<>();
    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  public void addDevice(BluetoothDevice device) {
    if(!devices.contains(device)) {
      devices.add(device);
    }
  }

  public BluetoothDevice getDevice(int position) {
    return devices.get(position);
  }

  public void clear() {
    devices.clear();
  }

  @Override
  public int getCount() {
    return devices.size();
  }

  @Override
  public Object getItem(int i) {
    return devices.get(i);
  }

  @Override
  public long getItemId(int i) {
    return i;
  }

  @Override
  public View getView(int index, View view, ViewGroup parent) {
    View v = view;
    ViewHolder viewHolder;
    if (view == null) {
      LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      v = li.inflate(R.layout.listitem_device, null);
      viewHolder = new ViewHolder();
      viewHolder.deviceName =  v.findViewById(R.id.device_name);
      viewHolder.deviceAddress =  v.findViewById(R.id.device_address);
      v.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) v.getTag();
    }

    BluetoothDevice device = devices.get(index);
    final String deviceName = device.getName();

    if (deviceName != null && deviceName.length() > 0) {
      viewHolder.deviceName.setText(deviceName);
    }
    else {
      viewHolder.deviceName.setText(R.string.unknown_device);
    }

    viewHolder.deviceAddress.setText(device.getAddress());
    return v;
  }
}

class ViewHolder {
  TextView deviceName;
  TextView deviceAddress;
}