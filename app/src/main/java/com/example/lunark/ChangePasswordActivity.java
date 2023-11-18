package com.example.lunark;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ChangePasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
    }

    public void onSaveClicked(View view){
        showPasswordUpdateDialog();
    }

    public void showPasswordUpdateDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("SUCCESS!")
                .setMessage("Your password has been updated.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openProfileScreen();
                    }
                }).show();
    }

    public void openProfileScreen() {
        Intent intent = new Intent(this, AccountScreen.class);
        startActivity(intent);
        finish();
    }
}