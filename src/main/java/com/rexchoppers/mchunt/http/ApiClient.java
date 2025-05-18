package com.rexchoppers.mchunt.http;

import com.google.gson.Gson;
import com.rexchoppers.mchunt.http.requests.RegisterServerRequest;
import com.rexchoppers.mchunt.http.responses.GetArenaSetupByPlayerIdResponse;
import com.rexchoppers.mchunt.http.responses.RegisterServerResponse;
import com.rexchoppers.mchunt.security.RequestSigner;
import com.rexchoppers.mchunt.util.KeyUtils;
import okhttp3.*;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

public class ApiClient {
    private final OkHttpClient client;
    private final String baseUrl;
    private final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private final Gson gson;
    private String privateKey;
    private String serverUuid;

    public ApiClient(
            String baseUrl,
            Gson gson
    ) {
        this.baseUrl = baseUrl;
        this.gson = gson;
        this.client = new OkHttpClient.Builder()
                .callTimeout(Duration.ofSeconds(10))
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    public RegisterServerResponse registerServer(RegisterServerRequest registerRequest) throws IOException {
        String jsonBody = this.gson.toJson(registerRequest);
        String responseJson = post("/server/register", jsonBody);

        return this.gson.fromJson(responseJson, RegisterServerResponse.class);
    }

    public GetArenaSetupByPlayerIdResponse getArenaSetupByPlayerId(String playerId) throws IOException {
        String path = "/arena/setup/player/" + playerId;
        String responseJson = null;
        try {
            responseJson = getSigned(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (responseJson == null) {
            return null;
        }

        return this.gson.fromJson(responseJson, GetArenaSetupByPlayerIdResponse.class);
    }

    private String getSigned(String path) throws Exception {
        RequestSigner signer = new RequestSigner(
                KeyUtils.loadPrivateKeyFromPem(this.privateKey),
                this.serverUuid
        );

        Bukkit.getConsoleSender().sendMessage(baseUrl + path);

        Map<String, String> headers = signer.signRequest("GET", path);

        Request.Builder requestBuilder = new Request.Builder()
                .url(baseUrl + path)
                .get();

        headers.forEach(requestBuilder::addHeader);

        Request request = requestBuilder.build();

        try (Response response = client.newCall(request).execute()) {
            Bukkit.getConsoleSender().sendMessage(response.body().string());

            if (!response.isSuccessful() &&
                    response.code() != 400 &&
                    response.code() != 403
            ) {
                throw new IOException("Unexpected response code: " + response.code());
            }

            return response.body() != null ? response.body().string() : null;
        }
    }

    private String post(String path, String jsonBody) throws IOException {
        RequestBody body = RequestBody.create(jsonBody, JSON);
        Request request = new Request.Builder()
                .url(baseUrl + path)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response.code());
            }
            return response.body() != null ? response.body().string() : null;
        }
    }

    public void setServerUuid(String serverUuid) {
        this.serverUuid = serverUuid;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}
