package com.example.lunark.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lunark.LunarkApplication;
import com.example.lunark.adapters.ReservationsCancelListAdapter;
import com.example.lunark.databinding.FragmentGuestCancelReservationBinding;
import com.example.lunark.models.Login;
import com.example.lunark.models.ReservationStatus;
import com.example.lunark.repositories.LoginRepository;
import com.example.lunark.viewmodels.ReservationsViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
public class GuestCancelReservationFragment extends Fragment {
    private FragmentGuestCancelReservationBinding binding;
    private ReservationsViewModel reservationsViewModel;
    private ReservationsCancelListAdapter adapter;
    private RecyclerView recyclerView;
    @Inject
    public LoginRepository loginRepository;
    private MaterialDatePicker<Pair<Long, Long>> mDateRangePicker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ((LunarkApplication) getActivity().getApplication()).applicationComponent.inject(this);
        ;
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentGuestCancelReservationBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        reservationsViewModel = new ViewModelProvider(this, ViewModelProvider.Factory.from(ReservationsViewModel.initializer)).get(ReservationsViewModel.class);
        binding.setViewModel(reservationsViewModel);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpReservationList();
        setUpDateRangePicker();
        setUpReservationStatusSpinner();

        loginRepository.getLogin().subscribe(new SingleObserver<Login>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onSuccess(Login login) {
                Long profileId = login.getProfileId();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        observePendingReservations(profileId);
                    }
                });
            }

            @Override
            public void onError(Throwable e) {
            }
        });

        binding.datePickerButton.setOnClickListener(this::onDateRangeButtonClick);
        binding.searchButton.setOnClickListener(this::onSearchButtonClick);
    }

    private void observePendingReservations(Long profileId) {
        reservationsViewModel.getAcceptedReservations(profileId).observe(getViewLifecycleOwner(), reservations -> {
            adapter.setReservations(reservations);
            recyclerView.setAdapter(adapter);
        });
    }

    private void setUpReservationList() {
        recyclerView = binding.reservationsCancelRecyclerView;
        adapter = new ReservationsCancelListAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        int scrollPosition = 0;
        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.scrollToPosition(scrollPosition);
    }

    private void setUpReservationStatusSpinner() {
        ArrayAdapter<ReservationStatus> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, ReservationStatus.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.statusSpinner.setAdapter(adapter);
        binding.statusSpinner.setSelection(0);
    }

    private void setUpDateRangePicker() {
        Pair<Long, Long> selection;
        if (reservationsViewModel.getStartDate().getValue() != null && reservationsViewModel.getEndDate().getValue() != null) {
            selection = new Pair<>(reservationsViewModel.getStartDate().getValue(), reservationsViewModel.getEndDate().getValue());
        } else {
            selection = new Pair<>(MaterialDatePicker.todayInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds());
        }

        mDateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setSelection(selection)
                .build();
        mDateRangePicker.addOnPositiveButtonClickListener(selection1 -> {
            reservationsViewModel.setStartDate(selection1.first);
            reservationsViewModel.setEndDate(selection1.second);
        });
    }

    private void onDateRangeButtonClick(View view) {
        mDateRangePicker.show(getChildFragmentManager(), "DATE_PICKER");
    }

    private void onSearchButtonClick(View view) {
        reservationsViewModel.getCurrentReservations().observe(getViewLifecycleOwner(), reservations -> {
            adapter.setReservations(reservations);
            recyclerView.setAdapter(adapter);
        });
    }
}
