package codox.uk.mchunt;

import codox.uk.mchunt.util.GeneralUtility;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public final class MCHunt extends JavaPlugin {

    private MCHunt plugin;

    private void createConfigFilesIfNotExists() throws IOException {
        // Create plugin config folder if it doesn't already exist
        new File(this.getDataFolder().getAbsolutePath()).mkdirs();

        List<File> filesToCreate = Arrays.asList(
                new File(this.getDataFolder().getAbsolutePath() + System.getProperty("file.separator") + "arenas.json"),
                new File(this.getDataFolder().getAbsolutePath() + System.getProperty("file.separator") + "tmp-inventory.json")
        );

        for (File file: filesToCreate) {
            if(!file.exists()) file.createNewFile();
        }

    }

    @Override
    public void onEnable() {

        // Create the JSON data files
        try {
            createConfigFilesIfNotExists();
        } catch (IOException e) {
            // If something goes wrong, disable the plugin and error
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
        GeneralUtility.sendConsoleMessage("Clsoing plugin");
        // Plugin shutdown logic

        plugin = null;
    }
}
