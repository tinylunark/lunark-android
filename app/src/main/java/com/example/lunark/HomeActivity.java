package com.example.lunark;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.lunark.activities.PropertyActivity;
import com.example.lunark.adapters.PropertyListAdapter;
import com.example.lunark.databinding.ActivityHomeBinding;
import com.example.lunark.models.Property;
import com.google.android.material.navigation.NavigationView;

import java.util.HashSet;
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
            FilterDialogFragment filterDialog = new FilterDialogFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            filterDialog.show(fragmentManager, "filter_dialog");
                }
        );

        propertyListView = binding.activityHomeBase.list;
        propertyListView.setAdapter(propertyListAdapter);
    }
    @Override
    public boolean onSupportNavigateUp() {
        navController = Navigation.findNavController(this, R.id.fragment_nav_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();

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
}
