package com.example.lunark.datasources;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.lunark.dtos.AccountSignUpDto;
import com.example.lunark.models.Property;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountRepository {
    private AccountNetworkDataSource accountNetworkDataSource;

    @Inject
    public AccountRepository(AccountNetworkDataSource accountNetworkDataSource) {
        this.accountNetworkDataSource = accountNetworkDataSource;
    }

    public Completable signUp(AccountSignUpDto accountSignUpDto) {
        return this.accountNetworkDataSource.signUp(accountSignUpDto);
    }

    public LiveData<List<Property>> getFavoriteProperties() {
        return accountNetworkDataSource.getFavoriteProperties();
    }
}
