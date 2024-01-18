package com.example.lunark.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableChar;
import androidx.lifecycle.AndroidViewModel;

import io.reactivex.Completable;

public class AccountReportViewModel extends AndroidViewModel {
    private final static String TAG ="ACCOUNT_REPORT_VIEW_MODEL";
    private String reason;
    private Long reportedAccountId;
    public AccountReportViewModel(@NonNull Application application) {
        super(application);
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getReportedAccountId() {
        return reportedAccountId;
    }

    public void setReportedAccountId(Long reportedAccountId) {
        this.reportedAccountId = reportedAccountId;
    }

    public Completable uploadReport() {
        Log.d(TAG, "Reporting user with id " + reportedAccountId + " for reason: " + reason);
        return Completable.complete();
    }
}
