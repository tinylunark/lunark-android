package com.example.lunark.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lunark.R;
import com.example.lunark.databinding.FragmentBookingRequestBinding;

public class BookingRequestFragment extends Fragment {
    public static final String TAG = "BookingRequestFragment";
    FragmentBookingRequestBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         mBinding = FragmentBookingRequestBinding.inflate(inflater, container, false);
         return mBinding.getRoot();
    }
}