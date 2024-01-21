package com.example.lunark.util;

import android.os.StrictMode;
import android.util.Log;

import androidx.preference.PreferenceDataStore;

import com.example.lunark.clients.AccountService;
import com.example.lunark.dtos.AccountDto;
import com.example.lunark.dtos.NotificationSettingsDto;
import com.example.lunark.models.Login;
import com.example.lunark.repositories.LoginRepository;

import java.io.IOException;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RestPreferenceDataStore extends PreferenceDataStore {
    private static final String TAG = "RestPreferenceDataStore";
    private final AccountService mAccountService;
    private final LoginRepository mLoginRepository;

    @Inject
    public RestPreferenceDataStore(Retrofit retrofit, LoginRepository loginRepository) {
        this.mAccountService = retrofit.create(AccountService.class);
        this.mLoginRepository = loginRepository;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public void putBoolean(String key, boolean value) {
        Log.d(TAG, "putBoolean: " + key + " " + value);

        String type;
        switch (key) {
            case "reservation_response_notifications":
                type = "RESERVATION_ACCEPTED";
                break;
            case "property_review_notifications":
                type = "PROPERTY_REVIEW";
                break;
            case "host_review_notifications":
                type = "HOST_REVIEW";
                break;
            case "reservation_request_notifications":
                type = "RESERVATION_CREATED";
                break;
            case "reservation_cancellation_notifications":
                type = "RESERVATION_CANCELED";
                break;
            default:
                return;
        }

        NotificationSettingsDto notificationSettingsDto = new NotificationSettingsDto();
        notificationSettingsDto.setType(type);

        mAccountService.toggleNotifications(notificationSettingsDto).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.i(TAG, "toggleNotifications: " + response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "toggleNotifications: " + t.getMessage());
            }
        });
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        Login login = mLoginRepository.getLogin().blockingGet();
        Long id = login.getProfileId();
        AccountDto accountDto = null;

        try {
            Response<AccountDto> response = mAccountService.getAccount(id).execute();
            accountDto = response.body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (accountDto != null) {
            Log.d(TAG, accountDto.toString());

            switch (key) {
                case "reservation_response_notifications":
                    return accountDto.getGuestNotificationSettings().isNotifyOnReservationRequestResponse();
                case "property_review_notifications":
                    return accountDto.getHostNotificationSettings().isNotifyOnPropertyReview();
                case "host_review_notifications":
                    return accountDto.getHostNotificationSettings().isNotifyOnHostReview();
                case "reservation_request_notifications":
                    return accountDto.getHostNotificationSettings().isNotifyOnReservationCreation();
                case "reservation_cancellation_notifications":
                    return accountDto.getHostNotificationSettings().isNotifyOnReservationCancellation();
            }
        } else {
            Log.d(TAG, "accountDto is null");
        }

        return defValue;
    }
}
