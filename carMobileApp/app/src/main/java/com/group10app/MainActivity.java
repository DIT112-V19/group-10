package com.group10app;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

  Dialog dialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    new DeviceConnection(this, getIntent()).execute();
    dialog = new Dialog(this);
  }

  public void openDeviceScan(View view) {
    startActivity(new Intent(this, DeviceList.class));
  }

  public void openAutoMode(View view) {
    startActivity(new Intent(this, AutonomousMode.class));
    String outputMessage = "b";

    try {
      DeviceConnection.btSocket.getOutputStream().write(outputMessage.getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void openManualMode(View view) {
    startActivity(new Intent(this, ManualMode.class));
    String outputMessage = "a";

    try {
      DeviceConnection.btSocket.getOutputStream().write(outputMessage.getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  //Links to github repository
  public void openRepo(View view) {
    String gitUrl = getResources().getString(R.string.githubUrl);
    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(gitUrl));
    startActivity(browserIntent);
  }
  // Opens instructions
  public void openInstructions(View view) {

    TextView close;
    dialog.setContentView(R.layout.instructions);
    close = (TextView) dialog.findViewById(R.id.close);
    close.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
      }
    });
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    dialog.show();

  }

  // Opens credits
  public void openCredits(View view) {

    TextView close;
    dialog.setContentView(R.layout.credits);
    close = (TextView) dialog.findViewById(R.id.close);
    close.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
      }
    });
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    dialog.show();

  }
}