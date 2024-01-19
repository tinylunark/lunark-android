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

import com.example.lunark.R;
import com.example.lunark.databinding.FragmentAccountReportBinding;
import com.example.lunark.viewmodels.AccountReportViewModel;
import com.google.android.material.snackbar.Snackbar;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;

public class AccountReportFragment extends Fragment {
    public static String REQUEST_KEY = "account_report";
    public static String ACCOUNT_ID_KEY = "ACCOUNT_ID";
    private FragmentAccountReportBinding binding;
    private AccountReportViewModel viewModel;
    private Disposable subscription;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AccountReportViewModel.class);
        viewModel.setReportedAccountId(requireArguments().getLong(ACCOUNT_ID_KEY));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account_report, container, false);
        binding.submitButton.setOnClickListener(v -> submitReport());
        binding.setViewmodel(viewModel);
        return binding.getRoot();
    }

    private boolean isValid() {
        String reason = viewModel.getReason();
        return !reason.isEmpty() &&
                !reason.matches(" +") &&
                reason.matches("[a-zA-Z0-9 \\.!\\?]+");
    }

    private void submitReport() {
        if (!isValid()) {
            Snackbar.make(binding.getRoot(), "The reason was not filled or contains special characters other than punctuation marks", Snackbar.LENGTH_SHORT).show();
            return;
        }

        this.viewModel.uploadReport().subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
               subscription = d;
            }

            @Override
            public void onComplete() {
                Snackbar.make(binding.getRoot(), R.string.report_submitted_successfully, Snackbar.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().popBackStack();
            }

            @Override
            public void onError(Throwable e) {
                Snackbar.make(binding.getRoot(), R.string.something_went_wrong, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
        super.onDestroy();
    }
}
