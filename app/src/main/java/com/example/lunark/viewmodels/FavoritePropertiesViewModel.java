package com.example.lunark.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.lunark.datasources.AccountRepository;
import com.example.lunark.models.Property;

import java.util.List;

import javax.inject.Inject;

public class FavoritePropertiesViewModel extends AndroidViewModel {
    AccountRepository accountRepository;

    @Inject
    public FavoritePropertiesViewModel(@NonNull Application application, AccountRepository accountRepository) {
        super(application);

        this.accountRepository = accountRepository;
    }

    public LiveData<List<Property>> getFavoriteProperties() {
        return accountRepository.getFavoriteProperties();
    }
}