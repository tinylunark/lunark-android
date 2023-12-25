package com.example.lunark.datasources;

import android.app.Application;
import android.content.Context;

import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;

import dagger.Module;
import dagger.Provides;

@Module
public class DiskModule {
   @Provides
   public RxDataStore<Preferences> provideAuthDataStore(Context appContext) {
       return new RxPreferenceDataStoreBuilder(appContext, "auth").build();
   }
}
