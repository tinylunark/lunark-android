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

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginScreenActivity extends AppCompatActivity {
    private LoginScreenBinding binding;
    private LoginRepository loginRepository;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LoginScreenBinding.inflate(getLayoutInflater());
        loginRepository = new LoginRepository();

        setContentView(binding.getRoot());

        Button loginButton = binding.loginButton;
        EditText emailInput = binding.editTextTextEmailAddress;
        EditText passwordInput = binding.editTextTextPassword;
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginRepository.logIn(emailInput.getText().toString(), passwordInput.getText().toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<Login>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(Login login) {
                                Intent intent = new Intent(LoginScreenActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d("AUTH", "Activity fail");
                                Toast.makeText(LoginScreenActivity.this, R.string.wrong_username_or_password, Toast.LENGTH_SHORT);
                            }
                        });
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


    }
}
