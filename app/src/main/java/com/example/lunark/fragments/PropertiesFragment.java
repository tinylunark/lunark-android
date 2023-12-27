package com.example.lunark.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.lunark.adapters.PropertyListAdapter;
import com.example.lunark.databinding.FragmentPropertiesBinding;
import com.example.lunark.models.Property;
import com.example.lunark.viewmodels.PropertiesViewModel;

public class PropertiesFragment extends Fragment {
    private FragmentPropertiesBinding binding;
    private PropertiesViewModel propertiesViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        propertiesViewModel = new ViewModelProvider(this).get(PropertiesViewModel.class);
    }

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
        propertiesViewModel.getProperties().observe(getViewLifecycleOwner(), properties -> {
            ListView propertyList = binding.list;
            PropertyListAdapter propertyListAdapter = new PropertyListAdapter(getActivity(), properties);
            propertyList.setAdapter(propertyListAdapter);

            propertyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Property selectedProperty = (Property) parent.getItemAtPosition(position);

                    Bundle bundle = new Bundle();
                    bundle.putString("name", selectedProperty.getName());
                    bundle.putDouble("rating", selectedProperty.getAverageRating());
                    bundle.putString("location", selectedProperty.getLocation());
                    bundle.putString("description", selectedProperty.getDescription());
                    bundle.putInt("thumbnail", selectedProperty.getThumbnailId());

                    getParentFragmentManager().setFragmentResult("selectedProperty", bundle);
                }
            });
        });
    }
}