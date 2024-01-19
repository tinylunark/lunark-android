package com.example.lunark.repositories;

import com.example.lunark.datasources.AccountReportNetworkDataSource;
import com.example.lunark.models.AccountReport;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

public class AccountReportRepository {
    private AccountReportNetworkDataSource accountReportNetworkDataSource;

    @Inject
    public AccountReportRepository(AccountReportNetworkDataSource accountReportNetworkDataSource) {
        this.accountReportNetworkDataSource = accountReportNetworkDataSource;
    }

    public Single<Boolean> isEligibleToReportHost(Long id) {
        return this.accountReportNetworkDataSource.isEligibleToReportHost(id);
    }

    public Completable report(AccountReport report) {
        return this.accountReportNetworkDataSource.report(report);
    }

}
