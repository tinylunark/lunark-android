package com.example.lunark.clients;

import com.example.lunark.dtos.AccountDto;
import com.example.lunark.dtos.ProfileDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AccountService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("accounts/{id}")
    Call<AccountDto> getAccount(@Path("id") Long id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT("accounts/{id}")
    Call<ProfileDto> updateProfile(@Path("id") Long id, @Body ProfileDto profile);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @DELETE("accounts/{id}")
    Call<Void> deleteAccount(@Path("id") Long id);
}
