package com.example.lunark;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lunark.databinding.SignupScreenBinding;
import com.example.lunark.datasources.AccountRepository;
import com.example.lunark.dtos.AccountSignUpDto;

import javax.inject.Inject;

public class SignUpScreenActivity extends AppCompatActivity {

    private SignupScreenBinding signupScreenBinding;

    @Inject
    AccountRepository accountRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ((LunarkApplication) getApplication()).applicationComponent.inject(this);
        super.onCreate(savedInstanceState);
        signupScreenBinding = SignupScreenBinding.inflate(getLayoutInflater());
        setContentView(signupScreenBinding.getRoot());

        signupScreenBinding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpScreenActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private AccountSignUpDto getAccountSignUpDto() {
        return null;
    }
}
