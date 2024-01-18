package com.example.lunark.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.lunark.clients.ReservationService;
import com.example.lunark.dtos.CreateReservationDto;
import com.example.lunark.models.Reservation;
import com.example.lunark.util.ClientUtils;

import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReservationRepository {
    private static final String LOG_TAG = "ReservationRepository";
    ReservationService reservationService;

    @Inject
    public ReservationRepository() {
        this.reservationService = ClientUtils.reservationService;
    }

    @Inject
    public void setRetrofit(Retrofit retrofit) {
        this.reservationService = retrofit.create(ReservationService.class);
    }

    public LiveData<List<Reservation>> getPendingReservations(@NonNull Long hostId) {
        final MutableLiveData<List<Reservation>> data = new MutableLiveData<>();

        reservationService.getIncomingReservations(hostId).enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
                if (response.isSuccessful()) {
                    List<Reservation> reservations = response.body();
                    data.setValue(response.body());
                } else {
                    Log.w(LOG_TAG, "Get reservations response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Reservation>> call, Throwable t) {
                Log.e(LOG_TAG, "Get reservations failure: " + t.getMessage());
            }
        });

        return data;
    }

    public LiveData<List<Reservation>> getCurrentReservations() {
        final MutableLiveData<List<Reservation>> data = new MutableLiveData<>();

        reservationService.getCurrentReservations().enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
                if (response.isSuccessful()) {
                    List<Reservation> reservations = response.body();
                    data.setValue(response.body());
                } else {
                    Log.w(LOG_TAG, "Get reservations response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Reservation>> call, Throwable t) {
                Log.e(LOG_TAG, "Get reservations failure: " + t.getMessage());
            }
        });

        return data;
    }


    public void acceptReservation(Long reservationId) {
        reservationService.acceptReservation(reservationId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.e(LOG_TAG, "Accept reservation success: " + response.code());
                } else {
                    Log.e(LOG_TAG, "Accept reservation error: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(LOG_TAG, "Accept reservation failure: " + t.getMessage());
            }
        });
    }

    public void declineReservation(Long reservationId) {
        ClientUtils.reservationService.declineReservation(reservationId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.e(LOG_TAG, "Decline reservation success: " + response.code());
                } else {
                    Log.e(LOG_TAG, "Decline reservation error: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(LOG_TAG, "Decline reservation failure: " + t.getMessage());
            }
        });
    }

    public LiveData<Reservation> createReservation(CreateReservationDto dto) {
        final MutableLiveData<Reservation> data = new MutableLiveData<>(null);

        reservationService.createReservation(dto).enqueue(new Callback<Reservation>() {
            @Override
            public void onResponse(Call<Reservation> call, Response<Reservation> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    Log.e(LOG_TAG, "Create reservation error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Reservation> call, Throwable t) {
                Log.e(LOG_TAG, "Create reservation failure: " + t.getMessage());
            }
        });

        return data;
    }
}