package codox.uk.mchunt.processor;

import com.google.gson.Gson;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ArenaSetupProcessor {

    private ArrayList<UUID> playersInSetupMode;

    public ArenaSetupProcessor() {
        this.playersInSetupMode = new ArrayList<>();
    }

    /**
     */
    public void loadPlayersFromTemporaryFiles() {

    }

    public ArrayList<UUID> getPlayersInSetupMode() {
        return this.playersInSetupMode;
    }

    /**
     * Dump a player's information into the temporary file
     */
    public void stashPlayerInformation(Player player) {
        HashMap<String, Object> playerInformation = new HashMap<>();
        List<Map<String, Object>> playerItems = new ArrayList<>();
        String uuid = player.getUniqueId().toString();

        // Add the player's UUID
        playerInformation.put("uuid", uuid);

        // Serialize the player's inventory
        for(ItemStack item: player.getInventory().getContents()) {
            if(item == null) continue;

            Map<String, Object> serializedItem = item.serialize();
            playerItems.add(serializedItem);
        }

        playerInformation.put("inventory", playerItems);

        Gson gson = new Gson();
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
