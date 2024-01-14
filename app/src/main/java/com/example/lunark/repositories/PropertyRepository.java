package com.example.lunark.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.lunark.models.Property;
import com.example.lunark.util.ClientUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PropertyRepository {
    private static final String LOG_TAG = "PropertyRepository";

    public LiveData<List<Property>> getProperties(Map<String, String> options) {
        final MutableLiveData<List<Property>> data = new MutableLiveData<>();

        ClientUtils.propertyService.getProperties(options).enqueue(new Callback<List<Property>>() {
            @Override
            public void onResponse(Call<List<Property>> call, Response<List<Property>> response) {
                if (response.isSuccessful()) {
                    Log.i(LOG_TAG, "Get properties response: " + response.body());
                    data.setValue(response.body());
                } else {
                    Log.w(LOG_TAG, "Get properties response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Property>> call, Throwable t) {
                Log.e(LOG_TAG, "Get properties failure: " + t.getMessage());
            }
        });

        return data;
    }

    public LiveData<Property> getProperty(Long id) {
        final MutableLiveData<Property> data = new MutableLiveData<>();

        ClientUtils.propertyService.getProperty(id).enqueue(new Callback<Property>() {
            @Override
            public void onResponse(Call<Property> call, Response<Property> response) {
                if (response.isSuccessful()) {
                    Log.i(LOG_TAG, "Get property response: " + response.body());
                    data.setValue(response.body());
                } else {
                    Log.w(LOG_TAG, "Get properties response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Property> call, Throwable t) {
                Log.e(LOG_TAG, "Get property failure: " + t.getMessage());
            }
        });

        return data;
    }

    public Single<Property> createProperty(Property property) {
        return ClientUtils.propertyService.createProperty(property)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(property1 -> {
                    Log.i(LOG_TAG, "Uploaded property. New property id" + property1.getId());
                })
                .doOnError(throwable -> {
                    Log.e(LOG_TAG, "Upload property failure: " + throwable.getMessage());
                });
    }

}
