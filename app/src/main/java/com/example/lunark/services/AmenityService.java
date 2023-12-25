package com.example.lunark.services;

import com.example.lunark.models.Amenity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface AmenityService {
    String PATH = "amenities";

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json"
    })
    @GET(PATH)
    Call<List<Amenity>> getAll();
}
