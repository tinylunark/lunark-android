package com.example.lunark.clients;

import com.example.lunark.dtos.LoginDto;
import com.example.lunark.models.Login;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface LoginService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json",
            "skip: true"
    })
    @POST("auth/login")
    Single<Login> logIn(@Body LoginDto loginDto);
}
