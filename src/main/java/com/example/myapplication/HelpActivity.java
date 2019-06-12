package com.example.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity
{
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" Text Recognition App");
        TextView txt = findViewById(R.id.textView);
        txt.setText("Press REAILTIME to run app in real time\nAllow access to camera\nIf you want to stop press button stop\nIf you want to continue press button again\nPress PHOTO to run app and use photo for detection\nIf you want to load new image press appropriate button\nEnjoy");
    }
}
