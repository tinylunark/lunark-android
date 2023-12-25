package com.example.lunark.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lunark.databinding.ActivityPropertyBinding;
import com.example.lunark.models.Property;
import com.example.lunark.util.ClientUtils;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PropertyActivity extends AppCompatActivity {

    private Property property;
    private ActivityPropertyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPropertyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();

        Long propertyId = intent.getLongExtra("propertyId", 0);
        getProperty(propertyId);
    }

    private void getPropertyImage(Property property) {
        if (property.getImages().size() == 0) {
            return;
        }

        Call<ResponseBody> call = ClientUtils.propertyService.getImage(property.getId(), property.getImages().get(0).getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                        binding.thumbnail.setImageBitmap(bmp);
                    } else {
                        Log.d("REZ", "Response body is null.");
                    }
                } else {
                    Log.d("REZ", "Message received: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("PROPERTY", "Failed to get property image: " + t.getMessage());
            }
        });
    }

    private void getProperty(Long propertyId) {
        Call<Property> call = ClientUtils.propertyService.getById(propertyId);
        call.enqueue(new Callback<Property>() {
            @Override
            public void onResponse(Call<Property> call, Response<Property> response) {
                if (response.code() == 200) {
                    Property property = response.body();
                    binding.name.setText(property.getName());
                    binding.location.setText(property.getAddress().toString());
                    binding.description.setText(property.getDescription());
                    binding.minGuests.setText("Minimum guests: " + String.valueOf(property.getMinGuests()));
                    binding.maxGuests.setText("Maximum guests: " + String.valueOf(property.getMaxGuests()));
                    getPropertyImage(property);
                }
            }

            @Override
            public void onFailure(Call<Property> call, Throwable t) {
                Log.e("PROPERTY", "Failed to get property: " + t.getMessage());
            }
        });
    }
}