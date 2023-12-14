package com.example.lunark.services;

import com.example.lunark.models.Property;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface PropertyService {
    String PATH = "properties";

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json"
    })
    @GET(PATH)
    Call<List<Property>> getAll(@QueryMap Map<String, String> params);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json"
    })
    @GET(PATH + "/{id}")
    Call<Property> getById(@Path("id") Long id);
}
