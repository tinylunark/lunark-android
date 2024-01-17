package com.example.lunark;


import android.content.Context;

import com.example.lunark.datasources.DiskModule;
import com.example.lunark.datasources.NetworkModule;
import com.example.lunark.fragments.FavoritePropertiesFragment;
import com.example.lunark.fragments.GeneralReportFragment;
import com.example.lunark.fragments.NotificationsFragment;
import com.example.lunark.fragments.PropertyDetailFragment;
import com.example.lunark.fragments.PropertyReportFragment;
import com.example.lunark.notifications.NotificationService;
import com.example.lunark.repositories.PropertyRepository;

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
    void inject(HomeActivity homeActivity);
    void inject(AccountScreen accountScreen);
    void inject(SignUpScreenActivity signUpScreenActivity);
    void inject(PropertyRepository propertyRepository);
    void inject(FavoritePropertiesFragment favoritePropertiesFragment);
    void inject(PropertyDetailFragment propertyDetailFragment);
    void inject(GeneralReportFragment generalReportFragment);
    void inject(PropertyReportFragment propertyReportFragment);
    void inject(NotificationService notificationService);
    void inject(NotificationsFragment notificationsFragment);
}
