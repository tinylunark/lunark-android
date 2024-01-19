package com.example.lunark.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.lunark.LunarkApplication;
import com.example.lunark.models.Review;
import com.example.lunark.models.ReviewType;
import com.example.lunark.repositories.ReviewRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;

public class ReviewViewModel extends AndroidViewModel {
    @Inject
    ReviewRepository reviewRepository;

    private Long reviewedEntityId;
    private ReviewType type;
    private String comment;
    private Integer rating;

    private ObservableInt reviewCount;

    private List<Review> reviews = new ArrayList<>();

    public ReviewViewModel(@NonNull Application application) {
        super(application);
        ((LunarkApplication) application).applicationComponent.inject(this);
    }

    public Long getReviewedEntityId() {
        return reviewedEntityId;
    }

    public LiveData<List<Review>> getUnapprovedReviews() {
        return reviewRepository.getUnapprovedReviews();
    }

    public void setReviewedEntityId(Long reviewedEntityId) {
        this.reviewedEntityId = reviewedEntityId;
    }

    public ReviewType getType() {
        return type;
    }

    public void setType(ReviewType type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Completable uploadReview() {
        Review review = new Review(null, null, null, this.rating, this.comment);
        if (this.type.equals(ReviewType.PROPERTY)) {
            return uploadPropertyReview(review);
        } else if (this.type.equals(ReviewType.HOST)) {
            return uploadHostReview(review);
        }
        return Completable.error(new IllegalArgumentException("Invalid review type in ReviewViewModel"));
    }

    public Completable uploadPropertyReview(Review review) {
        Log.d("REVIEW", "Uploading property review. Rating: " + review.getRating() + " Comment: " + review.getDescription());
        return reviewRepository.createPropertyReview(review, reviewedEntityId);
    }

    public Completable uploadHostReview(Review review) {
        // TODO: Upload host reviews
        Log.d("REVIEW", "Uploading host review. Rating: " + review.getRating() + " Comment: " + review.getDescription());
        return reviewRepository.createHostReview(review, reviewedEntityId);
    }


}
