package com.example.lunark.fragments.createProperty;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lunark.databinding.FragmentCreatePropertyStep2Binding;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

public class CreatePropertyStep2Fragment extends Fragment implements Step {
    FragmentCreatePropertyStep2Binding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCreatePropertyStep2Binding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}
