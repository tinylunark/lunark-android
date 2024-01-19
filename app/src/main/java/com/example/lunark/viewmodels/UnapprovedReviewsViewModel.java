package com.example.lunark.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.lunark.LunarkApplication;
import com.example.lunark.models.Review;
import com.example.lunark.repositories.ReviewRepository;

import java.util.List;

import javax.inject.Inject;

public class UnapprovedReviewsViewModel extends AndroidViewModel {

    @Inject
    ReviewRepository reviewRepository;
    private final LiveData<List<Review>> properties = new MutableLiveData<>();

    public UnapprovedReviewsViewModel(@NonNull Application application, ReviewRepository propertyRepository) {
        super(application);
        ((LunarkApplication) application).applicationComponent.inject(this);
    }

    public LiveData<List<Review>> getReviews() {
        return properties;
    }

    public LiveData<List<Review>> getUnapprovedReviews() {
        return reviewRepository.getUnapprovedReviews();
    }
}
