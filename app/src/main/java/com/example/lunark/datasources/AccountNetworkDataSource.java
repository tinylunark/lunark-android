package com.example.lunark.datasources;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.lunark.clients.AccountService;
import com.example.lunark.dtos.AccountSignUpDto;
import com.example.lunark.models.Property;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AccountNetworkDataSource {
    private static final String TAG = "AccountNetworkDataSource";
    private static AccountService accountService;

    @Inject
    public AccountNetworkDataSource(Retrofit retrofit) {
        accountService = retrofit.create(AccountService.class);
    }

    public Completable signUp(AccountSignUpDto accountSignUpDto)  {
        return accountService.signUp(accountSignUpDto)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
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
