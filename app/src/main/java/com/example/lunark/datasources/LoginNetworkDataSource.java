package com.example.lunark.datasources;

import android.util.Log;

import com.example.lunark.clients.LoginService;
import com.example.lunark.dtos.LoginDto;
import com.example.lunark.models.Login;
import com.example.lunark.util.ClientUtils;

import javax.inject.Inject;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;
import retrofit2.Retrofit;

public class LoginNetworkDataSource {
    public static LoginService loginService = ClientUtils.retrofit.create(LoginService.class);
    @Inject
    public LoginNetworkDataSource(Retrofit retrofit) {
        loginService = retrofit.create(LoginService.class);
    }
    public Single<Login> logIn(String username, String password) {
        LoginDto loginDto = new LoginDto(username, password);

        Single<Login> call = loginService.logIn(loginDto);
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Login>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Login login) {
                        Log.d("AUTH", "Login successful");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("AUTH", "Login fail. Message: " + e.getMessage());
                    }
        });
        return call;
    }

    public Completable logOut() {
        return loginService.logOut()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> Log.e("AUTH", "Logout request error: ", throwable));
    }
}
