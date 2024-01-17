package com.example.lunark.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingMethod;
import androidx.databinding.InverseBindingMethods;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.lunark.models.ReviewType;
import com.example.lunark.viewmodels.PropertyDetailViewModel;
import com.example.lunark.viewmodels.ReviewViewModel;

public class WriteReviewFragment extends Fragment {

    public static String REVIEWED_ENTITY_ID = "REVIEWED_ENTITY_ID ";
    public static String REVIEW_TYPE = "REVIEW_TYPE";
    private Long reviewedEntityId;
    private ReviewType reviewType;
    private ReviewViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ReviewViewModel.class);
        if (getArguments() != null) {
            reviewedEntityId = getArguments().getLong(REVIEWED_ENTITY_ID);
            reviewType = ReviewType.fromString(getArguments().getString(REVIEW_TYPE));
            viewModel.setReviewedEntityId(reviewedEntityId);
            viewModel.setType(reviewType);
        }

    }


}
