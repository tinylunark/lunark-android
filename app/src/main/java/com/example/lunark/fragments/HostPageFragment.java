package com.example.lunark.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.lunark.R;
import com.example.lunark.adapters.ReviewListAdapter;
import com.example.lunark.databinding.FragmentHostPageBinding;
import com.example.lunark.models.Host;
import com.example.lunark.models.Review;
import com.example.lunark.models.ReviewType;
import com.example.lunark.util.ClientUtils;
import com.example.lunark.viewmodels.HostViewModel;
import com.example.lunark.viewmodels.ReviewViewModel;

import java.util.List;
import java.util.Objects;

public class HostPageFragment extends Fragment {
    public static final String HOST_ID_KEY = "HOST_ID";
    public static final String REQUEST_KEY = "HOST";
    private HostViewModel viewModel;
    private FragmentHostPageBinding binding;
    private Long id;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(HostViewModel.class);
        if (getArguments() != null) {
            id = requireArguments().getLong(HOST_ID_KEY);
            viewModel.init(id);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_host_page, container, false);
        binding.setViewmodel(viewModel);
        viewModel.getReviews().observe(getViewLifecycleOwner(), this::setUpReviewsRecyclerView);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadHostProfilePicture(id);
        binding.writeReviewButton.setOnClickListener(v -> openWriteReviewFragment());
        binding.reportButton.setOnClickListener(v -> openReportFragment());
    }

    private void setUpReviewsRecyclerView(List<Review> reviews) {
        RecyclerView recyclerView = binding.reviewsRecyclerview;
        ReviewListAdapter adapter = new ReviewListAdapter(this, reviews);
        recyclerView.setAdapter(adapter);

        int scrollPosition = 0;
        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.scrollToPosition(scrollPosition);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), ((LinearLayoutManager) recyclerView.getLayoutManager()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void loadHostProfilePicture(Long id) {
        Glide.with(this)
                .load(ClientUtils.SERVICE_API_PATH + "accounts/" + id + "/profile-image")
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.ic_account_on_primary_container)
                .into(binding.profilePictureImageview);
    }

    private void openWriteReviewFragment() {
        Bundle bundle = new Bundle();
        bundle.putLong(WriteReviewFragment.REVIEWED_ENTITY_ID, this.id);
        bundle.putString(WriteReviewFragment.REVIEW_TYPE, ReviewType.HOST.toString());
        getParentFragmentManager().setFragmentResult("review", bundle);
    }

    private void openReportFragment() {
        Bundle bundle = new Bundle();
        bundle.putLong(AccountReportFragment.ACCOUNT_ID_KEY, this.id);
        getParentFragmentManager().setFragmentResult(AccountReportFragment.REQUEST_KEY, bundle);
    }
}
