package com.example.lunark;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lunark.databinding.SignupScreenBinding;
import com.example.lunark.datasources.AccountRepository;
import com.example.lunark.dtos.AccountSignUpDto;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;

public class SignUpScreenActivity extends AppCompatActivity {

    @Inject
    AccountRepository accountRepository;
    private Disposable subscription;
    private SignupScreenBinding signupScreenBinding;

    private EditText firstNameInput;
    private EditText lastNameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private EditText confirmPasswordInput;
    private EditText addressInput;
    private EditText phoneNumberInput;
    private RadioButton guestRadio;
    private RadioButton hostRadio;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ((LunarkApplication) getApplication()).applicationComponent.inject(this);
        super.onCreate(savedInstanceState);
        signupScreenBinding = SignupScreenBinding.inflate(getLayoutInflater());
        setUpInputBindings();
        setContentView(signupScreenBinding.getRoot());

        signupScreenBinding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    private void signUp() {
        if (!this.isFormValid()) {
            Toast.makeText(this.getApplicationContext(), R.string.entered_data_is_not_valid, Toast.LENGTH_SHORT);
        }
        this.accountRepository.signUp(this.getAccountSignUpDto()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
                SignUpScreenActivity.this.subscription = d;
            }

            @Override
            public void onComplete() {
                Toast.makeText(SignUpScreenActivity.this.getApplicationContext(), R.string.signed_up_successfully_check_your_email_for_a_verification_link, Toast.LENGTH_SHORT);
                Intent intent = new Intent(SignUpScreenActivity.this, LoginScreenActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(SignUpScreenActivity.this.getApplicationContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT);
            }
        });
    }

    private void setUpInputBindings() {
        firstNameInput = signupScreenBinding.editTextTextFirstName;
        lastNameInput = signupScreenBinding.editTextTextLastName;
        emailInput = signupScreenBinding.editTextTextEmailAddress;
        passwordInput = signupScreenBinding.editTextTextPassword;
        confirmPasswordInput = signupScreenBinding.editTextTextConfirmPassword;
        addressInput = signupScreenBinding.editTextTextAddress;
        phoneNumberInput = signupScreenBinding.editTextTextPhoneNumber;
        guestRadio = signupScreenBinding.radioButtonGuest;
        hostRadio = signupScreenBinding.radioButtonHost;
    }

    private boolean isFormValid() {
        return firstNameInput.getText().toString().length() > 0 &&
                lastNameInput.getText().toString().length() > 0 &&
                emailInput.getText().toString().length() > 0 &&
                passwordInput.getText().toString().length() > 0 &&
                confirmPasswordInput.getText().toString().length() > 0 &&
                addressInput.getText().toString().length() > 0 &&
                phoneNumberInput.getText().toString().length() > 0 &&
                (guestRadio.isChecked() || hostRadio.isChecked()) &&
                (passwordInput.getText().toString().equals(confirmPasswordInput.getText().toString()));
    }

    private AccountSignUpDto getAccountSignUpDto() {
        String role = guestRadio.isChecked() ? "GUEST" : "HOST";
        return new AccountSignUpDto(
                emailInput.getText().toString(),
                passwordInput.getText().toString(),
                firstNameInput.getText().toString(),
                lastNameInput.getText().toString(),
                addressInput.getText().toString(),
                phoneNumberInput.getText().toString(),
                role
        );
    }
}
