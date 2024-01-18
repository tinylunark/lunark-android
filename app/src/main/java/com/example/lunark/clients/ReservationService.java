package com.example.lunark.clients;

import com.example.lunark.dtos.CreateReservationDto;
import com.example.lunark.models.Reservation;

import java.util.List;

import io.reactivex.Single;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

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
    Call<List<Reservation>> getCurrentReservations();

    @GET("reservations/accepted")
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
    Single<Reservation> cancelReservation(@Body RequestBody emptyBody);

    @POST("reservations")
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    Call<Reservation> createReservation(@Body CreateReservationDto dto);
}
