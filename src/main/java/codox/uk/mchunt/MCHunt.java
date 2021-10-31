package codox.uk.mchunt;

import codox.uk.mchunt.util.GeneralUtility;
import org.bukkit.plugin.java.JavaPlugin;

public final class MCHunt extends JavaPlugin {

    @Override
    public void onEnable() {
        GeneralUtility.sendConsoleMessage("Starting plugin");
    }

    @Override
    public void onDisable() {
        GeneralUtility.sendConsoleMessage("Clsoing plugin");
        // Plugin shutdown logic
    }
}
