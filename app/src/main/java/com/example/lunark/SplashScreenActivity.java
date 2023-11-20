package com.example.lunark;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.example.lunark.tools.CheckConnectionTools;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends Activity {
    private static final int SPLASH_SCREEN_TIMEOUT = 5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        if(!isConnected()) {
            Toast.makeText(SplashScreenActivity.this, R.string.not_connected_to_the_internet, Toast.LENGTH_SHORT).show();
            showConnectionDialog();
        } else {
            openLoginScreen();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(!isConnected()){
            Toast.makeText(this, R.string.failed_to_connect_to_the_internet, Toast.LENGTH_SHORT).show();
            this.finish();
        } else {
            openLoginScreen();
        }
    }

    private void openLoginScreen() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, LoginScreenActivity.class);
                startActivity(intent);

                finish();
            }
        }, SPLASH_SCREEN_TIMEOUT);
    }

    public boolean isConnected() {
        return CheckConnectionTools.getConnectivityStatus(this.getApplicationContext()) != CheckConnectionTools.TYPE_NOT_CONNECTED;
    }

    private void showConnectionDialog()
    {
        int LAUNCH_SECOND_ACTIVITY = 1;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.connect_to_wifi_or_mobile_data_or_quit)
                .setCancelable(false)
                .setPositiveButton(R.string.connect_to_wifi, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), LAUNCH_SECOND_ACTIVITY);
                    }
                }).setNegativeButton(R.string.connect_to_mobile_data, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityForResult(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS), LAUNCH_SECOND_ACTIVITY);
                    }
                })
                .setNeutralButton(R.string.quit, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SplashScreenActivity.this.finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}

