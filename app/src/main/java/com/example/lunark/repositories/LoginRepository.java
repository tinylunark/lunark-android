package com.example.lunark.repositories;

import android.app.Application;
import android.util.Log;

import androidx.datastore.preferences.core.Preferences;

import com.example.lunark.datasources.LoginLocalDataSource;
import com.example.lunark.datasources.LoginNetworkDataSource;
import com.example.lunark.models.Login;

import javax.inject.Inject;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class LoginRepository {
    private final LoginNetworkDataSource loginNetworkDataSource;
    private final LoginLocalDataSource loginLocalDataSource;

    @Inject
    public LoginRepository(LoginNetworkDataSource loginNetworkDataSource, LoginLocalDataSource loginLocalDataSource) {
        this.loginNetworkDataSource = loginNetworkDataSource;
        this.loginLocalDataSource = loginLocalDataSource;
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
        return loginLocalDataSource.getToken();
    }

    public Completable logOut() {
        return loginNetworkDataSource.logOut().doOnComplete(() -> loginLocalDataSource.deleteToken());
    }

    public Completable clearToken() {
        return loginLocalDataSource.deleteToken().flatMapCompletable(preferences -> Completable.complete());
    }
}
