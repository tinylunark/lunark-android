package com.example.lunark.fragments.createProperty;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.lunark.R;
import com.example.lunark.databinding.FragmentCreatePropertyStep4Binding;
import com.example.lunark.models.Property;
import com.example.lunark.viewmodels.PropertyDetailViewModel;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class CreatePropertyStep4Fragment extends Fragment implements BlockingStep {
    FragmentCreatePropertyStep4Binding binding;
    private PropertyDetailViewModel viewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCreatePropertyStep4Binding.inflate(inflater, container, false);
        View view = binding.getRoot();
        assert this.getParentFragment() != null;
        viewModel = new ViewModelProvider(this.getParentFragment(), ViewModelProvider.Factory.from(PropertyDetailViewModel.initializer)).get(PropertyDetailViewModel.class);
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

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
        this.viewModel.uploadProperty().subscribe(new SingleObserver<Property>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Property property) {
                Toast.makeText(getContext(), getString(R.string.property_created_successfully), Toast.LENGTH_SHORT).show();
                callback.complete();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getContext(), R.string.something_went_wrong_while_adding_your_property, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }
}
