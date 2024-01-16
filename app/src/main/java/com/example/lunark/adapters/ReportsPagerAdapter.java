package com.example.lunark.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.lunark.fragments.GeneralReportFragment;

public class ReportsPagerAdapter extends FragmentStateAdapter {

    public ReportsPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new GeneralReportFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }


}
