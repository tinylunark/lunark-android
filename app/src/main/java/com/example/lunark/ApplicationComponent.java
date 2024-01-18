package com.example.lunark;


import android.content.Context;

import com.example.lunark.adapters.ReservationsListAdapter;
import com.example.lunark.adapters.ReservationsListAdapterBase;
import com.example.lunark.adapters.ReservationsCancelListAdapter;
import com.example.lunark.adapters.ReviewListAdapter;
import com.example.lunark.adapters.UnapprovedPropertiesListAdapter;
import com.example.lunark.datasources.DiskModule;
import com.example.lunark.datasources.NetworkModule;
import com.example.lunark.fragments.FavoritePropertiesFragment;
import com.example.lunark.fragments.GeneralReportFragment;
import com.example.lunark.fragments.GuestSettingsFragment;
import com.example.lunark.fragments.HostSettingsFragment;
import com.example.lunark.fragments.NotificationsFragment;
import com.example.lunark.fragments.PendingReservationsFragment;
import com.example.lunark.fragments.AllReservationsFragment;
import com.example.lunark.repositories.ReservationRepository;
import com.example.lunark.fragments.GuestCancelReservationFragment;
import com.example.lunark.viewmodels.*;
import com.example.lunark.fragments.PropertyDetailFragment;
import com.example.lunark.fragments.PropertyApprovalFragment;
import com.example.lunark.fragments.PropertyReportFragment;
import com.example.lunark.notifications.NotificationService;
import com.example.lunark.repositories.PropertyRepository;
import com.example.lunark.viewmodels.HostViewModel;
import com.example.lunark.viewmodels.PropertiesViewModel;
import com.example.lunark.viewmodels.ReviewViewModel;

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
    void inject(PendingReservationsFragment pendingReservationsFragment);
    void inject(AllReservationsFragment pendingReservationsFragment);
    void inject(PropertyRepository propertyRepository);
    void inject(FavoritePropertiesFragment favoritePropertiesFragment);
    void inject(PropertyDetailFragment propertyDetailFragment);
    void inject(GeneralReportFragment generalReportFragment);
    void inject(PropertyReportFragment propertyReportFragment);
    void inject(GuestCancelReservationFragment guestCancelReservationFragment);
    void inject(NotificationService notificationService);
    void inject(NotificationsFragment notificationsFragment);
    void inject(ReservationsListAdapter reservationListAdapter);
    void inject(ReservationsCancelListAdapter reservationsCancelListAdapter);
    void inject(ReservationsListAdapterBase reservationListAdapterBase);
    void inject(ReservationsViewModel reservationListAdapter);
    void inject(UnapprovedPropertiesViewModel unapprovedPropertiesViewModel);
    void inject(ReviewViewModel reviewViewModel);
    void inject(ReviewListAdapter reviewListAdapter);
    void inject(GuestSettingsFragment guestSettingsFragment);
    void inject(HostSettingsFragment hostSettingsFragment);
    void inject(ReservationRepository reservationRepository);
    void inject(PropertyApprovalFragment propertyApprovalFragment);
    void inject(UnapprovedPropertiesListAdapter unapprovedPropertiesListAdapter);
    void inject(HostViewModel hostViewModel);
}
