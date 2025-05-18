package com.rexchoppers.mchunt.security;

import com.rexchoppers.mchunt.MCHunt;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.util.Base64;

public class ED25519 {
    private static final String PRIVATE_KEY_PATH = "keys/ed25519_private.pem";
    private static final String PUBLIC_KEY_PATH = "keys/ed25519_public.pem";

    private final MCHunt plugin;

    public ED25519(MCHunt plugin) {
        this.plugin = plugin;
    }

    public String getPublicKeyContents() {
        try {
            File publicKeyFile = new File(this.plugin.getDataFolder(), PUBLIC_KEY_PATH);
            return new String(Files.readAllBytes(publicKeyFile.toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getPrivateKeyContents() {
        try {
            File privateKeyFile = new File(this.plugin.getDataFolder(), PRIVATE_KEY_PATH);
            return new String(Files.readAllBytes(privateKeyFile.toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void generateKeys() {
        File pluginDir = new File(this.plugin.getDataFolder().getAbsolutePath());

        // Check if keys exist
        if (!Files.exists(Paths.get(
                pluginDir.getAbsolutePath() + FileSystems.getDefault().getSeparator() + PRIVATE_KEY_PATH
        )) || !Files.exists(Paths.get(pluginDir.getAbsolutePath() + FileSystems.getDefault().getSeparator() + PUBLIC_KEY_PATH))) {
            System.out.println("Generating Ed25519 key pair...");

            try {
                File privateKeyFile = new File(pluginDir, PRIVATE_KEY_PATH);
                File publicKeyFile = new File(pluginDir, PUBLIC_KEY_PATH);

                if (!privateKeyFile.exists() || !publicKeyFile.exists()) {
                    System.out.println("Generating Ed25519 key pair...");

                    KeyPairGenerator generator = KeyPairGenerator.getInstance("Ed25519", "BC");
                    KeyPair pair = generator.generateKeyPair();

                    // Save private key as PEM
                    savePem("PRIVATE KEY", pair.getPrivate().getEncoded(), privateKeyFile.getAbsolutePath());

                    // Save public key in OpenSSH format
                    String openSshPublicKey = generateOpenSSHPublicKey(pair.getPublic(), "mchunt@plugin");
                    Files.write(publicKeyFile.toPath(), openSshPublicKey.getBytes());

                    System.out.println("Keys generated.");
                } else {
                    System.out.println("Keys already exist.");
                }
            } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        } else {
            System.out.println("Keys already exist.");
        }
    }

    private static void savePem(String type, byte[] bytes, String path) throws IOException {
        try (PemWriter writer = new PemWriter(new FileWriter(path))) {
            PemObject object = new PemObject(type, bytes);
            writer.writeObject(object);
        }
    }

    private String generateOpenSSHPublicKey(PublicKey pubKey, String comment) {
        byte[] encoded = pubKey.getEncoded();

        // Strip X.509 prefix to get raw Ed25519 key (last 32 bytes)
        byte[] rawKey = new byte[32];
        System.arraycopy(encoded, encoded.length - 32, rawKey, 0, 32);

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            this.writeSshString(out, "ssh-ed25519");
            this.writeSshBytes(out, rawKey);

            String b64 = Base64.getEncoder().encodeToString(out.toByteArray());
            return "ssh-ed25519 " + b64 + " " + comment;
        } catch (IOException e) {
            throw new RuntimeException("Error generating OpenSSH key", e);
        }
    }

    private void writeSshString(ByteArrayOutputStream out, String str) throws IOException {
        byte[] bytes = str.getBytes("UTF-8");
        this.writeSshInt(out, bytes.length);
        out.write(bytes);
    }

    private void writeSshBytes(ByteArrayOutputStream out, byte[] bytes) throws IOException {
        this.writeSshInt(out, bytes.length);
        out.write(bytes);
    }

    private void writeSshInt(ByteArrayOutputStream out, int value) {
        out.write((value >>> 24) & 0xFF);
        out.write((value >>> 16) & 0xFF);
        out.write((value >>> 8) & 0xFF);
        out.write(value & 0xFF);
    }
}
