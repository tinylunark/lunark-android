package com.example.lunark.interceptors;

import com.example.lunark.datasources.LoginLocalDataSource;
import com.example.lunark.repositories.LoginRepository;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class JwtInterceptor implements Interceptor {
    private final LoginLocalDataSource loginLocalDataSource;

    @Inject
    public JwtInterceptor(LoginLocalDataSource loginLocalDataSource) {
        this.loginLocalDataSource = loginLocalDataSource;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (request.header("skip") == null) {
            try {
                String token = loginLocalDataSource.getToken().blockingGet().getAccessToken();
                request = request.newBuilder()
                        .addHeader("Authorization", "Bearer " + loginLocalDataSource.getToken().blockingGet().getAccessToken())
                        .build();
            } catch (Exception e) {
                return chain.proceed(request);
            }
        }
        return chain.proceed(request);
    }
}
