package com.rexchoppers.mchunt.http.requests;

import com.google.gson.annotations.Expose;

public class RegisterServerRequest {
    @Expose
    private String publicKey;

    public RegisterServerRequest(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPublicKey() {
        return publicKey;
    }
}
