package com.example.lunark.services;

import com.example.lunark.models.AccountReport;
import com.example.lunark.models.AccountReportDisplay;
import com.example.lunark.models.HostReportEligibility;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AccountReportService {
    @GET("reports/accounts")
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    Call<List<AccountReportDisplay>> getReportedAccounts();

    @DELETE("reports/accounts/{id}")
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    Call<ResponseBody> blockAccount(@Path("id") Long id);


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
