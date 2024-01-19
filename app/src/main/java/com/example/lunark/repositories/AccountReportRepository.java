package com.example.lunark.repositories;

import com.example.lunark.datasources.AccountReportNetworkDataSource;
import com.example.lunark.models.AccountReport;
import com.example.lunark.services.AccountReportService;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.Retrofit;

public class AccountReportRepository {
    private AccountReportNetworkDataSource accountReportNetworkDataSource;
    private LoginRepository loginRepository;

    @Inject
    public AccountReportRepository(AccountReportNetworkDataSource accountReportNetworkDataSource, LoginRepository loginRepository) {
        this.accountReportNetworkDataSource = accountReportNetworkDataSource;
        this.loginRepository = loginRepository;
    }

    public Single<Boolean> isEligibleToReportHost(Long id) {
        return this.accountReportNetworkDataSource.isEligibleToReportHost(id);
    }

    public Completable report(AccountReport report) {
        return this.accountReportNetworkDataSource.report(report);
    }

}
