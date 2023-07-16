package codox.uk.mchunt.config;

import codox.uk.mchunt.MCHunt;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MCHuntConfiguration {
    private transient MCHunt plugin;

    private boolean enabled;

    public MCHuntConfiguration(
            MCHunt plugin
    ) {
        this.plugin = plugin;
    }

    public boolean doesConfigurationExists() {
        File file = new File(plugin.getDataFolder().getAbsolutePath() + System.getProperty("file.separator") + "config.json");

        return file.exists();
    }

    public void createConfiguration() {
        String version = MCHunt.getVersionNumber();

        System.out.println("Creating config file for MCHunt v" + version);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String toJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static MCHuntConfiguration fromJSON(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, MCHuntConfiguration.class);
    }
}
