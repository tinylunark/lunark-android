package com.example.lunark.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.lunark.R;
import com.google.android.material.tabs.TabLayout;

public class HostReservationScreenFragment extends Fragment {

    private TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_host_reservation_screen, container, false);
        tabLayout = rootView.findViewById(R.id.tablayout);

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
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
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
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

        return rootView;
    }
}
