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
    @Inject
    ReservationRepository reservationRepository;
    @Inject
    LoginRepository loginRepository;
    private final LiveData<List<Reservation>> reservations = new MutableLiveData<>();

    @Inject
    public ReservationsViewModel(@NonNull Application application, ReservationRepository reservationRepository) {
        super(application);
        ((LunarkApplication) application).applicationComponent.inject(this);
    }

    public LiveData<List<Reservation>> getReservations() {
        return reservations;
    }

    public LiveData<List<Reservation>> getPendingReservations(Long hostId) {
        return reservationRepository.getPendingReservations(hostId);
    }

    public LiveData<List<Reservation>> getAcceptedReservations(Long guestId) {
        return reservationRepository.getAcceptedReservations(guestId);
    }

    public LiveData<List<Reservation>> getCurrentReservations() {
        return reservationRepository.getCurrentReservations();
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
