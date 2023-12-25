package com.example.lunark;

import android.app.ActionBar;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.lunark.activities.PropertyActivity;
import com.example.lunark.adapters.PropertyListAdapter;
import com.example.lunark.databinding.ActivityHomeBinding;
import com.example.lunark.fragments.FiltersDialogFragment;
import com.example.lunark.models.Property;
import com.google.android.material.navigation.NavigationView;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private NavController navController;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Set<Integer> topLevelDestinations = new HashSet<>();
    private ListView propertyListView;
    private PropertyListAdapter propertyListAdapter = new PropertyListAdapter(HomeActivity.this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        drawer = binding.drawerLayout;
        navigationView = binding.navView;
        toolbar = binding.activityHomeBase.toolbar;

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        actionBar = getActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_hamburger);
            actionBar.setHomeButtonEnabled(true);
        }

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        findViewById(R.id.filterButton).setOnClickListener(v -> {
                new FiltersDialogFragment().show(
                        getSupportFragmentManager(), FiltersDialogFragment.TAG
                );
            }
        );

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.menu_home) {
                    if (!isActivityRunning(HomeActivity.class)) {
                        Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                } else if (itemId == R.id.menu_account) {

                    if (!isActivityRunning(AccountScreen.class)) {
                        Intent intent = new Intent(HomeActivity.this, AccountScreen.class);
                        startActivity(intent);
                    }

                } else if (itemId == R.id.menu_reservations) {
                    Toast.makeText(HomeActivity.this, "Screen not implemented", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.menu_notifications) {
                    Toast.makeText(HomeActivity.this, "Screen not implemented", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.menu_logout) {
                        if (!isActivityRunning(SignUpScreenActivity.class)) {
                            Intent intent = new Intent(HomeActivity.this, LoginScreenActivity.class);
                            startActivity(intent);
                        }
                }

                drawer.closeDrawers();
                return true;
            }
        });



        propertyListView = binding.activityHomeBase.list;
        propertyListView.setAdapter(propertyListAdapter);

        propertyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Property selectedProperty = (Property) parent.getItemAtPosition(position);

                Intent intent = new Intent(HomeActivity.this, PropertyActivity.class);
                intent.putExtra("name", selectedProperty.getName());
                intent.putExtra("rating", selectedProperty.getAverageRating());
                intent.putExtra("location", selectedProperty.getLocation());
                intent.putExtra("description", selectedProperty.getDescription());
                intent.putExtra("thumbnail", selectedProperty.getThumbnailId());

                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        navController = Navigation.findNavController(this, R.id.fragment_nav_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    private boolean isActivityRunning(Class<?> activityClass) {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            assert task.baseActivity != null;
            if (Objects.equals(activityClass.getCanonicalName(), task.baseActivity.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
