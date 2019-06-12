package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button butSt = findViewById(R.id.buttSt);
        Button butHelp = findViewById(R.id.buttH);
        Button butPh=  findViewById(R.id.buttPh);
        getSupportActionBar().setTitle(" Text Recognition App");
        butHelp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent doJumpH = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(doJumpH);
            }
        });
        butSt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent doJumpS = new Intent(MainActivity.this, StartActivityB.class);
                startActivity(doJumpS);
            }
        });
        butPh.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent doJumpH = new Intent(MainActivity.this, StartActivityA.class);
                startActivity(doJumpH);
            }
        });
    }

}
