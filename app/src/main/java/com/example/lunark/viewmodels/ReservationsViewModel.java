package com.example.lunark.viewmodels;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import com.example.lunark.LunarkApplication;
import com.example.lunark.models.Reservation;
import com.example.lunark.repositories.LoginRepository;
import com.example.lunark.repositories.ReservationRepository;

import java.util.List;

import javax.inject.Inject;

public class ReservationsViewModel extends AndroidViewModel {
    private final ReservationRepository reservationRepository;
    @Inject
    LoginRepository loginRepository;
    private final LiveData<List<Reservation>> reservations = new MutableLiveData<>();

    public ReservationsViewModel(@NonNull Application application, ReservationRepository reservationRepository) {
        super(application);
        this.reservationRepository = reservationRepository;
    }

    public LiveData<List<Reservation>> getReservations() {
        return reservations;
    }

    public LiveData<List<Reservation>> getPendingReservations(Long hostId) {
        return reservationRepository.getPendingReservations(hostId);
    }
    public void acceptReservation(long reservationId) {
        reservationRepository.acceptReservation(reservationId);
    }

    public void declineReservation(long reservationId) {
        reservationRepository.declineReservation(reservationId);
    }

    public static final ViewModelInitializer<ReservationsViewModel> initializer = new ViewModelInitializer<>(
        ReservationsViewModel.class,
        creationExtras -> {
            LunarkApplication app = (LunarkApplication)  creationExtras.get(APPLICATION_KEY);
            assert app != null;

            return new ReservationsViewModel(app, app.getReservationRepository());
        }
    );
}
