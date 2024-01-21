package com.example.lunark.fragments.updateProperty;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lunark.LunarkApplication;
import com.example.lunark.databinding.*;
import com.example.lunark.adapters.MyPropertiesListAdapter;
import com.example.lunark.databinding.FragmentPropertyApprovalBinding;
import com.example.lunark.models.Login;
import com.example.lunark.repositories.LoginRepository;
import com.example.lunark.viewmodels.UnapprovedPropertiesViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class MyPropertiesFragment extends Fragment {
    private FragmentMyPropertiesBinding binding;
    private UnapprovedPropertiesViewModel viewModel;
    private MyPropertiesListAdapter adapter;
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
        binding = FragmentMyPropertiesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        viewModel = new ViewModelProvider(this, ViewModelProvider.Factory.from(UnapprovedPropertiesViewModel.initializer)).get(UnapprovedPropertiesViewModel.class);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpUnapprovedPropertiesList();
        loginRepository.getLogin().subscribe(new SingleObserver<Login>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onSuccess(Login login) {
                Long profileId = login.getProfileId();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        observeUnapprovedProperties(profileId);
                    }
                });
            }

            @Override
            public void onError(Throwable e) {
            }
        });

    }

    private void observeUnapprovedProperties(Long hostId) {
        viewModel.getMyProperties(hostId).observe(getViewLifecycleOwner(), properties -> {
            adapter.setProperties(properties);
            recyclerView.setAdapter(adapter);
        });
    }

    private void setUpUnapprovedPropertiesList() {
        recyclerView = binding.myPropertiesRecyclerView;
        adapter = new MyPropertiesListAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        int scrollPosition = 0;
        if (recyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.scrollToPosition(scrollPosition);
    }
}