package com.rexchoppers.mchunt.http.requests;

import com.google.gson.annotations.SerializedName;

import java.time.Instant;

public class RegisterRequest {
    private String publicKey;

    public RegisterRequest(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
