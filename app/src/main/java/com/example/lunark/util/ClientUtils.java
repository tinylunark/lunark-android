package com.example.lunark.util;

import com.example.lunark.BuildConfig;
import com.example.lunark.clients.AccountService;
import com.example.lunark.clients.ReservationService;
import com.example.lunark.services.NominatimService;
import com.example.lunark.services.PropertyService;
import com.example.lunark.services.ReviewReportService;
import com.example.lunark.services.ReviewService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientUtils {
    public static final String SERVICE_API_PATH = "http://" + BuildConfig.IP_ADDR + ":8080/api/";

    public static OkHttpClient test() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();

        return client;
    }

    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new JsonDateDeserializer())
            .create();

    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(SERVICE_API_PATH)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(test())
            .build();

    private static Retrofit nominatimRetrofit = new Retrofit.Builder()
            .baseUrl("https://nominatim.openstreetmap.org/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(test())
            .build();

    public static PropertyService propertyService = retrofit.create(PropertyService.class);
    public static ReservationService reservationService = retrofit.create(ReservationService.class);
    public static AccountService accountService = retrofit.create(AccountService.class);
    public static ReviewService reviewService = retrofit.create(ReviewService.class);
    public static NominatimService nominatimService = nominatimRetrofit.create(NominatimService.class);
}
