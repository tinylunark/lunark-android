package com.example.lunark.datasources;

import com.example.lunark.clients.AccountService;
import com.example.lunark.dtos.AccountSignUpDto;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class AccountNetworkDataSource {
    private static AccountService accountService;

    @Inject
    public AccountNetworkDataSource(Retrofit retrofit) {
        accountService = retrofit.create(AccountService.class);
    }

    public Completable signUp(AccountSignUpDto accountSignUpDto)  {
        return accountService.signUp(accountSignUpDto)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
