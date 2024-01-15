package com.example.lunark;

import android.app.Application;

import com.example.lunark.repositories.PropertyRepository;


public class LunarkApplication extends Application {
    public ApplicationComponent applicationComponent;
    PropertyRepository propertyRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.factory().create(this.getApplicationContext());
        propertyRepository = new PropertyRepository();
        applicationComponent.inject(propertyRepository);
    }

    public PropertyRepository getPropertyRepository() {
        return this.propertyRepository;
    }
}
