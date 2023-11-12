package com.example.lunark;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        int SPLASH_SCREEN_TIMEOUT = 5000;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, LoginScreenActivity.class);
                startActivity(intent);

                finish();
            }
        }, SPLASH_SCREEN_TIMEOUT);
    }
}

