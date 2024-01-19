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
import com.example.lunark.adapters.ReservationsListAdapterBase;
import com.example.lunark.databinding.FragmentPendingReservationBinding;
import com.example.lunark.models.Login;
import com.example.lunark.repositories.LoginRepository;
import com.example.lunark.viewmodels.ReservationsViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class AllReservationsFragment extends Fragment {
    private FragmentPendingReservationBinding binding;
    private ReservationsViewModel reservationsViewModel;
    private ReservationsListAdapterBase adapter;
    private RecyclerView recyclerView;
    @Inject
    public LoginRepository loginRepository;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        ((LunarkApplication)getActivity().getApplication()).applicationComponent.inject(this); ;
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPendingReservationBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        reservationsViewModel = new ViewModelProvider(this, ViewModelProvider.Factory.from(ReservationsViewModel.initializer)).get(ReservationsViewModel.class);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpReservationList();

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
                        if (getView() != null) {
                            observeCurrentReservations();
                        }
                    }
                });
            }

            @Override
            public void onError(Throwable e) {
            }
        });
    }

    private void observeCurrentReservations() {
        reservationsViewModel.getCurrentReservations().observe(getViewLifecycleOwner(), reservations -> {
            adapter.setReservations(reservations);
            recyclerView.setAdapter(adapter);
        });
    }

    public void acceptReservation(long reservationId) {
        reservationsViewModel.acceptReservation(reservationId);
        observeCurrentReservations();
    }

    public void declineReservation(long reservationId) {
        reservationsViewModel.declineReservation(reservationId);
        observeCurrentReservations();
    }


    private void setUpReservationList() {
        recyclerView = binding.reservationsRecyclerView;
        adapter = new ReservationsListAdapterBase(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        int scrollPosition = 0;
        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.scrollToPosition(scrollPosition);
    }
}
