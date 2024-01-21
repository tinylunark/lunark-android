package com.example.lunark;

import static android.app.PendingIntent.getActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lunark.clients.AccountService;
import com.example.lunark.models.ChangePassword;
import com.example.lunark.models.Login;
import com.example.lunark.LunarkApplication;
import com.example.lunark.repositories.*;
import com.google.android.material.textfield.TextInputEditText;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChangePasswordActivity extends AppCompatActivity {


    @Inject
    Retrofit retrofit;
    @Inject
    LoginRepository loginRepository;

    Long userId = 1L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((LunarkApplication)this.getApplication()).applicationComponent.inject(this); ;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        this.loginRepository.getLogin().subscribe(new SingleObserver<Login>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Login login) {
                userId = login.getProfileId();
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    public void onChangePasswordSaveClicked(View view) {
        updatePassword();
    }

    private void updatePassword() {
        ChangePassword changePassword = collectProfileData();
        AccountService apiService = retrofit.create(AccountService.class);
        Call<ChangePassword> call = apiService.updatePassword(changePassword);

        call.enqueue(new Callback<ChangePassword>() {
            @Override
            public void onResponse(Call<ChangePassword> call, Response<ChangePassword> response) {
                    new AlertDialog.Builder(ChangePasswordActivity.this)
                            .setTitle("SUCCESS!")
                            .setMessage("Your profile has been updated.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    openProfileScreen();
                                }
                            }).show();
            }

            @Override
            public void onFailure(Call<ChangePassword> call, Throwable t) {
                Toast.makeText( ChangePasswordActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ChangePassword collectProfileData() {
        TextInputEditText etOldPassword = findViewById(R.id.etOldPassword);
        TextInputEditText etNewPassword = findViewById(R.id.etNewPassword);

        String oldPassword = etOldPassword.getText().toString();
        String newPassword = etNewPassword.getText().toString();

        ChangePassword changePass = new ChangePassword(userId, oldPassword, newPassword);
        return changePass;
    }


    public void onSaveClicked(View view){
        showPasswordUpdateDialog();
    }

    public void showPasswordUpdateDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("SUCCESS!")
                .setMessage("Your password has been updated.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openProfileScreen();
                    }
                }).show();
    }

    public void openProfileScreen() {
        Intent intent = new Intent(this, AccountScreen.class);
        startActivity(intent);
        finish();
    }
}