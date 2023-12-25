package com.example.lunark.datasources;

import android.app.Application;

import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;

import com.example.lunark.models.Login;

import javax.inject.Inject;

import dagger.Provides;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class LoginLocalDataSource {
    RxDataStore<Preferences> dataStore;
    Preferences.Key<String> AUTH_TOKEN_KEY = PreferencesKeys.stringKey("auth_token");

    @Inject
    public LoginLocalDataSource(RxDataStore<Preferences> dataStore) {
        this.dataStore = dataStore;
    }

    public Single<Login> getToken() {
        return dataStore.data().firstOrError()
                .onErrorResumeNext(throwable -> Single.error(throwable))
                .map(preferences -> preferences.get(AUTH_TOKEN_KEY))
                .map(token -> token != null ? new Login(token, 0): null);
    }

    public Single<Preferences> writeToken(String token) {
        return dataStore.updateDataAsync(preferences -> {
            MutablePreferences mutablePreferences = preferences.toMutablePreferences();
            mutablePreferences.set(AUTH_TOKEN_KEY, token);
            return Single.just(mutablePreferences);
        });
    }

}
