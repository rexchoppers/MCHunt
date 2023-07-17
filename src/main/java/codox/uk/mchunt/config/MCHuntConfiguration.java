package codox.uk.mchunt.config;

import codox.uk.mchunt.MCHunt;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static codox.uk.mchunt.MCHunt.BASE_URL;

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

    public void createConfiguration() throws IOException {
        String version = MCHunt.getVersionNumber();

        System.out.println("Version " + version);

        // Send HTTP request to get configuration information
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(BASE_URL + "/configuration");
        System.out.println("Get created " + version);

        HttpResponse httpResponse = httpClient.execute(httpGet);
        System.out.println("Get Called " + version);
        HttpEntity httpEntity = httpResponse.getEntity();
        System.out.println("Entity got " + version);
        String response = EntityUtils.toString(httpEntity);
        Gson gson = new Gson();

        

        System.out.println("Response got " + version);

        System.out.println("Creating config file for MCHunt v" + version);
        System.out.println("Response: " + response);
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
