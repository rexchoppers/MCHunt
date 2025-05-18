package com.rexchoppers.mchunt.http.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.time.Instant;

public class RegisterServerResponse {
    @SerializedName("uuid")
    @Expose
    public String uuid;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("createdAt")
    @Expose
    public Instant createdAt;

    @SerializedName("updatedAt")
    @Expose
    public Instant updatedAt;
}
