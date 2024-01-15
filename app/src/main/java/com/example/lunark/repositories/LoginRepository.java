package com.example.lunark.repositories;

import android.util.Base64;
import android.util.Log;

import com.example.lunark.datasources.LoginLocalDataSource;
import com.example.lunark.datasources.LoginNetworkDataSource;
import com.example.lunark.models.Login;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
public String extractRoleFromJwt(String token) {
        try {
            String[] splitToken = token.split("\\.");
            String base64EncodedBody = splitToken[1];
            String body = new String(Base64.decode(base64EncodedBody, Base64.DEFAULT));
            JSONObject jsonObject = new JSONObject(body);

            JSONArray roles = jsonObject.getJSONArray("role");
            if (roles.length() > 0) {
                JSONObject firstRole = roles.getJSONObject(0);
                return firstRole.getString("authority");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public String getLoggedUserId() {
        String token = loginLocalDataSource.getToken().toString();
        if (token == null || token.isEmpty()) {
            return null;
        }

        try {
            String[] splitToken = token.split("\\.");
            String base64EncodedBody = splitToken[1];
            String body = new String(Base64.decode(base64EncodedBody, Base64.DEFAULT));
            JSONObject jsonObject = new JSONObject(body);
            if (jsonObject.has("id")) {
                return jsonObject.getString("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public Completable logOut() {
        return loginNetworkDataSource.logOut().doOnComplete(() -> loginLocalDataSource.deleteToken());
    }

    public Completable clearToken() {
        return loginLocalDataSource.deleteToken().flatMapCompletable(preferences -> Completable.complete());
    }
}
