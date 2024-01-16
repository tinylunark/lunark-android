package com.example.lunark.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.lunark.LunarkApplication;
import com.example.lunark.R;
import com.example.lunark.adapters.PropertySpinnerAdapter;
import com.example.lunark.databinding.FragmentPropertyReportBinding;
import com.example.lunark.models.Login;
import com.example.lunark.models.Property;
import com.example.lunark.repositories.LoginRepository;
import com.example.lunark.repositories.PropertyRepository;
import com.example.lunark.repositories.ReportRepository;
import com.example.lunark.viewmodels.PropertyDetailViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class PropertyReportFragment extends Fragment {
    private static final String TAG = "PropertyReportFragment";
    FragmentPropertyReportBinding mBinding;
    @Inject
    public ReportRepository mReportRepository;
    @Inject
    public LoginRepository mLoginRepository;
    PropertyDetailViewModel mViewModel; // TODO: Shortcut for now, in future use dedicated view model
    final MutableLiveData<Long> mProfileId = new MutableLiveData<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        ((LunarkApplication) getActivity().getApplication()).applicationComponent.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentPropertyReportBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this, ViewModelProvider.Factory.from(PropertyDetailViewModel.initializer)).get(PropertyDetailViewModel.class);
        View view = mBinding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.btnGetReports.setEnabled(false); // Initially disabled, when properties are fetched it should be enabled

        mLoginRepository.getLogin().subscribe(new SingleObserver<Login>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Login login) {
                Log.d(TAG, "Login onSuccess: " + login);
                mProfileId.postValue(login.getProfileId());
            }

            @Override
            public void onError(Throwable e) {

            }
        });

        mBinding.btnGetReports.setOnClickListener(v -> {
            String year = mBinding.etYear.getText().toString();
            String propertyId = String.valueOf(mBinding.spnProperty.getSelectedItemId());

            mReportRepository.getPropertyReport(year, propertyId).observe(getViewLifecycleOwner(), report -> {
                Log.d(TAG, "getPropertyReport: " + report);
            });
        });

        mProfileId.observe(getViewLifecycleOwner(), this::getMyProperties);
    }

    private void getMyProperties(Long id) {
        mViewModel.getMyProperties(id.toString()).observe(getViewLifecycleOwner(), properties -> {
            Log.d(TAG, "getMyProperties: " + properties);
            mBinding.btnGetReports.setEnabled(true);

            ArrayAdapter<Property> adapter = new PropertySpinnerAdapter(getContext(), android.R.layout.simple_spinner_item, properties);
            adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
            mBinding.spnProperty.setAdapter(adapter);
        });
    }
}