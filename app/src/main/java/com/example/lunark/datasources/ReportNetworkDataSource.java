package com.example.lunark.datasources;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.lunark.clients.ReportService;
import com.example.lunark.models.GeneralReport;
import com.example.lunark.models.PropertyReport;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReportNetworkDataSource {
    private static final String TAG = "ReportNetworkDataSource";
    public ReportService mReportService;

    @Inject
    public ReportNetworkDataSource(Retrofit retrofit) {
        mReportService = retrofit.create(ReportService.class);
    }

    public LiveData<GeneralReport> getGeneralReport(@NonNull String start, @NonNull String end) {
        MutableLiveData<GeneralReport> data = new MutableLiveData<>();

        mReportService.getGeneralReport(start, end).enqueue(new Callback<GeneralReport>() {
            @Override
            public void onResponse(Call<GeneralReport> call, Response<GeneralReport> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "getGeneralReport: " + response.body());
                    data.setValue(response.body());
                } else {
                    Log.w(TAG, "getGeneralReport: " + response.code());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<GeneralReport> call, Throwable t) {
                Log.e(TAG, "getGeneralReport: " + t.getMessage(), t);
            }
        });

        return data;
    }

    public LiveData<PropertyReport> getPropertyReport(@NonNull String year, @NonNull String propertyId) {
        MutableLiveData<PropertyReport> data = new MutableLiveData<>();

        mReportService.getPropertyReport(year, propertyId).enqueue(new Callback<PropertyReport>() {
            @Override
            public void onResponse(Call<PropertyReport> call, Response<PropertyReport> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "getPropertyReport: " + response.body());
                    data.setValue(response.body());
                } else {
                    Log.w(TAG, "getPropertyReport: " + response.code());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<PropertyReport> call, Throwable t) {
                Log.e(TAG, "getPropertyReport: " + t.getMessage(), t);
            }
        });

        return data;
    }
}
