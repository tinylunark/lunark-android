package com.example.lunark.datasources;

import android.util.Log;

import com.example.lunark.models.Review;
import com.example.lunark.services.ReviewService;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class ReviewNetworkDataSource {
    private static final String TAG = "REVIEW_NETWORK_DATA_SOURCE";
    private ReviewService reviewService;

    @Inject
    ReviewNetworkDataSource(Retrofit retrofit) {
        reviewService = retrofit.create(ReviewService.class);
    }

    public Single<Boolean> isEligibleToReviewProperty(Long id) {
        return this.reviewService.isEligibleToReviewProperty(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(propertyReviewEligibility -> Log.d(TAG, "Successfully fetched property review eligibility for property with id " + propertyReviewEligibility.getPropertyId()))
                .flatMap(propertyReviewEligibility -> Single.just(propertyReviewEligibility.isEligible()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public Completable createPropertyReview(Review review, Long propertyId) {
        return this.reviewService.createPropertyReview(review, propertyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> Log.d(TAG, "Successfully uploaded property review"));
    }

}
