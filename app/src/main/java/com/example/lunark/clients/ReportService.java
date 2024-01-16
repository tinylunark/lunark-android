package com.example.lunark.clients;

import com.example.lunark.models.GeneralReport;
import com.example.lunark.models.PropertyReport;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ReportService {
    @GET("reports/general")
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    Call<GeneralReport> getGeneralReport(@Query("start") String start, @Query("end") String end);

    @GET("reports/property")
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    Call<PropertyReport> getPropertyReport(@Query("year") String year, @Query("propertyId") String propertyId);
}
