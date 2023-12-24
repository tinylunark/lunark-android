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

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginScreenActivity extends AppCompatActivity {
    private LoginScreenBinding binding;
    private LoginRepository loginRepository;
    Button loginButton;
    EditText emailInput;
    EditText passwordInput;
    private Disposable subscription;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LoginScreenBinding.inflate(getLayoutInflater());
        loginRepository = new LoginRepository(this.getApplication());

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
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
        super.onDestroy();
    }

    private void openHomeActivity() {
        Intent intent = new Intent(LoginScreenActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void trySkipLogin() {
        subscription = loginRepository.getLogin()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(login -> openHomeActivity());
    }

    private void tryLogIn() {
        loginRepository.logIn(emailInput.getText().toString(), passwordInput.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Login>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Login login) {
                        loginRepository.getLogin().
                                observeOn(Schedulers.io())
                                .subscribeOn(AndroidSchedulers.mainThread())
                                .subscribe(login1 -> openHomeActivity());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(LoginScreenActivity.this, R.string.wrong_username_or_password, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
