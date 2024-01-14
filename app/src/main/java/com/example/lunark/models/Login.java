package com.example.lunark.models;

import android.util.Base64;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.Date;

import io.jsonwebtoken.Jwts;

public class Login {
    private String accessToken;
    private int expiresIn;

    public Login(String accessToken, int expiresIn) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public Long getProfileId() {
        try {
            return this.toJson().getLong("profileId");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public String getRole() {
        try {
            return ((JSONObject)this.toJson().getJSONArray("role").get(0)).getString("authority");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean hasExpired() {
        try {
            Long expirationTimestamp = this.toJson().getLong("exp");
            return expirationTimestamp < (new Date().getTime() / 1000);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private JSONObject toJson() {
        String payload = this.accessToken.split("\\.")[1];
        try {
            payload = new String(Base64.decode(payload, Base64.URL_SAFE), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        try {
            JSONObject payloadJson = new JSONObject(payload);
            return payloadJson;
        } catch (JSONException e) {
            return null;
        }
    }

}
