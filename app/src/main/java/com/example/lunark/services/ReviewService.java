package com.example.lunark.services;

import com.example.lunark.models.PropertyReviewEligibility;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface ReviewService {
    @GET("reviews/property-review-eligibility/{propertyId}")
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    public Single<PropertyReviewEligibility> isEligibleToReviewProperty(@Path("propertyId") Long id);
}
