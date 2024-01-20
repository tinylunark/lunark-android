package com.example.lunark.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.lunark.models.ReviewReport;
import com.example.lunark.services.ReviewReportService;
import com.example.lunark.util.ClientUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReviewReportRepository {
    private static final String LOG_TAG = "ReviewReportRepository";
    ReviewReportService reviewReportService;

    @Inject
    public ReviewReportRepository(Retrofit retrofit) {
        this.reviewReportService = retrofit.create(ReviewReportService.class);
    }

    public LiveData<List<ReviewReport>> getReviewReports() {
        final MutableLiveData<List<ReviewReport>> data = new MutableLiveData<>();

        reviewReportService.getReviewReports().enqueue(new Callback<List<ReviewReport>>() {
            @Override
            public void onResponse(Call<List<ReviewReport>> call, Response<List<ReviewReport>> response) {
                if (response.isSuccessful()) {
                    List<ReviewReport> ReviewReports = response.body();
                    data.setValue(response.body());
                } else {
                    Log.w(LOG_TAG, "Get ReviewReports response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ReviewReport>> call, Throwable t) {
                Log.e(LOG_TAG, "Get ReviewReports failure: " + t.getMessage());
            }
        });

        return data;
    }


    public void deleteReview(Long ReviewReportId) {
        reviewReportService.deleteReview(ReviewReportId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.e(LOG_TAG, "Accept ReviewReport success: " + response.code());
                } else {
                    Log.e(LOG_TAG, "Accept ReviewReport error: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(LOG_TAG, "Accept ReviewReport failure: " + t.getMessage());
            }
        });
    }

    public Completable createReport(ReviewReport report) {
        return this.reviewReportService.createReport(report)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
