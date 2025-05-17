package com.rexchoppers.mchunt;

import co.aikar.commands.PaperCommandManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rexchoppers.mchunt.commands.CommandMCHunt;
import com.rexchoppers.mchunt.managers.*;
import com.rexchoppers.mchunt.models.Arena;
import com.rexchoppers.mchunt.serializers.*;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import fr.minuskube.inv.InventoryManager;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.util.Base64;
import java.util.Locale;

public final class MCHunt extends JavaPlugin {

    private Gson gson;

    private static Locale currentLocale;

    private InventoryManager inventoryManager;
    private ArenaManager arenaManager;
    private ArenaSetupManager arenaSetupManager;
    private ItemManager itemManager;

    private EventManager eventManager;

    private EventBusManager eventBusManager;

    private PacketManager packetManager;

    private TaskManager taskManager;

    private SignManager signManager;

    private WorldGuard worldGuard;

    private static final String PRIVATE_KEY_PATH = "keys/ed25519_private.pem";
    private static final String PUBLIC_KEY_PATH = "keys/ed25519_public.pem";

    @Override
    public void onEnable() {
        Security.addProvider(new BouncyCastleProvider());

        this.gson = new GsonBuilder()
                .registerTypeAdapter(Arena.class, new ArenaDeserializer())
                .registerTypeAdapter(ItemStack[].class, new ItemStackArraySerializer())
                .registerTypeAdapter(ItemStack[].class, new ItemStackArrayDeserializer())
                .registerTypeAdapter(Location[].class, new LocationArraySerializer())
                .registerTypeAdapter(Location[].class, new LocationArrayDeserializer())
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        WorldGuardPlugin wgPlugin = getWorldGuard();
        if (wgPlugin == null) {
            getLogger().severe("WorldGuard plugin not found. Disabling plugin.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // Create MCHunt directory if it doesn't exist
        File pluginDir = new File(this.getDataFolder().getAbsolutePath());
        if (!pluginDir.exists()) {
            pluginDir.mkdirs();
        }

        // Create keys directory if it doesn't exist
        File keysDir = new File(pluginDir.getAbsolutePath() + FileSystems.getDefault().getSeparator() + "keys");
        if (!keysDir.exists()) {
            keysDir.mkdirs();
        }

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


        currentLocale = Locale.getDefault();

        // Setup managers
        this.signManager = new SignManager();

        this.arenaManager = new ArenaManager(this,
                this.getDataFolder().getAbsolutePath() + FileSystems.getDefault().getSeparator() + "arenas.json"
        );

        this.arenaSetupManager = new ArenaSetupManager(this,
                this.getDataFolder().getAbsolutePath() + FileSystems.getDefault().getSeparator() + "arenaSetup.json"
        );

        this.eventBusManager = new EventBusManager(this);


        this.packetManager = new PacketManager(this);
        this.packetManager.registerPackets();

        this.taskManager = new TaskManager(this);
        this.taskManager.registerTasks();

        this.eventManager = new EventManager(this);
        this.eventManager.registerEvents();

        this.itemManager = new ItemManager(this);

        inventoryManager = new InventoryManager(this);
        inventoryManager.init();

        // Setup commands
        PaperCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new CommandMCHunt(this));
    }

    private static void savePem(String type, byte[] bytes, String path) throws IOException {
        try (PemWriter writer = new PemWriter(new FileWriter(path))) {
            PemObject object = new PemObject(type, bytes);
            writer.writeObject(object);
        }
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
    }

    public Gson getGson() {
        return gson;
    }

    public static Locale getCurrentLocale() {
        return currentLocale;
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public ArenaSetupManager getArenaSetupManager() {
        return arenaSetupManager;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public PacketManager getPacketManager() {
        return packetManager;
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    public SignManager getSignManager() {
        return signManager;
    }

    public EventBusManager getEventBusManager() {
        return eventBusManager;
    }

    public WorldGuardPlugin getWorldGuard() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }
        return (WorldGuardPlugin) plugin;
    }

    private static String generateOpenSSHPublicKey(PublicKey pubKey, String comment) {
        byte[] encoded = pubKey.getEncoded();

        // Strip X.509 prefix to get raw Ed25519 key (last 32 bytes)
        byte[] rawKey = new byte[32];
        System.arraycopy(encoded, encoded.length - 32, rawKey, 0, 32);

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            writeSshString(out, "ssh-ed25519");
            writeSshBytes(out, rawKey);

            String b64 = Base64.getEncoder().encodeToString(out.toByteArray());
            return "ssh-ed25519 " + b64 + " " + comment;
        } catch (IOException e) {
            throw new RuntimeException("Error generating OpenSSH key", e);
        }
    }

    private static void writeSshString(ByteArrayOutputStream out, String str) throws IOException {
        byte[] bytes = str.getBytes("UTF-8");
        writeSshInt(out, bytes.length);
        out.write(bytes);
    }

    private static void writeSshBytes(ByteArrayOutputStream out, byte[] bytes) throws IOException {
        writeSshInt(out, bytes.length);
        out.write(bytes);
    }

    private static void writeSshInt(ByteArrayOutputStream out, int value) {
        out.write((value >>> 24) & 0xFF);
        out.write((value >>> 16) & 0xFF);
        out.write((value >>> 8) & 0xFF);
        out.write(value & 0xFF);
    }
}
