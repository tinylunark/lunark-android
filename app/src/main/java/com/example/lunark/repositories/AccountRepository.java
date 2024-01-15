package com.example.lunark.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.lunark.models.Property;
import com.example.lunark.services.AccountService;
import com.example.lunark.util.ClientUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountRepository {
    private final static String TAG = "AccountRepository";
    private final AccountService accountService;

    public AccountRepository() {
        this.accountService = ClientUtils.accountService;
    }

    public LiveData<List<Property>> getFavoriteProperties() {
        final MutableLiveData<List<Property>> data = new MutableLiveData<>();

        accountService.getFavoriteProperties().enqueue(new Callback<List<Property>>() {
            @Override
            public void onResponse(Call<List<Property>> call, Response<List<Property>> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "Get favorite properties response: " + response.body());
                    data.setValue(response.body());
                } else {
                    Log.w(TAG, "Get favorite properties response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Property>> call, Throwable t) {
                Log.e(TAG, "Get favorite properties failure: " + t.getMessage());
            }
        });

        return data;
    }
}
