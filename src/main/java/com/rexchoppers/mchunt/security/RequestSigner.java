package com.rexchoppers.mchunt.security;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.Signature;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class RequestSigner {
    private final PrivateKey privateKey;
    private final String serverUuid;

    public RequestSigner(PrivateKey privateKey, String serverUuid) {
        this.privateKey = privateKey;
        this.serverUuid = serverUuid;
    }

    public Map<String, String> signRequest(String method, String path) throws Exception {
        String timestamp = Instant.now().toString();

        // Colon-delimited message for signing
        String message = method.toUpperCase() + ":" + path + ":" + timestamp;
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);

        // Sign the message
        Signature signature = Signature.getInstance("Ed25519");
        signature.initSign(privateKey);
        signature.update(messageBytes);
        byte[] signatureBytes = signature.sign();

        String encodedSignature = Base64.getEncoder().encodeToString(signatureBytes);

        Map<String, String> headers = new HashMap<>();
        headers.put("X-Signature", encodedSignature);
        headers.put("X-Timestamp", timestamp);
        headers.put("X-Server-UUID", serverUuid);
        return headers;
    }
}
