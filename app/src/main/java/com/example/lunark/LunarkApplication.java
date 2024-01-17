package com.example.lunark;

import android.app.Application;
import com.example.lunark.repositories.*;

import com.example.lunark.repositories.PropertyRepository;


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
    }

    public ReservationRepository getReservationRepository() {
        return this.reservationRepository;
    }

    public PropertyRepository getPropertyRepository() {
        return this.propertyRepository;
    }
}
