package com.example.lunark.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lunark.R;
import com.example.lunark.databinding.FragmentBookingRequestBinding;
import com.example.lunark.viewmodels.BookingRequestViewModel;

public class BookingRequestFragment extends Fragment {
    public static final String TAG = "BookingRequestFragment";
    public static final String PROPERTY_ID = "propertyId";
    FragmentBookingRequestBinding mBinding;
    Long mPropertyId;
    BookingRequestViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mPropertyId = getArguments().getLong(PROPERTY_ID);
            Log.d(TAG, "Property ID: " + mPropertyId);
        } else {
            throw new IllegalStateException("Property ID not found");
        }

        mViewModel = new ViewModelProvider(this, ViewModelProvider.Factory.from(BookingRequestViewModel.initializer)).get(BookingRequestViewModel.class);
        mViewModel.setPropertyId(mPropertyId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentBookingRequestBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel.getProperty().observe(getViewLifecycleOwner(), property -> {
            if (property != null) {
                mBinding.tvPropertyName.setText(property.getName());
            }
        });
    }
}