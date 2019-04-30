package com.group10app;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void openAutoMode(View view) {
        Intent intent = new Intent(this, AutonomousMode.class);
        startActivity(intent);
    }


    public void openRepo(View view) {
        String gitUrl = getResources().getString(R.string.githubUrl);
        Intent browserIntent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(gitUrl));
            startActivity(browserIntent);
    }

}
