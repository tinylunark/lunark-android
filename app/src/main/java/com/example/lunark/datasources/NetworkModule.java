package com.example.lunark.datasources;

import com.example.lunark.BuildConfig;
import com.example.lunark.interceptors.JwtInterceptor;
import com.example.lunark.repositories.LoginRepository;
import com.example.lunark.util.JsonDateDeserializer;
import com.example.lunark.util.JsonDateTimeDeserializer;
import com.example.lunark.util.JsonLocalDateTimeSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {
    public static final String SERVICE_API_PATH = "http://" + BuildConfig.IP_ADDR + ":8080/api/";
    @Provides
    public static JwtInterceptor provideJwtInterceptor(LoginLocalDataSource loginLocalDataSource) {
        return  new JwtInterceptor(loginLocalDataSource);
    }
    @Provides
    public static OkHttpClient provideOkHttpClient(JwtInterceptor jwtInterceptor) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(jwtInterceptor)
                .addInterceptor(interceptor)
                .build();

        return client;
    }

    @Provides
    public static Gson provideGson() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new JsonDateDeserializer())
                .registerTypeAdapter(ZonedDateTime.class, new JsonDateTimeDeserializer())
                .registerTypeAdapter(LocalDateTime.class, new JsonLocalDateTimeSerializer())
                .create();
        return gson;
    }

    @Provides
    public static Retrofit provideRetrofit(OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder()
            .baseUrl(SERVICE_API_PATH)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build();
    }
}
