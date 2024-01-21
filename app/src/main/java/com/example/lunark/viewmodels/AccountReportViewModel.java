package com.example.lunark.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.lunark.LunarkApplication;
import com.example.lunark.models.AccountReport;
import com.example.lunark.models.AccountReportDisplay;
import com.example.lunark.repositories.AccountReportRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;

public class AccountReportViewModel extends AndroidViewModel {
    private final static String TAG ="ACCOUNT_REPORT_VIEW_MODEL";
    private String reason;
    private Long reportedAccountId;
    @Inject
    AccountReportRepository accountReportRepository;
    public AccountReportViewModel(@NonNull Application application) {
        super(application);
        ((LunarkApplication) application).applicationComponent.inject(this);
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

    public LiveData<List<AccountReportDisplay>> getReportedAccounts() {
        return accountReportRepository.getReportedAccounts();
    }

    public void setReportedAccountId(Long reportedAccountId) {
        this.reportedAccountId = reportedAccountId;
    }

    public Completable uploadReport() {
        Log.d(TAG, "Reporting user with id " + reportedAccountId + " for reason: " + reason);
        AccountReport report = new AccountReport(this.reportedAccountId, this.getReason());
        return this.accountReportRepository.report(report);
    }

}
