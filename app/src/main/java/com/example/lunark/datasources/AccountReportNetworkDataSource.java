package com.example.lunark.datasources;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.lunark.models.AccountReport;
import com.example.lunark.models.AccountReportDisplay;
import com.example.lunark.services.AccountReportService;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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

    public LiveData<List<AccountReportDisplay>> getReportedAccounts() {
        final MutableLiveData<List<AccountReportDisplay>> data = new MutableLiveData<>();
        accountReportService.getReportedAccounts().enqueue(new Callback<List<AccountReportDisplay>>() {
            @Override
            public void onResponse(Call<List<AccountReportDisplay>> call, Response<List<AccountReportDisplay>> response) {
                if (response.isSuccessful()) {
                    List<AccountReportDisplay> reservations = response.body();
                    data.setValue(response.body());
                } else {
                }
            }

            @Override
            public void onFailure(Call<List<AccountReportDisplay>> call, Throwable t) {
            }
        });

        return data;
    }

    public void blockAccount(Long id) {
        accountReportService.blockAccount(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                } else {
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }
}
