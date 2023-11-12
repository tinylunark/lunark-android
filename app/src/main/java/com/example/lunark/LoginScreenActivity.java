package com.example.lunark;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.example.lunark.databinding.LoginScreenBinding;

public class LoginScreenActivity extends Activity {
    private LoginScreenBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LoginScreenBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        Button loginButton = binding.loginButton;
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginScreenActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
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
