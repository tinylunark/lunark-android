package com.example.lunark.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lunark.LunarkApplication;
import com.example.lunark.adapters.UnapprovedReviewsListAdapter;
import com.example.lunark.databinding.FragmentAdminCommentsApprovalBinding;
import com.example.lunark.repositories.LoginRepository;
import com.example.lunark.viewmodels.ReviewViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

public class AdminCommentsApprovalFragment extends Fragment {

    private FragmentAdminCommentsApprovalBinding binding;

    private ReviewViewModel viewModel;
    private UnapprovedReviewsListAdapter adapter;
    private RecyclerView recyclerView;

    public AdminCommentsApprovalFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminCommentsApprovalBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        viewModel = new ViewModelProvider(this).get(ReviewViewModel.class);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        observeReviews();
        setUpReviewList();
    }

    private void observeReviews() {
        viewModel.getUnapprovedReviews().observe(getViewLifecycleOwner(), reviews -> {
            adapter.setReviews(reviews);
            recyclerView.setAdapter(adapter);
        });
    }

    private void setUpReviewList() {
        recyclerView = binding.commentApprovalRecyclerView;
        adapter = new UnapprovedReviewsListAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        int scrollPosition = 0;
        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.scrollToPosition(scrollPosition);
    }

}