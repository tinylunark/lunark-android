package com.example.lunark.services;

import com.example.lunark.models.AccountReport;
import com.example.lunark.models.HostReportEligibility;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AccountReportService {
    @POST("reports/accounts")
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    Completable report(@Body AccountReport accountReport);

    @GET("reports/accounts/host-report-eligibility/{id}")
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    Single<HostReportEligibility> isEligibleToReportHost(@Path("id") Long id);
}
