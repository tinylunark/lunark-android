package com.example.lunark.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingMethod;
import androidx.databinding.InverseBindingMethods;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.lunark.R;
import com.example.lunark.databinding.FragmentWriteReviewBinding;
import com.example.lunark.models.ReviewType;
import com.example.lunark.viewmodels.PropertyDetailViewModel;
import com.example.lunark.viewmodels.ReviewViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;

public class WriteReviewFragment extends Fragment {

    public static String REVIEWED_ENTITY_ID = "REVIEWED_ENTITY_ID ";
    public static String REVIEW_TYPE = "REVIEW_TYPE";
    private Long reviewedEntityId;
    private ReviewType reviewType;
    private ReviewViewModel viewModel;
    private FragmentWriteReviewBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ReviewViewModel.class);
        if (getArguments() != null) {
            reviewedEntityId = requireArguments().getLong(REVIEWED_ENTITY_ID);
            reviewType = ReviewType.fromString(Objects.requireNonNull(requireArguments().getString(REVIEW_TYPE)));
            viewModel.setReviewedEntityId(reviewedEntityId);
            viewModel.setType(reviewType);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_write_review, container, false);
        binding.setViewmodel(viewModel);
        return binding.getRoot();
    }
}
