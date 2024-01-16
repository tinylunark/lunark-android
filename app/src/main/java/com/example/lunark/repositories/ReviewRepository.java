package com.example.lunark.repositories;

import com.example.lunark.datasources.ReviewNetworkDataSource;

import javax.inject.Inject;

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
}
