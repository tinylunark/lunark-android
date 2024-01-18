package com.example.lunark.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lunark.databinding.FragmentBookingRequestBinding;
import com.example.lunark.validators.BookingDateValidator;
import com.example.lunark.viewmodels.BookingRequestViewModel;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.List;
import java.util.stream.Collectors;

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
        mBinding.setViewModel(mViewModel);
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

        mBinding.btnChooseDate.setOnClickListener(this::openDatePicker);
    }

    private void openDatePicker(View view) {
        final String pickerTag = "DatePickerDialog";

        List<Long> availableDates = mViewModel.getProperty().getValue().getAvailabilityEntries().stream()
                .map(entry -> entry.getDate().toEpochDay() * 86400000) // DateValidator works with milliseconds so we need to convert Epoch days to milliseconds
                .collect(Collectors.toList());

        Pair<Long, Long> selection;
        if (mViewModel.getStartDate().getValue() == null || mViewModel.getEndDate().getValue() == null || availableDates.isEmpty()) {
            selection = null;
        }
        else {
            selection = new Pair<>(mViewModel.getStartDate().getValue(), mViewModel.getEndDate().getValue());
        }

        CalendarConstraints constraints = new CalendarConstraints.Builder()
                .setValidator(new BookingDateValidator(availableDates))
                .build();

        MaterialDatePicker<Pair<Long, Long>> picker = MaterialDatePicker.Builder.dateRangePicker()
                .setSelection(selection)
                .setCalendarConstraints(constraints)
                .build();

        picker.addOnPositiveButtonClickListener(selection1 -> {
            mViewModel.setStartDate(selection1.first);
            mViewModel.setEndDate(selection1.second);
        });

        picker.show(getChildFragmentManager(), pickerTag);
    }
}