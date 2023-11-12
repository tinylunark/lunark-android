package com.example.lunark;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.lunark.databinding.SignupScreenBinding;

public class SignUpScreenActivity extends Activity {

    private SignupScreenBinding signupScreenBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
}
