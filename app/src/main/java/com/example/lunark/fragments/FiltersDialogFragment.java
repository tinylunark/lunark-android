package com.example.lunark.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.lunark.R;
import com.example.lunark.databinding.FragmentFiltersDialogBinding;
import com.example.lunark.models.Amenity;
import com.example.lunark.util.ClientUtils;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FiltersDialogFragment extends DialogFragment implements View.OnClickListener {
    public static String TAG = "FiltersDialog";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        FragmentFiltersDialogBinding binding = FragmentFiltersDialogBinding.inflate(getLayoutInflater());

        binding.datePicker.setOnClickListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setTitle(R.string.filters)
                .setPositiveButton(R.string.apply, (dialog, which) -> {})
                .setNegativeButton(R.string.cancel, (dialog, which) -> {});

        // Property type spinner
        Spinner typeSpinner = (Spinner) binding.spType;
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.property_types_array,
                android.R.layout.simple_spinner_item
        );
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        Call<List<Amenity>> call = ClientUtils.amenityService.getAll();
        call.enqueue(new Callback<List<Amenity>>() {
            @Override
            public void onResponse(Call<List<Amenity>> call, Response<List<Amenity>> response) {
                if (response.code() == 200) {
                    List<Amenity> amenities = response.body();
                    for (Amenity amenity : amenities) {
                        CheckBox cb = new CheckBox(getContext());
                        cb.setText(amenity.getName());
                        cb.setTag(amenity.getId());
                        ViewGroup vg = (ViewGroup) binding.viewgroup;
                        vg.addView(cb);
                    }
                } else {
                    Log.d("REZ", "Message received: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Amenity>> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });

        builder.setView(binding.getRoot());

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