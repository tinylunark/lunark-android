package com.example.lunark.services;

import com.example.lunark.models.NominatimReverseResult;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NominatimService {
    @GET("reverse?format=json&addressdetails=1")
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    Single<NominatimReverseResult> reverseGeocode(@Query("lat") Double latitude, @Query("lon") Double longitude);
}
