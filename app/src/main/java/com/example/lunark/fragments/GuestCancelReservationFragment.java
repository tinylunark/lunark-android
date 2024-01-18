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
import com.example.lunark.adapters.ReservationsCancelListAdapter;
import com.example.lunark.databinding.FragmentGuestCancelReservationBinding;
import com.example.lunark.models.Login;
import com.example.lunark.repositories.LoginRepository;
import com.example.lunark.viewmodels.ReservationsViewModel;

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
                        observePendingReservations(profileId);
                    }
                });
            }

            @Override
            public void onError(Throwable e) {
            }
        });
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
}
