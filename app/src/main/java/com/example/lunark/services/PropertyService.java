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
    @GET("properties")
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    Call<List<Property>> getProperties(@QueryMap Map<String, String> options);

    @GET("properties/{id}")
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    Call<Property> getProperty(@Path("id") Long id);

    @GET("properties/{id}/images/{imageId}")
    @Headers({
            "User-Agent: Mobile-Android",
    })
    Call<ResponseBody> getPropertyImage(@Path("id") Long id, @Path("imageId") Long imageId);
}
