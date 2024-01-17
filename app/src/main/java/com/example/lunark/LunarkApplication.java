package com.example.lunark;

import android.app.Application;
import com.example.lunark.repositories.*;


public class LunarkApplication extends Application {
    public ApplicationComponent applicationComponent;
    ReservationRepository reservationRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.factory().create(this.getApplicationContext());
        reservationRepository = new ReservationRepository();
        applicationComponent.inject(reservationRepository);
    }

    public ReservationRepository getReservationRepository() {
        return this.reservationRepository;
    }
}
