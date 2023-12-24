package com.example.lunark.repositories;

import android.app.Application;
import android.util.Log;

import com.example.lunark.datasources.LoginLocalDataSource;
import com.example.lunark.datasources.LoginNetworkDataSource;
import com.example.lunark.models.Login;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class LoginRepository {
    private LoginNetworkDataSource loginNetworkDataSource;
    private LoginLocalDataSource loginLocalDataSource;

    public LoginRepository(Application application) {
        this.loginNetworkDataSource = new LoginNetworkDataSource();
        this.loginLocalDataSource = new LoginLocalDataSource(application);
    }
    public Single<Login> logIn(String username, String password) {
        return loginNetworkDataSource.logIn(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(throwable -> Single.error(throwable))
                .flatMap(login -> {
                    loginLocalDataSource.writeToken(login.getAccessToken()).subscribe(
                            preferences -> Log.d("AUTH", "Wrote token to datastore")
                    );
                    return Single.just(login);
                });
    }

    public Single<Login> getLogin() {
        Log.d("AUTH", "Getting token from datastore");
        return loginLocalDataSource.getToken().firstOrError();
    }
}
