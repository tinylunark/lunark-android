package com.example.lunark.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.lunark.LunarkApplication;
import com.example.lunark.models.Review;
import com.example.lunark.models.ReviewType;
import com.example.lunark.repositories.ReviewRepository;

import java.time.LocalDate;

import javax.inject.Inject;

public class ReviewViewModel extends AndroidViewModel {
    @Inject
    ReviewRepository reviewRepository;

    private Long reviewedEntityId;
    private ReviewType type;
    private String comment;
    private Integer rating;

    public ReviewViewModel(@NonNull Application application) {
        super(application);
        ((LunarkApplication) application).applicationComponent.inject(this);
    }

    public Long getReviewedEntityId() {
        return reviewedEntityId;
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

    public void uploadReview() {
        Review review = new Review(null, null, null, this.rating, this.comment);
        if (this.type.equals(ReviewType.PROPERTY)) {
            uploadPropertyReview(review);
        } else if (this.type.equals(ReviewType.HOST)) {
            uploadHostReview(review);
        }
    }

    public void uploadPropertyReview(Review review) {
        // TODO: Upload review; call appropriate method of repository based on type
        Log.d("REVIEW", "Uploading property review. Rating: " + review.getRating() + " Comment: " + review.getDescription());
    }

    public void uploadHostReview(Review review) {
        // TODO: Upload host reviews
    }


}
