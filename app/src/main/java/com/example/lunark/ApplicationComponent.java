package com.example.lunark;


import android.content.Context;

import com.example.lunark.datasources.DiskModule;
import com.example.lunark.datasources.NetworkModule;
import com.example.lunark.repositories.ReservationRepository;
import com.example.lunark.fragments.*;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Component(modules = {DiskModule.class, NetworkModule.class})
@Singleton
public interface ApplicationComponent {
    @Component.Factory
    interface Factory {
        ApplicationComponent create(@BindsInstance Context context);
    }
    void inject(LoginScreenActivity loginScreenActivity);
    void inject(ReservationRepository reservationRepository);
    void inject(HomeActivity homeActivity);
    void inject(AccountScreen accountScreen);
    void inject(SignUpScreenActivity signUpScreenActivity);
    void inject(PendingReservationsFragment pendingReservationsFragment);
}
