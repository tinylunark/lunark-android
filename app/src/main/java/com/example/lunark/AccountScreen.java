package com.example.lunark;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.lunark.clients.AccountService;
import com.example.lunark.dtos.AccountDto;
import com.example.lunark.dtos.ProfileDto;
import com.example.lunark.models.Login;
import com.example.lunark.repositories.LoginRepository;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AccountScreen extends AppCompatActivity {
    @Inject
    Retrofit retrofit;
    @Inject
    LoginRepository loginRepository;

    Long userId = 1L;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((LunarkApplication) this.getApplication()).applicationComponent.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);


        this.loginRepository.getLogin().subscribe(new SingleObserver<Login>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(Login login) {
                userId = login.getProfileId();
                fetchUserData(userId);
            }

            @Override
            public void onError(Throwable e) {

            }
        });

        fetchUserData(userId);


        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_main) {
                    if (!isCurrentActivity(HomeActivity.class)) {
                        startActivity(new Intent(AccountScreen.this, HomeActivity.class));
                    }
                } else if (itemId == R.id.menu_account) {
                    if (!isCurrentActivity(AccountScreen.class)) {
                        startActivity(new Intent(AccountScreen.this, AccountScreen.class));
                    }
                }  else if (itemId == R.id.menu_logout) {
                    if (!isCurrentActivity(LoginScreenActivity.class)) {
                        Intent intent = new Intent(AccountScreen.this, LoginScreenActivity.class);
                        startActivity(intent);
                    }
                }

                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private void fetchUserData(Long userId) {
        AccountService apiService = retrofit.create(AccountService.class);
        Call<AccountDto> call = apiService.getAccount(userId);
        call.enqueue(new Callback<AccountDto>() {
            @Override
            public void onResponse(Call<AccountDto> call, Response<AccountDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AccountDto accountDto = response.body();
                    populateFields(accountDto);
                } else {
                    Toast.makeText(AccountScreen.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AccountDto> call, Throwable t) {
                Toast.makeText(AccountScreen.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void populateFields(AccountDto accountDto) {
        setContentView(R.layout.activity_account);

        TextView fullName = findViewById(R.id.profile_full_name);
        fullName.setText(accountDto.getName() + " " + accountDto.getSurname());

        TextInputEditText firstNameEditText = findViewById(R.id.textInputEditTextFirstName);
        firstNameEditText.setText(accountDto.getName());

        TextInputEditText lastNameEditText = findViewById(R.id.textInputEditTextLastName);
        lastNameEditText.setText(accountDto.getSurname());

        TextInputEditText phoneEditText = findViewById(R.id.textInputEditTextPhone);
        phoneEditText.setText(accountDto.getPhoneNumber());

        TextInputEditText emailEditText = findViewById(R.id.textInputEditTextEmail);
        emailEditText.setText(accountDto.getEmail());

        TextInputEditText addressEditText = findViewById(R.id.textInputEditTextAddress);
        addressEditText.setText(accountDto.getAddress());
    }

    public void onChangePasswordClicked(View view) {
        startActivity(new Intent(this, ChangePasswordActivity.class));
    }

    public void onSaveClicked(View view) {
        updateProfile();
    }

    public void onDeleteClicked(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Profile")
                .setMessage("Are you sure you want to delete your profile?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // getUserId
                        deleteAccount(userId);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void deleteAccount(Long userId) {
        AccountService apiService = retrofit.create(AccountService.class);
        Call<Void> call = apiService.deleteAccount(userId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    AccountScreen.this.loginRepository.clearToken().subscribe(() -> redirectToLoginScreen());
                } else {
                    Toast.makeText(AccountScreen.this, "Failed to delete account", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AccountScreen.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateProfile() {
        ProfileDto updatedProfile = collectProfileData();

        AccountService apiService = retrofit.create(AccountService.class);

        //String authToken = getAuthorizationToken();
        String authToken = "123456789";

        Call<ProfileDto> call = apiService.updateProfile(userId, updatedProfile);

        call.enqueue(new Callback<ProfileDto>() {
            @Override
            public void onResponse(Call<ProfileDto> call, Response<ProfileDto> response) {
                if (response.isSuccessful()) {
                    new AlertDialog.Builder(AccountScreen.this)
                            .setTitle("SUCCESS!")
                            .setMessage("Your profile has been updated.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    openHomeScreen();
                                }
                            }).show();
                } else {
                    Toast.makeText(AccountScreen.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileDto> call, Throwable t) {
                Toast.makeText(AccountScreen.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private ProfileDto collectProfileData() {
        ProfileDto profile = new ProfileDto();

        TextInputEditText firstNameEditText = findViewById(R.id.textInputEditTextFirstName);
        TextInputEditText lastNameEditText = findViewById(R.id.textInputEditTextLastName);
        TextInputEditText emailEditText = findViewById(R.id.textInputEditTextEmail);
        TextInputEditText phoneEditText = findViewById(R.id.textInputEditTextPhone);
        TextInputEditText addressEditText = findViewById(R.id.textInputEditTextAddress);

        profile.setName(firstNameEditText.getText().toString());
        profile.setSurname(lastNameEditText.getText().toString());
        profile.setEmail(emailEditText.getText().toString());
        profile.setPhoneNumber(phoneEditText.getText().toString());
        profile.setAddress(addressEditText.getText().toString());
        profile.setRole("HOST");

        return profile;
    }

    private void redirectToLoginScreen() {
        startActivity(new Intent(AccountScreen.this, LoginScreenActivity.class));
        finish();
    }

    public void openHomeScreen() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    private boolean isCurrentActivity(Class<?> activityClass) {
        return activityClass.isInstance(this);
    }
}
