package com.example.lunark.services;

import com.example.lunark.models.HostReviewEligibility;
import com.example.lunark.models.PropertyReviewEligibility;
import com.example.lunark.models.Review;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ReviewService {
    @GET("reviews/property-review-eligibility/{propertyId}")
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    public Single<PropertyReviewEligibility> isEligibleToReviewProperty(@Path("propertyId") Long id);

    @POST("reviews/property/{propertyId}")
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    public Completable createPropertyReview(@Body Review review, @Path("propertyId") Long id);

    @DELETE("reviews/{id}")
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    public Completable deleteReview(@Path("id") Long id);

    @GET("reviews/host/{id}")
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    public Single<List<Review>> getHostReviews(@Path("id") Long id);

    @GET("reviews/host-review-eligibility/{hostId}")
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    public Single<HostReviewEligibility> isEligibleToReviewHost(@Path("hostId") Long id);

    @POST("reviews/host/{hostId}")
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    public Completable createHostReview(@Body Review review, @Path("hostId") Long id);
}
