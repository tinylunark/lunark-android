package com.example.lunark;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lunark.databinding.LoginScreenBinding;
import com.example.lunark.models.Login;
import com.example.lunark.repositories.LoginRepository;

import org.reactivestreams.Subscription;

import java.util.concurrent.Flow;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginScreenActivity extends AppCompatActivity {
    @Inject
    LoginRepository loginRepository;
    private LoginScreenBinding binding;
    Button loginButton;
    EditText emailInput;
    EditText passwordInput;
    private Disposable subscription;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ((LunarkApplication) getApplicationContext()).applicationComponent.inject(this);
        super.onCreate(savedInstanceState);
        binding = LoginScreenBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        loginButton = binding.loginButton;
        emailInput = binding.editTextTextEmailAddress;
        passwordInput = binding.editTextTextPassword;
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryLogIn();
            }
        });

        Button signupButton = binding.signupButton;
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginScreenActivity.this, SignUpScreenActivity.class);
                startActivity(intent);
                finish();
            }
        });

        trySkipLogin();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void openHomeActivity() {
        Intent intent = new Intent(LoginScreenActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void trySkipLogin() {
        loginRepository.getLogin()
                .subscribe(new SingleObserver<Login>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LoginScreenActivity.this.subscription = d;
                    }

                    @Override
                    public void onSuccess(Login login) {
                        openHomeActivity();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void tryLogIn() {
        loginRepository.logIn(emailInput.getText().toString(), passwordInput.getText().toString())
                .subscribe(new SingleObserver<Login>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LoginScreenActivity.this.subscription = d;
                    }

                    @Override
                    public void onSuccess(Login login) {
                        loginRepository.getLogin()
                                .observeOn(Schedulers.io())
                                .subscribeOn(AndroidSchedulers.mainThread())
                                .subscribe(new SingleObserver<Login>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {
                                        LoginScreenActivity.this.subscription = d;
                                    }

                                    @Override
                                    public void onSuccess(Login login) {
                                        openHomeActivity();
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.e("AUTH", "Could not read saved token.");
                                    }
                                });
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(LoginScreenActivity.this, R.string.wrong_username_or_password, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
