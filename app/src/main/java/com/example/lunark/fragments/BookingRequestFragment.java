package com.example.lunark.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lunark.R;
import com.example.lunark.databinding.FragmentBookingRequestBinding;

public class BookingRequestFragment extends Fragment {
    public static final String TAG = "BookingRequestFragment";
    public static final String PROPERTY_ID = "propertyId";
    FragmentBookingRequestBinding mBinding;
    Long mPropertyId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mPropertyId = getArguments().getLong(PROPERTY_ID);
            Log.d(TAG, "Property ID: " + mPropertyId);
        } else {
            throw new IllegalStateException("Property ID not found");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         mBinding = FragmentBookingRequestBinding.inflate(inflater, container, false);
         return mBinding.getRoot();
    }
}