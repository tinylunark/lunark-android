package com.example.lunark.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.lunark.R;
import com.example.lunark.databinding.FragmentPropertyDetailBinding;
import com.example.lunark.models.Property;
import com.example.lunark.util.ClientUtils;
import com.example.lunark.viewmodels.PropertyDetailViewModel;

public class PropertyDetailFragment extends Fragment {
    private FragmentPropertyDetailBinding binding;
    private static final String PROPERTY_ID = "propertyId";
    private Long propertyId;
    private PropertyDetailViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(PropertyDetailViewModel.class);

        if (getArguments() != null) {
            propertyId = getArguments().getLong(PROPERTY_ID);
            viewModel.initProperty(propertyId);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPropertyDetailBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getProperty().observe(getViewLifecycleOwner(), property -> {
            binding.name.setText(property.getName());
            binding.location.setText(property.getAddress().getCity() + ", " + property.getAddress().getCountry());
            binding.description.setText(property.getDescription());
            binding.minGuestsValue.setText(String.format("%d", property.getMinGuests()));
            binding.maxGuestsValue.setText(String.format("%d", property.getMaxGuests()));

            if (property.getAverageRating() != null) {
                binding.rating.setText(String.format("%.2f", property.getAverageRating()));
            } else {
                binding.rating.setText(R.string.no_ratings);
            }

            if (property.getImages().size() > 0) {
                Glide.with(this)
                        .load(ClientUtils.SERVICE_API_PATH + "properties/" + property.getId() + "/images/" + property.getImages().get(0).getId())
                        .into(binding.thumbnail);
            }

            if (property.getAmenities().isEmpty()) {
                TextView tv = new TextView(getContext());
                tv.setText(R.string.none);
                binding.amenitiesList.addView(tv);
            } else {
                property.getAmenities().forEach(amenity -> {
                    TextView tv = new TextView(getContext());
                    tv.setText(amenity.getName());
                    binding.amenitiesList.addView(tv);
                });
            }
        });
    }
}