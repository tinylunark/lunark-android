package com.example.lunark.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lunark.R;
import com.example.lunark.databinding.FragmentPropertyReportBinding;

public class PropertyReportFragment extends Fragment {
    FragmentPropertyReportBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentPropertyReportBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();
        return view;
    }
}