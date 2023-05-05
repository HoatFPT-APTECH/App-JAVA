package com.hoatapp.myqr.qrscanner.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.hoatapp.myqr.R;


public class LaunchScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_screen);

        int secondsDelayed = 3;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(LaunchScreenActivity.this, MainQRSCanerActivity.class));
                finish();
            }
        }, secondsDelayed * 1000);

    }
}
