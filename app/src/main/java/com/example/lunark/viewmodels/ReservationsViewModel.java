package com.example.lunark.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.lunark.models.Reservation;
import com.example.lunark.repositories.LoginRepository;
import com.example.lunark.repositories.ReservationRepository;

import java.util.List;

import javax.inject.Inject;

public class ReservationsViewModel extends AndroidViewModel {
    private final ReservationRepository reservationRepository;
    @Inject
    LoginRepository loginRepository;
    private final LiveData<List<Reservation>> reservations;

    public ReservationsViewModel(@NonNull Application application) {
        super(application);
        reservationRepository = new ReservationRepository();
        reservations = new MutableLiveData<>();
    }

    public LiveData<List<Reservation>> getReservations() {
        return reservations;
    }

    public LiveData<List<Reservation>> getPendingReservations() {
        return reservationRepository.getPendingReservations();
    }
    public void acceptReservation(long reservationId) {
        reservationRepository.acceptReservation(reservationId);
    }

    public void declineReservation(long reservationId) {
        reservationRepository.declineReservation(reservationId);
    }
}
