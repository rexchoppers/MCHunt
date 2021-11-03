package codox.uk.mchunt;

import codox.uk.mchunt.util.GeneralUtility;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class MCHunt extends JavaPlugin {

    private MCHunt plugin;

    @Override
    public void onEnable() {
        plugin = this;

        GeneralUtility.sendConsoleMessage("Starting plugin");

        // Find a decent way to store the data
        //

        // Init all arenas
        // Start game loop

        // Run a thread every 1 second

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () ->
                Bukkit.getConsoleSender().sendMessage("CAlled"), 0, 20L);
    }

    @Override
    public void onDisable() {
        GeneralUtility.sendConsoleMessage("Clsoing plugin");
        // Plugin shutdown logic
    }
}
