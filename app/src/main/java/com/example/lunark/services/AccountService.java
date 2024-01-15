package com.example.lunark.services;

import com.example.lunark.models.Property;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface AccountService {
    @GET("accounts/favorites")
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    Call<List<Property>> getFavoriteProperties();
}
