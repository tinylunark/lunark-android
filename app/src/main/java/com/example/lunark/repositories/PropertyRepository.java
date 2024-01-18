package com.example.lunark.repositories;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.lunark.models.Property;
import com.example.lunark.services.PropertyService;
import com.example.lunark.util.ClientUtils;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.internal.operators.completable.CompletableAmb;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PropertyRepository {
    private static final String LOG_TAG = "PropertyRepository";
    PropertyService propertyService;

    public PropertyRepository() {
        this.propertyService = ClientUtils.propertyService;
    }

    @Inject
    public void setRetrofit(Retrofit retrofit) {
        this.propertyService = retrofit.create(PropertyService.class);
    }

    public LiveData<List<Property>> getProperties(Map<String, String> options) {
        final MutableLiveData<List<Property>> data = new MutableLiveData<>();

        propertyService.getProperties(options).enqueue(new Callback<List<Property>>() {
            @Override
            public void onResponse(Call<List<Property>> call, Response<List<Property>> response) {
                if (response.isSuccessful()) {
                    Log.i(LOG_TAG, "Get properties response: " + response.body());
                    data.setValue(response.body());
                } else {
                    Log.w(LOG_TAG, "Get properties response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Property>> call, Throwable t) {
                Log.e(LOG_TAG, "Get properties failure: " + t.getMessage());
            }
        });

        Log.d(LOG_TAG, "Returning data");
        return data;
    }

    public LiveData<Property> getProperty(Long id) {
        final MutableLiveData<Property> data = new MutableLiveData<>();

        propertyService.getProperty(id).enqueue(new Callback<Property>() {
            @Override
            public void onResponse(Call<Property> call, Response<Property> response) {
                if (response.isSuccessful()) {
                    Log.i(LOG_TAG, "Get property response: " + response.body());
                    data.setValue(response.body());
                } else {
                    Log.w(LOG_TAG, "Get properties response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Property> call, Throwable t) {
                Log.e(LOG_TAG, "Get property failure: " + t.getMessage());
            }
        });

        return data;
    }

    public Single<Property> createProperty(Property property) {
        return propertyService.createProperty(property)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(property1 -> {
                    Log.i(LOG_TAG, "Uploaded property. New property id" + property1.getId());
                })
                .doOnError(throwable -> {
                    Log.e(LOG_TAG, "Upload property failure: " + throwable.getMessage());
                });
    }

    public Completable uploadImage(Long propertyId, Bitmap image) {
        return propertyService.uploadImage(propertyId, getImagePart(image))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> Log.i(LOG_TAG, "Uploaded image for property id" + propertyId));
    }

    private MultipartBody.Part getImagePart(Bitmap image) {
        return MultipartBody.Part.createFormData("image", "image", getRequestBodyForBitmap(image));
    }
    public static RequestBody getRequestBodyForBitmap(Bitmap bitmap) {
        byte[] byteArray = getByteArrayFromBitmap(bitmap);
        return RequestBody.create(MediaType.parse("image/jpg"), byteArray);
    }
    public static byte[] getByteArrayFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.WEBP, 80, stream);
        return stream.toByteArray();
    }

    public LiveData<List<Property>> getMyProperties(@NonNull String hostId) {
        final MutableLiveData<List<Property>> data = new MutableLiveData<>();

        propertyService.getMyProperties(hostId).enqueue(new Callback<List<Property>>() {
            @Override
            public void onResponse(Call<List<Property>> call, Response<List<Property>> response) {
                if (response.isSuccessful()) {
                    Log.i(LOG_TAG, "Get my properties response: " + response.body());
                    data.setValue(response.body());
                } else {
                    Log.w(LOG_TAG, "Get my properties response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Property>> call, Throwable t) {
                Log.e(LOG_TAG, "Get my properties failure: " + t.getMessage());
            }
        });

        return data;
    }
}
