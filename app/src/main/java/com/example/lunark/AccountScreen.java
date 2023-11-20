package com.example.lunark;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class AccountScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.menu_home) {
                    if (!isCurrentActivity(HomeActivity.class)) {
                        startActivity(new Intent(AccountScreen.this, HomeActivity.class));
                    }
                } else if (itemId == R.id.menu_account) {
                    if (!isCurrentActivity(AccountScreen.class)) {
                        startActivity(new Intent(AccountScreen.this, AccountScreen.class));
                    }
                } else if (itemId == R.id.menu_reservations || itemId == R.id.menu_notifications) {
                    Toast.makeText(AccountScreen.this, "Screen not implemented", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.menu_logout) {
                    if (!isCurrentActivity(LoginScreenActivity.class)) {
                        Intent intent = new Intent(AccountScreen.this, LoginScreenActivity.class);
                        startActivity(intent);
                    }
                }

                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    public void onChangePasswordClicked(View view) {
        startActivity(new Intent(this, ChangePasswordActivity.class));
    }

    public void onSaveClicked(View view) {
        new AlertDialog.Builder(this)
                .setTitle("SUCCESS!")
                .setMessage("Your profile has been updated.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openHomeScreen();
                    }
                }).show();
    }

    public void onDeleteClicked(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Profile")
                .setMessage("Are you sure you want to delete your profile?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        redirectToLoginScreen();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void redirectToLoginScreen() {
        startActivity(new Intent(this, LoginScreenActivity.class));
        finish();
    }

    public void openHomeScreen() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    private boolean isCurrentActivity(Class<?> activityClass) {
        return activityClass.isInstance(this);
    }
}
