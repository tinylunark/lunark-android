package com.example.lunark.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lunark.databinding.FragmentPropertyDetailBinding;

public class PropertyDetailFragment extends Fragment {
    private FragmentPropertyDetailBinding binding;
    private static final String NAME = "name";
    private static final String LOCATION = "location";
    private static final String DESCRIPTION = "description";
    private static final String RATING = "rating";
    private static final String THUMBNAIL = "thumbnail";

    private String name;
    private String location;
    private String description;
    private double rating;
    private int thumbnail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(NAME);
            location = getArguments().getString(LOCATION);
            description = getArguments().getString(DESCRIPTION);
            rating = getArguments().getDouble(RATING);
            thumbnail = getArguments().getInt(THUMBNAIL);
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

        binding.name.setText(name);
        binding.location.setText(location);
        binding.description.setText(description);
        binding.rating.setText(String.valueOf(rating));
        binding.thumbnail.setImageResource(thumbnail);
    }
}