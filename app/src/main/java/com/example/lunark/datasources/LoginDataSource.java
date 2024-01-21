package com.example.lunark.datasources;

import com.example.lunark.models.Login;

public interface LoginDataSource {
    public Login logIn(String username, String password);
}
