package codox.uk.mchunt.processor;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


/**
 * Created by brady on 11/02/2017.
 *
 * File name: tmp-arena-setup.json
 */
public class ArenaSetupProcessor {

    private ArrayList<UUID> playersInSetupMode;

    public ArenaSetupProcessor() {
        this.playersInSetupMode = new ArrayList<>();
    }

    /**
     */
    public List<HashMap<String, Object>> loadDataFromFile() throws IOException {
        Gson gson = new Gson();

        Reader reader = Files.newBufferedReader(Paths.get("tmp-arena-setup.json"));

        List<HashMap<String, Object>> list = gson.fromJson(reader, List.class);

        reader.close();

        return list;
    }

    public ArrayList<UUID> getPlayersInSetupMode() {
        return this.playersInSetupMode;
    }

    /**
     * Dump a player's information into the temporary file
     */
    public void stashPlayer(Player player) throws Exception {
        UUID uuid = player.getUniqueId();

        // Check if the player has already been stashed
        if (this.playersInSetupMode.contains(uuid)) {
            throw new Exception("Player has already been stashed");
        }

        List<HashMap<String, Object>> existingInformation = this.loadDataFromFile();

        HashMap<String, Object> playerInformation = new HashMap<>();
        List<Map<String, Object>> playerItems = new ArrayList<>();

        // Add the player's UUID
        playerInformation.put("uuid", uuid.toString());

        // Serialize the player's inventory
        for(ItemStack item: player.getInventory().getContents()) {
            if(item == null) continue;

            Map<String, Object> serializedItem = item.serialize();
            playerItems.add(serializedItem);
        }

        playerInformation.put("inventory", playerItems);

        existingInformation.add(playerInformation);

        Gson gson = new Gson();
        String gsonString = gson.toJson(playerInformation);

        Bukkit.getConsoleSender().sendMessage(gsonString);
    }

    /**
     * Retrieves the player's dumped information
     *
     * If removeFromTemporaryFile is set, it will also remove them
     * from the temporary file
     */
    public void getPlayerInformation(boolean removeFromTemporaryFile) {

    }
}
