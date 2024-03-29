package com.example.lunark.clients;

import com.example.lunark.dtos.CreateReservationDto;
import com.example.lunark.models.Reservation;

import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ReservationService {

    @GET("reservations")
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    Call<List<Reservation>> getReservations();

    @GET("reservations/incoming-reservations")
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    Call<List<Reservation>> getIncomingReservations(@Query("hostId") Long hostId);

    @GET("reservations/current")
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    Call<List<Reservation>> getCurrentReservations(@QueryMap Map<String, String> filters);

    @GET("reservations/accepted-reservations")
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    Call<List<Reservation>> getAcceptedReservations(@Query("guestId") Long guestId);

    @POST("reservations/accept/{id}")
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    Call<ResponseBody> acceptReservation(@Path("id") Long reservationId);

    @POST("reservations/reject/{id}")
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    Call<ResponseBody> declineReservation(@Path("id") Long reservationId);


    @POST("reservations/cancel/{id}")
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    Call<ResponseBody> cancelReservation(@Path("id") Long reservationId);

    @DELETE("reservations/{id}")
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    Completable deleteReservation(@Path("id") Long id);

    @POST("reservations")
    Call<Reservation> createReservation(@Body CreateReservationDto dto);
}
