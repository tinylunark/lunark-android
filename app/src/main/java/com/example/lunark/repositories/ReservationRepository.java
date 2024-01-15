package com.example.lunark.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.lunark.models.Reservation;
import com.example.lunark.util.ClientUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationRepository {
    private static final String LOG_TAG = "ReservationRepository";

    public LiveData<List<Reservation>> getReservations() {
        final MutableLiveData<List<Reservation>> data = new MutableLiveData<>();

        ClientUtils.reservationService.getReservations().enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
                if (response.isSuccessful()) {
                    Log.i(LOG_TAG, "Get reservations response: " + response.body());
                    Log.e("AYO", "REZERVACIJE FECOVANE");
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

    public LiveData<List<Reservation>> getPendingReservations() {
        final MutableLiveData<List<Reservation>> data = new MutableLiveData<>();

        ClientUtils.reservationService.getIncomingReservations(2L).enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
                if (response.isSuccessful()) {
                    List<Reservation> reservations = response.body();
                    List<Reservation> pendingReservations = new ArrayList<>();

                    for (Reservation reservation : reservations) {
                        if ("PENDING".equals(reservation.getStatus())) {
                            pendingReservations.add(reservation);
                            Log.e("AYO", reservation.toString());
                        }
                    }
                    Log.e("AYO", "REZERVACIJE FECOVANE");

                    data.setValue(pendingReservations);
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


    public void acceptReservation(long reservationId) {
        Call<ResponseBody> call = ClientUtils.reservationService.acceptReservation(reservationId);
        call.enqueue(new Callback<ResponseBody>() {
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

    public void declineReservation(long reservationId) {
        Call<ResponseBody> call = ClientUtils.reservationService.declineReservation(reservationId);
        call.enqueue(new Callback<ResponseBody>() {
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
}