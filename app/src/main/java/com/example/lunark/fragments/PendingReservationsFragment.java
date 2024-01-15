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

import com.example.lunark.adapters.ReservationsListAdapter;
import com.example.lunark.databinding.FragmentPendingReservationBinding;
import com.example.lunark.viewmodels.ReservationsViewModel;

import java.util.ArrayList;

public class PendingReservationsFragment extends Fragment {
    private FragmentPendingReservationBinding binding;
    private ReservationsViewModel reservationsViewModel;
    private ReservationsListAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reservationsViewModel = new ViewModelProvider(this).get(ReservationsViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPendingReservationBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpReservationList();

        reservationsViewModel.getReservations().observe(getViewLifecycleOwner(), reservations -> {
            adapter.setReservations(reservations);
            recyclerView.setAdapter(adapter);
        });
    }

    private void setUpReservationList() {
        recyclerView = binding.reservationsRecyclerView;
        adapter = new ReservationsListAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        int scrollPosition = 0;
        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.scrollToPosition(scrollPosition);
    }
}