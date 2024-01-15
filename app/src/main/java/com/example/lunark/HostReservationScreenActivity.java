package com.example.lunark;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.lunark.databinding.ActivityHostReservationScreenBinding;
import com.example.lunark.fragments.AllReservationsFragment;
import com.example.lunark.fragments.*;
import com.google.android.material.tabs.TabLayout;

public class HostReservationScreenActivity extends AppCompatActivity {

    private ActivityHostReservationScreenBinding binding;
    FrameLayout frameLayout;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_reservation_screen);
        binding = ActivityHostReservationScreenBinding.inflate(getLayoutInflater());
        frameLayout = (FrameLayout) findViewById(R.id.all_res_frame_lay);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right
        );
        transaction.replace(R.id.all_res_frame_lay, new PendingReservationsFragment());
        transaction.commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch(tab.getPosition()) {
                    case 0:
                        fragment = new PendingReservationsFragment();
                        break;
                    case 1:
                        fragment = new AllReservationsFragment();
                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(
                        android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right
                );
                transaction.addToBackStack(null);
                transaction.replace(R.id.all_res_frame_lay, fragment);
                transaction.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}