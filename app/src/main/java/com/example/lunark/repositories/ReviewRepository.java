package com.example.lunark.repositories;

import com.example.lunark.datasources.ReviewNetworkDataSource;
import com.example.lunark.models.Review;

import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

public class ReviewRepository {
    private ReviewNetworkDataSource reviewNetworkDataSource;

    @Inject
    public ReviewRepository(ReviewNetworkDataSource reviewNetworkDataSource) {
        this.reviewNetworkDataSource = reviewNetworkDataSource;
    }

    public Single<Boolean> isEligibleToReviewProperty(Long id)  {
        return this.reviewNetworkDataSource.isEligibleToReviewProperty(id);
    }

    public Completable createPropertyReview(Review review, Long propertyId) {
        return this.reviewNetworkDataSource.createPropertyReview(review, propertyId);
    }

    public Completable deleteReview(Long id) {
        return this.reviewNetworkDataSource.deleteReview(id);
    }

    public Single<List<Review>> getHostReviews(Long id) {
        return this.reviewNetworkDataSource.getHostReviews(id);
    }

    public Single<Boolean> isEligibleToReviewHost(Long id)  {
        return this.reviewNetworkDataSource.isEligibleToReviewHost(id);
    }
}
