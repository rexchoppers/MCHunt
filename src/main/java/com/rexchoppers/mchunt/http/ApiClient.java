package com.rexchoppers.mchunt.http;

import com.google.gson.Gson;
import com.rexchoppers.mchunt.http.requests.RegisterServerRequest;
import com.rexchoppers.mchunt.http.responses.RegisterServerResponse;
import okhttp3.*;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.time.Duration;

public class ApiClient {
    private final OkHttpClient client;
    private final String baseUrl;
    private final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private final Gson gson;

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

        Bukkit.getConsoleSender().sendMessage(jsonBody);

        String responseJson = post("/server/register", jsonBody);
        return this.gson.fromJson(responseJson, RegisterServerResponse.class);
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
}
