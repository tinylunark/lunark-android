package com.example.lunark.fragments.createProperty;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.lunark.databinding.FragmentCreatePropertyBinding;
import com.example.lunark.sensors.ProximitySensorFragment;
import com.example.lunark.viewmodels.PropertiesViewModel;
import com.example.lunark.viewmodels.PropertyDetailViewModel;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

public class CreatePropertyFragment extends ProximitySensorFragment implements IAllowBackPressed {
    private final static int NUM_STEPS = 4;
    private FragmentCreatePropertyBinding binding;
    private StepperLayout stepper;
    private PropertyDetailViewModel viewModel;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       binding = FragmentCreatePropertyBinding.inflate(inflater, container, false);
       viewModel = new ViewModelProvider(this, ViewModelProvider.Factory.from(PropertyDetailViewModel.initializer)).get(PropertyDetailViewModel.class);
       if (viewModel.getProperty().getValue() == null) {
           viewModel.initProperty();
       }
       View view = binding.getRoot();
       stepper = binding.stepper;
       stepper.setAdapter(new MyStepperAdapter(getChildFragmentManager(), this.getContext()));
       return view;
    }

    @Override
    public boolean allowBackPressed() {
        if (stepper.getCurrentStepPosition() == 0) {
            Log.d("CREATE_PROPERTY", "BACK_PRESS_ALLOW");
            return true;
        } else {
            Log.d("CREATE_PROPERTY", "BACK_PRESS_FORBID");
            stepper.setCurrentStepPosition(stepper.getCurrentStepPosition() - 1);
            return false;
        }
    }

    @Override
    public void onWave() {
        Log.d("CREATE_PROPERTY", "GOT WAVE");
        viewModel.initProperty();
        stepper.setCurrentStepPosition(0);
    }


    private static class MyStepperAdapter extends AbstractFragmentStepAdapter {

        public MyStepperAdapter(FragmentManager fm, Context context) {
            super(fm, context);
        }

        @Override
        public Step createStep(int position) {
            switch (position) {
                case 0:
                    return new CreatePropertyStep1Fragment();
                case 1:
                    return new CreatePropertyStep2Fragment();
                case 2:
                    return new CreatePropertyStep3Fragment();
                case 3:
                    return new CreatePropertyStep4Fragment();
            }
            throw new RuntimeException("Attempted to create nonexistent create property step");
        }

        @Override
        public int getCount() {
            return NUM_STEPS;
        }

        @NonNull
        @Override
        public StepViewModel getViewModel(@IntRange(from = 0) int position) {
            //Override this method to set Step title for the Tabs, not necessary for other stepper types
            return new StepViewModel.Builder(context)
                    .setEndButtonLabel(position == (NUM_STEPS-1) ? "SUBMIT" : "Next")
                    .setTitle("Title") //can be a CharSequence instead
                    .create();
        }
    }
}
