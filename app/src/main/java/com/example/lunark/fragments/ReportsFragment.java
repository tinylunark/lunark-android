package com.example.lunark.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lunark.R;
import com.example.lunark.adapters.ReportsPagerAdapter;
import com.example.lunark.databinding.FragmentReportsBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ReportsFragment extends Fragment {
    FragmentReportsBinding mBinding;
    ReportsPagerAdapter mReportsPagerAdapter;
    ViewPager2 mViewPager;
    TabLayout mTabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentReportsBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mReportsPagerAdapter = new ReportsPagerAdapter(getChildFragmentManager(), getLifecycle());
        mViewPager = mBinding.pager;
        mViewPager.setAdapter(mReportsPagerAdapter);
        mTabLayout = mBinding.tabLayout;
        new TabLayoutMediator(mTabLayout, mViewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("General");
                    break;
                case 1:
                    tab.setText("Property");
                    break;
            }
        }).attach();
    }
}