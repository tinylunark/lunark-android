package com.example.lunark.datasources;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.lunark.models.Review;
import com.example.lunark.services.ReviewService;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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

    public Completable deleteReview(Long id) {
        return this.reviewService.deleteReview(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> Log.d(TAG, "Successfully delete review with id: " + id));
    }

    public Single<List<Review>> getHostReviews(Long id) {
        return this.reviewService.getHostReviews(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(reviews -> Log.d(TAG, "Successfully fetched reviews for host with id: " + id));
    }

    public Single<Boolean> isEligibleToReviewHost(Long id) {
        return this.reviewService.isEligibleToReviewHost(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(hostReviewEligibility -> Log.d(TAG, "Successfully fetched host review eligibility for host with id " + hostReviewEligibility.isEligible()))
                .flatMap(hostReviewEligibility -> Single.just(hostReviewEligibility.isEligible()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public Completable createHostReview(Review review, Long hostId) {
        return this.reviewService.createHostReview(review, hostId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> Log.d(TAG, "Successfully uploaded host review"));
    }

    public LiveData<List<Review>> getUnapprovedReviews() {
        final MutableLiveData<List<Review>> data = new MutableLiveData<>();

        reviewService.getUnapprovedReviews().enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                if (response.isSuccessful()) {
                    List<Review> reservations = response.body();
                    data.setValue(response.body());
                } else {
                }
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
            }
        });

        return data;
    }

    public void approveReview(Long id) {
        reviewService.approveReview(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                } else {
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    public Single<List<Review>> getPropertyReviews(Long id) {
        return this.reviewService.getPropertyReviews(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(reviews -> Log.d(TAG, "Successfully fetched reviews for property with id: " + id));
    }
}
