package com.example.lunark;

import android.app.Application;
import android.util.Log;

import com.example.lunark.models.Login;
import com.example.lunark.repositories.LoginRepository;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;

import io.reactivex.Scheduler;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginTest {
    @Test
    @Ignore("This test needs a network connection")
    public void testLogin() {
        LoginRepository loginRepository = new LoginRepository(new Application());
        ArrayList<Login> loginArrayList = new ArrayList<>();

        loginRepository.logIn("user2@example.com", "password2")
                .subscribe(new SingleObserver<Login>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Login login) {
                        loginArrayList.add(login);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println(e.getMessage());
                        Assert.assertTrue(false);
                    }
                });
        Assert.assertEquals(1, loginArrayList.size());
        Assert.assertTrue(loginArrayList.get(0).getAccessToken().length() > 0);
        System.out.println("Access token: " + loginArrayList.get(0).getAccessToken());
    }
}
