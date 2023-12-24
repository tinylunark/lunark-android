package com.example.lunark.repositories;

import com.example.lunark.datasources.LoginNetworkDataSource;
import com.example.lunark.models.Login;

import io.reactivex.Single;

public class LoginRepository {
    private LoginNetworkDataSource loginNetworkDataSource;

    public LoginRepository() {
        this.loginNetworkDataSource = new LoginNetworkDataSource();
    }
    public Single<Login> logIn(String username, String password) {
        return loginNetworkDataSource.logIn(username, password);
    }

    public Single<Login> getLogin() {
        return Single.error(new Exception("Not implemented yet"));
    }
}
