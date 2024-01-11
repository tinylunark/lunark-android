package com.example.lunark.fragments.createProperty;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.lunark.R;
import com.example.lunark.databinding.FragmentCreatePropertyStep3Binding;
import com.example.lunark.models.Property;
import com.example.lunark.viewmodels.PropertyDetailViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.time.LocalDate;
import java.util.Calendar;

public class CreatePropertyStep3Fragment extends Fragment implements Step {
    PropertyDetailViewModel viewModel;
    Property property;
    FragmentCreatePropertyStep3Binding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCreatePropertyStep3Binding.inflate(inflater, container, false);
        View view = binding.getRoot();
        binding.datePicker.setOnClickListener(v -> openDateRangePicker());
        assert this.getParentFragment() != null;
        viewModel = new ViewModelProvider(this.getParentFragment()).get(PropertyDetailViewModel.class);
        return view;
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    private void openDateRangePicker() {
        MaterialDatePicker picker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText(R.string.choose_date)
                .build();

        picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                Pair<Long, Long> selectedDates = (Pair<Long, Long>) selection;
                LocalDate beginDate = convertTime(selectedDates.first);
                LocalDate endDate = convertTime(selectedDates.second);
                binding.startDateTextview.setText(beginDate.toString());
                binding.endDateTextview.setText(endDate.toString());
            }
        });
        picker.show(getParentFragmentManager(), "DATE_RANGE_PICKER");
    }

    private LocalDate convertTime(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    }
}
