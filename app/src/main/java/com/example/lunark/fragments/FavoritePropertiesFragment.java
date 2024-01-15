package com.example.lunark.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lunark.LunarkApplication;
import com.example.lunark.adapters.PropertyListAdapter;
import com.example.lunark.databinding.FragmentFavoritePropertiesBinding;
import com.example.lunark.datasources.AccountRepository;

import java.util.ArrayList;

import javax.inject.Inject;

public class FavoritePropertiesFragment extends Fragment {
    @Inject
    AccountRepository mAccountRepository;
    private FragmentFavoritePropertiesBinding mBinding;
    private RecyclerView mRecyclerView;
    private PropertyListAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ((LunarkApplication) getActivity().getApplication()).applicationComponent.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentFavoritePropertiesBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpRecyclerView();

        mAccountRepository.getFavoriteProperties().observe(getViewLifecycleOwner(), properties -> {
            mAdapter.setProperties(properties);
            mRecyclerView.setAdapter(mAdapter);
        });
    }

    private void setUpRecyclerView() {
        mRecyclerView = mBinding.recyclerView;
        mAdapter = new PropertyListAdapter(this, new ArrayList<>());
        mRecyclerView.setAdapter(mAdapter);

        int scrollPosition = 0;
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.scrollToPosition(scrollPosition);
    }
}