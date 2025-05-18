package com.rexchoppers.mchunt.http.responses;

import com.google.gson.annotations.SerializedName;

import java.time.Instant;

public class RegisterServerResponse {
    @SerializedName("uuid")
    public String uuid;

    @SerializedName("name")
    public String name;

    @SerializedName("createdAt")
    public Instant createdAt;

    @SerializedName("updatedAt")
    public Instant updatedAt;
}
