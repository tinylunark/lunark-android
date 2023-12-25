package com.example.lunark.datasources;

import com.example.lunark.dtos.AccountSignUpDto;

import javax.inject.Inject;

import io.reactivex.Completable;

public class AccountRepository {
    private AccountNetworkDataSource accountNetworkDataSource;

    @Inject
    public AccountRepository(AccountNetworkDataSource accountNetworkDataSource) {
        this.accountNetworkDataSource = accountNetworkDataSource;
    }

    public Completable signUp(AccountSignUpDto accountSignUpDto) {
        return this.accountNetworkDataSource.signUp(accountSignUpDto);
    }
}
