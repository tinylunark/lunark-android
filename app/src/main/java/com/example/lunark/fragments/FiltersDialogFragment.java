package com.example.lunark.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.lunark.R;
import com.example.lunark.databinding.FragmentFiltersDialogBinding;
import com.google.android.material.datepicker.MaterialDatePicker;

public class FiltersDialogFragment extends DialogFragment implements View.OnClickListener {
    public static String TAG = "FiltersDialog";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_filters_dialog, null);

        view.findViewById(R.id.datePicker).setOnClickListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setTitle(R.string.filters)
                .setPositiveButton(R.string.apply, (dialog, which) -> {})
                .setNegativeButton(R.string.cancel, (dialog, which) -> {});

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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.datePicker) {
            MaterialDatePicker.Builder.dateRangePicker()
                    .setTitleText(R.string.choose_date)
                    .build()
                    .show(getChildFragmentManager(), "DATE_RANGE_PICKER");
        }
    }
}