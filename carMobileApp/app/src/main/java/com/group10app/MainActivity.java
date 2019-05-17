package com.group10app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
 DeviceControl deviceControl;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    new DeviceConnection(this, getIntent()).execute();
    deviceControl = new DeviceControl(this);
  }

  public void openDeviceScan(View view) {
    startActivity(new Intent(this, DeviceList.class));
  }

  public void openAutoMode(View view) {
    startActivity(new Intent(this, AutonomousMode.class));
  }

  public void openManualMode(View view) {
    startActivity(new Intent(this, ManualMode.class));
    String msg = "a";
    deviceControl.write(msg);
  }


  //Links to github repository
  public void openRepo(View view) {
    String gitUrl = getResources().getString(R.string.githubUrl);
    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(gitUrl));
    startActivity(browserIntent);
  }
}