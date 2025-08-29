package com.rexchoppers.mchunt.menus;

import com.rexchoppers.mchunt.MCHunt;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.entity.Player;

public class MenuInGame extends MenuBase {
    public MenuInGame(MCHunt plugin) {
        this.setPlugin(plugin);

        this.setInventory(SmartInventory.builder()
                .id("mchunt:menuInGame")
                .manager(plugin.getInventoryManager())
                .provider(new MenuInGameProvider())
                .size(3, 9)
                .title("MCHunt - In Game")
                .closeable(true)
                .build());
    }

    private static class MenuInGameProvider implements InventoryProvider {
        @Override
        public void init(Player player, InventoryContents inventoryContents) {

        }
    }
}
