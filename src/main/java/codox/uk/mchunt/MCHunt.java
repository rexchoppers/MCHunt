package codox.uk.mchunt;

import co.aikar.commands.PaperCommandManager;
import codox.uk.mchunt.commands.CommandMCHunt;
import codox.uk.mchunt.config.MCHuntConfiguration;
import codox.uk.mchunt.objects.Arena;
import codox.uk.mchunt.util.GeneralUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.minuskube.inv.InventoryManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public final class MCHunt extends JavaPlugin {

    private static MCHunt plugin;

    private static MCHuntConfiguration config;

    private static InventoryManager inventoryManager;

    public static String BASE_URL = "https://mchunt.rexchoppers.com";

    private void createConfigFilesIfNotExists() throws IOException {
        // Create plugin config folder if it doesn't already exist
        new File(this.getDataFolder().getAbsolutePath()).mkdirs();

        List<File> filesToCreate = Arrays.asList(
                new File(this.getDataFolder().getAbsolutePath() + System.getProperty("file.separator") + "arenas.json"),
                new File(this.getDataFolder().getAbsolutePath() + System.getProperty("file.separator") + "tmp-arena-setup.json")
        );

        for (File file: filesToCreate) {
            if(!file.exists()) file.createNewFile();
        }
    }

    public static String getVersionNumber() {
        String version;
        try (InputStream inputStream = MCHunt.class.getResourceAsStream("/META-INF/maven/codox.uk/mchunt/pom.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);
            version = properties.getProperty("version");
        } catch (Exception e) {
            version = "Unknown";
        }
        return version;
    }

    @Override
    public void onEnable() {
        // Setup config file
        config = new MCHuntConfiguration(this);

        if(!config.doesConfigurationExists()) {
            try {
                config.createConfiguration();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Create the JSON data files
        try {
            createConfigFilesIfNotExists();
        } catch (IOException e) {
            // If something goes wrong, disable the plugin and error
            e.printStackTrace();
        }

        // Setup inventory manager
        inventoryManager = new InventoryManager(this);
        inventoryManager.init();

        // Setup command manager
        PaperCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new CommandMCHunt());

        // Try to save down list of arenas
        ArrayList<Arena> arenas = new ArrayList<>();
        arenas.add(new Arena());
        arenas.add(new Arena());
        arenas.add(new Arena());

        GsonBuilder gson = new GsonBuilder();

        String jsonString = gson.excludeFieldsWithoutExposeAnnotation()
                .create().toJson(arenas);

        File configFile = new File(this.getDataFolder().getAbsolutePath() + System.getProperty("file.separator") + "arenas.json");

        try (PrintWriter pw = new PrintWriter(configFile)) {
            pw.write(jsonString);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        GeneralUtility.sendConsoleMessage("Starting plugin");

        // areas.json (Used to store all arena data)
        // inventory.json (Used for temp storing user's inventory + armour etc...)

        /*
        ID- UUID
        area: {
            seekerSpawnLocation: LOC
            hiderSpawnLocation: LOC
            returnLocation: LOC
            // boundries:
        }
         */

        // {
            // ID - UUID
            // arena {}


        // }

        // Find a decent way to store the data
        //

        // Init all arenas
        // Start game loop

        // Run a thread every 1 second

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
            }
        }, 0, 20L);

        plugin = this;
    }

    @Override
    public void onDisable() {
        GeneralUtility.sendConsoleMessage("Closing plugin");
        // Plugin shutdown logic

        plugin = null;
    }

    public static MCHunt getInstance() {
        return plugin;
    }

    public static InventoryManager getInventoryManagerInstance() {
        return inventoryManager;
    }
}
