package com.example.lunark.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lunark.LunarkApplication;
import com.example.lunark.R;
import com.example.lunark.databinding.FragmentPropertyReportBinding;
import com.example.lunark.repositories.ReportRepository;

import javax.inject.Inject;

public class PropertyReportFragment extends Fragment {
    private static final String TAG = "PropertyReportFragment";
    FragmentPropertyReportBinding mBinding;
    @Inject
    public ReportRepository mReportRepository;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ((LunarkApplication) getActivity().getApplication()).applicationComponent.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentPropertyReportBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mReportRepository.getPropertyReport("2024", "4").observe(getViewLifecycleOwner(), report -> {
            Log.d(TAG, "onViewCreated: " + report);
        });
    }
}