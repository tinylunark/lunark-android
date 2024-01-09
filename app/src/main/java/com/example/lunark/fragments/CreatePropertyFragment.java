package com.example.lunark.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lunark.R;
import com.example.lunark.databinding.FragmentCreatePropertyBinding;

public class CreatePropertyFragment extends Fragment {
    private FragmentCreatePropertyBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       binding = FragmentCreatePropertyBinding.inflate(inflater, container, false);
       View view = binding.getRoot();
       return view;
    }
}
