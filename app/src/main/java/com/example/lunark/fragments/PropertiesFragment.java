package com.example.lunark.fragments;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lunark.R;
import com.example.lunark.adapters.PropertyListAdapter;
import com.example.lunark.databinding.FragmentPropertiesBinding;
import com.example.lunark.viewmodels.PropertiesViewModel;
import com.squareup.seismic.ShakeDetector;

import java.util.ArrayList;

public class PropertiesFragment extends Fragment implements ShakeDetector.Listener {
    private FragmentPropertiesBinding binding;
    private PropertiesViewModel propertiesViewModel;
    private PropertyListAdapter adapter;
    private RecyclerView recyclerView;
    private FiltersDialogFragment mFiltersDialogFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        propertiesViewModel = new ViewModelProvider(this, ViewModelProvider.Factory.from(PropertiesViewModel.initializer)).get(PropertiesViewModel.class);
        mFiltersDialogFragment = new FiltersDialogFragment();
        SensorManager sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        ShakeDetector shakeDetector = new ShakeDetector(this);
        shakeDetector.start(sensorManager, SensorManager.SENSOR_DELAY_GAME);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPropertiesBinding.inflate(inflater, container, false);
        binding.setViewModel(propertiesViewModel);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.filterButton.setOnClickListener(v -> {
                    mFiltersDialogFragment.show(
                            getChildFragmentManager(), FiltersDialogFragment.TAG
                    );
                }
        );

        setUpPropertyList();

        propertiesViewModel.getProperties().observe(getViewLifecycleOwner(), properties -> {
            adapter.setProperties(properties);
        });

        binding.searchButton.setOnClickListener(v -> propertiesViewModel.search());
    }

    private void setUpPropertyList() {
        recyclerView = binding.propertiesRecyclerView;
        adapter = new PropertyListAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        int scrollPosition = 0;
        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.scrollToPosition(scrollPosition);
    }

    @Override
    public void hearShake() {
        Toast.makeText(requireContext(), R.string.shake_detected_sorting_properties, Toast.LENGTH_SHORT).show();
        propertiesViewModel.toggleSortOrder();
        propertiesViewModel.search();
    }
}