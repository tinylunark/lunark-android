package com.example.lunark.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.lunark.HomeActivity;
import com.example.lunark.R;
import com.example.lunark.activities.PropertyActivity;
import com.example.lunark.adapters.PropertyListAdapter;
import com.example.lunark.databinding.FragmentPropertiesBinding;
import com.example.lunark.models.Property;

public class PropertiesFragment extends Fragment {
    private FragmentPropertiesBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPropertiesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.filterButton.setOnClickListener(v -> {
                    new FiltersDialogFragment().show(
                            getChildFragmentManager(), FiltersDialogFragment.TAG
                    );
                }
        );

        setUpPropertyList();
    }

    private void setUpPropertyList() {
        ListView propertyListView = binding.list;
        PropertyListAdapter propertyListAdapter = new PropertyListAdapter(getActivity());
        propertyListView.setAdapter(propertyListAdapter);

        propertyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Property selectedProperty = (Property) parent.getItemAtPosition(position);

                Intent intent = new Intent(getActivity(), PropertyActivity.class);
                intent.putExtra("name", selectedProperty.getName());
                intent.putExtra("rating", selectedProperty.getAverageRating());
                intent.putExtra("location", selectedProperty.getLocation());
                intent.putExtra("description", selectedProperty.getDescription());
                intent.putExtra("thumbnail", selectedProperty.getThumbnailId());

                startActivity(intent);
            }
        });
    }
}