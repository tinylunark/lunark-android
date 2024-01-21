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
import com.example.lunark.adapters.UserManagementListAdapter;
import com.example.lunark.databinding.FragmentBlockUserBinding;
import com.example.lunark.repositories.LoginRepository;
import com.example.lunark.viewmodels.AccountReportViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

public class BlockUserFragment extends Fragment {

    private FragmentBlockUserBinding binding;

    private AccountReportViewModel viewModel;
    private UserManagementListAdapter adapter;
    private RecyclerView recyclerView;
    @Inject
    public LoginRepository loginRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ((LunarkApplication)getActivity().getApplication()).applicationComponent.inject(this); ;
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBlockUserBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        viewModel = new ViewModelProvider(this).get(AccountReportViewModel.class);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        observeAccountReports();
        setUpReviewReportList();
    }

    private void observeAccountReports() {
        viewModel.getReportedAccounts().observe(getViewLifecycleOwner(), accounts -> {
            adapter.setReviews(accounts);
            recyclerView.setAdapter(adapter);
        });
    }

    private void setUpReviewReportList() {
        recyclerView = binding.blockUserRecyclerView;
        adapter = new UserManagementListAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        int scrollPosition = 0;
        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.scrollToPosition(scrollPosition);
    }
}