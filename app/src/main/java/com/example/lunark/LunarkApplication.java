package com.example.lunark;

import android.app.Application;


public class LunarkApplication extends Application {
    ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.factory().create(this.getApplicationContext());
    }
}
