package com.example.lunark.datasources;

import com.example.lunark.models.AccountReport;
import com.example.lunark.services.AccountReportService;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class AccountReportNetworkDataSource {

    private AccountReportService accountReportService;
    @Inject
    AccountReportNetworkDataSource(Retrofit retrofit) {
        accountReportService = retrofit.create(AccountReportService.class);
    }

    public Single<Boolean> isEligibleToReportHost(Long id) {
        return this.accountReportService.isEligibleToReportHost(id)
                .flatMap(hostReportEligibility -> Single.just(hostReportEligibility.isEligible()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable report(AccountReport report) {
        return this.accountReportService.report(report)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
