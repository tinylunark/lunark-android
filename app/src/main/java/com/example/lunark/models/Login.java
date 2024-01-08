package com.example.lunark.models;

import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;

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
        String payload = this.accessToken.split("\\.")[1];
        try {
            payload = new String(Base64.decode(payload, Base64.URL_SAFE), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        try {
            JSONObject payloadJson = new JSONObject(payload);
            return payloadJson.getLong("profileId");
        } catch (JSONException e) {
            return null;
        }
    }
    public String getRole() {
        String payload = this.accessToken.split("\\.")[1];
        try {
            payload = new String(Base64.decode(payload, Base64.URL_SAFE), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        try {
            JSONObject payloadJson = new JSONObject(payload);
            return ((JSONObject)payloadJson.getJSONArray("role").get(0)).getString("authority");
        } catch (JSONException e) {
            return null;
        }
    }
}
