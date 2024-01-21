package com.example.lunark.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lunark.LunarkApplication;
import com.example.lunark.adapters.UnapprovedPropertiesListAdapter;
import com.example.lunark.databinding.FragmentPropertyApprovalBinding;
import com.example.lunark.viewmodels.UnapprovedPropertiesViewModel;

import java.util.ArrayList;

public class PropertyApprovalFragment extends Fragment {
    private FragmentPropertyApprovalBinding binding;
    private UnapprovedPropertiesViewModel viewModel;
    private UnapprovedPropertiesListAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ((LunarkApplication)getActivity().getApplication()).applicationComponent.inject(this); ;
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPropertyApprovalBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        viewModel = new ViewModelProvider(this, ViewModelProvider.Factory.from(UnapprovedPropertiesViewModel.initializer)).get(UnapprovedPropertiesViewModel.class);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpUnapprovedPropertiesList();
        observeUnapprovedProperties();
    }

    private void observeUnapprovedProperties() {
        viewModel.getUnapprovedProperties().observe(getViewLifecycleOwner(), properties -> {
            adapter.setProperties(properties);
            recyclerView.setAdapter(adapter);
        });
    }

    private void setUpUnapprovedPropertiesList() {
        recyclerView = binding.unapprovedPropertiesRecyclerView;
        adapter = new UnapprovedPropertiesListAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        int scrollPosition = 0;
        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.scrollToPosition(scrollPosition);
    }
}
