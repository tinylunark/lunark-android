package com.example.lunark.fragments;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

import com.example.lunark.LunarkApplication;
import com.example.lunark.R;
import com.example.lunark.clients.AccountService;
import com.example.lunark.dtos.AccountDto;
import com.example.lunark.models.Login;
import com.example.lunark.repositories.LoginRepository;
import com.example.lunark.util.RestPreferenceDataStore;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GuestSettingsFragment extends PreferenceFragmentCompat {
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
        setPreferencesFromResource(R.xml.guest_preferences, rootKey);
        SwitchPreferenceCompat preference = findPreference("reservation_response_notifications");
        if (preference != null) {
            preference.setPreferenceDataStore(mDataStore);
            preference.setChecked(mDataStore.getBoolean("reservation_response_notifications", false));
        }
    }
}
