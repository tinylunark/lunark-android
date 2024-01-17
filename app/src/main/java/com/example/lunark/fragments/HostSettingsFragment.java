package com.example.lunark.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.example.lunark.LunarkApplication;
import com.example.lunark.R;
import com.example.lunark.util.RestPreferenceDataStore;

import javax.inject.Inject;

public class HostSettingsFragment extends PreferenceFragmentCompat {

    private static final String TAG = "GuestSettingsFragment";
    @Inject
    RestPreferenceDataStore mDataStore;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ((LunarkApplication) getActivity().getApplication()).applicationComponent.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.host_preferences, rootKey);

        SwitchPreferenceCompat reservationRequestPreference = findPreference("reservation_request_notifications");
        if (reservationRequestPreference != null) {
            reservationRequestPreference.setPreferenceDataStore(mDataStore);
            reservationRequestPreference.setChecked(mDataStore.getBoolean("reservation_request_notifications", false));
        }

        SwitchPreferenceCompat reservationCancellationPreference = findPreference("reservation_cancellation_notifications");
        if (reservationCancellationPreference != null) {
            reservationCancellationPreference.setPreferenceDataStore(mDataStore);
            reservationCancellationPreference.setChecked(mDataStore.getBoolean("reservation_cancellation_notifications", false));
        }

        SwitchPreferenceCompat propertyReviewPreference = findPreference("property_review_notifications");
        if (propertyReviewPreference != null) {
            propertyReviewPreference.setPreferenceDataStore(mDataStore);
            propertyReviewPreference.setChecked(mDataStore.getBoolean("property_review_notifications", false));
        }

        SwitchPreferenceCompat hostReviewPreference = findPreference("host_review_notifications");
        if (hostReviewPreference != null) {
            hostReviewPreference.setPreferenceDataStore(mDataStore);
            hostReviewPreference.setChecked(mDataStore.getBoolean("host_review_notifications", false));
        }
    }
}
