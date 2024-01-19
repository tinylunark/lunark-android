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
import com.example.lunark.adapters.CommentManagementListAdapter;
import com.example.lunark.databinding.FragmentAdminCommentsRemovalBinding;
import com.example.lunark.repositories.LoginRepository;
import com.example.lunark.viewmodels.ReviewReportViewModel;

import java.util.ArrayList;

import javax.inject.Inject;
public class AdminCommentsRemovalFragment extends Fragment {

    private FragmentAdminCommentsRemovalBinding binding;

    private ReviewReportViewModel viewModel;
    private CommentManagementListAdapter adapter;
    private RecyclerView recyclerView;
    @Inject
    public LoginRepository loginRepository;

    public AdminCommentsRemovalFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ((LunarkApplication)getActivity().getApplication()).applicationComponent.inject(this); ;
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminCommentsRemovalBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        viewModel = new ViewModelProvider(this, ViewModelProvider.Factory.from(ReviewReportViewModel.initializer)).get(ReviewReportViewModel.class);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        observeReviewReports();
        setUpReviewReportList();
    }

    private void observeReviewReports() {
        viewModel.getReviewReports().observe(getViewLifecycleOwner(), reports -> {
            adapter.setReviews(reports);
            recyclerView.setAdapter(adapter);
        });
    }

    private void setUpReviewReportList() {
        recyclerView = binding.commentRemovalRecyclerView;
        adapter = new CommentManagementListAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        int scrollPosition = 0;
        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.scrollToPosition(scrollPosition);
    }
}