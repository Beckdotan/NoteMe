package com.example.noteme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {
    //handler for splashscreen
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //cosmetics for splash screen
        getSupportActionBar().hide();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //change the second element todecide to which windows it goest to. (currently its on MapActivity).
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
                //the finish function making sure that if you press on the beck button of android you will not go back to the splash screen.
                finish();
            }
        }, 3000);
    }
}