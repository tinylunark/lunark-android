package com.example.lunark.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.util.Pair;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.lunark.R;
import com.example.lunark.databinding.FragmentFiltersDialogBinding;
import com.example.lunark.viewmodels.PropertiesViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.time.LocalDate;

public class FiltersDialogFragment extends DialogFragment {
    public static String TAG = "FiltersDialog";
    public PropertiesViewModel mViewModel;
    private FragmentFiltersDialogBinding mBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ViewModelProvider(requireParentFragment(), ViewModelProvider.Factory.from(PropertiesViewModel.initializer)).get(PropertiesViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        mBinding = FragmentFiltersDialogBinding.inflate(LayoutInflater.from(getContext()));
        View view = mBinding.getRoot();
        mBinding.setViewModel(mViewModel);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setTitle(R.string.filters)
                .setPositiveButton(R.string.apply, (dialog, which) -> {
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                });

        // Property type spinner
        Spinner typeSpinner = (Spinner) view.findViewById(R.id.spType);
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.property_types_array,
                android.R.layout.simple_spinner_item
        );
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        builder.setView(view);

        return builder.create();
    }
}