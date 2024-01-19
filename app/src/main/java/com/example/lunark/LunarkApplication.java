package com.example.lunark;

import android.app.Application;

import com.example.lunark.repositories.PropertyRepository;
import com.example.lunark.repositories.ReservationRepository;
import com.example.lunark.repositories.ReviewReportRepository;


public class LunarkApplication extends Application {
    public ApplicationComponent applicationComponent;
    ReservationRepository reservationRepository;
    PropertyRepository propertyRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.factory().create(this.getApplicationContext());
        propertyRepository = new PropertyRepository();
        applicationComponent.inject(propertyRepository);
        reservationRepository = new ReservationRepository();
        applicationComponent.inject(reservationRepository);
    }

    public ReservationRepository getReservationRepository() {
        return this.reservationRepository;
    }

    public PropertyRepository getPropertyRepository() {
        return this.propertyRepository;
    }

}
