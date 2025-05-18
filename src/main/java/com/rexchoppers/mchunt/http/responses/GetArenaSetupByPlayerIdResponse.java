package com.rexchoppers.mchunt.http.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.time.Instant;

public class GetArenaSetupByPlayerIdResponse {
    @SerializedName("uuid")
    @Expose
    public String uuid;

    @SerializedName("playerId")
    @Expose
    public String playerId;

    @SerializedName("serverId")
    @Expose
    public String serverId;

    @SerializedName("createdAt")
    @Expose
    public Instant createdAt;

    @SerializedName("updatedAt")
    @Expose
    public Instant updatedAt;
}
